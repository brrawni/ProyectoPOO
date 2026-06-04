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
    private int teclaArriba1 = KeyEvent.VK_UP;
    private int teclaAbajo1 = KeyEvent.VK_DOWN;
    private int teclaArriba2 = KeyEvent.VK_W;
    private int teclaAbajo2 = KeyEvent.VK_S;

    /**
     * Se llama cuando se presiona una tecla
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int codigo = e.getKeyCode();
        
        // Jugador 1
        if (codigo == teclaArriba1) {
            arriba1 = true;
        }
        if (codigo == teclaAbajo1) {
            abajo1 = true;
        }
        
        // Jugador 2
        if (codigo == teclaArriba2) {
            arriba2 = true;
        }
        if (codigo == teclaAbajo2) {
            abajo2 = true;
        }
    }

    /**
     * Se llama cuando se suelta una tecla
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int codigo = e.getKeyCode();
        
        // Jugador 1
        if (codigo == teclaArriba1) {
            arriba1 = false;
        }
        if (codigo == teclaAbajo1) {
            abajo1 = false;
        }
        
        // Jugador 2
        if (codigo == teclaArriba2) {
            arriba2 = false;
        }
        if (codigo == teclaAbajo2) {
            abajo2 = false;
        }
    }

    /**
     * Se llama cuando se escribe una tecla (no usado en este caso)
     */
    @Override
    public void keyTyped(KeyEvent e) {
        // No necesitamos implementar esta funcionalidad
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

    // Setters para configurar las teclas personalizadas
    public void setTeclaArriba1(int tecla) {
        this.teclaArriba1 = tecla;
    }

    public void setTeclaAbajo1(int tecla) {
        this.teclaAbajo1 = tecla;
    }

    public void setTeclaArriba2(int tecla) {
        this.teclaArriba2 = tecla;
    }

    public void setTeclaAbajo2(int tecla) {
        this.teclaAbajo2 = tecla;
    }

    // Getters para obtener las teclas configuradas
    public int getTeclaArriba1() {
        return teclaArriba1;
    }

    public int getTeclaAbajo1() {
        return teclaAbajo1;
    }

    public int getTeclaArriba2() {
        return teclaArriba2;
    }

    public int getTeclaAbajo2() {
        return teclaAbajo2;
    }
}
