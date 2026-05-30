package LodeRunner;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Escenario{
    private int ancho_bloque;
    private int[][] matrizMundo;
    private HashMap<Point, Long> mapaPozosTemporales;
    private int alto_bloque;
    private int numeroNivel;
    private boolean escaleraSalidaActiva;
    private HashMap<Integer, Image> sprites;
    private BufferedImage escalera;
    private BufferedImage ladrillo;
    
    public Escenario(int ancho_bloque, int alto_bloque, int numeroNivel){
        this.ancho_bloque = ancho_bloque;
        this.alto_bloque = alto_bloque;
        this.numeroNivel = numeroNivel;
        this.mapaPozosTemporales = new HashMap<>();
        this.sprites = new HashMap<>();
        try{
            BufferedImage hojaSprites = ImageIO.read(new File("proyecto/resources/escaleras.png"));
            BufferedImage hojaSpritesLadrillos = ImageIO.read(new File("proyecto/resources/ladrillos.png"));
            escalera = hojaSprites.getSubimage(0,0,32,32);
            ladrillo = hojaSpritesLadrillos.getSubimage(2*32, 5*32, 32, 32);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
                        /*g.setColor(new Color(139, 69, 19)); // Color marrón para el ladrillo
                        g.fillRect(x, y, ancho_bloque, alto_bloque);
                        g.setColor(Color.BLACK); // Borde estético para distinguir bloques
                        g.drawRect(x, y, ancho_bloque, alto_bloque);*/
                        g.drawImage(ladrillo, x, y, null);
                        break;
                    case 3: // Escalera
                        g.setColor(new Color(139, 69, 19)); //Marron para la escalera
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
    public void cargarNivel(int numeroNivel) {
        this.numeroNivel = numeroNivel;
        this.escaleraSalidaActiva = false;
        // Lógica para cargar la matrizMundo y los sprites según el número de nivel
        // 0 = Aire, 1 = Ladrillo Común, 3 = Escalera, 4 = Barra/Soga
        if (numeroNivel == 1) {
            matrizMundo = new int[][]{
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, //Fila 0
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //Fila 1
                    {1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0}, //Fila 2
                    {0, 0, 0, 0, 0, 0, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0}, //Fila 3
                    {0, 0, 0, 0, 0, 0, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 0, 0, 0, 0, 0, 0, 0}, //Fila 4
                    {0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 3, 1, 1, 3, 0, 0, 0, 1, 1, 1, 1, 1, 3, 1, 1}, //Fila 5
                    {0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 3, 1, 1, 3, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0}, //Fila 6
                    {1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1}, //Fila 7
                    {0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0}, //Fila 8
                    {0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0}, //Fila 9
                    {0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0}, //Fila 10
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 3, 0, 0, 0, 0, 0, 0}, //Fila 11
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0}, //Fila 12
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 4, 4, 4, 4, 4, 4, 4, 4, 3, 0, 0, 0, 0, 0, 0}, //Fila 13
                    {0, 0, 0, 0, 3, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 3, 0}, //Fila 14
                    {0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0}, //Fila 15
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}  //Fila 16
            };
        } else if (numeroNivel == 2) {
            matrizMundo = new int[][]{
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, // Fila 0: Techo
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, // Fila 1
                    {1, 1, 1, 1, 3, 0, 0, 0, 0, 3, 0, 1, 1, 1, 1, 1, 1, 1, 1, 3, 0, 0, 1, 1, 1}, // Fila 2
                    {0, 0, 0, 0, 3, 0, 0, 0, 0, 3, 0, 1, 0, 0, 0, 0, 0, 1, 0, 3, 0, 0, 1, 0, 0}, // Fila 3
                    {0, 0, 0, 0, 3, 4, 4, 4, 4, 3, 0, 1, 0, 0, 0, 0, 4, 4, 4, 3, 0, 0, 1, 0, 0}, // Fila 4
                    {1, 1, 1, 1, 3, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 3, 0, 0, 1, 0, 0}, // Fila 5
                    {0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 3, 0, 0, 1, 0, 0}, // Fila 6
                    {0, 0, 0, 0, 3, 0, 3, 1, 1, 1, 1, 1, 1, 3, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0}, // Fila 7
                    {0, 0, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 0}, // Fila 8: Puente
                    {0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0}, // Fila 9
                    {0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 1, 1, 1, 1, 1, 0, 0, 3, 0}, // Fila 10
                    {1, 1, 3, 0, 0, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 1, 0, 0, 3, 0}, // Fila 11
                    {0, 0, 3, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 3, 0}, // Fila 12
                    {0, 0, 3, 4, 4, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 3, 0}, // Fila 13
                    {0, 0, 3, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1}, // Fila 14
                    {0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, // Fila 15: Corredor de escape
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}  // Fila 16: Piso principal
            };
        } else if (numeroNivel == 3) {
            matrizMundo = new int[][]{
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, // 0: Techo
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, // 1
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 0}, // 2: Pasillo estrecho
                    {0, 0, 0, 0, 0, 0, 0, 0, 1, 3, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 3, 0}, // 3
                    {0, 0, 0, 3, 1, 1, 3, 0, 1, 3, 0, 0, 0, 0, 1, 3, 1, 1, 1, 3, 0, 1, 0, 3, 0}, // 4
                    {0, 0, 0, 3, 0, 0, 3, 0, 1, 3, 0, 0, 0, 0, 1, 3, 0, 0, 0, 3, 0, 1, 0, 3, 0}, // 5
                    {0, 0, 0, 3, 0, 0, 3, 0, 1, 1, 1, 1, 1, 1, 1, 3, 0, 0, 0, 3, 0, 1, 0, 3, 0}, // 6
                    {0, 0, 0, 3, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 3, 0, 1, 0, 3, 0}, // 7
                    {1, 1, 1, 1, 3, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1}, // 8: PISO GRUESO (Capa 1)
                    {1, 1, 1, 1, 3, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1}, // 9: PISO GRUESO (Capa 2)
                    {1, 1, 1, 1, 3, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1}, // 10: PISO GRUESO (Capa 3 - Obliga a cavar en diagonal)
                    {0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 3, 0}, // 11
                    {0, 4, 4, 4, 3, 0, 0, 0, 0, 3, 4, 4, 4, 3, 0, 0, 1, 0, 0, 0, 0, 0, 0, 3, 0}, // 12
                    {0, 0, 0, 0, 3, 1, 1, 1, 0, 3, 0, 0, 0, 3, 0, 0, 1, 0, 0, 0, 0, 0, 0, 3, 0}, // 13
                    {1, 1, 1, 0, 3, 0, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 1, 0, 0, 0, 0, 0, 0, 3, 0}, // 14
                    {0, 0, 0, 0, 3, 0, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 1, 0, 0, 0, 0, 0, 0, 3, 0}, // 15
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}  // 16: Base
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

    public boolean isEscaleraSalidaActiva() {
        return escaleraSalidaActiva;
    }
    public void setEscaleraSalidaActiva(boolean escaleraSalidaActiva){
        this.escaleraSalidaActiva = escaleraSalidaActiva;
    }
    public void activarEscalera(int nivel){
        if (escaleraSalidaActiva && nivel == 2){
            matrizMundo[0][12] = matrizMundo[1][12] = matrizMundo[2][12] = matrizMundo[3][12] = matrizMundo[4][12] = 3;
            matrizMundo[1][19] = 1; //trampa
        }
        else if (escaleraSalidaActiva && nivel == 1){
            //columna 20, filas 0, 1, 2, 3 y 4, trampa:
            matrizMundo[5][22] = 1;
            matrizMundo[5][13] = 1; //trampas
            matrizMundo[0][20] = matrizMundo[1][20] = matrizMundo[2][20] = matrizMundo[3][20] = matrizMundo[4][20] = 3;
        }
        else if (escaleraSalidaActiva && nivel == 3){
            matrizMundo[0][2] = matrizMundo[1][2] = 3;
        }
    }
}