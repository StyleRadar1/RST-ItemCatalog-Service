package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.tiendas;

import com.fasterxml.jackson.annotation.JsonAlias;

// ms-tiendas pasó sus campos a inglés en QA; se aceptan ambos nombres
public record AlmacenTiendasResponse(String nit,
                                     @JsonAlias("tradeName") String nombreComercial,
                                     @JsonAlias("rating") Double reputacion) {
}
