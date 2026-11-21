package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.tiendas;

import feign.RequestTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.assertj.core.api.Assertions.assertThat;

class TiendasFeignConfigTest {

    @AfterEach
    void limpiar() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void reenviaLaIdentidadDeLaPeticion() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token");
        request.addHeader("X-User-Id", "10");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        RequestTemplate template = new RequestTemplate();

        new TiendasFeignConfig().reenviarIdentidad().apply(template);

        assertThat(template.headers().get("Authorization")).containsExactly("Bearer token");
        assertThat(template.headers().get("X-User-Id")).containsExactly("10");
        assertThat(template.headers()).doesNotContainKey("X-User-Roles");
    }

    @Test
    void sinPeticionNoAgregaCabeceras() {
        RequestTemplate template = new RequestTemplate();

        TiendasFeignConfig.reenviar(template);

        assertThat(template.headers()).isEmpty();
    }
}
