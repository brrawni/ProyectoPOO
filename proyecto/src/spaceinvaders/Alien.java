package spaceinvaders;

import motor.Entidad;
import java.awt.Graphics;

public class Alien extends Entidad {

    public static final int CALAMAR = 0;
    public static final int CANGREJO = 1;
    public static final int PULPO = 2;
    private int tipo;
    
    private boolean direccionDerecha;
    private int FrameAnimacion;

    public Alien(int x, int y, int velocidad) {
        super(x, y);
        this.velocidad = velocidad;
        this.direccionDerecha = true; // Comienza moviéndose hacia la derecha
    }

    public void mover() {
        if (direccionDerecha) {
            x += velocidad;
        } else {
            x -= velocidad;
        }
    }

    public void cambiarDireccion() {
        direccionDerecha = !direccionDerecha;
    }

    @Override
    public void dibujar(Graphics g) {
        // Aquí puedes dibujar el alien usando g.drawImage o g.fillRect, etc.
        g.fillRect(x, y, 40, 30); // Ejemplo de un rectángulo representando al alien
    }
}