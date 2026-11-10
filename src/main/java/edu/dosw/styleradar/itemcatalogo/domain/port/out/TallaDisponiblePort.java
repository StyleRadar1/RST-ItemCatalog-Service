package edu.dosw.styleradar.itemcatalogo.domain.port.out;

import java.util.UUID;

public interface TallaDisponiblePort {

    boolean hayStock(UUID prendaId, String talla);
}
