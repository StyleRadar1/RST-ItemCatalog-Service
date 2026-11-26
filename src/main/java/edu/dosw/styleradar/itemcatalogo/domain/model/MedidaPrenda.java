package edu.dosw.styleradar.itemcatalogo.domain.model;

import edu.dosw.styleradar.itemcatalogo.domain.exception.DomainException;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public record MedidaPrenda(String talla, Double minPecho, Double maxPecho, Double minCintura, Double maxCintura,
                           Double minCadera, Double maxCadera, Double largo) {

    private static final String CODIGO = "MEDIDAS_INVALIDAS";
    private static final int MAXIMO_TALLAS = 20;
    private static final double MAXIMO_CM = 300;

    public MedidaPrenda {
        talla = talla == null ? null : talla.trim();
    }

    public static void validar(List<MedidaPrenda> medidas) {
        if (medidas == null || medidas.isEmpty()) {
            throw new DomainException("Debe registrar las medidas de al menos una talla", CODIGO);
        }
        if (medidas.size() > MAXIMO_TALLAS) {
            throw new DomainException("Se pueden registrar medidas de máximo " + MAXIMO_TALLAS + " tallas", CODIGO);
        }
        Set<String> tallas = new HashSet<>();
        for (MedidaPrenda medida : medidas) {
            medida.validar();
            if (!tallas.add(medida.clave())) {
                throw new DomainException("La talla " + medida.talla() + " está repetida", CODIGO);
            }
        }
    }

    public String clave() {
        return talla.toUpperCase(Locale.ROOT);
    }

    public GuiaMedidas.MedidasTalla comoMedidasTalla(String tallaOfertada) {
        return new GuiaMedidas.MedidasTalla(tallaOfertada, minPecho, maxPecho, minCintura, maxCintura, minCadera,
                maxCadera, largo);
    }

    private void validar() {
        if (talla == null || !ValoresUnificados.esTalla(talla)) {
            throw new DomainException(ValoresUnificados.MENSAJE_TALLA, CODIGO);
        }
        if (minPecho == null && maxPecho == null && minCintura == null && maxCintura == null
                && minCadera == null && maxCadera == null && largo == null) {
            throw new DomainException("La talla " + talla + " debe tener al menos una medida", CODIGO);
        }
        validarRango("pecho", minPecho, maxPecho);
        validarRango("cintura", minCintura, maxCintura);
        validarRango("cadera", minCadera, maxCadera);
        validarValor("largo", largo);
    }

    private void validarRango(String medida, Double minimo, Double maximo) {
        if ((minimo == null) != (maximo == null)) {
            throw new DomainException("En la talla " + talla + " el " + medida + " debe tener mínimo y máximo", CODIGO);
        }
        validarValor(medida, minimo);
        validarValor(medida, maximo);
        if (minimo != null && minimo > maximo) {
            throw new DomainException("En la talla " + talla + " el mínimo de " + medida + " no puede ser mayor que el máximo",
                    CODIGO);
        }
    }

    private void validarValor(String medida, Double valor) {
        if (valor != null && (valor <= 0 || valor > MAXIMO_CM)) {
            throw new DomainException("En la talla " + talla + " el " + medida + " debe estar entre 0 y " + (int) MAXIMO_CM
                    + " cm", CODIGO);
        }
    }
}
