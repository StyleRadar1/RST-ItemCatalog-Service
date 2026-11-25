package edu.dosw.styleradar.itemcatalogo.application.mapper.in;

import edu.dosw.styleradar.itemcatalogo.application.dto.request.PrendaRequest;
import edu.dosw.styleradar.itemcatalogo.application.dto.response.CatalogoResumenResponse;
import edu.dosw.styleradar.itemcatalogo.application.dto.response.GrupoPendientesResponse;
import edu.dosw.styleradar.itemcatalogo.application.dto.response.PendientesResponse;
import edu.dosw.styleradar.itemcatalogo.application.dto.response.PrendaResponse;
import edu.dosw.styleradar.itemcatalogo.domain.model.AtributosBusqueda;
import edu.dosw.styleradar.itemcatalogo.domain.model.Fotografia;
import edu.dosw.styleradar.itemcatalogo.domain.model.GrupoPendientes;
import edu.dosw.styleradar.itemcatalogo.domain.model.Precio;
import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;
import edu.dosw.styleradar.itemcatalogo.domain.model.PrendaEncontrada;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

public final class PrendaMapperIn {

    private static final int PRENDAS_EN_RESUMEN = 4;
    private static final String TIPO_PUBLICADOR = "TIENDA";

    private PrendaMapperIn() {
    }

    public static AtributosBusqueda toAtributos(PrendaRequest request) {
        return new AtributosBusqueda(request.tipo(), request.color(), request.marca(), request.estilo());
    }

    public static Precio toPrecio(PrendaRequest request) {
        return new Precio(request.precio());
    }

    public static List<Fotografia> toFotografias(PrendaRequest request) {
        return IntStream.range(0, request.fotografias().size())
                .mapToObj(i -> new Fotografia(request.fotografias().get(i), i + 1))
                .toList();
    }

    public static PrendaResponse toResponse(Prenda prenda, Map<UUID, String> nits) {
        return toResponse(prenda, null, nits);
    }

    public static List<PrendaResponse> toEncontradas(List<PrendaEncontrada> encontradas, Map<UUID, String> nits) {
        return encontradas.stream().map(e -> toResponse(e.prenda(), e.distanciaKm(), nits)).toList();
    }

    private static PrendaResponse toResponse(Prenda prenda, Double distanciaKm, Map<UUID, String> nits) {
        AtributosBusqueda a = prenda.getAtributos();
        return new PrendaResponse(prenda.getId(), prenda.getAlmacenId(), prenda.getNombre(), a.tipo(), a.color(),
                a.marca(), a.estilo(), prenda.getPrecio().valor(),
                prenda.getFotografias().stream().map(Fotografia::url).toList(),
                prenda.getEstado().name(), prenda.isDisponible(), prenda.getFechaPublicacion(), distanciaKm,
                nits.get(prenda.getAlmacenId()));
    }

    public static List<PrendaResponse> toResponse(List<Prenda> prendas, Map<UUID, String> nits) {
        return prendas.stream().map(prenda -> toResponse(prenda, nits)).toList();
    }

    public static CatalogoResumenResponse toResumen(UUID almacenId, List<Prenda> prendas, Map<UUID, String> nits) {
        return new CatalogoResumenResponse(almacenId, prendas.size(),
                toResponse(prendas.stream().limit(PRENDAS_EN_RESUMEN).toList(), nits));
    }

    public static PendientesResponse toPendientes(List<GrupoPendientes> grupos, Map<UUID, String> nits) {
        List<GrupoPendientesResponse> respuesta = grupos.stream()
                .map(grupo -> new GrupoPendientesResponse(grupo.almacenId(), grupo.almacen(), grupo.total(),
                        toResponse(grupo.prendas(), nits)))
                .toList();
        return new PendientesResponse(TIPO_PUBLICADOR, grupos.stream().mapToInt(GrupoPendientes::total).sum(), respuesta);
    }
}
