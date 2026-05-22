package spaceinvaders;

import motor.Entidad;
import java.awt.Graphics;

public class Alien extends Entidad {
    private int velocidad;
    private boolean direccionDerecha;

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