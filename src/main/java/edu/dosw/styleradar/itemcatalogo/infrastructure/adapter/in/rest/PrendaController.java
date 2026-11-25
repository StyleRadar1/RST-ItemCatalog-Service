package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest;

import edu.dosw.styleradar.itemcatalogo.application.dto.request.PrendaRequest;
import edu.dosw.styleradar.itemcatalogo.application.dto.response.PrendaResponse;
import edu.dosw.styleradar.itemcatalogo.application.mapper.in.PrendaMapperIn;
import edu.dosw.styleradar.itemcatalogo.domain.exception.ParametroInvalidoException;
import edu.dosw.styleradar.itemcatalogo.domain.model.Solicitante;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.GestionarPrendaUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.SincronizarAlmacenesUseCase;
import edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest.doc.PrendaApi;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
public class PrendaController implements PrendaApi {

    private final GestionarPrendaUseCase gestionarPrenda;
    private final SincronizarAlmacenesUseCase almacenes;

    public PrendaController(GestionarPrendaUseCase gestionarPrenda, SincronizarAlmacenesUseCase almacenes) {
        this.gestionarPrenda = gestionarPrenda;
        this.almacenes = almacenes;
    }

    @Override
    @PostMapping("/garments")
    public ResponseEntity<PrendaResponse> publicar(@Valid @RequestBody PrendaRequest request,
            @RequestHeader(value = Identidad.HEADER_USUARIO, required = false) String usuarioId) {
        UUID almacenId = almacenes.almacenDelUsuario(Identidad.usuario(usuarioId)).orElse(request.almacenId());
        if (almacenId == null) {
            throw new ParametroInvalidoException("La solicitud tiene campos inválidos", "DATOS_INVALIDOS",
                    Map.of("almacenId", "El almacén es obligatorio"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(PrendaMapperIn.toResponse(gestionarPrenda.publicar(
                almacenId, request.nombre(), PrendaMapperIn.toAtributos(request),
                PrendaMapperIn.toPrecio(request), PrendaMapperIn.toFotografias(request))));
    }

    @Override
    @GetMapping("/garments/{id}")
    public ResponseEntity<PrendaResponse> obtener(@PathVariable UUID id) {
        return ResponseEntity.ok(PrendaMapperIn.toResponse(gestionarPrenda.obtener(id)));
    }

    @Override
    @PutMapping("/garments/{id}")
    public ResponseEntity<PrendaResponse> editar(@PathVariable UUID id,
            @RequestHeader(value = Identidad.HEADER_ALMACEN, required = false) UUID almacenId,
            @RequestHeader(value = Identidad.HEADER_ROL, required = false) String rol,
            @RequestHeader(value = Identidad.HEADER_ROLES, required = false) String roles,
            @RequestHeader(value = Identidad.HEADER_USUARIO, required = false) String usuarioId,
            @Valid @RequestBody PrendaRequest request) {
        return ResponseEntity.ok(PrendaMapperIn.toResponse(gestionarPrenda.editar(id,
                solicitante(almacenId, rol, roles, usuarioId), request.nombre(), PrendaMapperIn.toAtributos(request),
                PrendaMapperIn.toPrecio(request), PrendaMapperIn.toFotografias(request))));
    }

    @Override
    @DeleteMapping("/garments/{id}")
    public ResponseEntity<PrendaResponse> retirar(@PathVariable UUID id,
            @RequestHeader(value = Identidad.HEADER_ALMACEN, required = false) UUID almacenId,
            @RequestHeader(value = Identidad.HEADER_ROL, required = false) String rol,
            @RequestHeader(value = Identidad.HEADER_ROLES, required = false) String roles,
            @RequestHeader(value = Identidad.HEADER_USUARIO, required = false) String usuarioId) {
        return ResponseEntity.ok(PrendaMapperIn.toResponse(
                gestionarPrenda.retirar(id, solicitante(almacenId, rol, roles, usuarioId))));
    }

    private Solicitante solicitante(UUID almacenId, String rol, String roles, String usuarioId) {
        boolean admin = Identidad.esAdmin(rol, roles);
        UUID almacen = almacenId != null || admin ? almacenId
                : almacenes.almacenDelUsuario(Identidad.usuario(usuarioId)).orElse(null);
        return new Solicitante(almacen, admin);
    }
}
