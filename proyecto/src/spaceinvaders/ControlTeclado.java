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

    //agregar para ingreso de nombre
    private StringBuilder textoIngresado = new StringBuilder();
    private boolean enterPresionado = false;

    @Override
    public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                enterPresionado = true;
                return;
            }
            if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                if (textoIngresado.length() > 0)
                    textoIngresado.deleteCharAt(textoIngresado.length() - 1);
                    return;
            }

    // Solo letras y números
        char c = e.getKeyChar();
        if (Character.isLetterOrDigit(c) && textoIngresado.length() < 10) {
            textoIngresado.append(c);
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:  izquierda = true;  break;
            case KeyEvent.VK_RIGHT: derecha   = true;  break;
            case KeyEvent.VK_SPACE: disparo   = true;  break;
        }
    }

    // Cuando se suelta una tecla, actualizamos el estado
    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:  izquierda = false; break;
            case KeyEvent.VK_RIGHT: derecha   = false; break;
            case KeyEvent.VK_SPACE: disparo   = false; break;
        }
    }

    // El gameloop llama esto cada frame
    public void procesarEntrada(CanonJugador canon) {
        if (izquierda) canon.moverIzquierda();
        if (derecha)   canon.moverDerecha();
        if (disparo)   canon.disparar(); // Pasamos la lista de escudos para que el proyectil pueda detectar colisiones
    }

    // Setters para inyectar dependencias
    public String getTextoIngresado() { return textoIngresado.toString(); }
    public boolean isEnterPresionado() { return enterPresionado; }
    public void resetEntrada() {
        textoIngresado = new StringBuilder();
        enterPresionado = false;
    }
}