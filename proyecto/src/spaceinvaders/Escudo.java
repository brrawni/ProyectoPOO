package spaceinvaders;

import java.awt.Color;
import java.awt.Graphics2D;
import motor.Entidad;

public class Escudo extends Entidad {
    private boolean[][] segmentos; // Matriz para representar los segmentos del escudo
    private static final int TAMAÑO = 10; // Tamaño de cada segmento
    private static final int FILAS = 6; // Número de filas de segmentos
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
                segmentos[i][j] = true;
            }
        }
        recortarEsquinas();
    }

    private void recortarEsquinas() {
        // Esquina inferior izquierda y derecha (la entrada del bunker)
        for (int i = FILAS - 2; i < FILAS; i++) {
            for (int j = 0; j < 2; j++) {
                segmentos[i][j] = false;              // izquierda
                segmentos[i][COLUMNAS - 1 - j] = false;  // derecha
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
                if (segmentos[i][j]) return false; // hay alguno intacto
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
    int col  = (px - x) / TAMAÑO;
    int fila = (py - y) / TAMAÑO;

    // Verificar que las coordenadas caen dentro del escudo
    if (col < 0 || col >= COLUMNAS || fila < 0 || fila >= FILAS) {
        return false; // fuera del escudo
    }

    // Si el segmento ya está destruido, no hay impacto real
    if (!segmentos[fila][col]) {
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
        g.setColor(Color.GREEN); // El color clásico de los escudos

        // Recorremos la matriz fila por fila, columna por columna
        for (int fila = 0; fila < forma.length; fila++) {
            for (int col = 0; col < forma[fila].length; col++) {

                // Solo dibujamos si en esta posición hay un 1 (bloque intacto)
                if (forma[fila][col] == 1) {
                    int bloqueX = x + (col * TAMAÑO);
                    int bloqueY = y + (fila * TAMAÑO);
                    g.fillRect(bloqueX, bloqueY, TAMAÑO, TAMAÑO);
                }
            }
        }
    }

    @Override
    public void mover(){
        //no se mueve
    }

    
}