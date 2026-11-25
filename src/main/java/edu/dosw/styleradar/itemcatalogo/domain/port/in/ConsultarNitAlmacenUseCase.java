package edu.dosw.styleradar.itemcatalogo.domain.port.in;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public interface ConsultarNitAlmacenUseCase {

    Map<UUID, String> nitsDe(Collection<UUID> almacenIds);
}
