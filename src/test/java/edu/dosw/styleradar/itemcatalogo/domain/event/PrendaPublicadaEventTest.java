package edu.dosw.styleradar.itemcatalogo.domain.event;

import edu.dosw.styleradar.itemcatalogo.domain.model.AtributosBusqueda;
import edu.dosw.styleradar.itemcatalogo.domain.model.Fotografia;
import edu.dosw.styleradar.itemcatalogo.domain.model.Precio;
import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PrendaPublicadaEventTest {

    @Test
    void usaLaPrimeraFotoComoImagen() {
        Prenda prenda = Prenda.publicar(UUID.randomUUID(), "Chaqueta denim",
                new AtributosBusqueda("Chaqueta", "Azul", "Levis", "Urbano"), new Precio(new BigDecimal("190000")),
                List.of(new Fotografia("https://foto/2.jpg", 2), new Fotografia("https://foto/1.jpg", 1)));

        PrendaPublicadaEvent evento = PrendaPublicadaEvent.de(prenda);

        assertThat(evento.imagenUrl()).isEqualTo("https://foto/1.jpg");
        assertThat(evento.nombre()).isEqualTo("Chaqueta denim");
    }
}
