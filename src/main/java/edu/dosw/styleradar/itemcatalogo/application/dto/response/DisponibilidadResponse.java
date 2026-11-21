package edu.dosw.styleradar.itemcatalogo.application.dto.response;

import java.util.List;

public record DisponibilidadResponse(String itemId, int stockTotal, List<String> tallasConStock) {
}
