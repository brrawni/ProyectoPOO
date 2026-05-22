package spaceinvaders;

import java.awt.Graphics;
import motor.Proyectil;

public class ProyectilAlien extends Proyectil {

    private CanonJugador jugador;

    public ProyectilAlien(int x, int y, CanonJugador jugador) {
        super(x, y, 5, 10, 0, 5.0f);
        this.jugador = jugador;
    }

    @Override
    public void actualizar() {
        y += dy;
        if (y > 600) desactivar();
        verificarImpacto();
    }

    @Override
    public void verificarImpacto() {
        if (jugador != null && this.obtenerLimites().intersects(jugador.obtenerLimites())) {
            jugador.perderVida();
            this.desactivar();
        }
    }

    @Override
    public void dibujar(Graphics g) {
        g.fillRect(x, y, ancho, alto);
    }
}