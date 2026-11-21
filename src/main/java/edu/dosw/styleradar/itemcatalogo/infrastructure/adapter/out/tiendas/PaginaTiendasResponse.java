package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.tiendas;

import java.util.List;

public record PaginaTiendasResponse(List<AlmacenTiendasResponse> content, boolean last) {
}
