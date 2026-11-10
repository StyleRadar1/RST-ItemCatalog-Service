package edu.dosw.styleradar.itemcatalogo.domain.port.in;

import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;

import java.util.List;
import java.util.UUID;

public interface ConsultarCatalogoAlmacenUseCase {

    List<Prenda> catalogo(UUID almacenId);

    List<Prenda> novedades(UUID almacenId);
}
