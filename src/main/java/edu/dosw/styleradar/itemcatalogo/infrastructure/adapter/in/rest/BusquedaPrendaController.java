package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest;

import edu.dosw.styleradar.itemcatalogo.application.dto.response.PrendaResponse;
import edu.dosw.styleradar.itemcatalogo.application.mapper.in.PrendaMapperIn;
import edu.dosw.styleradar.itemcatalogo.domain.model.FiltroBusqueda;
import edu.dosw.styleradar.itemcatalogo.domain.model.Ordenamiento;
import edu.dosw.styleradar.itemcatalogo.domain.model.Ubicacion;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.BuscarPrendasUseCase;
import edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest.doc.BusquedaPrendaApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/prendas")
public class BusquedaPrendaController implements BusquedaPrendaApi {

    private final BuscarPrendasUseCase buscarPrendas;

    public BusquedaPrendaController(BuscarPrendasUseCase buscarPrendas) {
        this.buscarPrendas = buscarPrendas;
    }

    @Override
    @GetMapping
    public ResponseEntity<List<PrendaResponse>> buscar(@RequestParam(required = false) String texto,
                                                       @RequestParam(required = false) String tipo,
                                                       @RequestParam(required = false) String color,
                                                       @RequestParam(required = false) String marca,
                                                       @RequestParam(required = false) String estilo,
                                                       @RequestParam(required = false) BigDecimal precioMin,
                                                       @RequestParam(required = false) BigDecimal precioMax,
                                                       @RequestParam(required = false) Ordenamiento orden,
                                                       @RequestParam(required = false) Double lat,
                                                       @RequestParam(required = false) Double lng) {
        Ubicacion ubicacion = lat != null && lng != null ? new Ubicacion(lat, lng) : null;
        FiltroBusqueda filtro = new FiltroBusqueda(texto, tipo, color, marca, estilo, precioMin, precioMax, orden, ubicacion);
        return ResponseEntity.ok(PrendaMapperIn.toResponse(buscarPrendas.buscar(filtro)));
    }

    @Override
    @GetMapping("/{id}/similares")
    public ResponseEntity<List<PrendaResponse>> similares(@PathVariable UUID id) {
        return ResponseEntity.ok(PrendaMapperIn.toResponse(buscarPrendas.similares(id)));
    }
}
