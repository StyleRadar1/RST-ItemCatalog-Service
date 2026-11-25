package edu.dosw.styleradar.itemcatalogo.domain.model;

import edu.dosw.styleradar.itemcatalogo.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MedidaPrendaTest {

    private static MedidaPrenda medida(String talla, Double minPecho, Double maxPecho, Double largo) {
        return new MedidaPrenda(talla, minPecho, maxPecho, null, null, null, null, largo);
    }

    @Test
    void aceptaMedidasValidas() {
        assertThatCode(() -> MedidaPrenda.validar(List.of(medida(" M ", 92.0, 100.0, 70.0), medida("L", null, null, 72.0))))
                .doesNotThrowAnyException();
        assertThat(medida(" m ", 92.0, 100.0, null).clave()).isEqualTo("M");
    }

    @Test
    void rechazaListasVaciasOMuyLargas() {
        assertThatThrownBy(() -> MedidaPrenda.validar(List.of())).isInstanceOf(DomainException.class);
        assertThatThrownBy(() -> MedidaPrenda.validar(null)).isInstanceOf(DomainException.class);
        assertThatThrownBy(() -> MedidaPrenda.validar(Collections.nCopies(21, medida("M", null, null, 70.0))))
                .isInstanceOf(DomainException.class);
    }

    @Test
    void rechazaTallasInvalidasORepetidas() {
        assertThatThrownBy(() -> MedidaPrenda.validar(List.of(medida("XXXL", null, null, 70.0))))
                .isInstanceOf(DomainException.class).extracting("codigo").isEqualTo("MEDIDAS_INVALIDAS");
        assertThatThrownBy(() -> MedidaPrenda.validar(List.of(medida("M", null, null, 70.0), medida("m", null, null, 71.0))))
                .hasMessageContaining("repetida");
    }

    @Test
    void rechazaRangosIncompletosInvertidosOFueraDeLimite() {
        assertThatThrownBy(() -> MedidaPrenda.validar(List.of(medida("M", null, null, null))))
                .hasMessageContaining("al menos una medida");
        assertThatThrownBy(() -> MedidaPrenda.validar(List.of(medida("M", 92.0, null, null))))
                .hasMessageContaining("mínimo y máximo");
        assertThatThrownBy(() -> MedidaPrenda.validar(List.of(medida("M", 100.0, 92.0, null))))
                .hasMessageContaining("no puede ser mayor");
        assertThatThrownBy(() -> MedidaPrenda.validar(List.of(medida("M", null, null, 350.0))))
                .hasMessageContaining("entre 0 y 300");
        assertThatThrownBy(() -> MedidaPrenda.validar(List.of(medida("M", 0.0, 10.0, null))))
                .hasMessageContaining("entre 0 y 300");
    }
}
