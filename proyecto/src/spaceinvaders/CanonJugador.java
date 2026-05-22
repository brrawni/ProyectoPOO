package spaceinvaders;

import java.awt.Graphics;
import motor.Jugador;

public class CanonJugador extends Jugador {
    private ProyectilCanon proyectil;
    private boolean puedeDisparar = true; // Controla si el jugador puede disparar

    public CanonJugador(int x, int y, int ancho, int alto, int vidas) {
        super(x, y, ancho, alto, vidas);
    }

    @Override
    public void mover() {
        
    }

    public void disparar() {
        if (puedeDisparar) {
            proyectil = new ProyectilCanon(x + ancho / 2, y); // Dispara desde el centro del cañón
            puedeDisparar = false; // El jugador no puede disparar hasta que el proyectil desaparezca
        }
    }

    @Override
    public void dibujar(Graphics g) {
        // Lógica para dibujar el cañón del jugador
        g.fillRect(x, y, ancho, alto); // Ejemplo de un rectángulo representando al cañón
    }
}