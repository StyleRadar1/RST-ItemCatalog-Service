package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest;

import edu.dosw.styleradar.itemcatalogo.application.dto.response.CatalogoResumenResponse;
import edu.dosw.styleradar.itemcatalogo.application.dto.response.PrendaResponse;
import edu.dosw.styleradar.itemcatalogo.application.mapper.in.PrendaMapperIn;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.ConsultarCatalogoAlmacenUseCase;
import edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest.doc.CatalogoAlmacenApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/almacenes/{almacenId}")
public class CatalogoAlmacenController implements CatalogoAlmacenApi {

    private final ConsultarCatalogoAlmacenUseCase consultarCatalogo;

    public CatalogoAlmacenController(ConsultarCatalogoAlmacenUseCase consultarCatalogo) {
        this.consultarCatalogo = consultarCatalogo;
    }

    @Override
    @GetMapping("/catalogo")
    public ResponseEntity<List<PrendaResponse>> catalogo(@PathVariable UUID almacenId) {
        return ResponseEntity.ok(PrendaMapperIn.toResponse(consultarCatalogo.catalogo(almacenId)));
    }

    @Override
    @GetMapping("/catalogo/resumen")
    public ResponseEntity<CatalogoResumenResponse> resumen(@PathVariable UUID almacenId) {
        return ResponseEntity.ok(PrendaMapperIn.toResumen(almacenId, consultarCatalogo.catalogo(almacenId)));
    }

    @Override
    @GetMapping("/novedades")
    public ResponseEntity<List<PrendaResponse>> novedades(@PathVariable UUID almacenId) {
        return ResponseEntity.ok(PrendaMapperIn.toResponse(consultarCatalogo.novedades(almacenId)));
    }
}
