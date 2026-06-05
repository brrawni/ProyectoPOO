package spaceinvaders;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class ControlTeclado extends KeyAdapter {
    private List<Escudo> escudos;

    // Variables para guardar las teclas configuradas
    private int teclaIzq;
    private int teclaDer;
    private int teclaDisparo;

    // Estado de cada tecla, true si está apretada
    private boolean izquierda = false;
    private boolean derecha   = false;
    private boolean disparo   = false;
    private boolean escape    = false;

    private StringBuilder textoIngresado = new StringBuilder();
    private boolean enterPresionado = false;

    //recibe las teclas personalizadas
    public ControlTeclado(int izq, int der, int disp) {
        this.teclaIzq = izq;
        this.teclaDer = der;
        this.teclaDisparo = disp;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            enterPresionado = true;
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            escape = true;
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            if (textoIngresado.length() > 0)
                textoIngresado.deleteCharAt(textoIngresado.length() - 1);
            return;
        }

        // Cambiamos el switch por if para poder usar nuestras variables
        if (e.getKeyCode() == teclaIzq) izquierda = true;
        if (e.getKeyCode() == teclaDer) derecha = true;
        if (e.getKeyCode() == teclaDisparo) disparo = true;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (Character.isLetterOrDigit(c) && textoIngresado.length() < 10) {
            textoIngresado.append(c);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Lo mismo al soltar
        if (e.getKeyCode() == teclaIzq) izquierda = false;
        if (e.getKeyCode() == teclaDer) derecha = false;
        if (e.getKeyCode() == teclaDisparo) disparo = false;
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) escape = false;
    }

    public void procesarEntrada(CanonJugador canon) {
        if (izquierda) canon.moverIzquierda();
        if (derecha)   canon.moverDerecha();
        if (disparo)   canon.disparar(); 
    }

    public String getTextoIngresado() { return textoIngresado.toString(); }
    public boolean isEnterPresionado() { return enterPresionado; }
    public boolean isEscapePresionado() { return escape; }
    public void resetEntrada() {
        textoIngresado = new StringBuilder();
        enterPresionado = false;
        escape = false;
    }
}