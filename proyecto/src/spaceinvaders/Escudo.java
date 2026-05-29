package spaceinvaders;

import java.awt.Color;
import java.awt.Graphics2D;
import motor.Entidad;

public class Escudo extends Entidad {
    private boolean[][] segmentos; // Matriz para representar los segmentos del escudo
    private static final int TAMAÑO = 10; // Tamaño de cada segmento
    private static final int FILAS = 5; // Número de filas de segmentos
    private static final int COLUMNAS = 8; // Número de columnas de segmentos

    private int[][] forma = {
            {0, 1, 1, 1, 1, 1, 1, 0},
            {1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 0, 0, 1, 1, 1},
            {1, 1, 0, 0, 0, 0, 1, 1}
    };

    public Escudo(int x, int y) {
        super(x, y, TAMAÑO * COLUMNAS, TAMAÑO * FILAS);
        segmentos = new boolean[FILAS][COLUMNAS];
        // Inicializar todos los segmentos como activos

        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                segmentos[i][j] = (forma[i][j] == 1);
            }
        }
    }


/**
Recibe daño en una posición específica de la pantalla.
Destruye el segmento golpeado y los adyacentes.
*/  
    public void recibirDano(int px, int py) {
        int col = (px - x) / TAMAÑO;
        int fila = (py - y) / TAMAÑO;

        for(int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int filaImpactada = fila + i;
                int colImpactada = col + j;
                if (filaImpactada >= 0 && filaImpactada < FILAS && colImpactada >= 0 && colImpactada < COLUMNAS) {
                    segmentos[filaImpactada][colImpactada] = false;
                }
            }
        }
    }

    public boolean estaDestruido() {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                if (segmentos[i][j]) return false;
            }
        }
        return true;
    }

    public void actualizar() {
        // El escudo no tiene lógica de actualización por sí mismo
    }

    @Override
    public boolean detectarColision() {
        return false; // El escudo no detecta colisiones por sí mismo
    }

    private void destruirSegmento(int fila, int col) {
        if (fila >= 0 && fila < FILAS && col >= 0 && col < COLUMNAS) {
            segmentos[fila][col] = false; // Destruye el segmento
        }
    }

    public boolean recibirImpacto(int px, int py) {
<<<<<<< HEAD
    int col  = (px - x) / TAMAÑO;
    int fila = (py - y) / TAMAÑO;

    // Verificar que las coordenadas caen dentro del escudo
    if (col < 0 || col >= COLUMNAS || fila < 0 || fila >= FILAS) {
        return false; // fuera del escudo
    }

    // Si el segmento ya está destruido, no hay impacto real
    if (!segmentos[fila][col]) {
=======
        int col  = (px - x) / TAMAÑO;
        int fila = (py - y) / TAMAÑO;

        System.out.println("Impacto en col=" + col + " fila=" + fila +
                " escudo en x=" + x + " y=" + y);

        if (col < 0 || col >= COLUMNAS || fila < 0 || fila >= FILAS) {
            System.out.println("FUERA DEL ESCUDO");
            return false;
        }
        if (!segmentos[fila][col]) {
            System.out.println("SEGMENTO YA DESTRUIDO");
            return false;
        }

        // Destruir segmentos en radio 1
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int fi = fila + i;
                int co = col  + j;
                if (fi >= 0 && fi < FILAS && co >= 0 && co < COLUMNAS) {
                    segmentos[fi][co] = false;
                }
            }
        }
        return true; // hubo impacto real
    }

    public boolean verificarImpactoProyectil(int px, int py, int pAncho, int pAlto) {
        java.awt.Rectangle limiteProyectil = new java.awt.Rectangle(px, py, pAncho, pAlto);

        for (int fila = 0; fila < FILAS; fila++) {
            for (int col = 0; col < COLUMNAS; col++) {
                if (!segmentos[fila][col]) continue;

                java.awt.Rectangle limiteSegmento = new java.awt.Rectangle(
                        x + col  * TAMAÑO,
                        y + fila * TAMAÑO,
                        TAMAÑO, TAMAÑO
                );

                if (limiteProyectil.intersects(limiteSegmento)) {
                    // Solo destruye el segmento golpeado, sin radio
                    segmentos[fila][col] = false;
                    return true;
                }
            }
        }
>>>>>>> d733a0d653a67f81bd6c23aa6b6c362cd677e287
        return false;
    }

    // Destruir segmentos en radio 1
    for (int i = -1; i <= 1; i++) {
        for (int j = -1; j <= 1; j++) {
            int fi = fila + i;
            int co = col  + j;
            if (fi >= 0 && fi < FILAS && co >= 0 && co < COLUMNAS) {
                segmentos[fi][co] = false;
            }
        }
    }
    return true; // hubo impacto real
    }   

    @Override
    public void dibujar(Graphics2D g) {
        g.setColor(Color.GREEN);
        for (int fila = 0; fila < FILAS; fila++) {
            for (int col = 0; col < COLUMNAS; col++) {
                if (segmentos[fila][col]) { // ← ahora usa segmentos, no forma
                    g.fillRect(x + col * TAMAÑO, y + fila * TAMAÑO, TAMAÑO, TAMAÑO);
                }
            }
        }
    }

    @Override
    public void mover(){
        //no se mueve
    }

    
}