package edu.dosw.styleradar.itemcatalogo.domain.model;

import edu.dosw.styleradar.itemcatalogo.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PrendaTest {

    private final UUID almacenId = UUID.randomUUID();
    private final UUID moderadorId = UUID.randomUUID();
    private final AtributosBusqueda atributos = new AtributosBusqueda("Chaqueta", "Azul", "Levis", "Urbano");
    private final List<Fotografia> fotos = List.of(new Fotografia("https://foto/1.jpg", 1));

    private Prenda nueva() {
        return Prenda.publicar(almacenId, "Chaqueta denim", atributos, new Precio(new BigDecimal("190000")), fotos);
    }

    private Prenda aprobada() {
        Prenda prenda = nueva();
        prenda.aprobar(moderadorId);
        return prenda;
    }

    private FiltroBusqueda filtro(String texto, String tipo, String color, String marca, String estilo, String min, String max) {
        return new FiltroBusqueda(texto, tipo, color, marca, estilo,
                min == null ? null : new BigDecimal(min), max == null ? null : new BigDecimal(max), null);
    }

    @Test
    void alPublicarQuedaPendienteYDisponible() {
        Prenda prenda = nueva();

        assertThat(prenda.getEstado()).isEqualTo(EstadoPublicacion.PENDIENTE);
        assertThat(prenda.isDisponible()).isTrue();
        assertThat(prenda.esVisible()).isFalse();
        assertThat(prenda.getFotografias()).hasSize(1);
    }

    @Test
    void noSePublicaSinNombreNiFotos() {
        Precio precio = new Precio(BigDecimal.TEN);

        assertThatThrownBy(() -> Prenda.publicar(almacenId, " ", atributos, precio, fotos)).isInstanceOf(DomainException.class);
        assertThatThrownBy(() -> Prenda.publicar(almacenId, "Chaqueta", atributos, precio, List.of())).isInstanceOf(DomainException.class);
    }

    @Test
    void alAprobarseQuedaVisibleYRegistraLaModeracion() {
        Prenda prenda = aprobada();

        assertThat(prenda.esVisible()).isTrue();
        assertThat(prenda.getModeraciones()).hasSize(1);
        assertThat(prenda.getModeraciones().get(0).decision()).isEqualTo(EstadoPublicacion.APROBADA);
    }

    @Test
    void elRechazoExigeMotivo() {
        Prenda prenda = nueva();

        assertThatThrownBy(() -> prenda.rechazar(moderadorId, "")).isInstanceOf(DomainException.class);
        prenda.rechazar(moderadorId, "Foto borrosa");
        assertThat(prenda.getEstado()).isEqualTo(EstadoPublicacion.RECHAZADA);
    }

    @Test
    void soloSeModeranPrendasPendientes() {
        Prenda prenda = aprobada();

        assertThatThrownBy(() -> prenda.aprobar(moderadorId)).isInstanceOf(DomainException.class);
    }

    @Test
    void alEditarseVuelveAModeracion() {
        Prenda prenda = aprobada();

        prenda.editar("Chaqueta nueva", atributos, new Precio(new BigDecimal("150000")), fotos);

        assertThat(prenda.getNombre()).isEqualTo("Chaqueta nueva");
        assertThat(prenda.getPrecio().valor()).isEqualByComparingTo("150000");
        assertThat(prenda.getEstado()).isEqualTo(EstadoPublicacion.PENDIENTE);
    }

    @Test
    void noSeEditaNiSeRetiraDosVecesUnaPrendaRetirada() {
        Prenda prenda = nueva();
        prenda.retirar();
        Precio precio = new Precio(BigDecimal.TEN);

        assertThat(prenda.getEstado()).isEqualTo(EstadoPublicacion.RETIRADA);
        assertThatThrownBy(prenda::retirar).isInstanceOf(DomainException.class);
        assertThatThrownBy(() -> prenda.editar("Otra", atributos, precio, fotos)).isInstanceOf(DomainException.class);
    }

    @Test
    void agotadaDejaDeSerVisible() {
        Prenda prenda = aprobada();

        prenda.marcarAgotada();

        assertThat(prenda.isDisponible()).isFalse();
        assertThat(prenda.esVisible()).isFalse();
    }

    @Test
    void cumpleLosFiltros() {
        Prenda prenda = aprobada();

        assertThat(prenda.cumple(filtro(null, null, null, null, null, null, null))).isTrue();
        assertThat(prenda.cumple(filtro("denim", null, null, null, null, null, null))).isTrue();
        assertThat(prenda.cumple(filtro("levis", "chaqueta", "azul", "LEVIS", "urbano", "100000", "200000"))).isTrue();
        assertThat(prenda.cumple(filtro("vestido", null, null, null, null, null, null))).isFalse();
        assertThat(prenda.cumple(filtro(null, "Pantalón", null, null, null, null, null))).isFalse();
        assertThat(prenda.cumple(filtro(null, null, "Rojo", null, null, null, null))).isFalse();
        assertThat(prenda.cumple(filtro(null, null, null, "Zara", null, null, null))).isFalse();
        assertThat(prenda.cumple(filtro(null, null, null, null, "Formal", null, null))).isFalse();
        assertThat(prenda.cumple(filtro(null, null, null, null, null, "200000", null))).isFalse();
    }

    @Test
    void esSimilarSiCompartenTipoOEstilo() {
        Prenda prenda = aprobada();
        Prenda mismoTipo = Prenda.publicar(almacenId, "Otra chaqueta", new AtributosBusqueda("Chaqueta", null, null, "Formal"),
                new Precio(BigDecimal.TEN), fotos);
        Prenda distinta = Prenda.publicar(almacenId, "Vestido", new AtributosBusqueda("Vestido", null, null, "Formal"),
                new Precio(BigDecimal.TEN), fotos);

        assertThat(mismoTipo.esSimilarA(prenda)).isTrue();
        assertThat(distinta.esSimilarA(prenda)).isFalse();
        assertThat(prenda.esSimilarA(prenda)).isFalse();
    }
}
