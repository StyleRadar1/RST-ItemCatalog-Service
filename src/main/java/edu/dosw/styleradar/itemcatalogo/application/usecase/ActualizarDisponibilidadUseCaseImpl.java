package edu.dosw.styleradar.itemcatalogo.application.usecase;

import edu.dosw.styleradar.itemcatalogo.domain.port.in.ActualizarDisponibilidadUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.AlmacenRepositoryPort;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.PrendaRepositoryPort;

import java.util.UUID;

public class ActualizarDisponibilidadUseCaseImpl implements ActualizarDisponibilidadUseCase {

    private final PrendaRepositoryPort prendaRepository;
    private final AlmacenRepositoryPort almacenRepository;

    public ActualizarDisponibilidadUseCaseImpl(PrendaRepositoryPort prendaRepository, AlmacenRepositoryPort almacenRepository) {
        this.prendaRepository = prendaRepository;
        this.almacenRepository = almacenRepository;
    }

    @Override
    public void marcarAgotada(UUID prendaId) {
        prendaRepository.findById(prendaId).ifPresent(prenda -> {
            prenda.marcarAgotada();
            prendaRepository.save(prenda);
        });
    }

    @Override
    public void suspenderAlmacen(UUID almacenId) {
        almacenRepository.findById(almacenId).ifPresent(almacen -> almacenRepository.save(almacen.suspender()));
    }
}
