package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest;

import edu.dosw.styleradar.itemcatalogo.domain.model.IdAlmacen;
import edu.dosw.styleradar.itemcatalogo.application.dto.request.MedidasPrendaRequest;
import edu.dosw.styleradar.itemcatalogo.application.dto.response.MedidasPrendaResponse;
import edu.dosw.styleradar.itemcatalogo.application.mapper.in.MedidaPrendaMapperIn;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.GestionarMedidasPrendaUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.SincronizarAlmacenesUseCase;
import edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest.doc.MedidasPrendaApi;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class MedidasPrendaController implements MedidasPrendaApi {

    private final GestionarMedidasPrendaUseCase gestionarMedidas;
    private final SincronizarAlmacenesUseCase almacenes;

    public MedidasPrendaController(GestionarMedidasPrendaUseCase gestionarMedidas, SincronizarAlmacenesUseCase almacenes) {
        this.gestionarMedidas = gestionarMedidas;
        this.almacenes = almacenes;
    }

    @Override
    @PutMapping("/garments/{id}/measurements")
    public ResponseEntity<MedidasPrendaResponse> registrar(@PathVariable UUID id,
            @RequestHeader(value = Identidad.HEADER_ALMACEN, required = false) String almacenId,
            @RequestHeader(value = Identidad.HEADER_ROL, required = false) String rol,
            @RequestHeader(value = Identidad.HEADER_ROLES, required = false) String roles,
            @RequestHeader(value = Identidad.HEADER_USUARIO, required = false) String usuarioId,
            @Valid @RequestBody MedidasPrendaRequest request) {
        return ResponseEntity.ok(MedidaPrendaMapperIn.toResponse(id, gestionarMedidas.registrar(id,
                Identidad.solicitante(IdAlmacen.desdeTexto(almacenId), rol, roles, usuarioId, almacenes), MedidaPrendaMapperIn.toDomain(request))));
    }

    @Override
    @GetMapping("/garments/{id}/measurements")
    public ResponseEntity<MedidasPrendaResponse> consultar(@PathVariable UUID id) {
        return ResponseEntity.ok(MedidaPrendaMapperIn.toResponse(id, gestionarMedidas.consultar(id)));
    }
}
