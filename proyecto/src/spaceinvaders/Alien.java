package spaceinvaders;


import java.awt.Graphics;
import motor.Enemigo;

public class Alien extends Enemigo {

    public static final int CALAMAR = 0;
    public static final int CANGREJO = 1;
    public static final int PULPO = 2;
    private int tipo;
   
    private int frameAnimacion;

    public Alien(int tipo, int x, int y) {
        super(x, y, 32, 32, 1.0f); // Tamaño y velocidad del alien
        this.tipo = tipo;
    }

    public void mover(int direccion) {
        if (direccion == 1) {
            x += velocidad * direccion;
        }
    }

    public void disparar() {
        // Lógica para disparar un proyectil
    }

    public void actualizar() {
        // Lógica para actualizar la animación del alien
        frameAnimacion++;
        if (frameAnimacion >= 30) { // Cambia de frame cada 30 actualizaciones
            frameAnimacion = 0;
        }
    }

    public int obtenerFrameAnimacion() {
        return frameAnimacion < 15 ? 0 : 1; // Retorna 0 o 1 para alternar entre dos frames
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