package spaceinvaders;

import java.awt.Graphics2D;
import java.util.List;
import motor.Proyectil;

public class ProyectilCanon extends Proyectil {
    float velocidad = -5.0f; // Velocidad del proyectil hacia arriba
    private FormacionAlien formacion;
    private List<Escudo> escudos;

    public ProyectilCanon(int x, int y, FormacionAlien formacion, List<Escudo> escudos) {
        super(x, y, 5, 10, 0, -8.0f); // Tamaño y velocidad del proyectil
        this.formacion = formacion;
        this.escudos = escudos;
    }

    @Override
    public void mover() {
        y += dy; // Mueve el proyectil hacia arriba
    }
    


    public void actualizar() {
        // Aquí puedes agregar lógica para eliminar el proyectil si sale de la pantalla, etc.

        mover();                    // mueve usando dy del padre

        if (y < 0) desactivar();   // si sale de pantalla se destruye

        if(estaActivo()){
            detectarColision();
        } // Verifica colisión con los aliens


        //detectar colision con escudo
        if (activo && escudos != null) {
            for (Escudo escudo : escudos) {

                // Le pasamos el rectángulo de la bala al escudo.
                // Si el escudo devuelve true, significa que rompimos un bloque.
                if (escudo.recibirImpacto(this.obtenerLimites())) {

                    activo = false; // El proyectil se desactiva
                    break;          // Salimos del bucle porque la bala ya explotó
                }
            }
        }
    }

    @Override
    public boolean detectarColision() {
        for(Alien[] fila : formacion.getAliens()) {
            for (Alien alien : fila) {
                if (alien != null && alien.estaVivo() && this.obtenerLimites().intersects(alien.obtenerLimites())) {
                    alien.morir(); // El alien ha sido impactado
                    this.desactivar();
                    return true; //hubo colision
                }
            }
        }
        return false;
    }

    @Override
    public void dibujar(Graphics2D g) {
        if(estaActivo()) {
            g.fillRect(x, y, ancho, alto); // Dibuja el proyectil como un rectángulo
        }
    }

    public void verificarImpacto() {
    // 1. contra aliens
        for (Alien[] fila : formacion.getAliens()) {
            for (Alien alien : fila) {
                if (alien != null && alien.estaVivo() && obtenerLimites().intersects(alien.obtenerLimites())) {
                    alien.morir();
                    desactivar();
                    return;
                }
            }
        }
    // 2. contra nave nodriza (se agrega cuando tengamos PartidaSpaceInvaders)
    // 3. contra escudos (se agrega cuando tengamos PartidaSpaceInvaders)
    for (Escudo escudo : escudos) {
        if (obtenerLimites().intersects(escudo.obtenerLimites())) {
            escudo.recibirDano(x, y); // El escudo recibe daño
            desactivar(); // El proyectil se destruye
            return;
            }
        }
    }
}