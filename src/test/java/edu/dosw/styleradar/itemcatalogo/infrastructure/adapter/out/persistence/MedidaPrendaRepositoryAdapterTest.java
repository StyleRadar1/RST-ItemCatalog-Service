package edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.persistence;

import edu.dosw.styleradar.itemcatalogo.domain.model.MedidaPrenda;
import edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.persistence.entity.MedidaPrendaEntity;
import edu.dosw.styleradar.itemcatalogo.infrastructure.adapter.out.persistence.repository.MedidaPrendaJpaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MedidaPrendaRepositoryAdapterTest {

    private final MedidaPrendaJpaRepository jpa = mock(MedidaPrendaJpaRepository.class);
    private final MedidaPrendaRepositoryAdapter adapter = new MedidaPrendaRepositoryAdapter(jpa);
    private final UUID prendaId = UUID.randomUUID();
    private final MedidaPrenda medida = new MedidaPrenda("M", 92.0, 100.0, 74.0, 82.0, 98.0, 104.0, 70.0);

    @Test
    void reemplazaLasMedidasDeLaPrenda() {
        when(jpa.saveAll(anyList())).thenAnswer(inv -> inv.getArgument(0));

        assertThat(adapter.reemplazar(prendaId, List.of(medida))).containsExactly(medida);
        InOrder orden = inOrder(jpa);
        orden.verify(jpa).eliminarDePrenda(prendaId);
        orden.verify(jpa).flush();
        orden.verify(jpa).saveAll(anyList());
    }

    @Test
    void consultaLasMedidasDeLaPrenda() {
        MedidaPrendaEntity entity = MedidaPrendaRepositoryAdapter.toEntity(prendaId, medida);
        when(jpa.findByPrendaIdOrderByTalla(prendaId)).thenReturn(List.of(entity));

        assertThat(adapter.findByPrendaId(prendaId)).containsExactly(medida);
        assertThat(entity.getPrendaId()).isEqualTo(prendaId);
    }
}
