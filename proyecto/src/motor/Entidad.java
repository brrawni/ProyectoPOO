package motor;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract class Entidad {
    protected int x;
    protected int y;
    protected int ancho;
    protected int alto;
    protected boolean visible;

    public Entidad(int x, int y, int ancho, int alto) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.visible = true;
    }

    public Rectangle obtenerLimites() {
        return new Rectangle(x, y, ancho, alto);
    }

    public int obtenerX() {
        return x;
    }

    public int obtenerY() {
        return y;
    }

    public int obtenerAlto() {
        return alto;
    }

    public int obtenerAncho() {
        return ancho;
    }

    public abstract void mover();

    public abstract boolean detectarColision();

    public abstract void dibujar(Graphics2D g);
}
