package edu.dosw.styleradar.itemcatalogo.domain.model;

import edu.dosw.styleradar.itemcatalogo.domain.exception.DomainException;

public record AtributosBusqueda(String tipo, String color, String marca, String estilo) {

    public AtributosBusqueda {
        if (tipo == null || tipo.isBlank()) {
            throw new DomainException("El tipo de prenda es obligatorio");
        }
    }
}
