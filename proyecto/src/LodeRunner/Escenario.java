package LodeRunner;

import java.awt.*;
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
                        g.setColor(Color.BLACK); // Borde estético para distinguir bloques
                        g.drawRect(x, y, ancho_bloque, alto_bloque);
                        break;  
                    case 3: // Escalera
                        g.setColor(Color.BLUE); //Azul para la escalera
                        g.fillRect(x + 4, y, 4, alto_bloque);
                        g.fillRect(x + ancho_bloque - 8, y, 4, alto_bloque);
                        // Un peldaño en el medio
                        g.fillRect(x + 4, y + (alto_bloque / 2) - 2, ancho_bloque - 8, 4);
                        break;
                    case 4: // Barra/Soga
                        g.setColor(Color.WHITE);
                        g.fillRect(x, y + 4, ancho_bloque, 4);
                        break;
                    default:
                        //no dibujar, pantalla negra
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
        if (numeroNivel == 1){
            matrizMundo = new int[][]{
                    {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                    {0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0},
                    {1,1,1,1,1,1,3,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,3,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,3,4,4,4,4,4,4,4,4,4,4,4,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,3,0,0,0,3,1,1,3,0,0,0,1,1,1,1,1,3,1,1},
                    {0,0,0,0,0,0,3,0,0,0,3,1,1,3,0,0,0,0,0,0,0,0,3,0,0},
                    {1,1,3,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,3,1,1,1,1,1,1},
                    {0,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,0,0,0},
                    {0,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,0,0,0},
                    {0,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,0,0,0},
                    {1,1,1,1,1,1,1,1,1,3,1,1,1,1,1,1,1,1,3,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0,0,3,0,0,0,0,0,0,0,0,3,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0,0,3,4,4,4,4,4,4,4,4,3,0,0,0,0,0,0},
                    {0,0,0,0,3,1,1,1,1,1,0,0,0,0,0,0,0,1,1,1,1,1,1,3,0},
                    {0,0,0,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0},
                    {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
            };
        }
    }
    public void actualizarPozos(){
        long tiempoActual = System.currentTimeMillis();
        mapaPozosTemporales.entrySet().removeIf(entrada -> {
            Point celda = entrada.getKey();
            long tiempoRoto = entrada.getValue();
            if (tiempoActual - tiempoRoto >= 5000){ // si la diferencia entre el tiempo actual y el tiempoRoto >= 5000 ms, se rellena el bloque
                matrizMundo[celda.y][celda.x] = 1;
                return true;
            }
            return false;
        });
    }
    public int obtenerTipoBloqueEn(int fila, int columna){
        if (fila >= 0 && fila < matrizMundo.length && columna >= 0 && columna < matrizMundo[0].length){
            return matrizMundo[fila][columna];
        }
        return 1;
    }
    public HashMap obtenerMapaPozos(){
        return mapaPozosTemporales;
    }
    public void romperBloque(int fila, int columna){
        this.matrizMundo[fila][columna] = 0;
        long tiempo = System.currentTimeMillis(); //esto devuelve el tiempo en el que un metodo fue llamado
        mapaPozosTemporales.put(new Point(columna, fila), tiempo);
    }
}