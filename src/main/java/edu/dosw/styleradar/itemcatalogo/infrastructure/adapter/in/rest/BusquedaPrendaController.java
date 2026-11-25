package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest;

import edu.dosw.styleradar.itemcatalogo.application.dto.response.PrendaResponse;
import edu.dosw.styleradar.itemcatalogo.application.mapper.in.PrendaMapperIn;
import edu.dosw.styleradar.itemcatalogo.domain.model.FiltroBusqueda;
import edu.dosw.styleradar.itemcatalogo.domain.model.Ordenamiento;
import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;
import edu.dosw.styleradar.itemcatalogo.domain.model.PrendaEncontrada;
import edu.dosw.styleradar.itemcatalogo.domain.model.Ubicacion;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.BuscarPrendasUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.ConsultarNitAlmacenUseCase;
import edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.rest.doc.BusquedaPrendaApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
public class BusquedaPrendaController implements BusquedaPrendaApi {

    private final BuscarPrendasUseCase buscarPrendas;
    private final ConsultarNitAlmacenUseCase nitAlmacenes;

    public BusquedaPrendaController(BuscarPrendasUseCase buscarPrendas, ConsultarNitAlmacenUseCase nitAlmacenes) {
        this.buscarPrendas = buscarPrendas;
        this.nitAlmacenes = nitAlmacenes;
    }

    @Override
    @GetMapping("/garments")
    public ResponseEntity<List<PrendaResponse>> buscar(@RequestParam(required = false) String texto,
                                                       @RequestParam(required = false) String tipo,
                                                       @RequestParam(required = false) String color,
                                                       @RequestParam(required = false) String marca,
                                                       @RequestParam(required = false) String estilo,
                                                       @RequestParam(required = false) String talla,
                                                       @RequestParam(required = false) BigDecimal precioMin,
                                                       @RequestParam(required = false) BigDecimal precioMax,
                                                       @RequestParam(required = false) Ordenamiento orden,
                                                       @RequestParam(required = false) Double lat,
                                                       @RequestParam(required = false) Double lng) {
        Ubicacion ubicacion = lat != null && lng != null ? new Ubicacion(lat, lng) : null;
        FiltroBusqueda filtro = new FiltroBusqueda(texto, tipo, color, marca, estilo, talla, precioMin, precioMax, orden, ubicacion);
        List<PrendaEncontrada> encontradas = buscarPrendas.buscar(filtro);
        return ResponseEntity.ok(PrendaMapperIn.toEncontradas(encontradas,
                nitAlmacenes.nitsDe(encontradas.stream().map(e -> e.prenda().getAlmacenId()).toList())));
    }

    @Override
    @GetMapping("/garments/{id}/similar")
    public ResponseEntity<List<PrendaResponse>> similares(@PathVariable UUID id) {
        List<Prenda> similares = buscarPrendas.similares(id);
        return ResponseEntity.ok(PrendaMapperIn.toResponse(similares, NitsDePrendas.de(nitAlmacenes, similares)));
    }
}
