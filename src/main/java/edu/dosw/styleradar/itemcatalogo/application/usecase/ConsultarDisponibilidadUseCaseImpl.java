package edu.dosw.styleradar.itemcatalogo.application.usecase;

import edu.dosw.styleradar.itemcatalogo.domain.exception.ResourceNotFoundException;
import edu.dosw.styleradar.itemcatalogo.domain.model.Disponibilidad;
import edu.dosw.styleradar.itemcatalogo.domain.model.GuiaMedidas;
import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;
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
    public TallasYMedidas tallasYMedidas(UUID prendaId) {
        Prenda prenda = verificarPrenda(prendaId);
        List<String> tallas = stock.disponibilidad(prendaId).map(Disponibilidad::tallasConStock).orElse(null);
        List<GuiaMedidas.MedidasTalla> medidas = tallas == null ? List.of() : tallas.stream()
                .map(talla -> GuiaMedidas.de(prenda.getAtributos().tipo(), talla))
                .flatMap(Optional::stream)
                .toList();
        return new TallasYMedidas(tallas, medidas);
    }

    private Prenda verificarPrenda(UUID prendaId) {
        return prendaRepository.findById(prendaId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la prenda " + prendaId, "PRENDA_NO_ENCONTRADA"));
    }
}
