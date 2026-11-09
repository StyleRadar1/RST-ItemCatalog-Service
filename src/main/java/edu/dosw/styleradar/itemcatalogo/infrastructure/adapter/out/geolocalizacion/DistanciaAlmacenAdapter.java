package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.geolocalizacion;

import edu.dosw.styleradar.itemcatalogo.domain.model.Ubicacion;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.DistanciaAlmacenPort;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DistanciaAlmacenAdapter implements DistanciaAlmacenPort {

    private final GeolocalizacionClient geolocalizacionClient;

    public DistanciaAlmacenAdapter(GeolocalizacionClient geolocalizacionClient) {
        this.geolocalizacionClient = geolocalizacionClient;
    }

    @Override
    public double kilometros(UUID almacenId, Ubicacion ubicacion) {
        return geolocalizacionClient.distancia(almacenId, ubicacion.latitud(), ubicacion.longitud()).kilometros();
    }
}
