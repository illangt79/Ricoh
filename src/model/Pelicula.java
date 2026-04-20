package model;

public class Pelicula {
    private int id;
    private String titulo;
    private String genero;
    private int copiasDisponibles;

    public Pelicula(int id, String titulo, String genero, int copiasDisponibles) {
        this.id = id;
        this.titulo = titulo;
        this.genero = genero;
        this.copiasDisponibles = copiasDisponibles;
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getGenero() { return genero; }
    public int getCopiasDisponibles() { return copiasDisponibles; }

    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setGenero(String genero) { this.genero = genero; }
    public void setCopiasDisponibles(int copias) { this.copiasDisponibles = copias; }

    @Override
    public String toString() {
        return String.format("[%d] %s (%s) - Copias disponibles: %d", id, titulo, genero, copiasDisponibles);
    }
}
