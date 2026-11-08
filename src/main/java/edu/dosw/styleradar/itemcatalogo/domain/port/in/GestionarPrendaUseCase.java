package edu.dosw.styleradar.itemcatalogo.domain.port.in;

import edu.dosw.styleradar.itemcatalogo.domain.model.AtributosBusqueda;
import edu.dosw.styleradar.itemcatalogo.domain.model.Fotografia;
import edu.dosw.styleradar.itemcatalogo.domain.model.Precio;
import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;

import java.util.List;
import java.util.UUID;

public interface GestionarPrendaUseCase {

    Prenda publicar(UUID almacenId, String nombre, AtributosBusqueda atributos, Precio precio, List<Fotografia> fotografias);

    Prenda obtener(UUID prendaId);

    Prenda editar(UUID prendaId, String nombre, AtributosBusqueda atributos, Precio precio, List<Fotografia> fotografias);

    Prenda retirar(UUID prendaId);
}
