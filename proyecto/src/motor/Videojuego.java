package motor;
import java.awt.Graphics;

public abstract class Videojuego {
    protected int nivelActual;
    protected boolean enEjecucion;
    protected int puntaje;

    public Videojuego() {
        this.nivelActual = 1;
        this.enEjecucion = false;
        this.puntaje = 0;
    }

    public void iniciar(){
        this.enEjecucion = true;
        this.puntaje = 0;
        this.nivelActual = 1;
    }

    public void pausar(){
        this.enEjecucion = false;
    }

    public void reanudar() {
        this.enEjecucion = true;
    }

    public int obtenerPuntaje() {
        return puntaje;
    }

    public void sumarPuntaje(int puntos) {
        if(puntos > 0){
            this.puntaje += puntos;
        }
    }

    public abstract void actualizar();
    public abstract void dibujar(Graphics g);
    public abstract boolean verificarFinJuego();
}
