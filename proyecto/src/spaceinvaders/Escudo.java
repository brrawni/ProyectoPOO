package spaceinvaders;

import java.awt.Graphics2D;
import motor.Entidad;
import java.awt.Color;

public class Escudo extends Entidad {
    private boolean[][] segmentos; // Matriz para representar los segmentos del escudo
    private static final int TAMAÑO = 5; // Tamaño de cada segmento
    private static final int FILAS = 6; // Número de filas de segmentos
    private static final int COLUMNAS = 8; // Número de columnas de segmentos

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
    public void recibirDano(int xImpacto, int yImpacto) {
        int col = (xImpacto - x) / TAMAÑO;
        int fila = (yImpacto - y) / TAMAÑO;

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

    @Override
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

    @Override
    public void dibujar(Graphics2D g) {
        g.setColor(Color.GREEN);
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                if (segmentos[i][j]) {
                    g.fillRect(
                        x + j * TAMAÑO,
                        y + i * TAMAÑO,
                        TAMAÑO, TAMAÑO
                    );
                }
            }
        }
    }
    
}