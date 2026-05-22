package spaceinvaders;

import java.awt.Graphics;
import motor.Proyectil;

public class ProyectilAlien extends Proyectil {
    private float velocidad = 5.0f; // Velocidad del proyectil hacia abajo

    public ProyectilAlien(int x, int y, FormacionAlien formacion, Jugador jugador) {
        super(x, y, 5, 10, 0, velocidad); // Tamaño y velocidad del proyectil
        this.formacion = formacion;
        this.jugador = jugador;
    }

    @Override
    public void actualizar() {
        y += dy; // Mueve el proyectil hacia abajo
        if (y > 600) desactivar(); // Si sale de pantalla se destruye
        verificarImpacto();
    }

    public void verificarImpacto() {
        if (this.obtenerLimites().intersects(jugador.obtenerLimites())) {
            jugador.recibirImpacto(); // Lógica para que el jugador reciba daño o pierda una vida
            this.desactivar();
        }
    }

    @Override
    public void dibujar(Graphics g) {
        g.fillRect(x, y, ancho, alto); // Ejemplo de un rectángulo representando al proyectil alienígena
    }
}