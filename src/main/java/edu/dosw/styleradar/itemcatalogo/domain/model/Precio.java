package edu.dosw.styleradar.itemcatalogo.domain.model;

import edu.dosw.styleradar.itemcatalogo.domain.exception.DomainException;

import java.math.BigDecimal;

public record Precio(BigDecimal valor) {

    public Precio {
        if (valor == null || valor.signum() <= 0) {
            throw new DomainException("El precio debe ser mayor a cero");
        }
    }

    public boolean estaEntre(BigDecimal minimo, BigDecimal maximo) {
        return (minimo == null || valor.compareTo(minimo) >= 0)
                && (maximo == null || valor.compareTo(maximo) <= 0);
    }
}
