package spaceinvaders;


import java.awt.Graphics2D;
import motor.Enemigo;
import motor.GestorImagenes;

public class Alien extends Enemigo {

    public static final int CALAMAR = 0;
    public static final int CANGREJO = 1;
    public static final int PULPO = 2;

    private int tipo;
    private float xReal; //posicion real en float
    private int  direccion = 1;
    private int frameAnimacion;

    public Alien(int tipo, int x, int y) {
        super(x, y, 32, 32, 1.0f); // Tamaño y velocidad del alien
        this.tipo = tipo;
        this.xReal = x;
    }

    public void bajar(int cantidad) {
        y += cantidad; // Baja el alien
    }

    public void setVelocidad(float velocidad) {
        this.velocidad = velocidad;
    }

    public void setDireccion(int direccion) {
        this.direccion = direccion;
    }
    
    @Override
    public void mover() {
        xReal += velocidad * direccion;
        x = (int) xReal;
    }

    @Override
    public void disparar() {
        // Lógica para disparar un proyectil
    }

    public void actualizar() {
        // Lógica para actualizar la animación del alien
        frameAnimacion++;
        if (frameAnimacion >= 30) { // Cambia de frame cada 30 actualizaciones
            frameAnimacion = 0;
        }
    }

    public int obtenerFrameAnimacion() {
        return frameAnimacion < 15 ? 0 : 1; // Retorna 0 o 1 para alternar entre dos frames
    }

    @Override
    public int obtenerPuntaje() {
        // Retorna el puntaje basado en el tipo de alien
        switch (tipo) {
            case CALAMAR:
                return 10;
            case CANGREJO:
                return 20;
            case PULPO:
                return 30;
            default:
                return 0;
        }
    }
    
    @Override
    public boolean detectarColision() {
        // Lógica para detectar colisiones con proyectiles del jugador
        return false; // Placeholder
    }

    @Override
    public void dibujar(Graphics2D g) {
        GestorImagenes gestor = GestorImagenes.getInstance();
        String nombreSprite;
        Color color;
        switch (tipo) {
            case CALAMAR:
                nombreSprite = "calamar";
                color = Color.GREEN;
                break;
            case CANGREJO:
                nombreSprite = "cangrejo";
                color = Color.RED;
                break;
            case PULPO:
                nombreSprite = "pulpo";
                color = Color.MAGENTA;
                break;
            default:
                nombreSprite = "calamar";
                color = Color.WHITE;
        }
        int frame = obtenerFrameAnimacion();
        String ruta = "/img/spaceinvaders/" + nombreSprite + "_" + frame + ".png";

        BufferedImage img = gestor.cargar(ruta);
        img = gestor.colorear(img, color);

        if (img != null) {
            g.drawImage(img, x, y, ancho, alto, null);
        } else {
            g.setColor(color);
            g.fillRect(x, y, ancho, alto);
        }
    }
}