package edu.dosw.styleradar.itemcatalogo.domain.port.in;

import edu.dosw.styleradar.itemcatalogo.domain.model.EstadoPublicacion;
import edu.dosw.styleradar.itemcatalogo.domain.model.GrupoPendientes;
import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;

import java.util.List;
import java.util.UUID;

public interface ModerarPublicacionUseCase {

    Prenda moderar(UUID prendaId, EstadoPublicacion decision, String motivo, UUID moderadorId);

    List<GrupoPendientes> pendientesPorGrupo();

    List<Prenda> moderarGrupo(UUID almacenId, EstadoPublicacion decision, String motivo, UUID moderadorId);
}
