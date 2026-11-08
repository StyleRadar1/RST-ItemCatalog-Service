package edu.dosw.styleradar.itemcatalogo.application.usecase;

import edu.dosw.styleradar.itemcatalogo.domain.exception.DomainException;
import edu.dosw.styleradar.itemcatalogo.domain.exception.ResourceNotFoundException;
import edu.dosw.styleradar.itemcatalogo.domain.model.AlmacenResumen;
import edu.dosw.styleradar.itemcatalogo.domain.model.AtributosBusqueda;
import edu.dosw.styleradar.itemcatalogo.domain.model.Fotografia;
import edu.dosw.styleradar.itemcatalogo.domain.model.Precio;
import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.GestionarPrendaUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.AlmacenRepositoryPort;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.PrendaRepositoryPort;

import java.util.List;
import java.util.UUID;

public class GestionarPrendaUseCaseImpl implements GestionarPrendaUseCase {

    private final PrendaRepositoryPort prendaRepository;
    private final AlmacenRepositoryPort almacenRepository;

    public GestionarPrendaUseCaseImpl(PrendaRepositoryPort prendaRepository, AlmacenRepositoryPort almacenRepository) {
        this.prendaRepository = prendaRepository;
        this.almacenRepository = almacenRepository;
    }

    @Override
    public Prenda publicar(UUID almacenId, String nombre, AtributosBusqueda atributos, Precio precio, List<Fotografia> fotografias) {
        AlmacenResumen almacen = almacenRepository.findById(almacenId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el almacén " + almacenId));
        if (!almacen.activo()) {
            throw new DomainException("El almacén está suspendido y no puede publicar prendas");
        }
        return prendaRepository.save(Prenda.publicar(almacenId, nombre, atributos, precio, fotografias));
    }

    @Override
    public Prenda obtener(UUID prendaId) {
        return prendaRepository.findById(prendaId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la prenda " + prendaId));
    }

    @Override
    public Prenda editar(UUID prendaId, String nombre, AtributosBusqueda atributos, Precio precio, List<Fotografia> fotografias) {
        Prenda prenda = obtener(prendaId);
        prenda.editar(nombre, atributos, precio, fotografias);
        return prendaRepository.save(prenda);
    }

    @Override
    public Prenda retirar(UUID prendaId) {
        Prenda prenda = obtener(prendaId);
        prenda.retirar();
        return prendaRepository.save(prenda);
    }
}
