package edu.dosw.styleradar.itemcatalogo.domain.port.in;

import edu.dosw.styleradar.itemcatalogo.domain.model.FiltroBusqueda;
import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;

import java.util.List;
import java.util.UUID;

public interface BuscarPrendasUseCase {

    List<Prenda> buscar(FiltroBusqueda filtro);

    List<Prenda> similares(UUID prendaId);
}
