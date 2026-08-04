package spaceinvaders;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import motor.Proyectil;

public class ProyectilCanon extends Proyectil {

    private FormacionAlien formacion;
    private List<Escudo>   escudos;
    private SpaceInvaders  juego;

    public ProyectilCanon(int x, int y, FormacionAlien formacion, List<Escudo> escudos, SpaceInvaders juego) {
        super(x, y, 5, 10, 0, -8.0f);
        this.formacion = formacion;
        this.escudos   = escudos;
        this.juego     = juego;
    }

    // El gameloop llama esto cada frame
    public void actualizar() {
        y += dy;
        boolean debeDesactivar = y < 0;
        if (debeDesactivar) {
            desactivar();
        } else {
            detectarColision();
        }
    }


    @Override
    public boolean detectarColision() {
        boolean impactoDetectado = false;

        //contra aliens
        if (!impactoDetectado) {
            for (Alien[] fila : formacion.getAliens()) {
                for (Alien alien : fila) {
                    if (alien != null && alien.estaVivo()
                            && obtenerLimites().intersects(alien.obtenerLimites())) {
                        juego.sumarPuntaje(alien.obtenerPuntaje());
                        juego.getGestorSonidos().reproducirEfecto("explosion.wav"); //sonido de explosion
                        alien.morir();
                        desactivar();
                        impactoDetectado = true;
                        break;
                    }
                }
                if (impactoDetectado) {
                    break;
                }
            }
        }

        //contra nave nodriza
        if (!impactoDetectado) {
            NaveNodriza nave = juego.getNaveNodriza();
            if (nave.esVisible() && obtenerLimites().intersects(nave.obtenerLimites())) {
                juego.sumarPuntaje(nave.obtenerPuntaje());
                nave.morir();
                desactivar();
                impactoDetectado = true;
            }
        }

        //contra escudos
        if (!impactoDetectado) {
            for (Escudo escudo : escudos) {
                if (escudo.verificarImpactoProyectil(x, y, ancho, alto)) {
                    desactivar();
                    impactoDetectado = true;
                    break;
                }
            }
        }

        return impactoDetectado;
    }

    @Override
    public void mover() { }

    @Override
    public void dibujar(Graphics2D g2d) {
        if (estaActivo()) {
            if ("alternativa".equals(GestorConfiguracionSpaceInvaders.getInstance().getSkinProyectil())) {
                g2d.setColor(Color.CYAN);
                g2d.fillOval(x, y, ancho, alto);
            } else {
                g2d.setColor(Color.WHITE);
                g2d.fillRect(x, y, ancho, alto);
            }
        }
    }
}