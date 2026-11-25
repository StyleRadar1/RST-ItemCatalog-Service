package edu.dosw.styleradar.itemcatalogo.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ValoresUnificadosTest {

    @Test
    void reconoceLosValoresDeLasListas() {
        assertThat(ValoresUnificados.esTipo("Pantalón")).isTrue();
        assertThat(ValoresUnificados.esTipo("pantalon")).isTrue();
        assertThat(ValoresUnificados.esTipo("Blazer")).isFalse();
        assertThat(ValoresUnificados.esColor("CAFÉ")).isTrue();
        assertThat(ValoresUnificados.esColor("Rojo")).isFalse();
        assertThat(ValoresUnificados.esEstilo("vintage")).isTrue();
        assertThat(ValoresUnificados.esTalla("XXL")).isTrue();
        assertThat(ValoresUnificados.esTalla("34")).isTrue();
        assertThat(ValoresUnificados.esTalla("46")).isFalse();
        assertThat(ValoresUnificados.esTalla("30")).isTrue();
        assertThat(ValoresUnificados.esTalla("31")).isFalse();
        assertThat(ValoresUnificados.esTalla("unica")).isTrue();
        assertThat(ValoresUnificados.esMarca("nomada studio")).isTrue();
        assertThat(ValoresUnificados.esMarca("Levis")).isTrue();
        assertThat(ValoresUnificados.esMarca("Almacén Pruebas QA")).isTrue();
        assertThat(ValoresUnificados.esMarca(null)).isFalse();
        assertThat(ValoresUnificados.esMarca("   ")).isFalse();
        assertThat(ValoresUnificados.esMarca("m".repeat(61))).isFalse();
        assertThat(ValoresUnificados.esTipo(null)).isFalse();
    }
}
