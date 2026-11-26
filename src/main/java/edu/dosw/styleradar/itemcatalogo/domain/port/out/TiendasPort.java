package edu.dosw.styleradar.itemcatalogo.domain.port.out;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TiendasPort {

    List<AlmacenTienda> almacenes();

    Optional<UUID> almacenDelUsuario(Long usuarioId);

    record AlmacenTienda(UUID id, String nombre, BigDecimal reputacion, String nit) {

        public AlmacenTienda(UUID id, String nombre, BigDecimal reputacion) {
            this(id, nombre, reputacion, null);
        }
    }
}
