package ranking;

import java.time.LocalDate;

public class EntradaRanking {
    private String    nombreJugador;
    private int       nivel;
    private int       puntaje;
    private LocalDate fecha;

    public EntradaRanking(String nombre, int nivel, int puntaje) {
        this.nombreJugador = nombre;
        this.nivel         = nivel;
        this.puntaje       = puntaje;
        this.fecha         = LocalDate.now();
    }

    // Constructor para cargar desde archivo
    public EntradaRanking(String nombre, int nivel, int puntaje, LocalDate fecha) {
        this.nombreJugador = nombre;
        this.nivel         = nivel;
        this.puntaje       = puntaje;
        this.fecha         = fecha;
    }

    public String    getNombre()  { return nombreJugador; }
    public int       getNivel()   { return nivel; }
    public int       getPuntaje() { return puntaje; }
    public LocalDate getFecha()   { return fecha; }

    @Override
    public String toString() {
        return nombreJugador + "," + nivel + "," + puntaje + "," + fecha;
    }
}
