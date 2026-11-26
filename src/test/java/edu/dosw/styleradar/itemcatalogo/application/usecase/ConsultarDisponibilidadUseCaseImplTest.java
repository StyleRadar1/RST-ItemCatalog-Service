package edu.dosw.styleradar.itemcatalogo.application.usecase;

import edu.dosw.styleradar.itemcatalogo.domain.exception.ResourceNotFoundException;
import edu.dosw.styleradar.itemcatalogo.domain.model.Disponibilidad;
import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;
import edu.dosw.styleradar.itemcatalogo.domain.model.MedidaPrenda;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.MedidaPrendaRepositoryPort;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.PrendaRepositoryPort;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.StockPrendaPort;
import edu.dosw.styleradar.itemcatalogo.domain.model.AtributosBusqueda;
import edu.dosw.styleradar.itemcatalogo.domain.model.GuiaMedidas;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.ConsultarDisponibilidadUseCase;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class ConsultarDisponibilidadUseCaseImplTest {

    private final PrendaRepositoryPort prendas = mock(PrendaRepositoryPort.class);
    private final StockPrendaPort stock = mock(StockPrendaPort.class);
    private final MedidaPrendaRepositoryPort medidas = mock(MedidaPrendaRepositoryPort.class);
    private final ConsultarDisponibilidadUseCaseImpl useCase = new ConsultarDisponibilidadUseCaseImpl(prendas, stock, medidas);
    private final UUID prendaId = UUID.randomUUID();

    @Test
    void devuelveLaDisponibilidadDeInventario() {
        when(prendas.findById(prendaId)).thenReturn(Optional.of(mock(Prenda.class)));
        when(stock.disponibilidad(prendaId)).thenReturn(Optional.of(new Disponibilidad(18, List.of("M", "S"))));

        assertThat(useCase.consultar(prendaId)).isEqualTo(new Disponibilidad(18, List.of("M", "S")));
    }

    @Test
    void sinInventarioDevuelveCero() {
        when(prendas.findById(prendaId)).thenReturn(Optional.of(mock(Prenda.class)));
        when(stock.disponibilidad(prendaId)).thenReturn(Optional.empty());

        assertThat(useCase.consultar(prendaId)).isEqualTo(Disponibilidad.sinStock());
    }

    @Test
    void laPrendaDebeExistir() {
        when(prendas.findById(prendaId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.consultar(prendaId)).isInstanceOf(ResourceNotFoundException.class);
        verifyNoInteractions(stock);
    }

    @Test
    void armaLaTablaDeMedidasConLasTallasOfertadas() {
        Prenda prenda = mock(Prenda.class);
        when(prenda.getAtributos()).thenReturn(new AtributosBusqueda("Camiseta", "Negro", "Luna Store", "CASUAL"));
        when(prendas.findById(prendaId)).thenReturn(Optional.of(prenda));
        when(stock.disponibilidad(prendaId)).thenReturn(Optional.of(new Disponibilidad(4, List.of("M", "Única"))));

        ConsultarDisponibilidadUseCase.TallasYMedidas tabla = useCase.tallasYMedidas(prendaId);

        assertThat(tabla.tallas()).containsExactly("M", "Única");
        assertThat(tabla.medidas()).extracting(GuiaMedidas.MedidasTalla::talla).containsExactly("M");
    }

    @Test
    void sinInventarioNoHayTallasNiMedidas() {
        Prenda prenda = mock(Prenda.class);
        when(prendas.findById(prendaId)).thenReturn(Optional.of(prenda));
        when(stock.disponibilidad(prendaId)).thenReturn(Optional.empty());

        ConsultarDisponibilidadUseCase.TallasYMedidas tabla = useCase.tallasYMedidas(prendaId);

        assertThat(tabla.tallas()).isNull();
        assertThat(tabla.medidas()).isEmpty();
    }

    @Test
    void usaLasMedidasDeLaTiendaYCompletaConLaGuia() {
        Prenda prenda = mock(Prenda.class);
        when(prenda.getAtributos()).thenReturn(new AtributosBusqueda("Camiseta", "Negro", "Luna Store", "CASUAL"));
        when(prendas.findById(prendaId)).thenReturn(Optional.of(prenda));
        when(stock.disponibilidad(prendaId)).thenReturn(Optional.of(new Disponibilidad(6, List.of("m", "L"))));
        when(medidas.findByPrendaId(prendaId)).thenReturn(List.of(
                new MedidaPrenda("M", 90.0, 96.0, 72.0, 78.0, null, null, 70.0)));

        ConsultarDisponibilidadUseCase.TallasYMedidas tabla = useCase.tallasYMedidas(prendaId);

        assertThat(tabla.medidas()).containsExactly(
                new GuiaMedidas.MedidasTalla("m", 90.0, 96.0, 72.0, 78.0, null, null, 70.0),
                GuiaMedidas.de("Camiseta", "L").orElseThrow());
    }
}
