package edu.dosw.styleradar.itemcatalogo.application.usecase;

import edu.dosw.styleradar.itemcatalogo.domain.exception.DomainException;
import edu.dosw.styleradar.itemcatalogo.domain.exception.ResourceNotFoundException;
import edu.dosw.styleradar.itemcatalogo.domain.model.EstadoPublicacion;
import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.ModerarPublicacionUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.PrendaRepositoryPort;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class ModerarPublicacionUseCaseImpl implements ModerarPublicacionUseCase {

    private final PrendaRepositoryPort prendaRepository;

    public ModerarPublicacionUseCaseImpl(PrendaRepositoryPort prendaRepository) {
        this.prendaRepository = prendaRepository;
    }

    @Override
    public Prenda moderar(UUID prendaId, EstadoPublicacion decision, String motivo, UUID moderadorId) {
        Prenda prenda = prendaRepository.findById(prendaId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la prenda " + prendaId));
        if (decision == EstadoPublicacion.APROBADA) {
            prenda.aprobar(moderadorId);
        } else if (decision == EstadoPublicacion.RECHAZADA) {
            prenda.rechazar(moderadorId, motivo);
        } else {
            throw new DomainException("La decisión debe ser APROBADA o RECHAZADA");
        }
        return prendaRepository.save(prenda);
    }

    @Override
    public Map<UUID, List<Prenda>> pendientesPorAlmacen() {
        return prendaRepository.findByEstado(EstadoPublicacion.PENDIENTE).stream()
                .collect(Collectors.groupingBy(Prenda::getAlmacenId));
    }
}
