package spaceinvaders;

import motor.Videojuego;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import launcher.Boton;

public class MenuSpaceInvaders extends Videojuego {

    private static final int ANCHO = 800;
    private static final int ALTO  = 600;

    private BufferedImage buffer;
    private Boton[]       botones;

    public MenuSpaceInvaders() {
        super("Space Invaders", ANCHO, ALTO);
    }

    @Override
    public void gameStartup() {
        buffer = new BufferedImage(ANCHO, ALTO, BufferedImage.TYPE_INT_ARGB);

        int bAncho    = 260;
        int bAlto     = 50;
        int bX        = 400 - bAncho / 2;
        int espaciado = 65;
        int yInicio   = 220;

        botones = new Boton[] {
            new Boton(bX, yInicio,               bAncho, bAlto, "JUGAR"),
            new Boton(bX, yInicio + espaciado,   bAncho, bAlto, "CONFIGURACION"),
            new Boton(bX, yInicio + espaciado*2, bAncho, bAlto, "RANKING"),
            new Boton(bX, yInicio + espaciado*3, bAncho, bAlto, "VOLVER"),
        };

        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                manejarClick(e.getX(), e.getY());
            }
        });

        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                for (Boton b : botones) {
                    b.setHover(b.contienePunto(e.getX(), e.getY()));
                }
            }
        });

        canvas.setFocusable(true);
        canvas.requestFocus();
    }


    // Maneja los clicks en los botones del menú
    private void manejarClick(int mx, int my) {
        for (int i = 0; i < botones.length; i++) {
            if (botones[i].contienePunto(mx, my)) {
                switch (i) {
                    case 0: // Jugar
                        stop();
                        frame.dispose();
                        new SpaceInvaders().run();
                        break;
                    case 1: // Configuracion — próximamente
                        System.out.println("Config SI");
                        break;
                    case 2: // Ranking — próximamente
                        System.out.println("Ranking SI");
                        break;
                    case 3: // Volver al launcher
                        stop();
                        frame.dispose();
                        break;
                }
                return;
            }
        }
    }

    @Override
    public void gameUpdate(double delta) { }

    @Override
    public void gameDraw(Graphics2D g) {
        if (buffer == null) return;
        Graphics2D g2d = buffer.createGraphics();

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, ANCHO, ALTO);

        // Título
        g2d.setFont(new Font("Arial", Font.BOLD, 48));
        g2d.setColor(Color.WHITE);
        String titulo = "SPACE INVADERS";
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(titulo, 400 - fm.stringWidth(titulo)/2, 150);

        // Botones
        for (Boton b : botones) b.dibujar(g2d);

        g2d.dispose();
        g.drawImage(buffer, 0, 0, null);
    }

    @Override
    public void gameShutdown() { }
}