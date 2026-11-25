package edu.dosw.styleradar.itemcatalogo.infrastructure.config;

import edu.dosw.styleradar.itemcatalogo.application.usecase.ActualizarDisponibilidadUseCaseImpl;
import edu.dosw.styleradar.itemcatalogo.application.usecase.BuscarPrendasUseCaseImpl;
import edu.dosw.styleradar.itemcatalogo.application.usecase.ConsultarCatalogoAlmacenUseCaseImpl;
import edu.dosw.styleradar.itemcatalogo.application.usecase.ConsultarDisponibilidadUseCaseImpl;
import edu.dosw.styleradar.itemcatalogo.application.usecase.ConsultarNitAlmacenUseCaseImpl;
import edu.dosw.styleradar.itemcatalogo.application.usecase.GestionarPrendaUseCaseImpl;
import edu.dosw.styleradar.itemcatalogo.application.usecase.ModerarPublicacionUseCaseImpl;
import edu.dosw.styleradar.itemcatalogo.application.usecase.SincronizarAlmacenesUseCaseImpl;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.ActualizarDisponibilidadUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.BuscarPrendasUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.ConsultarCatalogoAlmacenUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.ConsultarDisponibilidadUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.ConsultarNitAlmacenUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.GestionarPrendaUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.ModerarPublicacionUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.SincronizarAlmacenesUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.AlmacenRepositoryPort;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.DistanciaAlmacenPort;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.EventPublisherPort;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.PrendaRepositoryPort;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.StockPrendaPort;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.TallaDisponiblePort;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.TiendasPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public BuscarPrendasUseCase buscarPrendasUseCase(PrendaRepositoryPort prendas, AlmacenRepositoryPort almacenes,
                                                     DistanciaAlmacenPort distancias, TallaDisponiblePort tallas) {
        return new BuscarPrendasUseCaseImpl(prendas, almacenes, distancias, tallas);
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
    public ActualizarDisponibilidadUseCase actualizarDisponibilidadUseCase(PrendaRepositoryPort prendas, AlmacenRepositoryPort almacenes, StockPrendaPort stock) {
        return new ActualizarDisponibilidadUseCaseImpl(prendas, almacenes, stock);
    }

    @Bean
    public ModerarPublicacionUseCase moderarPublicacionUseCase(PrendaRepositoryPort prendas, AlmacenRepositoryPort almacenes,
                                                               EventPublisherPort eventos, StockPrendaPort stock) {
        return new ModerarPublicacionUseCaseImpl(prendas, almacenes, eventos, stock);
    }

    @Bean
    public SincronizarAlmacenesUseCase sincronizarAlmacenesUseCase(AlmacenRepositoryPort almacenes, TiendasPort tiendas) {
        return new SincronizarAlmacenesUseCaseImpl(almacenes, tiendas);
    }

    @Bean
    public ConsultarNitAlmacenUseCase consultarNitAlmacenUseCase(AlmacenRepositoryPort almacenes) {
        return new ConsultarNitAlmacenUseCaseImpl(almacenes);
    }

    @Bean
    public ConsultarDisponibilidadUseCase consultarDisponibilidadUseCase(PrendaRepositoryPort prendas, StockPrendaPort stock) {
        return new ConsultarDisponibilidadUseCaseImpl(prendas, stock);
    }
}
