package edu.dosw.styleradar.itemcatalogo.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GuiaMedidasTest {

    @Test
    void usaLaTablaDeLetrasParaPrendasDeVestir() {
        assertThat(GuiaMedidas.de("Camiseta", "m"))
                .contains(new GuiaMedidas.MedidasTalla("M", 92.0, 100.0, 74.0, 82.0, 98.0, 104.0));
        assertThat(GuiaMedidas.de("Vestido", "S")).isPresent();
        assertThat(GuiaMedidas.de("Pantalón", "XL")).isPresent();
    }

    @Test
    void calculaLaCinturaDeLasTallasNumericasDePantalon() {
        assertThat(GuiaMedidas.de("Pantalón", "30"))
                .contains(new GuiaMedidas.MedidasTalla("30", null, null, 74.7, 77.7, null, null));
    }

    @Test
    void noHayMedidasParaCalzadoAccesoriosNiTallaUnica() {
        assertThat(GuiaMedidas.de("Calzado", "40")).isEmpty();
        assertThat(GuiaMedidas.de("Accesorio", "M")).isEmpty();
        assertThat(GuiaMedidas.de("Camiseta", "Única")).isEmpty();
        assertThat(GuiaMedidas.de("Camiseta", "30")).isEmpty();
        assertThat(GuiaMedidas.de(null, "M")).isEmpty();
        assertThat(GuiaMedidas.de("Camiseta", null)).isEmpty();
    }
}
