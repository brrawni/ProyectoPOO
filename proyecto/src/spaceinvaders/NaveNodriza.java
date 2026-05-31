package spaceinvaders;

import spaceinvaders.GestorImagenes;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import motor.Enemigo;

public class NaveNodriza extends Enemigo {
    private int contadorDisparos;
    private boolean visible = false;

    public NaveNodriza(Nivel nivel) {
        super(-50, 40, 60, 30, 2.0f); //arranca fuera de pantalla
        this.contadorDisparos = 0;
        this.vivo = false;
    }

    public void aparecer() {
        visible = true;
        x = -60; // Reinicia la posición para que vuelva a entrar
        vivo = true;
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
        // Lógica para actualizar la animación de la nave nodriza
        mover();
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
        // La nave nodriza no dispara
    }

    @Override
    public boolean detectarColision() {
        return false; // La nave nodriza no colisiona con proyectiles
    }

    @Override
    public void dibujar(Graphics2D g) {
        if (!visible || !vivo) return;

        GestorImagenes gestor = GestorImagenes.getInstance();
        String ruta       = "/img/spaceinvaders/navenodriza.png";
        String claveCache = ruta + "_" + Color.RED.getRGB();

        BufferedImage img = gestor.cargarDeCache(claveCache);
        if (img == null) {
            img = gestor.colorear(gestor.cargar(ruta), Color.RED);
            if (img != null) gestor.guardarEnCache(claveCache, img);
        }

        if (img != null) {
            g.drawImage(img, x, y, ancho, alto, null);
        } else {
            g.setColor(Color.RED);
            g.fillRect(x, y, ancho, alto);
        }
    }
}