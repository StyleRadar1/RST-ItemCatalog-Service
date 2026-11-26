package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest;

import edu.dosw.styleradar.itemcatalogo.application.dto.request.ModeracionRequest;
import edu.dosw.styleradar.itemcatalogo.application.dto.response.PendientesResponse;
import edu.dosw.styleradar.itemcatalogo.application.dto.response.PrendaResponse;
import edu.dosw.styleradar.itemcatalogo.application.mapper.in.PrendaMapperIn;
import edu.dosw.styleradar.itemcatalogo.domain.model.IdAlmacen;
import edu.dosw.styleradar.itemcatalogo.domain.model.GrupoPendientes;
import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.ConsultarNitAlmacenUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.ModerarPublicacionUseCase;
import edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest.doc.ModeracionApi;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class ModeracionController implements ModeracionApi {

    private final ModerarPublicacionUseCase moderarPublicacion;
    private final ConsultarNitAlmacenUseCase nitAlmacenes;

    public ModeracionController(ModerarPublicacionUseCase moderarPublicacion, ConsultarNitAlmacenUseCase nitAlmacenes) {
        this.moderarPublicacion = moderarPublicacion;
        this.nitAlmacenes = nitAlmacenes;
    }

    @Override
    @GetMapping("/garments/pending")
    public ResponseEntity<PendientesResponse> pendientes(
            @RequestHeader(value = Identidad.HEADER_ROL, required = false) String rol,
            @RequestHeader(value = Identidad.HEADER_ROLES, required = false) String roles) {
        Identidad.exigirAdmin(rol, roles);
        List<GrupoPendientes> grupos = moderarPublicacion.pendientesPorGrupo();
        return ResponseEntity.ok(PrendaMapperIn.toPendientes(grupos,
                nitAlmacenes.nitsDe(grupos.stream().map(GrupoPendientes::almacenId).toList())));
    }

    @Override
    @PutMapping("/garments/pending/stores/{storeId}/moderation")
    public ResponseEntity<List<PrendaResponse>> moderarGrupo(@PathVariable("storeId") String storeId,
            @RequestHeader(value = Identidad.HEADER_ROL, required = false) String rol,
            @RequestHeader(value = Identidad.HEADER_ROLES, required = false) String roles,
            @RequestHeader(value = Identidad.HEADER_USUARIO, required = false) String usuarioId,
            @Valid @RequestBody ModeracionRequest request) {
        Identidad.exigirAdmin(rol, roles);
        UUID almacenId = IdAlmacen.desdeTexto(storeId);
        return ResponseEntity.ok(PrendaMapperIn.toResponse(moderarPublicacion.moderarGrupo(
                almacenId, request.decision(), request.motivo(), Identidad.moderador(usuarioId, request.moderadorId())),
                nitAlmacenes.nitsDe(List.of(almacenId))));
    }

    @Override
    @PutMapping("/garments/{id}/moderation")
    public ResponseEntity<PrendaResponse> moderar(@PathVariable UUID id,
            @RequestHeader(value = Identidad.HEADER_ROL, required = false) String rol,
            @RequestHeader(value = Identidad.HEADER_ROLES, required = false) String roles,
            @RequestHeader(value = Identidad.HEADER_USUARIO, required = false) String usuarioId,
            @Valid @RequestBody ModeracionRequest request) {
        Identidad.exigirAdmin(rol, roles);
        Prenda prenda = moderarPublicacion.moderar(id, request.decision(), request.motivo(),
                Identidad.moderador(usuarioId, request.moderadorId()));
        return ResponseEntity.ok(PrendaMapperIn.toResponse(prenda, NitsDePrendas.de(nitAlmacenes, List.of(prenda))));
    }
}
