package launcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PanelLauncher extends JPanel {

    private Launcher launcher;
    private Boton[]  botones;

    public PanelLauncher(Launcher launcher) {
        this.launcher = launcher;
        setBackground(Color.BLACK);
        setLayout(null);

        int bAncho    = 260;
        int bAlto     = 50;
        int bX        = 400 - bAncho / 2;
        int espaciado = 70;
        int yInicio   = 200;

        botones = new Boton[] {
            new Boton(bX, yInicio,               bAncho, bAlto, "SPACE INVADERS"),
            new Boton(bX, yInicio + espaciado,   bAncho, bAlto, "LODE RUNNER"),
            new Boton(bX, yInicio + espaciado*2, bAncho, bAlto, "PONG"),
        };

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (int i = 0; i < botones.length; i++) {
                    if (botones[i].contienePunto(e.getX(), e.getY())) {
                        switch (i) {
                            case 0: launcher.lanzarJuego("spaceinvaders"); break;
                            case 1: launcher.lanzarJuego("loderunner");    break;
                            case 2: launcher.lanzarJuego("pong");          break;
                        }
                        return;
                    }
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                for (Boton b : botones) {
                    b.setHover(b.contienePunto(e.getX(), e.getY()));
                }
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.setFont(new Font("Arial", Font.BOLD, 42));
        g2d.setColor(Color.WHITE);
        String titulo = "RETRO ARCADE";
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(titulo, 400 - fm.stringWidth(titulo)/2, 130);

        g2d.setFont(new Font("Arial", Font.PLAIN, 13));
        g2d.setColor(new Color(120, 120, 120));
        String sub = "Selecciona un juego para comenzar";
        fm = g2d.getFontMetrics();
        g2d.drawString(sub, 400 - fm.stringWidth(sub)/2, 165);

        for (Boton b : botones) b.dibujar(g2d);
    }
}