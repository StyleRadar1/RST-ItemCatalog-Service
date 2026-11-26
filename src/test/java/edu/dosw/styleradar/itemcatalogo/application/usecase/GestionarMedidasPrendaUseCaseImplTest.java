package edu.dosw.styleradar.itemcatalogo.application.usecase;

import edu.dosw.styleradar.itemcatalogo.domain.exception.AccesoDenegadoException;
import edu.dosw.styleradar.itemcatalogo.domain.exception.DomainException;
import edu.dosw.styleradar.itemcatalogo.domain.exception.ResourceNotFoundException;
import edu.dosw.styleradar.itemcatalogo.domain.model.AtributosBusqueda;
import edu.dosw.styleradar.itemcatalogo.domain.model.Fotografia;
import edu.dosw.styleradar.itemcatalogo.domain.model.MedidaPrenda;
import edu.dosw.styleradar.itemcatalogo.domain.model.Precio;
import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;
import edu.dosw.styleradar.itemcatalogo.domain.model.Solicitante;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.MedidaPrendaRepositoryPort;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.PrendaRepositoryPort;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GestionarMedidasPrendaUseCaseImplTest {

    private final PrendaRepositoryPort prendas = mock(PrendaRepositoryPort.class);
    private final MedidaPrendaRepositoryPort medidas = mock(MedidaPrendaRepositoryPort.class);
    private final GestionarMedidasPrendaUseCaseImpl useCase = new GestionarMedidasPrendaUseCaseImpl(prendas, medidas);
    private final UUID almacenId = UUID.randomUUID();
    private final Prenda prenda = Prenda.publicar(almacenId, "Camiseta blanca",
            new AtributosBusqueda("Camiseta", "Blanco", "Luna Store", "CASUAL"), new Precio(new BigDecimal("50000")),
            List.of(new Fotografia("https://foto/1.jpg", 1)));
    private final List<MedidaPrenda> tabla = List.of(new MedidaPrenda("M", 92.0, 100.0, 74.0, 82.0, null, null, 70.0));

    @Test
    void elDuenoRegistraLasMedidas() {
        when(prendas.findById(prenda.getId())).thenReturn(Optional.of(prenda));
        when(medidas.reemplazar(prenda.getId(), tabla)).thenReturn(tabla);

        assertThat(useCase.registrar(prenda.getId(), new Solicitante(almacenId, false), tabla)).isEqualTo(tabla);
        assertThat(useCase.registrar(prenda.getId(), new Solicitante(null, true), tabla)).isEqualTo(tabla);
    }

    @Test
    void otroAlmacenNoPuedeRegistrarMedidas() {
        when(prendas.findById(prenda.getId())).thenReturn(Optional.of(prenda));

        assertThatThrownBy(() -> useCase.registrar(prenda.getId(), new Solicitante(UUID.randomUUID(), false), tabla))
                .isInstanceOf(AccesoDenegadoException.class);
        verify(medidas, never()).reemplazar(any(), any());
    }

    @Test
    void noSeRegistranMedidasEnPrendasRetiradasNiInvalidas() {
        when(prendas.findById(prenda.getId())).thenReturn(Optional.of(prenda));
        Solicitante dueno = new Solicitante(almacenId, false);

        assertThatThrownBy(() -> useCase.registrar(prenda.getId(), dueno, List.of()))
                .isInstanceOf(DomainException.class).extracting("codigo").isEqualTo("MEDIDAS_INVALIDAS");
        prenda.retirar();
        assertThatThrownBy(() -> useCase.registrar(prenda.getId(), dueno, tabla))
                .isInstanceOf(DomainException.class).extracting("codigo").isEqualTo("PRENDA_RETIRADA");
        verify(medidas, never()).reemplazar(any(), any());
    }

    @Test
    void consultaLasMedidasDeUnaPrendaExistente() {
        when(prendas.findById(prenda.getId())).thenReturn(Optional.of(prenda));
        when(medidas.findByPrendaId(prenda.getId())).thenReturn(tabla);
        UUID otra = UUID.randomUUID();
        when(prendas.findById(otra)).thenReturn(Optional.empty());

        assertThat(useCase.consultar(prenda.getId())).isEqualTo(tabla);
        assertThatThrownBy(() -> useCase.consultar(otra)).isInstanceOf(ResourceNotFoundException.class);
    }
}
