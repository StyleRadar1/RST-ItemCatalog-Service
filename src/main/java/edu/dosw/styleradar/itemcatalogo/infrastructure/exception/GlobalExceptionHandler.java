package edu.dosw.styleradar.itemcatalogo.infrastructure.exception;

import edu.dosw.styleradar.itemcatalogo.domain.exception.AccesoDenegadoException;
import edu.dosw.styleradar.itemcatalogo.domain.exception.DomainException;
import edu.dosw.styleradar.itemcatalogo.domain.exception.ParametroInvalidoException;
import edu.dosw.styleradar.itemcatalogo.domain.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String FOTOGRAFIAS_EXCEDIDAS = "La prenda puede tener máximo 5 fotografías";

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleNotFound(ResourceNotFoundException ex) {
        return problem(HttpStatus.NOT_FOUND, ex.getCodigo(), "Recurso no encontrado", ex.getMessage());
    }

    @ExceptionHandler(AccesoDenegadoException.class)
    public ProblemDetail handleForbidden(AccesoDenegadoException ex) {
        return problem(HttpStatus.FORBIDDEN, ex.getCodigo(), "Acceso denegado", ex.getMessage());
    }

    @ExceptionHandler(ParametroInvalidoException.class)
    public ProblemDetail handleParametroInvalido(ParametroInvalidoException ex) {
        ProblemDetail detail = problem(HttpStatus.BAD_REQUEST, ex.getCodigo(), "Solicitud inválida", ex.getMessage());
        if (!ex.getErrores().isEmpty()) {
            detail.setProperty("errors", ex.getErrores());
        }
        return detail;
    }

    @ExceptionHandler(DomainException.class)
    public ProblemDetail handleDomain(DomainException ex) {
        return problem(HttpStatus.UNPROCESSABLE_ENTITY, ex.getCodigo(), "Regla de negocio incumplida", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        if (errors.size() == 1 && FOTOGRAFIAS_EXCEDIDAS.equals(errors.get("fotografias"))) {
            return problem(HttpStatus.BAD_REQUEST, "FOTOGRAFIAS_EXCEDIDAS", "Solicitud inválida", FOTOGRAFIAS_EXCEDIDAS);
        }
        ProblemDetail detail = problem(HttpStatus.BAD_REQUEST, "DATOS_INVALIDOS", "Solicitud inválida", "La solicitud tiene campos inválidos");
        detail.setProperty("errors", errors);
        return detail;
    }

    @ExceptionHandler({MissingServletRequestParameterException.class, MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class})
    public ProblemDetail handleBadRequest(Exception ex) {
        return problem(HttpStatus.BAD_REQUEST, "PARAMETRO_INVALIDO", "Solicitud inválida", "Falta un dato o tiene un formato inválido");
    }

    @ExceptionHandler({NoResourceFoundException.class, NoHandlerFoundException.class})
    public ProblemDetail handleRouteNotFound(Exception ex) {
        return problem(HttpStatus.NOT_FOUND, "RUTA_NO_ENCONTRADA", "Ruta no encontrada", "El recurso o ruta solicitada no existe");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ProblemDetail handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        return problem(HttpStatus.METHOD_NOT_ALLOWED, "METODO_NO_PERMITIDO", "Método HTTP no permitido",
                "El método HTTP '" + ex.getMethod() + "' no está permitido para esta ruta");
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(Exception ex) {
        return problem(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR_INTERNO", "Error interno", "Ocurrió un error inesperado");
    }

    private ProblemDetail problem(HttpStatus status, String code, String title, String message) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(status, message);
        detail.setTitle(title);
        detail.setProperty("code", code);
        detail.setProperty("timestamp", OffsetDateTime.now().toString());
        return detail;
    }
}
