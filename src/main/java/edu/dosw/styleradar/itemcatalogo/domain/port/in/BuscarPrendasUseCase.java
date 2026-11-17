package edu.dosw.styleradar.itemcatalogo.domain.port.in;

import edu.dosw.styleradar.itemcatalogo.domain.model.FiltroBusqueda;
import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;
import edu.dosw.styleradar.itemcatalogo.domain.model.PrendaEncontrada;

import java.util.List;
import java.util.UUID;

public interface BuscarPrendasUseCase {

    List<PrendaEncontrada> buscar(FiltroBusqueda filtro);

    List<Prenda> similares(UUID prendaId);
}
