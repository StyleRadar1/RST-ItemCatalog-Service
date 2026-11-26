package edu.dosw.styleradar.itemcatalogo.domain.model;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public final class IdAlmacen {

    private IdAlmacen() {
    }

    // Misma regla que ms-geolocalizacion para que el almacén tenga el mismo id en ambos servicios
    public static UUID desdeNit(String nit) {
        if (nit == null || nit.isBlank()) {
            return null;
        }
        return UUID.nameUUIDFromBytes(("ms-tiendas:almacen:" + nit.trim()).getBytes(StandardCharsets.UTF_8));
    }

    public static UUID desdeTexto(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        String limpio = valor.trim();
        try {
            return UUID.fromString(limpio);
        } catch (IllegalArgumentException e) {
            return desdeNit(limpio);
        }
    }
}
