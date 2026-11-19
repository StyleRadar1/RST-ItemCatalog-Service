package edu.dosw.styleradar.itemcatalogo.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("RST-ItemCatalog-Service")
                        .version("0.0.1")
                        .description("StyleRadar microservice to publish, search and moderate garments. Package 2 Catalog, Restrepo team."))
                .components(new Components().addSecuritySchemes("bearerAuth",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }

    @Bean
    public OpenApiCustomizer apiV1Paths(@Value("${server.servlet.context-path:}") String contextPath) {
        return openApi -> {
            Paths paths = new Paths();
            openApi.getPaths().forEach((path, item) -> paths.addPathItem(contextPath + path, item));
            openApi.setPaths(paths);
            openApi.setServers(List.of(new Server().url("/")));
        };
    }
}
