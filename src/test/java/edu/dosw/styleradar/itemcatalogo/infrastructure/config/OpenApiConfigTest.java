package edu.dosw.styleradar.itemcatalogo.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OpenApiConfigTest {

    @Test
    void swaggerShowsEveryPathWithApiV1() {
        OpenAPI openApi = new OpenApiConfig().openApi().paths(new Paths().addPathItem("/garments", new PathItem()).addPathItem("/prendas", new PathItem()));

        new OpenApiConfig().apiV1Paths("/api/v1").customise(openApi);

        assertThat(openApi.getPaths()).containsOnlyKeys("/api/v1/garments");
        assertThat(openApi.getServers()).extracting("url").containsExactly("/");
    }
}
