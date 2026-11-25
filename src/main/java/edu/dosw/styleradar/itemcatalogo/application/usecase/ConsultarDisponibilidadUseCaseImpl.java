package edu.dosw.styleradar.itemcatalogo.application.usecase;

import edu.dosw.styleradar.itemcatalogo.domain.exception.ResourceNotFoundException;
import edu.dosw.styleradar.itemcatalogo.domain.model.Disponibilidad;
import edu.dosw.styleradar.itemcatalogo.domain.model.GuiaMedidas;
import edu.dosw.styleradar.itemcatalogo.domain.model.MedidaPrenda;
import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.ConsultarDisponibilidadUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.MedidaPrendaRepositoryPort;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.PrendaRepositoryPort;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.StockPrendaPort;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ConsultarDisponibilidadUseCaseImpl implements ConsultarDisponibilidadUseCase {

    private final PrendaRepositoryPort prendaRepository;
    private final StockPrendaPort stock;
    private final MedidaPrendaRepositoryPort medidaRepository;

    public ConsultarDisponibilidadUseCaseImpl(PrendaRepositoryPort prendaRepository, StockPrendaPort stock,
                                              MedidaPrendaRepositoryPort medidaRepository) {
        this.prendaRepository = prendaRepository;
        this.stock = stock;
        this.medidaRepository = medidaRepository;
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
        if (tallas == null) {
            return new TallasYMedidas(null, List.of());
        }
        Map<String, MedidaPrenda> propias = medidaRepository.findByPrendaId(prendaId).stream()
                .collect(Collectors.toMap(MedidaPrenda::clave, Function.identity(), (a, b) -> a));
        List<GuiaMedidas.MedidasTalla> medidas = tallas.stream()
                .map(talla -> Optional.ofNullable(propias.get(talla.trim().toUpperCase(Locale.ROOT)))
                        .map(propia -> propia.comoMedidasTalla(talla))
                        .or(() -> GuiaMedidas.de(prenda.getAtributos().tipo(), talla)))
                .flatMap(Optional::stream)
                .toList();
        return new TallasYMedidas(tallas, medidas);
    }

    private Prenda verificarPrenda(UUID prendaId) {
        return prendaRepository.findById(prendaId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la prenda " + prendaId, "PRENDA_NO_ENCONTRADA"));
    }
}
