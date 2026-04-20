package model;

import java.time.LocalDate;

public class Reserva {
    private int id;
    private Cliente cliente;
    private Pelicula pelicula;
    private LocalDate fecha;
    private boolean activa;

    public Reserva(int id, Cliente cliente, Pelicula pelicula) {
        this.id = id;
        this.cliente = cliente;
        this.pelicula = pelicula;
        this.fecha = LocalDate.now();
        this.activa = true;
    }

    public int getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public Pelicula getPelicula() { return pelicula; }
    public LocalDate getFecha() { return fecha; }
    public boolean isActiva() { return activa; }

    public void devolver() { this.activa = false; }

    @Override
    public String toString() {
        String estado = activa ? "ACTIVA" : "DEVUELTA";
        return String.format("[%d] %s reservó \"%s\" el %s [%s]",
                id, cliente.getNombre(), pelicula.getTitulo(), fecha, estado);
    }
}
