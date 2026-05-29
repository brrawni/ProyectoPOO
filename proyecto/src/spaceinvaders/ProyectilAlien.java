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
        // Contra escudos
        if (activo && escudos != null) {
            for (Escudo escudo : escudos) {
<<<<<<< HEAD
                if (escudo.recibirImpacto(x, y)) {
                    activo = false;
                    return true; // colisionó con escudo
=======
                if (escudo.verificarImpactoProyectil(x, y, ancho, alto)) { // ← este método
                    desactivar();
                    return true;
>>>>>>> d733a0d653a67f81bd6c23aa6b6c362cd677e287
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
            g.fillRect(x, y, ancho, alto);
        }
    }
}