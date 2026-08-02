package spaceinvaders;

import java.awt.Color;
import java.awt.Graphics2D;
import motor.Entidad;
import java.awt.Rectangle;

public class Escudo extends Entidad {

    private boolean[][] segmentos;
    private static final int TAMAÑO   = 10;
    private static final int FILAS    = 5;
    private static final int COLUMNAS = 8;

    private static final int[][] FORMA = {
            {0, 1, 1, 1, 1, 1, 1, 0},
            {1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 0, 0, 1, 1, 1},
            {1, 1, 0, 0, 0, 0, 1, 1}
    };

    public Escudo(int x, int y) {
        super(x, y, TAMAÑO * COLUMNAS, TAMAÑO * FILAS);
        segmentos = new boolean[FILAS][COLUMNAS];
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                segmentos[i][j] = (FORMA[i][j] == 1);
            }
        }
    }

    public boolean verificarImpactoProyectil(int px, int py, int pAncho, int pAlto) {
        Rectangle limiteProyectil = new Rectangle(px, py, pAncho, pAlto);
        boolean impacto = false;

        for (int fila = 0; fila < FILAS; fila++) {
            for (int col = 0; col < COLUMNAS; col++) {
                if (!segmentos[fila][col]) continue;

                Rectangle limiteSegmento = new Rectangle(
                        x + col  * TAMAÑO,
                        y + fila * TAMAÑO,
                        TAMAÑO, TAMAÑO
                );

                if (limiteProyectil.intersects(limiteSegmento)) {
                    segmentos[fila][col] = false;
                    impacto = true;
                }
            }
        }
        return impacto;
    }

    public boolean estaDestruido() {
        boolean destruido = true;
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                if (segmentos[i][j]) {
                    destruido = false;
                } 
            }
        }
        return destruido;
    }

    public void actualizar() { }

    @Override
    public boolean detectarColision() { return false; }

    @Override
    public void mover() { }

    @Override
    public void dibujar(Graphics2D g) {
        g.setColor(Color.GREEN);
        for (int fila = 0; fila < FILAS; fila++) {
            for (int col = 0; col < COLUMNAS; col++) {
                if (segmentos[fila][col]) {
                    g.fillRect(x + col * TAMAÑO, y + fila * TAMAÑO, TAMAÑO, TAMAÑO);
                }
            }
        }
    }
}