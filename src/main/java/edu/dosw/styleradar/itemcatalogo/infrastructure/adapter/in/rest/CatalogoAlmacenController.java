package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest;

import edu.dosw.styleradar.itemcatalogo.application.dto.response.CatalogoResumenResponse;
import edu.dosw.styleradar.itemcatalogo.application.dto.response.PrendaResponse;
import edu.dosw.styleradar.itemcatalogo.application.mapper.in.PrendaMapperIn;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.ConsultarCatalogoAlmacenUseCase;
import edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest.doc.CatalogoAlmacenApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class CatalogoAlmacenController implements CatalogoAlmacenApi {

    private final ConsultarCatalogoAlmacenUseCase consultarCatalogo;

    public CatalogoAlmacenController(ConsultarCatalogoAlmacenUseCase consultarCatalogo) {
        this.consultarCatalogo = consultarCatalogo;
    }

    @Override
    @GetMapping("/stores/{storeId}/catalog")
    public ResponseEntity<List<PrendaResponse>> catalogo(@PathVariable("storeId") UUID almacenId) {
        return ResponseEntity.ok(PrendaMapperIn.toResponse(consultarCatalogo.catalogo(almacenId)));
    }

    @Override
    @GetMapping("/stores/{storeId}/catalog/summary")
    public ResponseEntity<CatalogoResumenResponse> resumen(@PathVariable("storeId") UUID almacenId) {
        return ResponseEntity.ok(PrendaMapperIn.toResumen(almacenId, consultarCatalogo.catalogo(almacenId)));
    }

    @Override
    @GetMapping("/stores/{storeId}/new-arrivals")
    public ResponseEntity<List<PrendaResponse>> novedades(@PathVariable("storeId") UUID almacenId) {
        return ResponseEntity.ok(PrendaMapperIn.toResponse(consultarCatalogo.novedades(almacenId)));
    }
}
