package edu.dosw.styleradar.itemcatalogo.domain.exception;

public class DomainException extends RuntimeException {

    private final String codigo;

    public DomainException(String message) {
        this(message, "DATOS_INVALIDOS");
    }

    public DomainException(String message, String codigo) {
        super(message);
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
