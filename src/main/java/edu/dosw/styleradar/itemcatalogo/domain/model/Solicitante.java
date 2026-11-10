package edu.dosw.styleradar.itemcatalogo.domain.model;

import java.util.UUID;

// Quién hace la petición: el almacén que representa y si es administrador
public record Solicitante(UUID almacenId, boolean admin) {
}
