package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest;

import edu.dosw.styleradar.itemcatalogo.application.dto.request.ModeracionRequest;
import edu.dosw.styleradar.itemcatalogo.application.dto.response.PrendaResponse;
import edu.dosw.styleradar.itemcatalogo.application.mapper.in.PrendaMapperIn;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.ModerarPublicacionUseCase;
import edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest.doc.ModeracionApi;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/prendas")
public class ModeracionController implements ModeracionApi {

    private final ModerarPublicacionUseCase moderarPublicacion;

    public ModeracionController(ModerarPublicacionUseCase moderarPublicacion) {
        this.moderarPublicacion = moderarPublicacion;
    }

    @Override
    @GetMapping("/pendientes")
    public ResponseEntity<Map<UUID, List<PrendaResponse>>> pendientes() {
        Map<UUID, List<PrendaResponse>> grupos = new LinkedHashMap<>();
        moderarPublicacion.pendientesPorAlmacen()
                .forEach((almacenId, prendas) -> grupos.put(almacenId, PrendaMapperIn.toResponse(prendas)));
        return ResponseEntity.ok(grupos);
    }

    @Override
    @PutMapping("/{id}/moderacion")
    public ResponseEntity<PrendaResponse> moderar(@PathVariable UUID id, @Valid @RequestBody ModeracionRequest request) {
        return ResponseEntity.ok(PrendaMapperIn.toResponse(
                moderarPublicacion.moderar(id, request.decision(), request.motivo(), request.moderadorId())));
    }
}
