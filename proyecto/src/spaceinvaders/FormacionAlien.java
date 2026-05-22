package spaceinvaders;

import java.awt.Graphics;
import motor.Enemigo;

public class FormacionAlien {
    private Alien[][] aliens;
    private int filas;
    private int columnas;
    private int direccion = 1; // 1 para derecha, -1 para izquierda
    private float multiplicadorVelocidad = 1.0f; // Velocidad de movimiento de la formación

    public FormacionAlien(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        aliens = new Alien[filas][columnas];
        // Inicializar la formación con aliens de diferentes tipos
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                int tipo = (i % 3); // Alternar entre los tipos de alien
                aliens[i][j] = new Alien(tipo, j * 40, i * 40); // Posicionar cada alien
            }
        }
    }

    public void moverTodos() {
        // Lógica para mover toda la formación de aliens
        for (Alien[] fila : aliens) {
            for (Alien alien : fila) {
                if (alien != null) {
                    alien.mover();
                }
            }
        }
    }

    public void dibujarFormacion(Graphics g) {
        // Lógica para dibujar toda la formación de aliens
        for (Alien[] fila : aliens) {
            for (Alien alien : fila) {
                if (alien != null) {
                    alien.dibujar(g);
                }
            }
        }
    }

    public int contarVivos() {
        int contador = 0;
        for (Alien[] fila : aliens) {
            for (Alien alien : fila) {
                if (alien != null && alien.estaVivo()) {
                    contador++;
                }
            }
        }
        return contador;
    }

    public boolean llegoAlSuelo(){
        for (Alien[] fila : aliens) {
            for (Alien alien : fila) {
                if (alien != null && alien.estaVivo() && alien.obtenerY() >= 400) { // Suponiendo que el suelo está en y=400
                    return true;
                }
            }
        }
        return false;
    }

    public void actualizarVelocidad() {
        int vivos = contarVivos();
        multiplicadorVelocidad = 1.0f + (10 - vivos) * 0.1f; // Aumenta la velocidad a medida que quedan menos aliens
    }
}