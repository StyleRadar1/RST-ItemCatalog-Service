package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.persistence;

import edu.dosw.styleradar.itemcatalogo.domain.model.AlmacenResumen;
import edu.dosw.styleradar.itemcatalogo.domain.model.AtributosBusqueda;
import edu.dosw.styleradar.itemcatalogo.domain.model.EstadoPublicacion;
import edu.dosw.styleradar.itemcatalogo.domain.model.Fotografia;
import edu.dosw.styleradar.itemcatalogo.domain.model.Precio;
import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;
import edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.persistence.entity.AlmacenResumenEntity;
import edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.persistence.entity.PrendaEntity;
import edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.persistence.repository.AlmacenResumenJpaRepository;
import edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.persistence.repository.PrendaJpaRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RepositoryAdaptersTest {

    private final PrendaJpaRepository prendaJpa = mock(PrendaJpaRepository.class);
    private final PrendaRepositoryAdapter prendaAdapter = new PrendaRepositoryAdapter(prendaJpa);

    private Prenda moderada() {
        Prenda prenda = Prenda.publicar(UUID.randomUUID(), "Chaqueta denim",
                new AtributosBusqueda("Chaqueta", "Azul", "Levis", "Urbano"), new Precio(new BigDecimal("190000")),
                List.of(new Fotografia("https://foto/1.jpg", 1), new Fotografia("https://foto/2.jpg", 2)));
        prenda.rechazar(UUID.randomUUID(), "Foto borrosa");
        return prenda;
    }

    @Test
    void guardaYConvierteLaPrendaConFotosYModeraciones() {
        // Arrange
        when(prendaJpa.save(any())).thenAnswer(inv -> inv.getArgument(0));
        Prenda prenda = moderada();

        // Act
        Prenda guardada = prendaAdapter.save(prenda);

        // Assert
        assertThat(guardada.getId()).isEqualTo(prenda.getId());
        assertThat(guardada.getFotografias()).extracting(Fotografia::url).containsExactly("https://foto/1.jpg", "https://foto/2.jpg");
        assertThat(guardada.getModeraciones()).hasSize(1);
        assertThat(guardada.getModeraciones().get(0).motivo()).isEqualTo("Foto borrosa");
        assertThat(guardada.getEstado()).isEqualTo(EstadoPublicacion.RECHAZADA);
    }

    @Test
    void consultaPrendasPorIdEstadoYAlmacen() {
        when(prendaJpa.save(any())).thenAnswer(inv -> inv.getArgument(0));
        Prenda prenda = moderada();
        PrendaEntity entity = capturarEntidad(prenda);
        when(prendaJpa.findById(prenda.getId())).thenReturn(Optional.of(entity));
        when(prendaJpa.findByEstado(EstadoPublicacion.RECHAZADA)).thenReturn(List.of(entity));
        when(prendaJpa.findByAlmacenId(prenda.getAlmacenId())).thenReturn(List.of(entity));

        assertThat(prendaAdapter.findById(prenda.getId())).isPresent();
        assertThat(prendaAdapter.findByEstado(EstadoPublicacion.RECHAZADA)).hasSize(1);
        assertThat(prendaAdapter.findByAlmacenId(prenda.getAlmacenId())).hasSize(1);
    }

    @Test
    void convierteLosAlmacenes() {
        AlmacenResumenJpaRepository jpa = mock(AlmacenResumenJpaRepository.class);
        AlmacenResumenEntity entity = new AlmacenResumenEntity(UUID.randomUUID(), "Calle 85", new BigDecimal("4.20"), true);
        when(jpa.findAll()).thenReturn(List.of(entity));
        when(jpa.findById(entity.getAlmacenId())).thenReturn(Optional.of(entity));
        when(jpa.save(any())).thenAnswer(inv -> inv.getArgument(0));
        AlmacenRepositoryAdapter adapter = new AlmacenRepositoryAdapter(jpa);

        assertThat(adapter.findAll()).extracting(AlmacenResumen::nombre).containsExactly("Calle 85");
        AlmacenResumen almacen = adapter.findById(entity.getAlmacenId()).orElseThrow();
        assertThat(almacen.activo()).isTrue();
        assertThat(adapter.save(almacen.suspender()).activo()).isFalse();
    }

    private PrendaEntity capturarEntidad(Prenda prenda) {
        PrendaEntity[] capturada = new PrendaEntity[1];
        when(prendaJpa.save(any())).thenAnswer(inv -> {
            capturada[0] = inv.getArgument(0);
            return capturada[0];
        });
        prendaAdapter.save(prenda);
        return capturada[0];
    }
}
