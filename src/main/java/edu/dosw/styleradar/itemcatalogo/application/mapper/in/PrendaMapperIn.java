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

import java.util.List;
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

    // El orden de la lista es el orden de las fotos
    public static List<Fotografia> toFotografias(PrendaRequest request) {
        return IntStream.range(0, request.fotografias().size())
                .mapToObj(i -> new Fotografia(request.fotografias().get(i), i + 1))
                .toList();
    }

    public static PrendaResponse toResponse(Prenda prenda) {
        AtributosBusqueda a = prenda.getAtributos();
        return new PrendaResponse(prenda.getId(), prenda.getAlmacenId(), prenda.getNombre(), a.tipo(), a.color(),
                a.marca(), a.estilo(), prenda.getPrecio().valor(),
                prenda.getFotografias().stream().map(Fotografia::url).toList(),
                prenda.getEstado().name(), prenda.isDisponible(), prenda.getFechaPublicacion());
    }

    public static List<PrendaResponse> toResponse(List<Prenda> prendas) {
        return prendas.stream().map(PrendaMapperIn::toResponse).toList();
    }

    public static CatalogoResumenResponse toResumen(UUID almacenId, List<Prenda> prendas) {
        return new CatalogoResumenResponse(almacenId, prendas.size(),
                toResponse(prendas.stream().limit(PRENDAS_EN_RESUMEN).toList()));
    }

    public static PendientesResponse toPendientes(List<GrupoPendientes> grupos) {
        List<GrupoPendientesResponse> respuesta = grupos.stream()
                .map(grupo -> new GrupoPendientesResponse(grupo.almacenId(), grupo.almacen(), grupo.total(),
                        toResponse(grupo.prendas())))
                .toList();
        return new PendientesResponse(TIPO_PUBLICADOR, grupos.stream().mapToInt(GrupoPendientes::total).sum(), respuesta);
    }
}
