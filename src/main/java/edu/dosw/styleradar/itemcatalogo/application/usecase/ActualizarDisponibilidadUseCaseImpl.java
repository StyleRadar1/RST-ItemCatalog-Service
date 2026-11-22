package edu.dosw.styleradar.itemcatalogo.application.usecase;

import edu.dosw.styleradar.itemcatalogo.domain.model.Disponibilidad;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.ActualizarDisponibilidadUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.AlmacenRepositoryPort;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.PrendaRepositoryPort;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.StockPrendaPort;

import java.util.Optional;
import java.util.UUID;

public class ActualizarDisponibilidadUseCaseImpl implements ActualizarDisponibilidadUseCase {

    private final PrendaRepositoryPort prendaRepository;
    private final AlmacenRepositoryPort almacenRepository;
    private final StockPrendaPort stock;

    public ActualizarDisponibilidadUseCaseImpl(PrendaRepositoryPort prendaRepository, AlmacenRepositoryPort almacenRepository,
                                               StockPrendaPort stock) {
        this.prendaRepository = prendaRepository;
        this.almacenRepository = almacenRepository;
        this.stock = stock;
    }

    // Un evento de inventario es de una talla; la prenda se oculta solo si todas sus tallas quedan en 0 (SR-RF-57)
    @Override
    public void sincronizarStock(UUID prendaId) {
        prendaRepository.findById(prendaId).ifPresent(prenda -> {
            Optional<Disponibilidad> disponibilidad = stock.disponibilidad(prendaId);
            if (disponibilidad.isEmpty()) {
                return;
            }
            boolean conStock = disponibilidad.get().stockTotal() > 0;
            if (conStock == prenda.isDisponible()) {
                return;
            }
            if (conStock) {
                prenda.marcarDisponible();
            } else {
                prenda.marcarAgotada();
            }
            prendaRepository.save(prenda);
        });
    }

    @Override
    public void suspenderAlmacen(UUID almacenId) {
        almacenRepository.findById(almacenId).ifPresent(almacen -> almacenRepository.save(almacen.suspender()));
    }
}
