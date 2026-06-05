// ProyectilAlien.java
package spaceinvaders;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import motor.Proyectil;

public class ProyectilAlien extends Proyectil {
    private List<Escudo> escudos;
    private CanonJugador jugador;

    public ProyectilAlien(int x, int y, CanonJugador jugador, List<Escudo> escudos, float velocidadY) {
        super(x, y, 5, 10, 0, velocidadY);
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
        // Contra escudos
        if (activo && escudos != null) {
            for (Escudo escudo : escudos) {
                if (escudo.verificarImpactoProyectil(x, y, ancho, alto)) {
                    desactivar();
                    return true;
                }
            }
        }
        // Contra jugador
        if (jugador != null && obtenerLimites().intersects(jugador.obtenerLimites())) {
            jugador.perderVida();
            desactivar();
            return true;
        }
        return false;
    }
    
    @Override
    public void mover() {
        // El movimiento se maneja en actualizar() usando dy
    }

    @Override
    public void dibujar(Graphics2D g) {
        if (estaActivo()) {
            if ("alternativa".equals(GestorConfiguracionSpaceInvaders.getInstance().getSkinProyectil())) {
                g.setColor(Color.MAGENTA);
                g.fillOval(x, y, ancho, alto);
            } else {
                g.setColor(Color.RED);
                g.fillRect(x, y, ancho, alto);
            }
        }
    }
}