package edu.dosw.styleradar.itemcatalogo.domain.port.out;

import edu.dosw.styleradar.itemcatalogo.domain.event.NuevaPrendaCreadaEvent;
import edu.dosw.styleradar.itemcatalogo.domain.event.PrendaEliminadaEvent;

public interface EventPublisherPort {

    void publicar(NuevaPrendaCreadaEvent evento);

    void publicar(PrendaEliminadaEvent evento);
}
