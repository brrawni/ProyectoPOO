package spaceinvaders;

import java.awt.Graphics2D;
import motor.Proyectil;

public class ProyectilCanon extends Proyectil {
    float velocidad = -5.0f; // Velocidad del proyectil hacia arriba
    private FormacionAlien formacion;

    public ProyectilCanon(int x, int y, FormacionAlien formacion) {
        super(x, y, 5, 10, 0, -8.0f); // Tamaño y velocidad del proyectil
        this.formacion = formacion;
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
    }

}