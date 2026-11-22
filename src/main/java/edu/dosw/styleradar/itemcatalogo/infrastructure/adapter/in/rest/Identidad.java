package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest;

import edu.dosw.styleradar.itemcatalogo.domain.exception.AccesoDenegadoException;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;

final class Identidad {

    static final String HEADER_ALMACEN = "X-Almacen-Id";
    static final String HEADER_ROL = "X-User-Role";
    static final String HEADER_ROLES = "X-User-Roles";
    static final String HEADER_USUARIO = "X-User-Id";
    // ms-auth pasó los roles a inglés (STYLERADAR_ADMIN); se aceptan también los nombres anteriores
    private static final Set<String> ROLES_ADMIN = Set.of("ADMIN", "ADMIN_STYLERADAR", "STYLERADAR_ADMIN");

    private Identidad() {
    }

    static boolean esAdmin(String rol, String roles) {
        String todos = (rol == null ? "" : rol) + "," + (roles == null ? "" : roles);
        return Arrays.stream(todos.split("[,\\s]+"))
                .map(r -> r.trim().toUpperCase(Locale.ROOT))
                .map(r -> r.startsWith("ROLE_") ? r.substring(5) : r)
                .anyMatch(ROLES_ADMIN::contains);
    }

    static void exigirAdmin(String rol, String roles) {
        if (!esAdmin(rol, roles)) {
            throw new AccesoDenegadoException("Solo un administrador de StyleRadar puede moderar publicaciones");
        }
    }

    static Long usuario(String usuarioId) {
        try {
            return usuarioId == null || usuarioId.isBlank() ? null : Long.valueOf(usuarioId.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
