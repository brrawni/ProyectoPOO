package spaceinvaders;


import java.awt.Graphics2D;
import motor.Enemigo;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class Alien extends Enemigo {

    public static final int CALAMAR = 0;
    public static final int CANGREJO = 1;
    public static final int PULPO = 2;


    private int tipo;
    private float xReal; //posicion real en float
    private int  direccion = 1;
    private int frameAnimacion;
    private int ticksDestruido = 0;
    private static final int TICKS_ANIMACION_DESTRUCCION = 1; //duración de la animación de destrucción en ticks

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
        if (ticksDestruido > 0) {
            ticksDestruido--;
            return;
        }
        // Lógica para actualizar la animación del alien
        frameAnimacion++;
        if (frameAnimacion >= 8) { // Cambia de frame cada 8 actualizaciones
            frameAnimacion = 0;
        }
    }

    public int obtenerFrameAnimacion() {
        return frameAnimacion < 4 ? 0 : 1; // Retorna 0 o 1 para alternar entre dos frames
    }

    @Override
    public void morir() {
        vivo = false;
        ticksDestruido = TICKS_ANIMACION_DESTRUCCION;
    }

    public boolean estaDestruyendose() {
        return ticksDestruido > 0;
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
        return false;
    }

    @Override
    public void dibujar(Graphics2D g) {
        GestorImagenes gestor = GestorImagenes.getInstance();

        String nombreSprite;
        Color color;
        switch (tipo) {
            case CALAMAR:
                nombreSprite = "calamar";
                color = Color.YELLOW;
                break;
            case CANGREJO:
                nombreSprite = "cangrejo";
                color = Color.RED;
                break;
            case PULPO:
                nombreSprite = "pulpo";
                color = Color.CYAN;
                break;
            default:
                nombreSprite = "calamar";
                color = Color.YELLOW;
        }

        if (estaDestruyendose()) {
            BufferedImage img = gestor.cargar("/img/spaceinvaders/destruido.png");
            if (img != null) {
                g.drawImage(img, x, y, ancho, alto, null);
            } else {
                g.setColor(Color.ORANGE);
                g.fillRect(x, y, ancho, alto);
            }
            return;
        }

        int frame = obtenerFrameAnimacion();
        String skinInvasores = GestorConfiguracionSpaceInvaders.getInstance().getSkinInvasores();
        
        String sufijo = "alternativa".equals(skinInvasores) ? "_alternativo_" : "_";
        String ruta = "/img/spaceinvaders/" + nombreSprite + sufijo + frame + ".png";
        BufferedImage img = gestor.cargar(ruta);
        
        if (img != null) {
            // Si es skin original, colorea la imagen negra
            if ("original".equals(skinInvasores)) {
                img = gestor.colorear(img, color);
            }
            g.drawImage(img, x, y, ancho, alto, null);
        } else {
            g.setColor(color);
            g.fillRect(x, y, ancho, alto);
        }
    }

}