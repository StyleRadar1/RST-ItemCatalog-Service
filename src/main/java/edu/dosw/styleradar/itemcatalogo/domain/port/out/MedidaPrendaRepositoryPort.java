package edu.dosw.styleradar.itemcatalogo.domain.port.out;

import edu.dosw.styleradar.itemcatalogo.domain.model.MedidaPrenda;

import java.util.List;
import java.util.UUID;

public interface MedidaPrendaRepositoryPort {

    List<MedidaPrenda> findByPrendaId(UUID prendaId);

    List<MedidaPrenda> reemplazar(UUID prendaId, List<MedidaPrenda> medidas);
}
