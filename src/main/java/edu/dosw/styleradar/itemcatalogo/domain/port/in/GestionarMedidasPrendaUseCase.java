package edu.dosw.styleradar.itemcatalogo.domain.port.in;

import edu.dosw.styleradar.itemcatalogo.domain.model.MedidaPrenda;
import edu.dosw.styleradar.itemcatalogo.domain.model.Solicitante;

import java.util.List;
import java.util.UUID;

public interface GestionarMedidasPrendaUseCase {

    List<MedidaPrenda> registrar(UUID prendaId, Solicitante solicitante, List<MedidaPrenda> medidas);

    List<MedidaPrenda> consultar(UUID prendaId);
}
