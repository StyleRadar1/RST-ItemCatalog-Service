package edu.dosw.styleradar.itemcatalogo.application.usecase;

import edu.dosw.styleradar.itemcatalogo.domain.exception.DomainException;
import edu.dosw.styleradar.itemcatalogo.domain.exception.ResourceNotFoundException;
import edu.dosw.styleradar.itemcatalogo.domain.model.EstadoPublicacion;
import edu.dosw.styleradar.itemcatalogo.domain.model.MedidaPrenda;
import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;
import edu.dosw.styleradar.itemcatalogo.domain.model.Solicitante;
import edu.dosw.styleradar.itemcatalogo.domain.port.in.GestionarMedidasPrendaUseCase;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.MedidaPrendaRepositoryPort;
import edu.dosw.styleradar.itemcatalogo.domain.port.out.PrendaRepositoryPort;

import java.util.List;
import java.util.UUID;

public class GestionarMedidasPrendaUseCaseImpl implements GestionarMedidasPrendaUseCase {

    private final PrendaRepositoryPort prendaRepository;
    private final MedidaPrendaRepositoryPort medidaRepository;

    public GestionarMedidasPrendaUseCaseImpl(PrendaRepositoryPort prendaRepository,
                                             MedidaPrendaRepositoryPort medidaRepository) {
        this.prendaRepository = prendaRepository;
        this.medidaRepository = medidaRepository;
    }

    @Override
    public List<MedidaPrenda> registrar(UUID prendaId, Solicitante solicitante, List<MedidaPrenda> medidas) {
        Prenda prenda = buscar(prendaId);
        prenda.verificarPermiso(solicitante);
        if (prenda.getEstado() == EstadoPublicacion.RETIRADA) {
            throw new DomainException("No se pueden registrar medidas de una prenda retirada", "PRENDA_RETIRADA");
        }
        MedidaPrenda.validar(medidas);
        return medidaRepository.reemplazar(prendaId, medidas);
    }

    @Override
    public List<MedidaPrenda> consultar(UUID prendaId) {
        buscar(prendaId);
        return medidaRepository.findByPrendaId(prendaId);
    }

    private Prenda buscar(UUID prendaId) {
        return prendaRepository.findById(prendaId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la prenda " + prendaId, "PRENDA_NO_ENCONTRADA"));
    }
}
