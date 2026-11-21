package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest;

import edu.dosw.styleradar.itemcatalogo.application.dto.response.DisponibilidadResponse;
import edu.dosw.styleradar.itemcatalogo.application.dto.response.TallasMedidasResponse;
import edu.dosw.styleradar.itemcatalogo.domain.model.Disponibilidad;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.ConsultarDisponibilidadUseCase;
import edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest.doc.DisponibilidadApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/catalogo/items")
public class DisponibilidadController implements DisponibilidadApi {

    private final ConsultarDisponibilidadUseCase consultarDisponibilidad;

    public DisponibilidadController(ConsultarDisponibilidadUseCase consultarDisponibilidad) {
        this.consultarDisponibilidad = consultarDisponibilidad;
    }

    @Override
    @GetMapping("/{itemId}/disponibilidad")
    public ResponseEntity<DisponibilidadResponse> disponibilidad(@PathVariable UUID itemId) {
        Disponibilidad disponibilidad = consultarDisponibilidad.consultar(itemId);
        return ResponseEntity.ok(new DisponibilidadResponse(itemId.toString(), disponibilidad.stockTotal(),
                disponibilidad.tallasConStock()));
    }

    @Override
    @GetMapping("/{itemId}/tallas-medidas")
    public ResponseEntity<TallasMedidasResponse> tallasMedidas(@PathVariable UUID itemId) {
        List<String> tallas = consultarDisponibilidad.tallasOfertadas(itemId).orElse(null);
        return ResponseEntity.ok(new TallasMedidasResponse(tallas, List.of()));
    }
}
