package Pong;

import java.awt.*;
import java.awt.image.BufferedImage;
import launcher.Boton;

/**
 * Pantalla visual del menu principal de Pong.
 * No crea ventanas: solo dibuja botones y devuelve acciones al coordinador Pong.
 */
public class PantallaMenuPong {
    public enum Accion {
        NINGUNA,
        HUMANO_VS_HUMANO,
        HUMANO_VS_CPU,
        CONFIGURACION,
        RANKING,
        VOLVER
    }

    private final int ancho;
    private final int alto;
    private final BufferedImage buffer;
    private Boton[] botones;

    public PantallaMenuPong(int ancho, int alto) {
        this.ancho = ancho;
        this.alto = alto;
        this.buffer = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        crearBotones();
    }

    private void crearBotones() {
        int bAncho = 280;
        int bAlto = 50;
        int bX = ancho / 2 - bAncho / 2;
        int espaciado = 65;
        int yInicio = 150;

        botones = new Boton[]{
            new Boton(bX, yInicio, bAncho, bAlto, "HUMANO vs HUMANO"),
            new Boton(bX, yInicio + espaciado, bAncho, bAlto, "HUMANO vs CPU"),
            new Boton(bX, yInicio + espaciado * 2, bAncho, bAlto, "CONFIGURACION"),
            new Boton(bX, yInicio + espaciado * 3, bAncho, bAlto, "RANKING"),
            new Boton(bX, yInicio + espaciado * 4, bAncho, bAlto, "VOLVER")
        };
    }

    public Accion manejarClick(int mx, int my) {
        Accion accion = Accion.NINGUNA;
        for (int i = 0; i < botones.length; i++) {
            if (!botones[i].contienePunto(mx, my)) continue;
            switch (i) {
                case 0:
                    accion = Accion.HUMANO_VS_HUMANO;
                    break;
                case 1:
                    accion = Accion.HUMANO_VS_CPU;
                    break;
                case 2:
                    accion = Accion.CONFIGURACION;
                    break;
                case 3:
                    accion = Accion.RANKING;
                    break;
                case 4:
                    accion = Accion.VOLVER;
                    break;
                default:
                    accion = Accion.NINGUNA;
                    break;
            }
        }
        return accion;
    }

    public void actualizarHover(int mx, int my) {
        for (Boton boton : botones) {
            boton.setHover(boton.contienePunto(mx, my));
        }
    }

    public void renderizar(Graphics2D g) {
        Graphics2D bg = buffer.createGraphics();
        bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        bg.setColor(new Color(20, 20, 40));
        bg.fillRect(0, 0, ancho, alto);

        bg.setColor(new Color(100, 150, 255));
        bg.setStroke(new BasicStroke(3));
        bg.drawLine(50, 80, ancho - 50, 80);
        bg.drawLine(50, 120, ancho - 50, 120);

        bg.setColor(Color.WHITE);
        bg.setFont(new Font("Arial", Font.BOLD, 48));
        String titulo = "PONG";
        FontMetrics fm = bg.getFontMetrics();
        bg.drawString(titulo, (ancho - fm.stringWidth(titulo)) / 2, 68);

        for (Boton boton : botones) {
            boton.dibujar(bg);
        }

        bg.setColor(new Color(150, 150, 150));
        bg.setFont(new Font("Arial", Font.PLAIN, 12));
        String pie = "Clasico juego de tenis para 2 jugadores";
        fm = bg.getFontMetrics();
        bg.drawString(pie, (ancho - fm.stringWidth(pie)) / 2, alto - 20);

        bg.dispose();
        g.drawImage(buffer, 0, 0, null);
    }
}
