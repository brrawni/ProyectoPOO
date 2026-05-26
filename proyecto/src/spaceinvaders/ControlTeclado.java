package spaceinvaders;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class ControlTeclado extends KeyAdapter {
    private List<Escudo> escudos; // Para pasar la lista de escudos al proyectil

    // Estado de cada tecla — true si está apretada
    private boolean izquierda = false;
    private boolean derecha   = false;
    private boolean disparo   = false;

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:  izquierda = true;  break;
            case KeyEvent.VK_RIGHT: derecha   = true;  break;
            case KeyEvent.VK_SPACE: disparo   = true;  break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:  izquierda = false; break;
            case KeyEvent.VK_RIGHT: derecha   = false; break;
            case KeyEvent.VK_SPACE: disparo   = false; break;
        }
    }

    // El gameloop llama esto cada frame
    public void procesarEntrada(CanonJugador canon, FormacionAlien formacion) {
        if (izquierda) canon.moverIzquierda();
        if (derecha)   canon.moverDerecha();
        if (disparo)   canon.disparar(formacion); // Pasamos la lista de escudos para que el proyectil pueda detectar colisiones
    }
}