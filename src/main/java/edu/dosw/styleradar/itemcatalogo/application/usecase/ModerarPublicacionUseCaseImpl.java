package edu.dosw.styleradar.itemcatalogo.application.usecase;

import edu.dosw.styleradar.itemcatalogo.domain.exception.DomainException;
import edu.dosw.styleradar.itemcatalogo.domain.exception.ResourceNotFoundException;
import edu.dosw.styleradar.itemcatalogo.domain.model.AlmacenResumen;
import edu.dosw.styleradar.itemcatalogo.domain.model.EstadoPublicacion;
import edu.dosw.styleradar.itemcatalogo.domain.model.GrupoPendientes;
import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.ModerarPublicacionUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.AlmacenRepositoryPort;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.PrendaRepositoryPort;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class ModerarPublicacionUseCaseImpl implements ModerarPublicacionUseCase {

    private final PrendaRepositoryPort prendaRepository;
    private final AlmacenRepositoryPort almacenRepository;

    public ModerarPublicacionUseCaseImpl(PrendaRepositoryPort prendaRepository, AlmacenRepositoryPort almacenRepository) {
        this.prendaRepository = prendaRepository;
        this.almacenRepository = almacenRepository;
    }

    @Override
    public Prenda moderar(UUID prendaId, EstadoPublicacion decision, String motivo, UUID moderadorId) {
        Prenda prenda = prendaRepository.findById(prendaId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la prenda " + prendaId));
        aplicar(prenda, decision, motivo, moderadorId);
        return prendaRepository.save(prenda);
    }

    @Override
    public List<GrupoPendientes> pendientesPorGrupo() {
        Map<UUID, String> almacenes = almacenRepository.findAll().stream()
                .collect(Collectors.toMap(AlmacenResumen::almacenId, AlmacenResumen::nombre));
        return prendaRepository.findByEstado(EstadoPublicacion.PENDIENTE).stream()
                .collect(Collectors.groupingBy(Prenda::getAlmacenId))
                .entrySet().stream()
                .map(grupo -> new GrupoPendientes(grupo.getKey(), almacenes.get(grupo.getKey()), grupo.getValue()))
                .sorted(Comparator.comparingInt(GrupoPendientes::total).reversed())
                .toList();
    }

    @Override
    public List<Prenda> moderarGrupo(UUID almacenId, EstadoPublicacion decision, String motivo, UUID moderadorId) {
        List<Prenda> pendientes = prendaRepository.findByAlmacenId(almacenId).stream()
                .filter(prenda -> prenda.getEstado() == EstadoPublicacion.PENDIENTE)
                .toList();
        if (pendientes.isEmpty()) {
            throw new ResourceNotFoundException("El almacén " + almacenId + " no tiene publicaciones pendientes");
        }
        pendientes.forEach(prenda -> aplicar(prenda, decision, motivo, moderadorId));
        return pendientes.stream().map(prendaRepository::save).toList();
    }

    private void aplicar(Prenda prenda, EstadoPublicacion decision, String motivo, UUID moderadorId) {
        if (decision == EstadoPublicacion.APROBADA) {
            prenda.aprobar(moderadorId);
        } else if (decision == EstadoPublicacion.RECHAZADA) {
            prenda.rechazar(moderadorId, motivo);
        } else {
            throw new DomainException("La decisión debe ser APROBADA o RECHAZADA");
        }
    }
}
