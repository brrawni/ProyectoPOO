Package LodeRunner;

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
    }
    public void dibujar(){

    }
    public void cargarNivel(int numeroNivel){
        numeroNivel = numeroNivel;
    }
    public void actualizarPozos(){

    }
    public void obtenerTipoBloqueEn(){

    }
}