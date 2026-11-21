package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.tiendas;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

public class TiendasFeignConfig {

    static final List<String> CABECERAS = List.of("Authorization", "X-User-Id", "X-User-Role", "X-User-Roles");

    @Bean
    public RequestInterceptor reenviarIdentidad() {
        return TiendasFeignConfig::reenviar;
    }

    static void reenviar(RequestTemplate template) {
        if (!(RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes atributos)) {
            return;
        }
        HttpServletRequest request = atributos.getRequest();
        for (String cabecera : CABECERAS) {
            String valor = request.getHeader(cabecera);
            if (valor != null && !valor.isBlank()) {
                template.header(cabecera, valor);
            }
        }
    }
}
