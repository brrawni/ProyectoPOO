package Pong;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Clase que gestiona la entrada del teclado para ambos jugadores en Pong.
 * Implementa KeyListener para capturar eventos de teclado.
 * 
 * Controles por defecto:
 * - Jugador 1: Flecha Arriba / Flecha Abajo
 * - Jugador 2: W / S
 */
public class ControlTeclado implements KeyListener {
    // Estado de las teclas del Jugador 1
    private boolean arriba1 = false;
    private boolean abajo1 = false;
    
    // Estado de las teclas del Jugador 2
    private boolean arriba2 = false;
    private boolean abajo2 = false;
    
    // Teclas configurables (en el futuro se pueden cargar desde configuración)
    private static final int TECLA_ARRIBA_1 = KeyEvent.VK_UP;
    private static final int TECLA_ABAJO_1 = KeyEvent.VK_DOWN;
    private static final int TECLA_ARRIBA_2 = KeyEvent.VK_W;
    private static final int TECLA_ABAJO_2 = KeyEvent.VK_S;
    private String textoIngresado = "";
    private boolean enterPresionado = false;
    private static final int MAX_TEXTO = 12;

    /**
     * Se llama cuando se presiona una tecla
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int codigo = e.getKeyCode();
        
        // Jugador 1
        if (codigo == TECLA_ARRIBA_1) {
            arriba1 = true;
        }
        if (codigo == TECLA_ABAJO_1) {
            abajo1 = true;
        }
        
        // Jugador 2
        if (codigo == TECLA_ARRIBA_2) {
            arriba2 = true;
        }
        if (codigo == TECLA_ABAJO_2) {
            abajo2 = true;
        }
        if (codigo == KeyEvent.VK_ENTER) {
            enterPresionado = true;
        }
        if (codigo == KeyEvent.VK_BACK_SPACE && !textoIngresado.isEmpty()) {
            textoIngresado = textoIngresado.substring(0, textoIngresado.length() - 1);
        }
    }

    /**
     * Se llama cuando se suelta una tecla
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int codigo = e.getKeyCode();
        
        // Jugador 1
        if (codigo == TECLA_ARRIBA_1) {
            arriba1 = false;
        }
        if (codigo == TECLA_ABAJO_1) {
            abajo1 = false;
        }
        
        // Jugador 2
        if (codigo == TECLA_ARRIBA_2) {
            arriba2 = false;
        }
        if (codigo == TECLA_ABAJO_2) {
            abajo2 = false;
        }
    }

    /**
     * Se llama cuando se escribe una tecla (no usado en este caso)
     */
    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if ((Character.isLetterOrDigit(c) || c == ' ') && textoIngresado.length() < MAX_TEXTO) {
            textoIngresado += c;
        }
    }

    // Getters para consultar el estado de las teclas
    public boolean isArriba1Presionada() {
        return arriba1;
    }

    public boolean isAbajo1Presionada() {
        return abajo1;
    }

    public boolean isArriba2Presionada() {
        return arriba2;
    }

    public boolean isAbajo2Presionada() {
        return abajo2;
    }

    public String getTextoIngresado() {
        return textoIngresado.trim();
    }

    public boolean isEnterPresionado() {
        return enterPresionado;
    }

    public void resetEnter() {
        enterPresionado = false;
    }

    public void resetEntrada() {
        textoIngresado = "";
        enterPresionado = false;
    }
}
