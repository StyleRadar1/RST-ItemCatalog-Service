package edu.dosw.styleradar.itemcatalogo.infrastructure.config;

import edu.dosw.styleradar.itemcatalogo.application.usecase.ActualizarDisponibilidadUseCaseImpl;
import edu.dosw.styleradar.itemcatalogo.application.usecase.BuscarPrendasUseCaseImpl;
import edu.dosw.styleradar.itemcatalogo.application.usecase.ConsultarCatalogoAlmacenUseCaseImpl;
import edu.dosw.styleradar.itemcatalogo.application.usecase.GestionarPrendaUseCaseImpl;
import edu.dosw.styleradar.itemcatalogo.application.usecase.ModerarPublicacionUseCaseImpl;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.ActualizarDisponibilidadUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.BuscarPrendasUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.ConsultarCatalogoAlmacenUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.GestionarPrendaUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.ModerarPublicacionUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.AlmacenRepositoryPort;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.DistanciaAlmacenPort;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.EventPublisherPort;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.PrendaRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Conecta los casos de uso con sus puertos sin anotar la capa de aplicación
@Configuration
public class BeanConfig {

    @Bean
    public BuscarPrendasUseCase buscarPrendasUseCase(PrendaRepositoryPort prendas, AlmacenRepositoryPort almacenes,
                                                     DistanciaAlmacenPort distancias) {
        return new BuscarPrendasUseCaseImpl(prendas, almacenes, distancias);
    }

    @Bean
    public ConsultarCatalogoAlmacenUseCase consultarCatalogoAlmacenUseCase(PrendaRepositoryPort prendas, AlmacenRepositoryPort almacenes) {
        return new ConsultarCatalogoAlmacenUseCaseImpl(prendas, almacenes);
    }

    @Bean
    public GestionarPrendaUseCase gestionarPrendaUseCase(PrendaRepositoryPort prendas, AlmacenRepositoryPort almacenes,
                                                         EventPublisherPort eventos) {
        return new GestionarPrendaUseCaseImpl(prendas, almacenes, eventos);
    }

    @Bean
    public ActualizarDisponibilidadUseCase actualizarDisponibilidadUseCase(PrendaRepositoryPort prendas, AlmacenRepositoryPort almacenes) {
        return new ActualizarDisponibilidadUseCaseImpl(prendas, almacenes);
    }

    @Bean
    public ModerarPublicacionUseCase moderarPublicacionUseCase(PrendaRepositoryPort prendas, AlmacenRepositoryPort almacenes) {
        return new ModerarPublicacionUseCaseImpl(prendas, almacenes);
    }
}
