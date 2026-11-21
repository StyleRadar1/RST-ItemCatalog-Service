package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.in.scheduler;

import edu.dosw.styleradar.itemcatalogo.domain.port.in.SincronizarAlmacenesUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "tiendas.carga.habilitada", havingValue = "true", matchIfMissing = true)
public class CargaAlmacenesScheduler {

    private static final Logger log = LoggerFactory.getLogger(CargaAlmacenesScheduler.class);

    private final SincronizarAlmacenesUseCase sincronizar;

    public CargaAlmacenesScheduler(SincronizarAlmacenesUseCase sincronizar) {
        this.sincronizar = sincronizar;
    }

    @Scheduled(initialDelayString = "${tiendas.carga.retraso-inicial:PT20S}",
            fixedDelayString = "${tiendas.carga.intervalo:PT30M}")
    public void cargar() {
        try {
            SincronizarAlmacenesUseCase.CargaAlmacenes carga = sincronizar.cargarDesdeTiendas();
            log.info("Carga desde ms-tiendas: {} almacenes, {} descartados", carga.almacenes(), carga.descartados());
        } catch (RuntimeException e) {
            log.warn("No se pudo cargar los almacenes de ms-tiendas: {}", e.getMessage());
        }
    }
}
