package edu.dosw.styleradar.itemcatalogo.domain.exception;

import java.util.Map;

public class ParametroInvalidoException extends DomainException {

    private final Map<String, String> errores;

    public ParametroInvalidoException(String message) {
        this(message, Map.of());
    }

    public ParametroInvalidoException(String message, Map<String, String> errores) {
        super(message);
        this.errores = Map.copyOf(errores);
    }

    public Map<String, String> getErrores() {
        return errores;
    }
}
