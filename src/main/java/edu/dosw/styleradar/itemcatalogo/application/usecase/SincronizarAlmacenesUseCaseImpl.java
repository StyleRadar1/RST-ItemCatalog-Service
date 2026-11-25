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
            Optional<AlmacenResumen> actual = almacenRepository.findById(a.id());
            boolean activo = actual.map(AlmacenResumen::activo).orElse(true);
            Long administrador = actual.map(AlmacenResumen::administradorId).orElse(null);
            BigDecimal reputacion = a.reputacion() == null ? BigDecimal.ZERO : a.reputacion();
            String nit = a.nit() == null || a.nit().isBlank() ? actual.map(AlmacenResumen::nit).orElse(null) : a.nit().trim();
            almacenRepository.save(new AlmacenResumen(a.id(), a.nombre().trim(), reputacion, activo, administrador, nit));
            guardados++;
        }
        return new CargaAlmacenes(guardados, descartados);
    }

    @Override
    public Optional<UUID> almacenDelUsuario(Long usuarioId) {
        if (usuarioId == null) {
            return Optional.empty();
        }
        Optional<UUID> almacenId = tiendas.almacenDelUsuario(usuarioId);
        almacenId.flatMap(almacenRepository::findById)
                .filter(almacen -> !usuarioId.equals(almacen.administradorId()))
                .ifPresent(almacen -> almacenRepository.save(almacen.conAdministrador(usuarioId)));
        return almacenId;
    }
}
