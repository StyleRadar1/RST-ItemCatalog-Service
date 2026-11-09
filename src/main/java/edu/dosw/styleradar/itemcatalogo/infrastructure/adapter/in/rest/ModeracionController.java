package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest;

import edu.dosw.styleradar.itemcatalogo.application.dto.request.ModeracionRequest;
import edu.dosw.styleradar.itemcatalogo.application.dto.response.PendientesResponse;
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

import java.util.List;
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
    public ResponseEntity<PendientesResponse> pendientes() {
        return ResponseEntity.ok(PrendaMapperIn.toPendientes(moderarPublicacion.pendientesPorGrupo()));
    }

    @Override
    @PutMapping("/pendientes/almacenes/{almacenId}/moderacion")
    public ResponseEntity<List<PrendaResponse>> moderarGrupo(@PathVariable UUID almacenId,
                                                             @Valid @RequestBody ModeracionRequest request) {
        return ResponseEntity.ok(PrendaMapperIn.toResponse(moderarPublicacion.moderarGrupo(
                almacenId, request.decision(), request.motivo(), request.moderadorId())));
    }

    @Override
    @PutMapping("/{id}/moderacion")
    public ResponseEntity<PrendaResponse> moderar(@PathVariable UUID id, @Valid @RequestBody ModeracionRequest request) {
        return ResponseEntity.ok(PrendaMapperIn.toResponse(
                moderarPublicacion.moderar(id, request.decision(), request.motivo(), request.moderadorId())));
    }
}
