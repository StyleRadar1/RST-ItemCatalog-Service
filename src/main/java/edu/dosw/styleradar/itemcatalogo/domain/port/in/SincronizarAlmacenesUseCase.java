package edu.dosw.styleradar.itemcatalogo.domain.port.in;

import java.util.Optional;
import java.util.UUID;

public interface SincronizarAlmacenesUseCase {

    CargaAlmacenes cargarDesdeTiendas();

    Optional<UUID> almacenDelUsuario(Long usuarioId);

    record CargaAlmacenes(int almacenes, int descartados) {
    }
}
