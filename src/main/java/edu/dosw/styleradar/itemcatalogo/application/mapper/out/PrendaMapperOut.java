package edu.dosw.styleradar.itemcatalogo.application.mapper.out;

import edu.dosw.styleradar.itemcatalogo.domain.model.AlmacenResumen;
import edu.dosw.styleradar.itemcatalogo.domain.model.AtributosBusqueda;
import edu.dosw.styleradar.itemcatalogo.domain.model.Fotografia;
import edu.dosw.styleradar.itemcatalogo.domain.model.Moderacion;
import edu.dosw.styleradar.itemcatalogo.domain.model.Precio;
import edu.dosw.styleradar.itemcatalogo.domain.model.Prenda;
import edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.persistence.entity.AlmacenResumenEntity;
import edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.persistence.entity.FotografiaEntity;
import edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.persistence.entity.ModeracionEntity;
import edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.persistence.entity.PrendaEntity;

public final class PrendaMapperOut {

    private PrendaMapperOut() {
    }

    public static PrendaEntity toEntity(Prenda prenda) {
        AtributosBusqueda a = prenda.getAtributos();
        PrendaEntity entity = new PrendaEntity(prenda.getId(), prenda.getAlmacenId(), prenda.getNombre(), a.tipo(),
                a.color(), a.marca(), a.estilo(), prenda.getPrecio().valor(), prenda.getEstado(),
                prenda.isDisponible(), prenda.getFechaPublicacion());
        prenda.getFotografias().forEach(f -> entity.getFotografias().add(new FotografiaEntity(entity, f.url(), f.orden())));
        prenda.getModeraciones().forEach(m -> entity.getModeraciones().add(
                new ModeracionEntity(m.id(), entity, m.decision(), m.motivo(), m.moderadorId(), m.fecha())));
        return entity;
    }

    public static Prenda toDomain(PrendaEntity entity) {
        return new Prenda(entity.getId(), entity.getAlmacenId(), entity.getNombre(),
                new AtributosBusqueda(entity.getTipo(), entity.getColor(), entity.getMarca(), entity.getEstilo()),
                new Precio(entity.getPrecio()),
                entity.getFotografias().stream().map(f -> new Fotografia(f.getUrl(), f.getOrden())).toList(),
                entity.getModeraciones().stream().map(m -> new Moderacion(m.getId(), m.getDecision(), m.getMotivo(),
                        m.getModeradorId(), m.getFecha())).toList(),
                entity.getEstado(), entity.isDisponible(), entity.getFechaPublicacion());
    }

    public static AlmacenResumen toDomain(AlmacenResumenEntity entity) {
        return new AlmacenResumen(entity.getAlmacenId(), entity.getNombre(), entity.getReputacion(), entity.isActivo());
    }
}
