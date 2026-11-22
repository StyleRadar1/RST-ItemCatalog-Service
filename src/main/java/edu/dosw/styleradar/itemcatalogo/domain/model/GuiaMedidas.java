package edu.dosw.styleradar.itemcatalogo.domain.model;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

// Guía de referencia en cm por talla; no son medidas de cada prenda sino la tabla estándar del catálogo
public final class GuiaMedidas {

    private static final Set<String> SUPERIOR = Set.of("camisa", "camiseta", "buzo", "suéter", "sueter", "saco",
            "chaqueta", "blusa", "polo", "hoodie", "blazer");
    private static final Set<String> INFERIOR = Set.of("pantalón", "pantalon", "falda", "jean", "short");
    private static final Set<String> COMPLETA = Set.of("vestido", "conjunto", "enterizo");

    private static final Map<String, MedidasTalla> LETRAS = Map.of(
            "XS", new MedidasTalla("XS", 80.0, 86.0, 62.0, 68.0, 86.0, 92.0),
            "S", new MedidasTalla("S", 86.0, 92.0, 68.0, 74.0, 92.0, 98.0),
            "M", new MedidasTalla("M", 92.0, 100.0, 74.0, 82.0, 98.0, 104.0),
            "L", new MedidasTalla("L", 100.0, 108.0, 82.0, 90.0, 104.0, 110.0),
            "XL", new MedidasTalla("XL", 108.0, 116.0, 90.0, 98.0, 110.0, 118.0),
            "XXL", new MedidasTalla("XXL", 116.0, 124.0, 98.0, 106.0, 118.0, 126.0));

    private static final double CM_POR_PULGADA = 2.54;
    private static final double HOLGURA_CM = 1.5;

    private GuiaMedidas() {
    }

    public static Optional<MedidasTalla> de(String tipo, String talla) {
        if (tipo == null || talla == null) {
            return Optional.empty();
        }
        String zona = tipo.trim().toLowerCase(Locale.ROOT);
        String valor = talla.trim().toUpperCase(Locale.ROOT);
        boolean prenda = SUPERIOR.contains(zona) || INFERIOR.contains(zona) || COMPLETA.contains(zona);
        if (prenda && LETRAS.containsKey(valor)) {
            return Optional.of(LETRAS.get(valor));
        }
        if (INFERIOR.contains(zona) && valor.matches("\\d{2}")) {
            double cintura = Integer.parseInt(valor) * CM_POR_PULGADA;
            return Optional.of(new MedidasTalla(valor, null, null, redondear(cintura - HOLGURA_CM),
                    redondear(cintura + HOLGURA_CM), null, null));
        }
        return Optional.empty();
    }

    private static double redondear(double valor) {
        return Math.round(valor * 10) / 10.0;
    }

    public record MedidasTalla(String talla, Double minPecho, Double maxPecho, Double minCintura, Double maxCintura,
                               Double minCadera, Double maxCadera) {
    }
}
