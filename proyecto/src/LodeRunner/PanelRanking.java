package LodeRunner;

import launcher.Boton;
import motor.EntradaRanking;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import static com.sun.java.accessibility.util.AWTEventMonitor.addMouseMotionListener;

public class PanelRanking extends JPanel {
    private GestorPantallas gestor;
    private Boton btnVolver;

    private RankingLR rankingLR;

    public PanelRanking(GestorPantallas gestor) {
        this.gestor = gestor;
        this.setBackground(new Color(0, 40, 0)); // Fondo arcade verde oscuro

        this.rankingLR = new RankingLR();

        // Botón posicionado en la parte de abajo
        btnVolver = new Boton(300, 500, 200, 50, "VOLVER");

        configurarEventosMouse();

        // automáticamente justo en el milisegundo en que el jugador entra a esta pantalla.
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                rankingLR.cargar();
            }
        });
    }

    private void configurarEventosMouse() {
        addMouseMotionListener(new MouseAdapter() {
            public void mouseMoved(MouseEvent e) {
                btnVolver.setHover(btnVolver.contienePunto(e.getX(), e.getY()));
                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (btnVolver.contienePunto(e.getX(), e.getY())) {
                    gestor.cambiarPantalla(GestorPantallas.PANTALLA_MENU);
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("Arial", Font.BOLD, 45));
        g2.drawString("MEJORES PUNTAJES", 170, 70);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Monospaced", Font.BOLD, 26));

        List<EntradaRanking> top10 = rankingLR.obtenerTop10();

        if (top10.isEmpty()) {
            // Si el archivo está vacío o recién instalan el juego
            g2.drawString("NO HAY PUNTAJES AÚN.", 240, 250);
            g2.drawString("¡SÉ EL PRIMERO!", 270, 300);
        } else {
            int yPos = 140;

            // Iteramos sobre la lista real
            for (int i = 0; i < top10.size(); i++) {
                EntradaRanking entrada = top10.get(i);

                // para que no desalinee las columnas
                String nombre = entrada.getNombre();
                if (nombre.length() > 8) {
                    nombre = nombre.substring(0, 8);
                }

                // %2d = número de 2 cifras (para alinear el 10 con el 1)
                // %-8s = Texto alineado a la izquierda rellenado con espacios hasta 8 caracteres
                // %06d = Puntaje con 6 ceros a la izquierda (Ej: 005000)
                String lineaPuntaje = String.format("%2d. %-8s ...... %06d",
                        (i + 1), nombre, entrada.getPuntaje());

                g2.drawString(lineaPuntaje, 180, yPos);
                yPos += 35; // Espacio vertical entre cada jugador
            }
        }

        btnVolver.dibujar(g2);
    }
}
