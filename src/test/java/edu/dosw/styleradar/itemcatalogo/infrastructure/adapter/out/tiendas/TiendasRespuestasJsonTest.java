package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.tiendas;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TiendasRespuestasJsonTest {

    private final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Test
    void leeLaPaginaDeAlmacenesConLosNombresEnInglesDeQa() throws Exception {
        PaginaTiendasResponse pagina = mapper.readValue("""
                {"content":[{"tradeName":"Almacén Pruebas QA","latitude":4.6533,"longitude":-74.0602,"rating":4.5,
                 "categories":["CASUAL"],"nit":"900555777-1"}],"page":0,"size":3,"last":true}""",
                PaginaTiendasResponse.class);

        assertThat(pagina.last()).isTrue();
        assertThat(pagina.content()).containsExactly(new AlmacenTiendasResponse("900555777-1", "Almacén Pruebas QA", 4.5));
    }

    @Test
    void sigueLeyendoLosNombresEnEspanol() throws Exception {
        AlmacenTiendasResponse almacen = mapper.readValue("""
                {"nit":"1","nombreComercial":"Calle 85","reputacion":3.0}""", AlmacenTiendasResponse.class);

        assertThat(almacen).isEqualTo(new AlmacenTiendasResponse("1", "Calle 85", 3.0));
    }
}
