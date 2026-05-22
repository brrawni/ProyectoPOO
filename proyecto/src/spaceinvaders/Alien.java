package spaceinvaders;

import motor.Entidad;
import java.awt.Graphics;

public class Alien extends Entidad {

    public static final int CALAMAR = 0;
    public static final int CANGREJO = 1;
    public static final int PULPO = 2;
    private int tipo;

    private int FrameAnimacion;

    public Alien(int tipo, int x, int y) {
        super(x, y, 32, 32);
        this.tipo = tipo;
    }

    public void mover() {
        if (direccionDerecha) {
            x += velocidad;
        } else {
            x -= velocidad;
        }
    }

    public void disparar() {
        // Lógica para disparar un proyectil
    }

    public void actualizar() {
        // Lógica para actualizar la animación del alien
    }



    @Override
    public void dibujar(Graphics g) {
        // Aquí puedes dibujar el alien usando g.drawImage o g.fillRect, etc.
        g.fillRect(x, y, 40, 30); // Ejemplo de un rectángulo representando al alien
    }
}