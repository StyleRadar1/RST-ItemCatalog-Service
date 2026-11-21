package edu.dosw.styleradar.itemcatalogo.application.usecase;

import edu.dosw.styleradar.itemcatalogo.domain.model.AlmacenResumen;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.SincronizarAlmacenesUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.AlmacenRepositoryPort;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.TiendasPort;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public class SincronizarAlmacenesUseCaseImpl implements SincronizarAlmacenesUseCase {

    private final AlmacenRepositoryPort almacenRepository;
    private final TiendasPort tiendas;

    public SincronizarAlmacenesUseCaseImpl(AlmacenRepositoryPort almacenRepository, TiendasPort tiendas) {
        this.almacenRepository = almacenRepository;
        this.tiendas = tiendas;
    }

    @Override
    public CargaAlmacenes cargarDesdeTiendas() {
        int guardados = 0;
        int descartados = 0;
        for (TiendasPort.AlmacenTienda a : tiendas.almacenes()) {
            if (a.id() == null || a.nombre() == null || a.nombre().isBlank()) {
                descartados++;
                continue;
            }
            boolean activo = almacenRepository.findById(a.id()).map(AlmacenResumen::activo).orElse(true);
            BigDecimal reputacion = a.reputacion() == null ? BigDecimal.ZERO : a.reputacion();
            almacenRepository.save(new AlmacenResumen(a.id(), a.nombre().trim(), reputacion, activo));
            guardados++;
        }
        return new CargaAlmacenes(guardados, descartados);
    }

    @Override
    public Optional<UUID> almacenDelUsuario(Long usuarioId) {
        return usuarioId == null ? Optional.empty() : tiendas.almacenDelUsuario(usuarioId);
    }
}
