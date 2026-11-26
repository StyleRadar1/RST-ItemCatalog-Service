package edu.dosw.styleradar.itemcatalogo.domain.model;

import edu.dosw.styleradar.itemcatalogo.domain.exception.ParametroInvalidoException;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public record Fotografia(String url, int orden) {

    public Fotografia {
        if (url == null || url.isBlank()) {
            throw urlInvalida("La URL de la fotografía es obligatoria");
        }
        String sanitized = url.trim().replace(" ", "%20");
        validarHttpUrl(sanitized);
        url = sanitized;
    }

    private static void validarHttpUrl(String sanitized) {
        try {
            URI uri = new URI(sanitized);
            String scheme = uri.getScheme();
            if (scheme == null || (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme))) {
                throw urlInvalida("La URL de la fotografía debe usar el esquema http o https");
            }
            if (uri.getHost() == null || uri.getHost().isBlank()) {
                throw urlInvalida("La URL de la fotografía no es válida");
            }
            uri.toURL();
        } catch (URISyntaxException | MalformedURLException | IllegalArgumentException e) {
            throw urlInvalida("La URL de la fotografía no tiene un formato válido");
        }
    }

    private static ParametroInvalidoException urlInvalida(String detalle) {
        return new ParametroInvalidoException(detalle, "URL_FOTOGRAFIA_INVALIDA", Map.of("fotografias", detalle));
    }
}
