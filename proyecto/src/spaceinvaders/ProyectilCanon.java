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
        y += dy;                    // mueve usando dy del padre
        if (y < 0) desactivar();   // si sale de pantalla se destruye
        verificarImpacto();
    }

    @Override
    public void verificarImpacto() {
        for (Alien[] fila : formacion.getAliens()) {
            for (Alien alien : fila) {
                if (alien != null && alien.estaVivo() && this.obtenerLimites().intersects(alien.obtenerLimites())) {
                    alien.morir(); // El alien ha sido impactado
                    this.desactivar();
                    return;
                }
            }
        }
    }

    public void detectarColision(FormacionAlien formacion) {
        for (Alien[] fila : formacion.getAliens()) {
            for (Alien alien : fila) {
                if (alien != null && alien.estaVivo() && this.obtenerLimites().intersects(alien.obtenerLimites())) {
                    alien.morir(); // El alien ha sido impactado
                    this.desactivar();
                    return;
                }
            }
        }
    }

    @Override
    public void dibujar(Graphics2D g) {
        // Lógica para dibujar el proyectil del cañón
        g.fillRect(x, y, ancho, alto); // Ejemplo de un rectángulo representando al proyectil
    }
}