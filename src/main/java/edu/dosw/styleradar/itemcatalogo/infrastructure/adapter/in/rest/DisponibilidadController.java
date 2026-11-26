package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest;

import edu.dosw.styleradar.itemcatalogo.application.dto.response.DisponibilidadResponse;
import edu.dosw.styleradar.itemcatalogo.application.dto.response.TallasMedidasResponse;
import edu.dosw.styleradar.itemcatalogo.domain.model.Disponibilidad;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.ConsultarDisponibilidadUseCase;
import edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest.doc.DisponibilidadApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class DisponibilidadController implements DisponibilidadApi {

    private final ConsultarDisponibilidadUseCase consultarDisponibilidad;

    public DisponibilidadController(ConsultarDisponibilidadUseCase consultarDisponibilidad) {
        this.consultarDisponibilidad = consultarDisponibilidad;
    }

    @Override
    @GetMapping("/garments/{itemId}/availability")
    public ResponseEntity<DisponibilidadResponse> disponibilidad(@PathVariable UUID itemId) {
        Disponibilidad disponibilidad = consultarDisponibilidad.consultar(itemId);
        return ResponseEntity.ok(new DisponibilidadResponse(itemId.toString(), disponibilidad.stockTotal(),
                disponibilidad.tallasConStock()));
    }

    @Override
    @GetMapping("/garments/{itemId}/size-guide")
    public ResponseEntity<TallasMedidasResponse> tallasMedidas(@PathVariable UUID itemId) {
        ConsultarDisponibilidadUseCase.TallasYMedidas tabla = consultarDisponibilidad.tallasYMedidas(itemId);
        List<TallasMedidasResponse.RangoMedidas> medidas = tabla.medidas().stream()
                .map(m -> new TallasMedidasResponse.RangoMedidas(m.talla(), m.minPecho(), m.maxPecho(), m.minCintura(),
                        m.maxCintura(), m.minCadera(), m.maxCadera(), m.largo()))
                .toList();
        return ResponseEntity.ok(new TallasMedidasResponse(tabla.tallas(), medidas));
    }
}
