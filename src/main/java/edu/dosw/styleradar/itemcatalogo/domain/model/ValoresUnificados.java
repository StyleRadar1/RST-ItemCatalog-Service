package edu.dosw.styleradar.itemcatalogo.domain.model;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class ValoresUnificados {

    private static final List<String> TIPOS = List.of("Saco", "Chaqueta", "Buzo", "Suéter", "Camisa", "Camiseta",
            "Pantalón", "Falda", "Vestido", "Conjunto", "Calzado", "Accesorio");
    private static final List<String> COLORES = List.of("Negro", "Blanco", "Gris", "Azul", "Café", "Beige", "Crudo", "Plata");
    private static final List<String> ESTILOS = List.of("CASUAL", "FORMAL", "DEPORTIVO", "URBANO", "VINTAGE");
    private static final List<String> MARCAS = List.of("Kicks Local", "Luna Store", "Nómada Studio", "Segunda mano",
            "The Local Lab", "Ropero 57", "Norte Denim", "Casa Tweed", "Sastrería Antonia", "Calle 85 Store", "Nora Vintage");
    private static final List<String> TALLAS = Stream.concat(
            Stream.of("XS", "S", "M", "L", "XL", "XXL", "26", "28", "30", "32", "36", "Única"),
            IntStream.rangeClosed(34, 45).mapToObj(String::valueOf)).toList();

    public static final String MENSAJE_TIPO = "El tipo debe ser uno de: " + String.join(", ", TIPOS);
    public static final String MENSAJE_COLOR = "El color debe ser uno de: " + String.join(", ", COLORES);
    public static final String MENSAJE_ESTILO = "El estilo debe ser uno de: " + String.join(", ", ESTILOS);
    public static final String MENSAJE_MARCA = "La marca debe ser una de las tiendas: " + String.join(", ", MARCAS);
    public static final String MENSAJE_TALLA = "La talla debe ser XS, S, M, L, XL, XXL, de pantalón 26, 28, 30, 32, 34 o 36, "
            + "de calzado entre 34 y 45 o Única";

    private ValoresUnificados() {
    }

    public static boolean esTipo(String valor) {
        return contiene(TIPOS, valor);
    }

    public static boolean esColor(String valor) {
        return contiene(COLORES, valor);
    }

    public static boolean esEstilo(String valor) {
        return contiene(ESTILOS, valor);
    }

    public static boolean esMarca(String valor) {
        return contiene(MARCAS, valor);
    }

    public static boolean esTalla(String valor) {
        return contiene(TALLAS, valor);
    }

    private static boolean contiene(List<String> valores, String valor) {
        return valor != null && valores.stream().anyMatch(v -> normalizar(v).equals(normalizar(valor)));
    }

    private static String normalizar(String texto) {
        return Normalizer.normalize(texto.trim(), Normalizer.Form.NFD).replaceAll("\\p{M}", "").toLowerCase(Locale.ROOT);
    }
}
