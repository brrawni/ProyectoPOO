package spaceinvaders;

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
        if (y < 0) {
            desactivar();
            return;
        }
        verificarImpacto();
    }


    public void verificarImpacto() {
        //contra aliens
        for (Alien[] fila : formacion.getAliens()) {
            for (Alien alien : fila) {
                if (alien != null && alien.estaVivo()
                        && obtenerLimites().intersects(alien.obtenerLimites())) {
                    juego.sumarPuntaje(alien.obtenerPuntaje());
                    alien.morir();
                    desactivar();
                    return;
                }
            }
        }

        //contra nave nodriza
        NaveNodriza nave = juego.getNaveNodriza();
        if (nave.esVisible() && obtenerLimites().intersects(nave.obtenerLimites())) {
            juego.sumarPuntaje(nave.calcularPuntos(juego.getContadorDisparos()));
            nave.morir();
            desactivar();
            return;
        }

        //contra escudos
        for (Escudo escudo : escudos) {
            if (escudo.verificarImpactoProyectil(x, y, ancho, alto)) {
                desactivar();
                return;
            }
        }
    }

    // Métodos de la interfaz de entidad
    @Override
    public boolean detectarColision() {
        return !estaActivo();
    }

    @Override
    public void mover() { }

    @Override
    public void dibujar(Graphics2D g) {
        if (estaActivo()) {
            g.fillRect(x, y, ancho, alto);
        }
    }
}