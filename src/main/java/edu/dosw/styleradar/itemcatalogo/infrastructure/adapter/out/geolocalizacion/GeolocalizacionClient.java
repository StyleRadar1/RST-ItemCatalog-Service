package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.geolocalizacion;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "ms-geolocalizacion", url = "${geolocalizacion.url}")
public interface GeolocalizacionClient {

    @GetMapping("/almacenes/{id}/distancia")
    DistanciaAlmacenResponse distancia(@PathVariable("id") UUID almacenId,
                                       @RequestParam("lat") double latitud,
                                       @RequestParam("lng") double longitud);
}
