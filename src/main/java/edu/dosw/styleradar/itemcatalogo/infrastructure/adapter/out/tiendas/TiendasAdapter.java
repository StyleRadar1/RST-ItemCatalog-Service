package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.tiendas;

import edu.dosw.styleradar.itemcatalogo.domain.model.IdAlmacen;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.TiendasPort;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class TiendasAdapter implements TiendasPort {

    static final int TAMANO_PAGINA = 100;
    static final int MAXIMO_PAGINAS = 50;
    private static final BigDecimal REPUTACION_MAXIMA = new BigDecimal("5.00");
    private static final Logger log = LoggerFactory.getLogger(TiendasAdapter.class);

    private final TiendasClient client;

    public TiendasAdapter(TiendasClient client) {
        this.client = client;
    }

    @Override
    public List<AlmacenTienda> almacenes() {
        List<AlmacenTienda> almacenes = new ArrayList<>();
        int pagina = 0;
        PaginaTiendasResponse respuesta;
        do {
            respuesta = client.almacenes(pagina++, TAMANO_PAGINA);
            if (respuesta == null || respuesta.content() == null) {
                break;
            }
            respuesta.content().forEach(a -> almacenes.add(
                    new AlmacenTienda(idAlmacen(a.nit()), a.nombreComercial(), reputacion(a.reputacion()), a.nit())));
        } while (!respuesta.last() && pagina < MAXIMO_PAGINAS);
        return almacenes;
    }

    @Override
    public Optional<UUID> almacenDelUsuario(Long usuarioId) {
        try {
            AlmacenTiendasResponse almacen = client.almacenDelUsuario(usuarioId);
            return Optional.ofNullable(almacen).map(AlmacenTiendasResponse::nit).map(TiendasAdapter::idAlmacen);
        } catch (FeignException e) {
            log.warn("No se pudo consultar en ms-tiendas el almacén del usuario {}: {}", usuarioId, e.status());
            return Optional.empty();
        }
    }

    public static UUID idAlmacen(String nit) {
        return IdAlmacen.desdeNit(nit);
    }

    static BigDecimal reputacion(Double valor) {
        if (valor == null || valor < 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal reputacion = BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_UP);
        return reputacion.min(REPUTACION_MAXIMA);
    }
}
