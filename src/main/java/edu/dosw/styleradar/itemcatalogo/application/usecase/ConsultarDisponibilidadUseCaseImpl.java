package edu.dosw.styleradar.itemcatalogo.application.usecase;

import edu.dosw.styleradar.itemcatalogo.domain.exception.ResourceNotFoundException;
import edu.dosw.styleradar.itemcatalogo.domain.model.Disponibilidad;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.ConsultarDisponibilidadUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.PrendaRepositoryPort;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.StockPrendaPort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ConsultarDisponibilidadUseCaseImpl implements ConsultarDisponibilidadUseCase {

    private final PrendaRepositoryPort prendaRepository;
    private final StockPrendaPort stock;

    public ConsultarDisponibilidadUseCaseImpl(PrendaRepositoryPort prendaRepository, StockPrendaPort stock) {
        this.prendaRepository = prendaRepository;
        this.stock = stock;
    }

    @Override
    public Disponibilidad consultar(UUID prendaId) {
        verificarPrenda(prendaId);
        return stock.disponibilidad(prendaId).orElse(Disponibilidad.sinStock());
    }

    @Override
    public Optional<List<String>> tallasOfertadas(UUID prendaId) {
        verificarPrenda(prendaId);
        return stock.disponibilidad(prendaId).map(Disponibilidad::tallasConStock);
    }

    private void verificarPrenda(UUID prendaId) {
        if (prendaRepository.findById(prendaId).isEmpty()) {
            throw new ResourceNotFoundException("No existe la prenda " + prendaId, "PRENDA_NO_ENCONTRADA");
        }
    }
}
