package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.inventario;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "ms-inventario", url = "${inventario.url}")
public interface InventarioClient {

    @GetMapping("/inventario/prenda/{prendaId}")
    DisponibilidadPrendaResponse disponibilidad(@PathVariable("prendaId") UUID prendaId);
}
