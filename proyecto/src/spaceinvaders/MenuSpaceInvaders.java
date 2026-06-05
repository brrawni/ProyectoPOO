package spaceinvaders;

import motor.Videojuego;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import launcher.Boton;
import javax.swing.JFrame;
import launcher.Launcher;

public class MenuSpaceInvaders extends Videojuego {

    private static final int ANCHO = 800;
    private static final int ALTO  = 600;

    private BufferedImage buffer;
    private Boton[]       botones;

    private int[][] estrellas; // Para el fondo animado de estrellas

    private int siguientePantalla = 0; // 0=salir, 1=jugar, 2=configuracion
    private Launcher launcher;

    public MenuSpaceInvaders(Launcher launcher) {
        super("Space Invaders", ANCHO, ALTO);
        this.launcher = launcher;
    }

    @Override
    public void gameStartup() {
        buffer = new BufferedImage(ANCHO, ALTO, BufferedImage.TYPE_INT_ARGB);

    
        // Solo dejás esto:
        canvas.requestFocus();

        // Estrellas
        estrellas = new int[80][3];
        for (int i = 0; i < estrellas.length; i++) {
            estrellas[i][0] = (int)(Math.random() * ANCHO);
            estrellas[i][1] = (int)(Math.random() * ALTO);
            estrellas[i][2] = Math.random() < 0.3 ? 2 : 1;
        }

        // Botones
        int bAncho = 260, bAlto = 50;
        int bX = 400 - bAncho / 2;
        int espaciado = 65, yInicio = 220;

        botones = new Boton[] {
            new Boton(bX, yInicio,               bAncho, bAlto, "JUGAR"),
            new Boton(bX, yInicio + espaciado,   bAncho, bAlto, "CONFIGURACION"),
            new Boton(bX, yInicio + espaciado*2, bAncho, bAlto, "RANKING"),
            new Boton(bX, yInicio + espaciado*3, bAncho, bAlto, "VOLVER"),
        };

        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int xLogico = (int)(e.getX() * ((double)ANCHO / canvas.getWidth()));
                int yLogico = (int)(e.getY() * ((double)ALTO / canvas.getHeight()));
                manejarClick(xLogico, yLogico);
            }
        });

        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int xLogico = (int)(e.getX() * ((double)ANCHO / canvas.getWidth()));
                int yLogico = (int)(e.getY() * ((double)ALTO / canvas.getHeight()));
                for (Boton b : botones) {
                    b.setHover(b.contienePunto(xLogico, yLogico));
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
                        siguientePantalla = 1;
                        stop();
                        break;
                    case 1: // Configuracion
                        siguientePantalla = 2;
                        stop();
                        break;
                    case 2: // Ranking — próximamente
                        System.out.println("Ranking SI");
                        break;
                    case 3: // Volver al launcher
                        siguientePantalla = 0;
                        stop();
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

        //fondo negro
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, ANCHO, ALTO);

        //titlo
        g2d.setFont(new Font("Arial", Font.BOLD, 48));
        g2d.setColor(Color.WHITE);
        String titulo = "SPACE INVADERS";
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(titulo, 400 - fm.stringWidth(titulo)/2, 150);

        //botones
        for (Boton b : botones) b.dibujar(g2d);

        //animacion de estrellas
        for (int[] e : estrellas) {
            int brillo = 100 + (int)(Math.random() * 155);
            g2d.setColor(new Color(brillo, brillo, brillo));
            g2d.fillRect(e[0], e[1], e[2], e[2]);
        }
        //dispose y mostrar
        g2d.dispose();
        g.drawImage(buffer, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
    }

    @Override
    public void gameShutdown() { 
        switch (siguientePantalla) {
            case 0: // Volver al launcher
                javax.swing.SwingUtilities.invokeLater(() -> launcher.setVisible(true));
                break;
            case 1: // Jugar
                new SpaceInvaders(launcher).run();
                break;
            case 2: // Configuración
                new PantallaConfiguracion(launcher).run();
                break;
        }
    }
}