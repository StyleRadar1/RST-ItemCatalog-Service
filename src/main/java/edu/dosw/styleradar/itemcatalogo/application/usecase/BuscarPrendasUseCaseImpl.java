package edu.dosw.styleradar.itemcatalogo.application.usecase;

import edu.dosw.styleradar.itemcatalogo.domain.exception.ParametroInvalidoException;
import edu.dosw.styleradar.itemcatalogo.domain.exception.ResourceNotFoundException;
import edu.dosw.styleradar.itemcatalogo.domain.model.AlmacenResumen;
import edu.dosw.styleradar.itemcatalogo.domain.model.EstadoPublicacion;
import edu.dosw.styleradar.itemcatalogo.domain.model.FiltroBusqueda;
import edu.dosw.styleradar.itemcatalogo.domain.model.Ordenamiento;
import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.BuscarPrendasUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.AlmacenRepositoryPort;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.DistanciaAlmacenPort;
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
    private final DistanciaAlmacenPort distanciaAlmacen;

    public BuscarPrendasUseCaseImpl(PrendaRepositoryPort prendaRepository, AlmacenRepositoryPort almacenRepository,
                                    DistanciaAlmacenPort distanciaAlmacen) {
        this.prendaRepository = prendaRepository;
        this.almacenRepository = almacenRepository;
        this.distanciaAlmacen = distanciaAlmacen;
    }

    @Override
    public List<Prenda> buscar(FiltroBusqueda filtro) {
        if (filtro.orden() == Ordenamiento.DISTANCIA && filtro.ubicacion() == null) {
            throw new ParametroInvalidoException("Para ordenar por distancia se debe indicar la ubicación (lat y lng)");
        }
        Map<UUID, AlmacenResumen> almacenes = almacenesActivos();
        List<Prenda> resultado = visibles(almacenes).stream()
                .filter(p -> p.cumple(filtro))
                .toList();
        return resultado.stream()
                .sorted(orden(filtro, almacenes, resultado))
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

    private Comparator<Prenda> orden(FiltroBusqueda filtro, Map<UUID, AlmacenResumen> almacenes, List<Prenda> resultado) {
        if (filtro.orden() == Ordenamiento.DISTANCIA) {
            Map<UUID, Double> kilometros = resultado.stream()
                    .map(Prenda::getAlmacenId)
                    .distinct()
                    .collect(Collectors.toMap(Function.identity(), id -> distanciaAlmacen.kilometros(id, filtro.ubicacion())));
            return Comparator.comparingDouble(p -> kilometros.get(p.getAlmacenId()));
        }
        if (filtro.orden() == Ordenamiento.REPUTACION) {
            return Comparator.comparing((Prenda p) -> almacenes.get(p.getAlmacenId()).reputacion()).reversed();
        }
        return Comparator.comparing(Prenda::getFechaPublicacion).reversed();
    }

    private Map<UUID, AlmacenResumen> almacenesActivos() {
        return almacenRepository.findAll().stream()
                .filter(AlmacenResumen::activo)
                .collect(Collectors.toMap(AlmacenResumen::almacenId, Function.identity()));
    }
}
