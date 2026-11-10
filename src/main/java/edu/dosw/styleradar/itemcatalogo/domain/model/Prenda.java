package edu.dosw.styleradar.itemcatalogo.domain.model;

import edu.dosw.styleradar.itemcatalogo.domain.exception.AccesoDenegadoException;
import edu.dosw.styleradar.itemcatalogo.domain.exception.DomainException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Prenda {

    private final UUID id;
    private final UUID almacenId;
    private String nombre;
    private AtributosBusqueda atributos;
    private Precio precio;
    private List<Fotografia> fotografias;
    private final List<Moderacion> moderaciones;
    private EstadoPublicacion estado;
    private boolean disponible;
    private final LocalDateTime fechaPublicacion;

    public Prenda(UUID id, UUID almacenId, String nombre, AtributosBusqueda atributos, Precio precio,
                  List<Fotografia> fotografias, List<Moderacion> moderaciones, EstadoPublicacion estado,
                  boolean disponible, LocalDateTime fechaPublicacion) {
        this.id = id;
        this.almacenId = almacenId;
        this.nombre = nombre;
        this.atributos = atributos;
        this.precio = precio;
        this.fotografias = new ArrayList<>(fotografias);
        this.moderaciones = new ArrayList<>(moderaciones);
        this.estado = estado;
        this.disponible = disponible;
        this.fechaPublicacion = fechaPublicacion;
    }

    // Toda prenda nueva queda pendiente de moderación
    public static Prenda publicar(UUID almacenId, String nombre, AtributosBusqueda atributos, Precio precio,
                                  List<Fotografia> fotografias) {
        validar(nombre, fotografias);
        return new Prenda(UUID.randomUUID(), almacenId, nombre, atributos, precio, fotografias, List.of(),
                EstadoPublicacion.PENDIENTE, true, LocalDateTime.now());
    }

    // Al editarla vuelve a moderación
    public void editar(String nombre, AtributosBusqueda atributos, Precio precio, List<Fotografia> fotografias) {
        if (estado == EstadoPublicacion.RETIRADA) {
            throw new DomainException("No se puede editar una prenda retirada");
        }
        validar(nombre, fotografias);
        this.nombre = nombre;
        this.atributos = atributos;
        this.precio = precio;
        this.fotografias = new ArrayList<>(fotografias);
        this.estado = EstadoPublicacion.PENDIENTE;
    }

    public void retirar() {
        if (estado == EstadoPublicacion.RETIRADA) {
            throw new DomainException("La prenda ya está retirada");
        }
        estado = EstadoPublicacion.RETIRADA;
    }

    public void aprobar(UUID moderadorId) {
        moderar(EstadoPublicacion.APROBADA, null, moderadorId);
    }

    public void rechazar(UUID moderadorId, String motivo) {
        if (motivo == null || motivo.isBlank()) {
            throw new DomainException("El rechazo debe tener un motivo");
        }
        moderar(EstadoPublicacion.RECHAZADA, motivo, moderadorId);
    }

    // Solo el almacén dueño o un administrador pueden editarla o retirarla
    public void verificarPermiso(Solicitante solicitante) {
        if (!solicitante.admin() && !almacenId.equals(solicitante.almacenId())) {
            throw new AccesoDenegadoException("Solo el almacén dueño o un administrador pueden modificar la prenda");
        }
    }

    public void marcarAgotada() {
        disponible = false;
    }

    public boolean esVisible() {
        return estado == EstadoPublicacion.APROBADA && disponible;
    }

    public boolean cumple(FiltroBusqueda filtro) {
        return contieneTexto(filtro.texto())
                && coincide(filtro.tipo(), atributos.tipo())
                && coincide(filtro.color(), atributos.color())
                && coincide(filtro.marca(), atributos.marca())
                && coincide(filtro.estilo(), atributos.estilo())
                && precio.estaEntre(filtro.precioMin(), filtro.precioMax());
    }

    public boolean esSimilarA(Prenda otra) {
        return !id.equals(otra.id)
                && (coincide(otra.atributos.tipo(), atributos.tipo()) || coincide(otra.atributos.estilo(), atributos.estilo()));
    }

    private void moderar(EstadoPublicacion decision, String motivo, UUID moderadorId) {
        if (estado != EstadoPublicacion.PENDIENTE) {
            throw new DomainException("Solo se pueden moderar prendas pendientes");
        }
        moderaciones.add(new Moderacion(UUID.randomUUID(), decision, motivo, moderadorId, LocalDateTime.now()));
        estado = decision;
    }

    private boolean contieneTexto(String texto) {
        if (texto == null || texto.isBlank()) {
            return true;
        }
        String buscado = texto.toLowerCase();
        return String.join(" ", nombre, atributos.tipo(), String.valueOf(atributos.color()),
                String.valueOf(atributos.marca()), String.valueOf(atributos.estilo())).toLowerCase().contains(buscado);
    }

    private static boolean coincide(String filtro, String valor) {
        return filtro == null || filtro.isBlank() || filtro.equalsIgnoreCase(valor);
    }

    private static void validar(String nombre, List<Fotografia> fotografias) {
        if (nombre == null || nombre.isBlank()) {
            throw new DomainException("El nombre de la prenda es obligatorio");
        }
        if (fotografias == null || fotografias.isEmpty()) {
            throw new DomainException("La prenda debe tener al menos una fotografía");
        }
    }

    public UUID getId() {
        return id;
    }

    public UUID getAlmacenId() {
        return almacenId;
    }

    public String getNombre() {
        return nombre;
    }

    public AtributosBusqueda getAtributos() {
        return atributos;
    }

    public Precio getPrecio() {
        return precio;
    }

    public List<Fotografia> getFotografias() {
        return List.copyOf(fotografias);
    }

    public List<Moderacion> getModeraciones() {
        return List.copyOf(moderaciones);
    }

    public EstadoPublicacion getEstado() {
        return estado;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public LocalDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }
}
