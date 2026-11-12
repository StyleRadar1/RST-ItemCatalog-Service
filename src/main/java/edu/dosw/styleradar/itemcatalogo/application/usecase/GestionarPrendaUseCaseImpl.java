package edu.dosw.styleradar.itemcatalogo.application.usecase;

import edu.dosw.styleradar.itemcatalogo.domain.event.PrendaPublicadaEvent;
import edu.dosw.styleradar.itemcatalogo.domain.event.PrendaRetiradaEvent;
import edu.dosw.styleradar.itemcatalogo.domain.exception.DomainException;
import edu.dosw.styleradar.itemcatalogo.domain.exception.ResourceNotFoundException;
import edu.dosw.styleradar.itemcatalogo.domain.model.AlmacenResumen;
import edu.dosw.styleradar.itemcatalogo.domain.model.AtributosBusqueda;
import edu.dosw.styleradar.itemcatalogo.domain.model.Fotografia;
import edu.dosw.styleradar.itemcatalogo.domain.model.Precio;
import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;
import edu.dosw.styleradar.itemcatalogo.domain.model.Solicitante;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.GestionarPrendaUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.AlmacenRepositoryPort;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.EventPublisherPort;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.PrendaRepositoryPort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class GestionarPrendaUseCaseImpl implements GestionarPrendaUseCase {

    private static final String MOTIVO_RETIRO = "Retirada por el almacén";

    private final PrendaRepositoryPort prendaRepository;
    private final AlmacenRepositoryPort almacenRepository;
    private final EventPublisherPort eventPublisher;

    public GestionarPrendaUseCaseImpl(PrendaRepositoryPort prendaRepository, AlmacenRepositoryPort almacenRepository,
                                      EventPublisherPort eventPublisher) {
        this.prendaRepository = prendaRepository;
        this.almacenRepository = almacenRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Prenda publicar(UUID almacenId, String nombre, AtributosBusqueda atributos, Precio precio, List<Fotografia> fotografias) {
        AlmacenResumen almacen = almacenRepository.findById(almacenId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el almacén " + almacenId));
        if (!almacen.activo()) {
            throw new DomainException("El almacén está suspendido y no puede publicar prendas");
        }
        // Según la guía de RabbitMQ, prenda.publicada se avisa al crear o actualizar la prenda
        Prenda prenda = prendaRepository.save(Prenda.publicar(almacenId, nombre, atributos, precio, fotografias));
        eventPublisher.publicar(PrendaPublicadaEvent.de(prenda));
        return prenda;
    }

    @Override
    public Prenda obtener(UUID prendaId) {
        return prendaRepository.findById(prendaId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la prenda " + prendaId));
    }

    @Override
    public Prenda editar(UUID prendaId, Solicitante solicitante, String nombre, AtributosBusqueda atributos, Precio precio,
                         List<Fotografia> fotografias) {
        Prenda prenda = obtener(prendaId);
        prenda.verificarPermiso(solicitante);
        prenda.editar(nombre, atributos, precio, fotografias);
        Prenda editada = prendaRepository.save(prenda);
        eventPublisher.publicar(PrendaPublicadaEvent.de(editada));
        return editada;
    }

    @Override
    public Prenda retirar(UUID prendaId, Solicitante solicitante) {
        Prenda prenda = obtener(prendaId);
        prenda.verificarPermiso(solicitante);
        prenda.retirar();
        Prenda retirada = prendaRepository.save(prenda);
        eventPublisher.publicar(new PrendaRetiradaEvent(prendaId, prenda.getAlmacenId(), MOTIVO_RETIRO, LocalDateTime.now()));
        return retirada;
    }
}
