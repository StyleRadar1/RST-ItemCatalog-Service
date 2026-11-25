package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest;

import edu.dosw.styleradar.itemcatalogo.domain.exception.AccesoDenegadoException;
import edu.dosw.styleradar.itemcatalogo.domain.exception.ParametroInvalidoException;
import edu.dosw.styleradar.itemcatalogo.domain.model.Solicitante;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.SincronizarAlmacenesUseCase;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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

    static Solicitante solicitante(UUID almacenId, String rol, String roles, String usuarioId,
                                   SincronizarAlmacenesUseCase almacenes) {
        boolean admin = esAdmin(rol, roles);
        UUID almacen = almacenId != null || admin ? almacenId
                : almacenes.almacenDelUsuario(usuario(usuarioId)).orElse(null);
        return new Solicitante(almacen, admin);
    }

    static Long usuario(String usuarioId) {
        try {
            return usuarioId == null || usuarioId.isBlank() ? null : Long.valueOf(usuarioId.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // El usuario autenticado tiene prioridad sobre el moderadorId del cuerpo
    static UUID moderador(String usuarioId, String moderadorId) {
        Long usuario = usuario(usuarioId);
        if (usuario != null) {
            return UUID.nameUUIDFromBytes(("ms-auth:usuario:" + usuario).getBytes(StandardCharsets.UTF_8));
        }
        try {
            if (moderadorId != null && !moderadorId.isBlank()) {
                return UUID.fromString(moderadorId.trim());
            }
        } catch (IllegalArgumentException e) {
            // no es un UUID; se informa abajo
        }
        throw new ParametroInvalidoException("La solicitud tiene campos inválidos", "DATOS_INVALIDOS",
                Map.of("moderadorId", "El moderador es obligatorio: llega en X-User-Id o como UUID en moderadorId"));
    }
}
