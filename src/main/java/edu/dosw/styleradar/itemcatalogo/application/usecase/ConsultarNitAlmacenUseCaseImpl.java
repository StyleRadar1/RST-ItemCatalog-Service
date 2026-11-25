package edu.dosw.styleradar.itemcatalogo.application.usecase;

import edu.dosw.styleradar.itemcatalogo.domain.model.AlmacenResumen;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.ConsultarNitAlmacenUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.AlmacenRepositoryPort;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class ConsultarNitAlmacenUseCaseImpl implements ConsultarNitAlmacenUseCase {

    private final AlmacenRepositoryPort almacenRepository;

    public ConsultarNitAlmacenUseCaseImpl(AlmacenRepositoryPort almacenRepository) {
        this.almacenRepository = almacenRepository;
    }

    @Override
    public Map<UUID, String> nitsDe(Collection<UUID> almacenIds) {
        Map<UUID, String> nits = new HashMap<>();
        if (almacenIds == null || almacenIds.isEmpty()) {
            return nits;
        }
        almacenRepository.findAllById(almacenIds.stream().filter(Objects::nonNull).distinct().toList()).stream()
                .filter(almacen -> almacen.nit() != null)
                .forEach(almacen -> nits.put(almacen.almacenId(), almacen.nit()));
        return nits;
    }
}
