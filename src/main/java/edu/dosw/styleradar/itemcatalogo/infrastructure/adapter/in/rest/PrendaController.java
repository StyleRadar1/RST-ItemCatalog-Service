package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest;

import edu.dosw.styleradar.itemcatalogo.application.dto.request.PrendaRequest;
import edu.dosw.styleradar.itemcatalogo.application.dto.response.PrendaResponse;
import edu.dosw.styleradar.itemcatalogo.application.mapper.in.PrendaMapperIn;
import edu.dosw.styleradar.itemcatalogo.domain.exception.AccesoDenegadoException;
import edu.dosw.styleradar.itemcatalogo.domain.exception.ParametroInvalidoException;
import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;
import edu.dosw.styleradar.itemcatalogo.domain.model.Solicitante;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.ConsultarNitAlmacenUseCase;
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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
public class PrendaController implements PrendaApi {

    private final GestionarPrendaUseCase gestionarPrenda;
    private final SincronizarAlmacenesUseCase almacenes;
    private final ConsultarNitAlmacenUseCase nitAlmacenes;

    public PrendaController(GestionarPrendaUseCase gestionarPrenda, SincronizarAlmacenesUseCase almacenes,
                            ConsultarNitAlmacenUseCase nitAlmacenes) {
        this.gestionarPrenda = gestionarPrenda;
        this.almacenes = almacenes;
        this.nitAlmacenes = nitAlmacenes;
    }

    @Override
    @PostMapping("/garments")
    public ResponseEntity<PrendaResponse> publicar(@Valid @RequestBody PrendaRequest request,
            @RequestHeader(value = Identidad.HEADER_STORE, required = false) String storeIdHeader,
            @RequestHeader(value = Identidad.HEADER_ALMACEN, required = false) String almacenIdHeader,
            @RequestHeader(value = Identidad.HEADER_ROL, required = false) String rol,
            @RequestHeader(value = Identidad.HEADER_ROLES, required = false) String roles,
            @RequestHeader(value = Identidad.HEADER_USUARIO, required = false) String usuarioId) {
        Identidad.exigirRolAlmacenOAdmin(rol, roles);
        UUID almacenId = resolverAlmacenParaPublicacion(request.almacenId(), storeIdHeader, almacenIdHeader, rol, roles, usuarioId);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta(gestionarPrenda.publicar(
                almacenId, request.nombre(), PrendaMapperIn.toAtributos(request),
                PrendaMapperIn.toPrecio(request), PrendaMapperIn.toFotografias(request))));
    }

    @Override
    @GetMapping("/garments/{id}")
    public ResponseEntity<PrendaResponse> obtener(@PathVariable UUID id) {
        return ResponseEntity.ok(respuesta(gestionarPrenda.obtener(id)));
    }

    @Override
    @PutMapping("/garments/{id}")
    public ResponseEntity<PrendaResponse> editar(@PathVariable UUID id,
            @RequestHeader(value = Identidad.HEADER_STORE, required = false) String storeIdHeader,
            @RequestHeader(value = Identidad.HEADER_ALMACEN, required = false) String almacenIdHeader,
            @RequestHeader(value = Identidad.HEADER_ROL, required = false) String rol,
            @RequestHeader(value = Identidad.HEADER_ROLES, required = false) String roles,
            @RequestHeader(value = Identidad.HEADER_USUARIO, required = false) String usuarioId,
            @Valid @RequestBody PrendaRequest request) {
        Identidad.exigirRolAlmacenOAdmin(rol, roles);
        return ResponseEntity.ok(respuesta(gestionarPrenda.editar(id,
                solicitante(storeIdHeader, almacenIdHeader, rol, roles, usuarioId), request.nombre(),
                PrendaMapperIn.toAtributos(request), PrendaMapperIn.toPrecio(request),
                PrendaMapperIn.toFotografias(request))));
    }

    @Override
    @DeleteMapping("/garments/{id}")
    public ResponseEntity<PrendaResponse> retirar(@PathVariable UUID id,
            @RequestHeader(value = Identidad.HEADER_STORE, required = false) String storeIdHeader,
            @RequestHeader(value = Identidad.HEADER_ALMACEN, required = false) String almacenIdHeader,
            @RequestHeader(value = Identidad.HEADER_ROL, required = false) String rol,
            @RequestHeader(value = Identidad.HEADER_ROLES, required = false) String roles,
            @RequestHeader(value = Identidad.HEADER_USUARIO, required = false) String usuarioId) {
        Identidad.exigirRolAlmacenOAdmin(rol, roles);
        return ResponseEntity.ok(respuesta(gestionarPrenda.retirar(id,
                solicitante(storeIdHeader, almacenIdHeader, rol, roles, usuarioId))));
    }

    private UUID resolverAlmacenParaPublicacion(UUID bodyAlmacenId, String storeIdHeader, String almacenIdHeader,
                                                String rol, String roles, String usuarioId) {
        if (Identidad.esAdmin(rol, roles)) {
            UUID headerAlmacen = Identidad.resolverAlmacenHeader(storeIdHeader, almacenIdHeader);
            UUID resuelto = bodyAlmacenId != null ? bodyAlmacenId
                    : (headerAlmacen != null ? headerAlmacen
                    : almacenes.almacenDelUsuario(Identidad.usuario(usuarioId)).orElse(null));
            if (resuelto == null) {
                throw new ParametroInvalidoException("La solicitud tiene campos inválidos", "DATOS_INVALIDOS",
                        Map.of("almacenId", "El almacén es obligatorio"));
            }
            return resuelto;
        }
        UUID almacenTienda = resolverAlmacenParaTienda(storeIdHeader, almacenIdHeader, usuarioId);
        if (almacenTienda == null) {
            throw new AccesoDenegadoException("No se pudo verificar el almacén del usuario autenticado");
        }
        if (bodyAlmacenId != null && !almacenTienda.equals(bodyAlmacenId)) {
            throw new AccesoDenegadoException("No puedes publicar prendas en un almacén distinto al tuyo");
        }
        return almacenTienda;
    }

    private UUID resolverAlmacenParaTienda(String storeIdHeader, String almacenIdHeader, String usuarioId) {
        Optional<UUID> usuarioAlmacen = almacenes.almacenDelUsuario(Identidad.usuario(usuarioId));
        UUID headerAlmacen = Identidad.resolverAlmacenHeader(storeIdHeader, almacenIdHeader);
        if (usuarioAlmacen.isPresent() && headerAlmacen != null && !usuarioAlmacen.get().equals(headerAlmacen)) {
            throw new AccesoDenegadoException("El almacén de la solicitud no coincide con el almacén del usuario");
        }
        return usuarioAlmacen.orElse(headerAlmacen);
    }

    private PrendaResponse respuesta(Prenda prenda) {
        return PrendaMapperIn.toResponse(prenda, NitsDePrendas.de(nitAlmacenes, List.of(prenda)));
    }

    private Solicitante solicitante(String storeIdHeader, String almacenIdHeader, String rol, String roles, String usuarioId) {
        boolean admin = Identidad.esAdmin(rol, roles);
        UUID almacen = admin ? Identidad.resolverAlmacenHeader(storeIdHeader, almacenIdHeader)
                : resolverAlmacenParaTienda(storeIdHeader, almacenIdHeader, usuarioId);
        return new Solicitante(almacen, admin);
    }

    private Solicitante solicitante(UUID almacenId, String rol, String roles, String usuarioId) {
        return Identidad.solicitante(almacenId, rol, roles, usuarioId, almacenes);
    }
}
