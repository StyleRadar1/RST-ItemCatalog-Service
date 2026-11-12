package edu.dosw.styleradar.itemcatalogo.domain.port.out;

import edu.dosw.styleradar.itemcatalogo.domain.event.ModeracionCambiadaEvent;
import edu.dosw.styleradar.itemcatalogo.domain.event.PrendaPublicadaEvent;
import edu.dosw.styleradar.itemcatalogo.domain.event.PrendaRetiradaEvent;

public interface EventPublisherPort {

    void publicar(PrendaPublicadaEvent evento);

    void publicar(PrendaRetiradaEvent evento);

    void publicar(ModeracionCambiadaEvent evento);
}
