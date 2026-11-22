package edu.dosw.styleradar.itemcatalogo.domain.port.in;

import edu.dosw.styleradar.itemcatalogo.domain.model.Disponibilidad;
import edu.dosw.styleradar.itemcatalogo.domain.model.GuiaMedidas;

import java.util.List;
import java.util.UUID;

public interface ConsultarDisponibilidadUseCase {

    Disponibilidad consultar(UUID prendaId);

    TallasYMedidas tallasYMedidas(UUID prendaId);

    record TallasYMedidas(List<String> tallas, List<GuiaMedidas.MedidasTalla> medidas) {
    }
}
