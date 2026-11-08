package edu.dosw.styleradar.itemcatalogo.domain.model;

import edu.dosw.styleradar.itemcatalogo.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ValueObjectsTest {

    @Test
    void elPrecioDebeSerPositivo() {
        assertThatThrownBy(() -> new Precio(BigDecimal.ZERO)).isInstanceOf(DomainException.class);
        assertThatThrownBy(() -> new Precio(null)).isInstanceOf(DomainException.class);
    }

    @Test
    void elPrecioEstaEntreLimites() {
        Precio precio = new Precio(new BigDecimal("100"));

        assertThat(precio.estaEntre(null, null)).isTrue();
        assertThat(precio.estaEntre(new BigDecimal("50"), new BigDecimal("100"))).isTrue();
        assertThat(precio.estaEntre(new BigDecimal("150"), null)).isFalse();
        assertThat(precio.estaEntre(null, new BigDecimal("80"))).isFalse();
    }

    @Test
    void elTipoEsObligatorio() {
        assertThatThrownBy(() -> new AtributosBusqueda(null, "Azul", null, null)).isInstanceOf(DomainException.class);
        assertThatThrownBy(() -> new AtributosBusqueda(" ", "Azul", null, null)).isInstanceOf(DomainException.class);
    }

    @Test
    void laFotografiaNecesitaUrl() {
        assertThatThrownBy(() -> new Fotografia("", 1)).isInstanceOf(DomainException.class);
        assertThatThrownBy(() -> new Fotografia(null, 1)).isInstanceOf(DomainException.class);
    }
}
