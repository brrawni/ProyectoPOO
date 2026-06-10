package spaceinvaders;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import motor.Enemigo;

public class NaveNodriza extends Enemigo {
    private int contadorDisparos;
    private boolean visible = false;
    private int ticksDestruido = 0;
    private static final int TICKS_ANIMACION_DESTRUCCION = 12; 

    public NaveNodriza(Nivel nivel) {
        super(-50, 40, 60, 30, 2.0f); //arranca fuera de pantalla
        this.contadorDisparos = 0;
        this.vivo = false;
    }

    public void aparecer() {
        visible = true;
        x = -60; // Reinicia la posición para que vuelva a entrar
        vivo = true;
        ticksDestruido = 0;
    }

    @Override
    public void mover() {
        if (visible && vivo) {
            x += velocidad; // se mueve hacia la derecha
            if (x > 800) {  // salió de pantalla
                visible = false;
                vivo = false;
            }
        }
    }

    public void actualizar() {
        if (ticksDestruido > 0) {
            ticksDestruido--;
            if (ticksDestruido == 0) {
                visible = false;
            }
            return;
        }
        mover();
    }

    @Override
    public void morir() {
        vivo = false;
        ticksDestruido = TICKS_ANIMACION_DESTRUCCION;
        visible = true;
    }

    public boolean estaDestruyendose() {
        return ticksDestruido > 0;
    }

    public int calcularPuntos(int disparos) {
        if (disparos == 23 || (disparos > 23 && (disparos - 23) % 15 == 0)) {
            return 300;
        }
        // resto de disparos según tabla original
        int[] tabla = {100, 50, 150, 100, 100, 50, 100, 300, 100, 100, 150, 50};
        return tabla[disparos % tabla.length];
    }

    @Override
    public int obtenerPuntaje() {
        return calcularPuntos(contadorDisparos); // Puntaje fijo por destruir la nave nodriza
    }

    public void incrementarDisparos() {
        contadorDisparos++;
    }

    public boolean esVisible() {
        return visible;
    }

    @Override
    public void disparar() {
        //la nave nodriza no dispara
    }

    @Override
    public boolean detectarColision() {
        return false; 
    }

    @Override
    public void dibujar(Graphics2D g) {
        if (!visible || (!vivo && !estaDestruyendose())) return;

        GestorImagenes gestor = GestorImagenes.getInstance();
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

        String skin = GestorConfiguracionSpaceInvaders.getInstance().getSkinInvasores();
        String sufijo = "alternativa".equals(skin) ? "nodriza_alternativa.png" : "navenodriza.png";
        String ruta = "/img/spaceinvaders/" + sufijo;

        BufferedImage img = gestor.cargar(ruta);
        if (img != null) {
            //si es skin original, colorea la imagen
            if ("original".equals(skin)) {
                img = gestor.colorear(img, Color.WHITE);
            }
            g.drawImage(img, x, y, ancho, alto, null);
        } else {
            g.setColor(Color.WHITE);
            g.fillRect(x, y, ancho, alto);
        }
    }
}