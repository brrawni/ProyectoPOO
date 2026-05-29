// ProyectilAlien.java
package spaceinvaders;

import java.awt.Graphics2D;
import java.util.List;
import motor.Proyectil;

public class ProyectilAlien extends Proyectil {
    private List<Escudo> escudos;
    private CanonJugador jugador;

    public ProyectilAlien(int x, int y, CanonJugador jugador, List<Escudo> escudos) {
        super(x, y, 5, 10, 0, 5.0f);
        this.jugador = jugador;
        this.escudos = escudos;
    }

    public void actualizar() {
        y += dy;
        if (y > 740) desactivar();
        detectarColision();
    }

    @Override
    public boolean detectarColision() {
        if (activo && escudos != null) {
            for (Escudo escudo : escudos) {
                if (escudo.recibirImpacto(x, y)) {
                    activo = false;
                    return true; // colisionó con escudo
                }
            }
        }
        // verificar contra jugador
        if (jugador != null && obtenerLimites().intersects(jugador.obtenerLimites())) {
            jugador.perderVida();
            desactivar();
            return true;
        }
        return false; // no colisionó con nada
    }

    
    @Override
    public void mover() {
        // El movimiento se maneja en actualizar() usando dy
    }

    @Override
    public void dibujar(Graphics2D g) {
        if (estaActivo()) {
            g.fillRect(x, y, ancho, alto);
        }
    }
}