package edu.dosw.styleradar.itemcatalogo.application.usecase;

import edu.dosw.styleradar.itemcatalogo.domain.exception.ResourceNotFoundException;
import edu.dosw.styleradar.itemcatalogo.domain.model.AlmacenResumen;
import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.ConsultarCatalogoAlmacenUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.AlmacenRepositoryPort;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.PrendaRepositoryPort;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class ConsultarCatalogoAlmacenUseCaseImpl implements ConsultarCatalogoAlmacenUseCase {

    private static final int DIAS_NOVEDADES = 7;

    private final PrendaRepositoryPort prendaRepository;
    private final AlmacenRepositoryPort almacenRepository;

    public ConsultarCatalogoAlmacenUseCaseImpl(PrendaRepositoryPort prendaRepository, AlmacenRepositoryPort almacenRepository) {
        this.prendaRepository = prendaRepository;
        this.almacenRepository = almacenRepository;
    }

    @Override
    public List<Prenda> catalogo(UUID almacenId) {
        AlmacenResumen almacen = almacenRepository.findById(almacenId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el almacén " + almacenId));
        if (!almacen.activo()) {
            return List.of();
        }
        return prendaRepository.findByAlmacenId(almacenId).stream()
                .filter(Prenda::esVisible)
                .sorted(Comparator.comparing(Prenda::getFechaPublicacion).reversed())
                .toList();
    }

    @Override
    public List<Prenda> novedades(UUID almacenId) {
        LocalDateTime desde = LocalDateTime.now().minusDays(DIAS_NOVEDADES);
        return catalogo(almacenId).stream()
                .filter(p -> p.getFechaPublicacion().isAfter(desde))
                .toList();
    }
}
