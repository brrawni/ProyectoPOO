Package LodeRunner;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.util.HashMap;

public class Escenario{
    private int ancho_bloque;
    private int[][] matrizMundo;
    private HashMap<Point, Long> mapaPozosTemporales;
    private int alto_bloque;
    private int numeroNivel;
    private boolean escaleraSalidaActiva;
    private HashMap<Integer, Image> sprites;
    
    public Escenario(int ancho_bloque, int alto_bloque, int numeroNivel){
        this.ancho_bloque = ancho_bloque;
        this.alto_bloque = alto_bloque;
        this.numeroNivel = numeroNivel;
        this.mapaPozosTemporales = new HashMap<>();
        this.sprites = new HashMap<>();
        cargarNivel(numeroNivel);
    }
    public void dibujar(Graphics2D g){
        for (int fila = 0; fila < matrizMundo.length; fila++) {
            for (int columna = 0; columna < matrizMundo[fila].length; columna++) {

                int x = columna * ancho_bloque;
                int y = fila * alto_bloque;
                int tipoBloque = matrizMundo[fila][columna];

                switch(tipoBloque) {
                    case 0: // Aire
                        // No dibujar nada
                        break;
                    case 1: // Ladrillo Común
                        g.setColor(new Color(139, 69, 19)); // Color marrón para el ladrillo
                        g.fillRect(x, y, ancho_bloque, alto_bloque);
                        break;  
                    case 3: // Escalera
                        g.setColor(new Color(139, 69, 19)); // Color marrón para la escalera
                        g.fillRect(x, y, ancho_bloque, alto_bloque);
                        break;
                    case 4: // Barra/Soga
                        g.drawImage(sprites.get(4), x, y, ancho_bloque, alto_bloque, null);
                        break;
                }
            }
        }
    }
    public void cargarNivel(int numeroNivel){
        this.numeroNivel = numeroNivel;
        this.escaleraSalidaActiva = false;
        // Lógica para cargar la matrizMundo y los sprites según el número de nivel
        // 0 = Aire, 1 = Ladrillo Común, 3 = Escalera, 4 = Barra/Soga
        matrizMundo = new int[][]{
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,0,0,0,0,0,3,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,3,0,0,0,0,0,0,1},
            {1,1,1,1,3,1,1,1,1,1,4,4,4,1},
            {1,0,0,0,3,0,0,0,0,1,0,0,0,1},
            {1,0,0,0,3,0,0,0,0,1,0,0,0,1},
            {1,1,3,1,1,1,1,1,3,1,1,1,1,1},
            {1,0,3,0,0,0,0,0,3,0,0,0,0,1},
            {1,0,3,0,0,0,0,0,3,0,0,0,0,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1}
        };
    }
    public void actualizarPozos(){

    }
    public void obtenerTipoBloqueEn(int fila, int columna){

    }
}