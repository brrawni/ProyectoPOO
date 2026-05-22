package spaceinvaders;


import java.awt.Graphics;
import motor.Enemigo;

public class Alien extends Enemigo {

    public static final int CALAMAR = 0;
    public static final int CANGREJO = 1;
    public static final int PULPO = 2;
    private int tipo;
    private boolean direccionDerecha = true; // Dirección inicial del movimiento
    private int FrameAnimacion;

    public Alien(int tipo, int x, int y) {
        super(x, y, 32, 32, 1.0f); // Tamaño y velocidad del alien
        this.tipo = tipo;
    }

    public void mover() {
        if (direccionDerecha) {
            x += velocidad;
        } else {
            x -= velocidad;
        }
    }

    public void disparar() {
        // Lógica para disparar un proyectil
    }

    public void actualizar() {
        // Lógica para actualizar la animación del alien
        FrameAnimacion++;
        if (FrameAnimacion >= 30) { // Cambia de frame cada 30 actualizaciones
            FrameAnimacion = 0;
        }
    }

    public int obtenerPuntaje() {
        // Retorna el puntaje basado en el tipo de alien
        switch (tipo) {
            case CALAMAR:
                return 10;
            case CANGREJO:
                return 20;
            case PULPO:
                return 30;
            default:
                return 0;
        }
    }
    

    @Override
    public void dibujar(Graphics g) {
        // Aquí puedes dibujar el alien usando g.drawImage o g.fillRect, etc.
        g.fillRect(x, y, 40, 30); // Ejemplo de un rectángulo representando al alien
    }
}