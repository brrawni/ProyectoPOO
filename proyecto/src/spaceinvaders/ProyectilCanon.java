package spaceinvaders;

import java.awt.Graphics;
import motor.Proyectil;

public class ProyectilCanon extends Proyectil {
    float velocidad = -5.0f; // Velocidad del proyectil hacia arriba
    private FormacionAlien formacion;

    public ProyectilCanon(int x, int y, FormacionAlien formacion) {
        super(x, y, 5, 10, 0, -8.0f); // Tamaño y velocidad del proyectil
        this.formacion = formacion;
    }

    
    
    @Override
    public void actualizar() {
        // Aquí puedes agregar lógica para eliminar el proyectil si sale de la pantalla, etc.
        y += dy;                    // mueve usando dy del padre
        if (y < 0) desactivar();   // si sale de pantalla se destruye
        verificarImpacto();
    }

    public void verificarImpacto() {
        for (Alien[] fila : formacion.getAliens()) {
            for (Alien alien : fila) {
                if (alien != null && alien.estaVivo() && this.obtenerLimites().intersects(alien.obtenerLimites())) {
                    alien.vivo = false;
                    this.desactivar();
                    return;
                }
            }
        }
    }

    @Override
    public void dibujar(Graphics g) {
        // Lógica para dibujar el proyectil del cañón
        g.fillRect(x, y, ancho, alto); // Ejemplo de un rectángulo representando al proyectil
    }
}