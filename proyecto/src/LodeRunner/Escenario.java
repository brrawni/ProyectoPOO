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
    private boolean escaleraSalidaActiva;

    private BufferedImage escalera;
    private BufferedImage ladrillo;
    
    public Escenario(int ancho_bloque, int alto_bloque, int numeroNivel){
        this.ancho_bloque = ancho_bloque;
        this.alto_bloque = alto_bloque;
        this.mapaPozosTemporales = new HashMap<>();
        try{
            //Cargamos las imagenes correspondientes a los ladrillos y escaleras.
            BufferedImage hojaSpritesEscalera = ImageIO.read(getClass().getResourceAsStream("/img/loderunner/escaleras.png"));
            BufferedImage hojaSpritesLadrillos = ImageIO.read(getClass().getResourceAsStream("/img/loderunner/ladrillos.png"));
            escalera = hojaSpritesEscalera.getSubimage(0,0,32,32);
            ladrillo = hojaSpritesLadrillos.getSubimage(2*32, 5*32, 32, 32);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        cargarNivel(numeroNivel);
    }
    public void dibujar(Graphics2D g){
        //recorremos la "matriz mundo"
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
                        g.drawImage(ladrillo, x, y, null);
                        break;
                    case 2: // Ladrillo tambien, usado para identificar escalera de salida
                        g.drawImage(ladrillo, x, y, null);
                        break;
                    case 3: // Escalera
                        g.drawImage(escalera, x-16, y-16, 64, 64, null); //esto es para que no se vea borroso
                        break;
                    case 4: // Barra/Soga
                        g.setColor(Color.WHITE);
                        g.fillRect(x, y + 4, ancho_bloque, 4);
                        break;
                    case 5: // Aire tambien, usado para identificar escalera de salida
                        break;
                    default:
                        //no dibujar, pantalla negra
                        break;
                }
            }
        }
    }
    public void cargarNivel(int numeroNivel) {
        this.escaleraSalidaActiva = false;
        // Lógica para cargar la matrizMundo y los sprites según el número de nivel
        // 0 = Aire, 1 = Ladrillo Común, 3 = Escalera, 4 = Barra/Soga
        String ruta = "/niveles/loderunner/nivel" + numeroNivel + ".txt";
        matrizMundo = CargadorNivel.cargarMatrizNivel(ruta);
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
    public void activarEscalera() {
        if (escaleraSalidaActiva) {
            for (int fila = 0; fila < matrizMundo.length; fila++) {
                for (int columna = 0; columna < matrizMundo[fila].length; columna++) {
                    if (matrizMundo[fila][columna] == 5 || matrizMundo[fila][columna] == 2){
                        matrizMundo[fila][columna] = 3;
                    }
                }
            }
        }
    }
}