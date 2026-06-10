package spaceinvaders;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JPanel;
import launcher.Boton;
import motor.EntradaRanking;

public class PanelRankingSpaceInvaders extends JPanel {
    private static final int ANCHO_LOGICO = 800;
    private static final int ALTO_LOGICO = 600;

    private CardLayout cardLayout;
    private JPanel parentCardPanel;
    private Runnable alVolver;
    private RankingSpaceInvaders ranking;
    private Boton btnVolver;

    public PanelRankingSpaceInvaders(CardLayout cardLayout, JPanel parentCardPanel, Runnable alVolver) {
        this.cardLayout = cardLayout;
        this.parentCardPanel = parentCardPanel;
        this.alVolver = alVolver;
        this.ranking = new RankingSpaceInvaders();

        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(ANCHO_LOGICO, ALTO_LOGICO));

        btnVolver = new Boton(300, 500, 200, 50, "VOLVER");
        configurarEventosMouse();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                actualizarRanking();
            }
        });
    }

    public void actualizarRanking() {
        ranking.cargar();
        repaint();
    }

    private void configurarEventosMouse() {
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int xLogico = convertirX(e.getX());
                int yLogico = convertirY(e.getY());
                btnVolver.setHover(btnVolver.contienePunto(xLogico, yLogico));
                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int xLogico = convertirX(e.getX());
                int yLogico = convertirY(e.getY());

                if (btnVolver.contienePunto(xLogico, yLogico)) {
                    cardLayout.show(parentCardPanel, "GAME");
                    if (alVolver != null) {
                        alVolver.run();
                    }
                }
            }
        });
    }

    private int convertirX(int x) {
        return (int) (x * ((double) ANCHO_LOGICO / Math.max(1, getWidth())));
    }

    private int convertirY(int y) {
        return (int) (y * ((double) ALTO_LOGICO / Math.max(1, getHeight())));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.scale(getWidth() / (double) ANCHO_LOGICO, getHeight() / (double) ALTO_LOGICO);

        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("Arial", Font.BOLD, 45));
        g2.drawString("MEJORES PUNTAJES", 170, 70);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Monospaced", Font.BOLD, 26));

        List<EntradaRanking> top10 = ranking.obtenerTop10();
        if (top10.isEmpty()) {
            g2.drawString("NO HAY PUNTAJES AUN.", 240, 250);
        } else {
            int yPos = 140;
            for (int i = 0; i < top10.size(); i++) {
                EntradaRanking entrada = top10.get(i);
                String nombre = entrada.getNombre();
                if (nombre.length() > 8) {
                    nombre = nombre.substring(0, 8);
                }

                String lineaPuntaje = String.format("%2d. %-8s ...... %06d",
                        i + 1, nombre, entrada.getPuntaje());
                g2.drawString(lineaPuntaje, 180, yPos);
                yPos += 35;
            }
        }

        btnVolver.dibujar(g2);
        g2.dispose();
    }
}
