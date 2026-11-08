package edu.dosw.styleradar.itemcatalogo.infrastructure.config;

import edu.dosw.styleradar.itemcatalogo.application.usecase.BuscarPrendasUseCaseImpl;
import edu.dosw.styleradar.itemcatalogo.application.usecase.ConsultarCatalogoAlmacenUseCaseImpl;
import edu.dosw.styleradar.itemcatalogo.application.usecase.GestionarPrendaUseCaseImpl;
import edu.dosw.styleradar.itemcatalogo.application.usecase.ModerarPublicacionUseCaseImpl;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.BuscarPrendasUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.ConsultarCatalogoAlmacenUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.GestionarPrendaUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.ModerarPublicacionUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.AlmacenRepositoryPort;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.PrendaRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Conecta los casos de uso con sus puertos sin anotar la capa de aplicación
@Configuration
public class BeanConfig {

    @Bean
    public BuscarPrendasUseCase buscarPrendasUseCase(PrendaRepositoryPort prendas, AlmacenRepositoryPort almacenes) {
        return new BuscarPrendasUseCaseImpl(prendas, almacenes);
    }

    @Bean
    public ConsultarCatalogoAlmacenUseCase consultarCatalogoAlmacenUseCase(PrendaRepositoryPort prendas, AlmacenRepositoryPort almacenes) {
        return new ConsultarCatalogoAlmacenUseCaseImpl(prendas, almacenes);
    }

    @Bean
    public GestionarPrendaUseCase gestionarPrendaUseCase(PrendaRepositoryPort prendas, AlmacenRepositoryPort almacenes) {
        return new GestionarPrendaUseCaseImpl(prendas, almacenes);
    }

    @Bean
    public ModerarPublicacionUseCase moderarPublicacionUseCase(PrendaRepositoryPort prendas) {
        return new ModerarPublicacionUseCaseImpl(prendas);
    }
}
