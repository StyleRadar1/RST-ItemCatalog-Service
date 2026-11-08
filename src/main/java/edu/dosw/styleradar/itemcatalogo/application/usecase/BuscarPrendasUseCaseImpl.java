package edu.dosw.styleradar.itemcatalogo.application.usecase;

import edu.dosw.styleradar.itemcatalogo.domain.exception.ResourceNotFoundException;
import edu.dosw.styleradar.itemcatalogo.domain.model.AlmacenResumen;
import edu.dosw.styleradar.itemcatalogo.domain.model.EstadoPublicacion;
import edu.dosw.styleradar.itemcatalogo.domain.model.FiltroBusqueda;
import edu.dosw.styleradar.itemcatalogo.domain.model.Ordenamiento;
import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.BuscarPrendasUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.AlmacenRepositoryPort;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.PrendaRepositoryPort;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BuscarPrendasUseCaseImpl implements BuscarPrendasUseCase {

    private static final int MAX_SIMILARES = 10;

    private final PrendaRepositoryPort prendaRepository;
    private final AlmacenRepositoryPort almacenRepository;

    public BuscarPrendasUseCaseImpl(PrendaRepositoryPort prendaRepository, AlmacenRepositoryPort almacenRepository) {
        this.prendaRepository = prendaRepository;
        this.almacenRepository = almacenRepository;
    }

    @Override
    public List<Prenda> buscar(FiltroBusqueda filtro) {
        Map<UUID, AlmacenResumen> almacenes = almacenesActivos();
        Comparator<Prenda> orden = filtro.orden() == Ordenamiento.REPUTACION
                ? Comparator.comparing((Prenda p) -> almacenes.get(p.getAlmacenId()).reputacion()).reversed()
                : Comparator.comparing(Prenda::getFechaPublicacion).reversed();
        return visibles(almacenes).stream()
                .filter(p -> p.cumple(filtro))
                .sorted(orden)
                .toList();
    }

    @Override
    public List<Prenda> similares(UUID prendaId) {
        Prenda prenda = prendaRepository.findById(prendaId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la prenda " + prendaId));
        return visibles(almacenesActivos()).stream()
                .filter(p -> p.esSimilarA(prenda))
                .limit(MAX_SIMILARES)
                .toList();
    }

    // Solo se muestran prendas aprobadas, disponibles y de almacenes activos
    private List<Prenda> visibles(Map<UUID, AlmacenResumen> almacenes) {
        return prendaRepository.findByEstado(EstadoPublicacion.APROBADA).stream()
                .filter(Prenda::esVisible)
                .filter(p -> almacenes.containsKey(p.getAlmacenId()))
                .toList();
    }

    private Map<UUID, AlmacenResumen> almacenesActivos() {
        return almacenRepository.findAll().stream()
                .filter(AlmacenResumen::activo)
                .collect(Collectors.toMap(AlmacenResumen::almacenId, Function.identity()));
    }
}
