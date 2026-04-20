package manager;

import model.Cliente;
import model.Pelicula;
import model.Reserva;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VideoClub {
    private final List<Pelicula> peliculas = new ArrayList<>();
    private final List<Cliente> clientes = new ArrayList<>();
    private final List<Reserva> reservas = new ArrayList<>();

    private int nextPeliculaId = 1;
    private int nextClienteId = 1;
    private int nextReservaId = 1;

    public VideoClub() {
        peliculas.add(new Pelicula(nextPeliculaId++, "El Padrino", "Drama", 3));
        peliculas.add(new Pelicula(nextPeliculaId++, "Inception", "Ciencia ficción", 2));
        peliculas.add(new Pelicula(nextPeliculaId++, "El Rey León", "Animación", 4));
        peliculas.add(new Pelicula(nextPeliculaId++, "Interstellar", "Ciencia ficción", 1));
        clientes.add(new Cliente(nextClienteId++, "Ana García", "612345678"));
        clientes.add(new Cliente(nextClienteId++, "Luis Martínez", "698765432"));
    }

    // --- Getters ---
    public List<Pelicula> getPeliculas() { return new ArrayList<>(peliculas); }
    public List<Cliente> getClientes() { return new ArrayList<>(clientes); }
    public List<Reserva> getReservas() { return new ArrayList<>(reservas); }
    public List<Reserva> getReservasActivas() {
        return reservas.stream().filter(Reserva::isActiva).collect(Collectors.toList());
    }

    // --- Películas ---
    public String añadirPelicula(String titulo, String genero, int copias) {
        peliculas.add(new Pelicula(nextPeliculaId++, titulo, genero, copias));
        return "Película añadida correctamente.";
    }

    public String modificarPelicula(int id, String titulo, String genero, int copias) {
        Pelicula p = buscarPeliculaPorId(id);
        if (p == null) return "Película no encontrada.";
        p.setTitulo(titulo);
        p.setGenero(genero);
        p.setCopiasDisponibles(copias);
        return "Película actualizada.";
    }

    public String eliminarPelicula(int id) {
        Pelicula p = buscarPeliculaPorId(id);
        if (p == null) return "Película no encontrada.";
        boolean tieneReservas = reservas.stream()
                .anyMatch(r -> r.getPelicula().getId() == id && r.isActiva());
        if (tieneReservas) return "No se puede eliminar: tiene reservas activas.";
        peliculas.remove(p);
        return "Película eliminada.";
    }

    public List<Pelicula> filtrarPorTitulo(String texto) {
        return peliculas.stream()
                .filter(p -> p.getTitulo().toLowerCase().contains(texto.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Pelicula> filtrarPorGenero(String genero) {
        return peliculas.stream()
                .filter(p -> p.getGenero().equalsIgnoreCase(genero))
                .collect(Collectors.toList());
    }

    public Pelicula buscarPeliculaPorId(int id) {
        return peliculas.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    // --- Clientes ---
    public String añadirCliente(String nombre, String telefono) {
        clientes.add(new Cliente(nextClienteId++, nombre, telefono));
        return "Cliente añadido correctamente.";
    }

    public String modificarCliente(int id, String nombre, String telefono) {
        Cliente c = buscarClientePorId(id);
        if (c == null) return "Cliente no encontrado.";
        c.setNombre(nombre);
        c.setTelefono(telefono);
        return "Cliente actualizado.";
    }

    public String eliminarCliente(int id) {
        Cliente c = buscarClientePorId(id);
        if (c == null) return "Cliente no encontrado.";
        boolean tieneReservas = reservas.stream()
                .anyMatch(r -> r.getCliente().getId() == id && r.isActiva());
        if (tieneReservas) return "No se puede eliminar: tiene reservas activas.";
        clientes.remove(c);
        return "Cliente eliminado.";
    }

    public Cliente buscarClientePorId(int id) {
        return clientes.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }

    // --- Reservas ---
    public String crearReserva(int clienteId, int peliculaId) {
        Cliente cliente = buscarClientePorId(clienteId);
        if (cliente == null) return "Cliente no encontrado.";
        Pelicula pelicula = buscarPeliculaPorId(peliculaId);
        if (pelicula == null) return "Película no encontrada.";
        if (pelicula.getCopiasDisponibles() <= 0) return "No hay copias disponibles.";
        pelicula.setCopiasDisponibles(pelicula.getCopiasDisponibles() - 1);
        reservas.add(new Reserva(nextReservaId++, cliente, pelicula));
        return "Reserva creada correctamente.";
    }

    public String devolverPelicula(int reservaId) {
        Reserva r = reservas.stream().filter(res -> res.getId() == reservaId).findFirst().orElse(null);
        if (r == null) return "Reserva no encontrada.";
        if (!r.isActiva()) return "Esa reserva ya fue devuelta.";
        r.devolver();
        r.getPelicula().setCopiasDisponibles(r.getPelicula().getCopiasDisponibles() + 1);
        return "Devolución registrada.";
    }

    public String eliminarReserva(int id) {
        Reserva r = reservas.stream().filter(res -> res.getId() == id).findFirst().orElse(null);
        if (r == null) return "Reserva no encontrada.";
        if (r.isActiva()) {
            r.getPelicula().setCopiasDisponibles(r.getPelicula().getCopiasDisponibles() + 1);
        }
        reservas.remove(r);
        return "Reserva eliminada.";
    }
}
