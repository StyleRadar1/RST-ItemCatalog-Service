package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest;

import edu.dosw.styleradar.itemcatalogo.application.dto.request.PrendaRequest;
import edu.dosw.styleradar.itemcatalogo.application.dto.response.PrendaResponse;
import edu.dosw.styleradar.itemcatalogo.application.mapper.in.PrendaMapperIn;
import edu.dosw.styleradar.itemcatalogo.domain.model.Solicitante;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.GestionarPrendaUseCase;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/prendas")
public class PrendaController implements PrendaApi {

    static final String HEADER_ALMACEN = "X-Almacen-Id";
    static final String HEADER_ROL = "X-User-Role";
    static final String ROL_ADMIN = "ADMIN";

    private final GestionarPrendaUseCase gestionarPrenda;

    public PrendaController(GestionarPrendaUseCase gestionarPrenda) {
        this.gestionarPrenda = gestionarPrenda;
    }

    @Override
    @PostMapping
    public ResponseEntity<PrendaResponse> publicar(@Valid @RequestBody PrendaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(PrendaMapperIn.toResponse(gestionarPrenda.publicar(
                request.almacenId(), request.nombre(), PrendaMapperIn.toAtributos(request),
                PrendaMapperIn.toPrecio(request), PrendaMapperIn.toFotografias(request))));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<PrendaResponse> obtener(@PathVariable UUID id) {
        return ResponseEntity.ok(PrendaMapperIn.toResponse(gestionarPrenda.obtener(id)));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<PrendaResponse> editar(@PathVariable UUID id,
                                                 @RequestHeader(value = HEADER_ALMACEN, required = false) UUID almacenId,
                                                 @RequestHeader(value = HEADER_ROL, required = false) String rol,
                                                 @Valid @RequestBody PrendaRequest request) {
        return ResponseEntity.ok(PrendaMapperIn.toResponse(gestionarPrenda.editar(id, solicitante(almacenId, rol),
                request.nombre(), PrendaMapperIn.toAtributos(request), PrendaMapperIn.toPrecio(request),
                PrendaMapperIn.toFotografias(request))));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<PrendaResponse> retirar(@PathVariable UUID id,
                                                  @RequestHeader(value = HEADER_ALMACEN, required = false) UUID almacenId,
                                                  @RequestHeader(value = HEADER_ROL, required = false) String rol) {
        return ResponseEntity.ok(PrendaMapperIn.toResponse(gestionarPrenda.retirar(id, solicitante(almacenId, rol))));
    }

    private static Solicitante solicitante(UUID almacenId, String rol) {
        return new Solicitante(almacenId, ROL_ADMIN.equalsIgnoreCase(rol));
    }
}
