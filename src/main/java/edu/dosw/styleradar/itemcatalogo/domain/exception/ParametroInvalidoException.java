package edu.dosw.styleradar.itemcatalogo.domain.exception;

import java.util.Map;

public class ParametroInvalidoException extends DomainException {

    private final Map<String, String> errores;

    public ParametroInvalidoException(String message, String codigo) {
        this(message, codigo, Map.of());
    }

    public ParametroInvalidoException(String message, String codigo, Map<String, String> errores) {
        super(message, codigo);
        this.errores = Map.copyOf(errores);
    }

    public Map<String, String> getErrores() {
        return errores;
    }
}
