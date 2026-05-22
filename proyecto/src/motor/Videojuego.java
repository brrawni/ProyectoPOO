package motor;
import motor.Entidad;
import java.awt.Graphics2D;
import java.awt.Graphics;


public abstract class Videojuego extends JGame{
    protected int nivelActual;
    protected boolean enEjecucion;
    protected int nivelActual;

    public Videojuego(String title, int ancho, int alto) {
        super(title, ancho, alto);
        this.puntaje = 0;
        this.enEjecucion = true;
        this.nivelActual = 1;
    }

}
