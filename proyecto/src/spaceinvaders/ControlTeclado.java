package spaceinvaders;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ControlTeclado extends KeyAdapter {
    private CanonJugador jugador;
    private FormacionAlien formacion;
    private SpaceInvaders juego;

    public ControlTeclado(CanonJugador jugador, FormacionAlien formacion, SpaceInvaders juego) {
        this.jugador = jugador;
        this.formacion = formacion;
        this.juego = juego;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                jugador.moverIzquierda();
                break;
            case KeyEvent.VK_RIGHT:
                jugador.moverDerecha();
                break;
            case KeyEvent.VK_SPACE:
                jugador.disparar(formacion);
                break;
            case KeyEvent.VK_ESCAPE:
                juego.stop(); // pausa o salir
                break;
        }
    }
    
}