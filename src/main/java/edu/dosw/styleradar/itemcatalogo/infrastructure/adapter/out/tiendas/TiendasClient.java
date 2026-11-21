package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.tiendas;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ms-tiendas", url = "${tiendas.url}", configuration = TiendasFeignConfig.class)
public interface TiendasClient {

    @GetMapping("/stores")
    PaginaTiendasResponse almacenes(@RequestParam("page") int pagina, @RequestParam("size") int tamano);

    @GetMapping("/users/{userId}/store")
    AlmacenTiendasResponse almacenDelUsuario(@PathVariable("userId") Long usuarioId);
}
