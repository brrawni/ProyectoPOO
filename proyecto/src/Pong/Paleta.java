package Pong;

import motor.Entidad;
import java.awt.*;

/**
 * Clase que representa una paleta (raqueta) del juego Pong.
 * Extiende Entidad e implementa movimiento vertical dentro de los límites de la pantalla.
 * Detecta colisiones con la pelota.
 * Soporta temas visuales para cambiar apariencia según configuración.
 */
public class Paleta extends Entidad {
    private float velocidad = 5; // Velocidad de movimiento de la paleta
    private boolean esJugador1; // true si es la paleta del jugador 1 (izquierda)
    private int altoVentana; // Alto de la ventana (límite de movimiento)
    
    // Tema visual para obtener colores
    private TemasPong tema;

    /**
     * Constructor de Paleta
     * @param x Posición X inicial
     * @param y Posición Y inicial
     * @param ancho Ancho de la paleta
     * @param alto Alto de la paleta
     * @param esJugador1 true si pertenece al jugador 1
     */
    public Paleta(int x, int y, int ancho, int alto, boolean esJugador1) {
        super(x, y, ancho, alto);
        this.esJugador1 = esJugador1;
        this.visible = true;
        this.tema = null; // Se inicializa desde Pong.java
    }

    /**
     * Establece el tema visual para esta paleta
     * @param tema El tema a usar para colores
     */
    public void setTema(TemasPong tema) {
        this.tema = tema;
    }

    /**
     * Mueve la paleta hacia arriba
     */
    public void moverArriba() {
        if (y > 0) {
            y -= (int) velocidad;
        }
    }

    /**
     * Mueve la paleta hacia abajo
     */
    public void moverAbajo() {
        if (y + alto < altoVentana) {
            y += (int) velocidad;
        }
    }

    /**
     * Establece el alto de la ventana (necesario para limitar el movimiento)
     */
    public void setAltoVentana(int altoVentana) {
        this.altoVentana = altoVentana;
    }


    /**
     * Obtiene la velocidad actual
     */
    public float getVelocidad() {
        return velocidad;
    }

    /**
     * Establece la velocidad
     */
    public void setVelocidad(float velocidad) {
        this.velocidad = velocidad;
    }

    /**
     * Verifica si esta paleta pertenece al jugador 1
     */
    public boolean isJugador1() {
        return esJugador1;
    }

    /**
     * Implementación abstracta: mover (aquí no se usa, usamos moverArriba/Abajo)
     */
    @Override
    public void mover() {
        // Este método se reemplaza por moverArriba() y moverAbajo()
    }

    /**
     * Detecta colisión: este método se usa desde Pelota
     * @return true si la pelota está en colisión con la paleta
     */
    @Override
    public boolean detectarColision() {
        // Este método se implementa en Pelota.detectarColisionPaleta()
        return false;
    }

    /**
     * Dibuja la paleta en la pantalla usando colores del tema
     */
    @Override
    public void dibujar(Graphics2D g) {
        if (visible) {
            // Obtener color del tema (o usar blanco como fallback si no hay tema)
            Color colorPaleta = (tema != null) ? tema.getColorPaleta() : Color.WHITE;
            String skin = (tema != null) ? tema.getSkinBarras() : "original";
            int anchoDibujo = "delgado".equals(skin) ? Math.max(6, ancho / 2) : ancho;
            int xDibujo = x + (ancho - anchoDibujo) / 2;
            
            // Dibujar la paleta con color del tema
            g.setColor(colorPaleta);
            g.fillRect(xDibujo, y, anchoDibujo, alto);
            
            // Opcional: Dibujar borde para mejor visualización
            g.setColor(new Color(colorPaleta.getRed()/2, colorPaleta.getGreen()/2, colorPaleta.getBlue()/2));
            g.setStroke(new BasicStroke(2));
            g.drawRect(xDibujo, y, anchoDibujo, alto);
        }
    }
}
