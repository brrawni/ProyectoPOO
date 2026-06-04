package Pong;

import motor.Entidad;
import java.awt.*;

/**
 * Clase que representa la pelota del juego Pong. Extiende Entidad.
 * Maneja movimiento, rebotes contra bordes y paletas, y aceleración progresiva.
 */
public class Pelota extends Entidad {
    private float velocidadX;
    private float velocidadY;
    private float velocidadBase;
    private float velocidadActual;
    private int rebotesSinAnotar;
    private int anchoVentana;
    private int altoVentana;

    private TemasPong tema;

    // Velocidad inicial más perceptible que el 1.5 original (que causaba que pareciera quieta)
    private static final float VELOCIDAD_INICIAL = 5.5f;
    private static final float INCREMENTO_VELOCIDAD = 0.2f;
    private static final int REBOTES_PARA_ACELERAR = 4;

    public Pelota(int x, int y, int ancho, int alto, int anchoVentana, int altoVentana) {
        super(x, y, ancho, alto);
        this.anchoVentana = anchoVentana;
        this.altoVentana = altoVentana;
        this.velocidadBase = VELOCIDAD_INICIAL;
        this.velocidadActual = VELOCIDAD_INICIAL;
        this.rebotesSinAnotar = 0;
        this.tema = null;

        inicializarDireccion(0); // 0 = dirección aleatoria al inicio
    }

    public void setTema(TemasPong tema) {
        this.tema = tema;
    }

    /**
     * Sale en línea recta horizontal con una pequeña variación vertical.
     * @param paraJugador 0=aleatorio, 1=hacia derecha, 2=hacia izquierda
     */
    private void inicializarDireccion(int paraJugador) {
        // Determinar dirección horizontal
        boolean haciaLaDerecha;
        if (paraJugador == 0) {
            haciaLaDerecha = Math.random() < 0.5;
        } else {
            haciaLaDerecha = (paraJugador == 1);
        }

        // Velocidad horizontal fija, variación vertical 0
        // Así la pelota siempre sale claramente hacia un lado
        velocidadX = haciaLaDerecha ? velocidadActual : -velocidadActual;
        velocidadY = 0;
    }

    @Override
    public void mover() {
        x += velocidadX;
        y += velocidadY;
    }

    public boolean detectarColisionPaleta(Paleta paleta) {
        return obtenerLimites().intersects(paleta.obtenerLimites());
    }

    /**
     * Rebota contra una paleta. El ángulo depende de en qué segmento (1-8) golpeó.
     * El centro de la paleta devuelve la pelota casi recta; los extremos en ángulo agudo.
     */
    public void rebotar(Paleta paleta) {
        // Calcular en qué segmento (0-7) golpeó la pelota
        int altoSegmento = paleta.obtenerAlto() / 8;
        int centroRelativo = (y + alto / 2) - paleta.obtenerY();
        int segmento = Math.max(0, Math.min(7, centroRelativo / altoSegmento));

        // Segmento 3-4 (centro) = ángulo casi recto; 0 o 7 (extremo) = ángulo agudo
        // Rango: -60° a +60°, con el centro en 0°
        float anguloGrados = -52.5f + (segmento * 15f); // -52.5, -37.5, ..., +52.5
        float angulo = (float) Math.toRadians(anguloGrados);

        // Aplicar velocidad según lado de la paleta
        if (paleta.isJugador1()) {
            // Paleta izquierda: la pelota sale hacia la derecha
            velocidadX = velocidadActual * (float) Math.cos(angulo);
        } else {
            // Paleta derecha: la pelota sale hacia la izquierda
            velocidadX = -velocidadActual * (float) Math.cos(angulo);
        }
        velocidadY = velocidadActual * (float) Math.sin(angulo);

        // Aceleración progresiva cada N rebotes
        rebotesSinAnotar++;
        if (rebotesSinAnotar >= REBOTES_PARA_ACELERAR) {
            velocidadActual += INCREMENTO_VELOCIDAD;
            rebotesSinAnotar = 0;
        }

        // Sacar la pelota de dentro de la paleta para evitar que quede atrapada
        if (paleta.isJugador1()) {
            x = paleta.obtenerX() + paleta.obtenerAncho() + 1;
        } else {
            x = paleta.obtenerX() - ancho - 1;
        }
    }

    // Rebote contra borde superior o inferior
    public void rebotar() {
        velocidadY = -velocidadY;
        if (y <= 0) y = 0;
        else if (y + alto >= altoVentana) y = altoVentana - alto;
    }

    /**
     * Reinicia la pelota en el centro después de un punto.
     * Sale en línea recta hacia el jugador que perdió el punto.
     */
    public void resetear(int posX, int posY, int paraJugador) {
        x = posX - ancho / 2;
        y = posY - alto / 2;
        velocidadActual = velocidadBase;
        rebotesSinAnotar = 0;
        inicializarDireccion(paraJugador);
    }

    public float getVelocidadActual() { return velocidadActual; }
    public float getVelocidadX() { return velocidadX; } // Usado por la IA para saber si la pelota viene hacia ella
    public int getRebotesSinAnotar() { return rebotesSinAnotar; }

    @Override
    public boolean detectarColision() { return false; }

    @Override
    public void dibujar(Graphics2D g) {
        if (visible) {
            Color colorPelota = (tema != null) ? tema.getColorPelota() : Color.WHITE;
            g.setColor(colorPelota);
            g.fillOval(x, y, ancho, alto);
        }
    }
}