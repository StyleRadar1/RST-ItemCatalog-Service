package edu.dosw.styleradar.itemcatalogo.domain.port.out;

import java.util.Set;
import java.util.UUID;

public interface TallaDisponiblePort {

    Set<UUID> prendasConStock(String talla);
}
