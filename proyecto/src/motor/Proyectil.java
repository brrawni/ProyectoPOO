package motor;

import java.awt.Graphics2D;
/*Base abstracta para proyectiles disparados por jugador o enemigos.
tiene dirección (dx, dy), estado activo, y lógica de impacto.
*/ 

public abstract class Proyectil extends Entidad {
    protected float dx;
    protected float dy;
    protected boolean activo;

    public Proyectil(int x, int y, int ancho, int alto, float dx, float dy) {
        super(x, y, ancho, alto);
        this.dx = dx;
        this.dy = dy;
        this.activo = true;
    }

    public boolean estaActivo() {
        return activo;
    }

    public void desactivar() {
        activo = false;
    }

    public abstract void mover();
    //public abstract void verificarImpacto();
    public abstract void dibujar(Graphics2D g);
}