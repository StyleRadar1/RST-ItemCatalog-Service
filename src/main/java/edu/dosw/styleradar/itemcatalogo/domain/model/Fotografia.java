package edu.dosw.styleradar.itemcatalogo.domain.model;

import edu.dosw.styleradar.itemcatalogo.domain.exception.DomainException;

public record Fotografia(String url, int orden) {

    public Fotografia {
        if (url == null || url.isBlank()) {
            throw new DomainException("La fotografía debe tener una URL");
        }
    }
}
