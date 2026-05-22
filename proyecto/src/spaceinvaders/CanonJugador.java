package spaceinvaders;

import java.awt.Graphics2D;
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

    public void moverDerecha() {
        x += 5; // Mueve el cañón hacia la derecha
    }
    public void moverIzquierda() {
        x -= 5; // Mueve el cañón hacia la izquierda
    }

    public void disparar() {
        if (puedeDisparar) {
            proyectil = new ProyectilCanon(x + ancho / 2, y); // Dispara desde el centro del cañón
            puedeDisparar = false; // El jugador no puede disparar hasta que el proyectil desaparezca
        }
    }

    public void proyectilDestruido() {
        proyectil = null; // El proyectil ha sido destruido
        puedeDisparar = true; // El jugador puede disparar nuevamente
    }

    @Override
    public void actualizar() {
        if (proyectil != null && proyectil.estaVivo()) {
            proyectil = null; // El proyectil ha sido destruido
            proyectil.mover(); // Mueve el proyectil    
            puedeDisparar = true; // El jugador puede disparar nuevamente
            // Aquí puedes agregar lógica para verificar colisiones o eliminar el proyectil si sale de la pantalla
        }
    }

    @Override
    public void dibujar(Graphics2D g) {
        // Lógica para dibujar el cañón del jugador
        g.fillRect(x, y, ancho, alto); // Ejemplo de un rectángulo representando al cañón
    }
}