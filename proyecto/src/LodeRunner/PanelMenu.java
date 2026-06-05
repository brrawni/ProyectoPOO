package LodeRunner;

import launcher.Boton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static com.sun.java.accessibility.util.AWTEventMonitor.addMouseListener;
import static com.sun.java.accessibility.util.AWTEventMonitor.addMouseMotionListener;

public class PanelMenu extends JPanel {
    private GestorPantallas gestor; // Referencia al gestor para pedirle cambios de pantalla
    private Boton btnJugar;
    private Boton btnConfig;
    private Boton btnRanking;

    public PanelMenu(GestorPantallas gestor) {
        this.gestor = gestor;
        this.setBackground(Color.BLACK);

        btnJugar   = new Boton(300, 200, 200, 50, "JUGAR");
        btnConfig  = new Boton(300, 280, 200, 50, "CONFIGURACIÓN");
        btnRanking = new Boton(300, 360, 200, 50, "RANKING");

        configurarEventosMouse();
    }

    private void configurarEventosMouse() {
        addMouseMotionListener(new MouseAdapter() {
            public void mouseMoved(MouseEvent e) {
                btnJugar.setHover(btnJugar.contienePunto(e.getX(), e.getY()));
                btnConfig.setHover(btnConfig.contienePunto(e.getX(), e.getY()));
                btnRanking.setHover(btnRanking.contienePunto(e.getX(), e.getY()));
                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (btnJugar.contienePunto(e.getX(), e.getY())) {
                    LodeRunnerMain juego = new LodeRunnerMain(new ConfiguracionLR());
                    juego.run();
                    // gestor.cambiarPantalla(GestorPantallas.PANTALLA_JUEGO);
                } else if (btnConfig.contienePunto(e.getX(), e.getY())) {
                    gestor.cambiarPantalla(GestorPantallas.PANTALLA_CONFIG); // Pide cambiar de pantalla
                } else if (btnRanking.contienePunto(e.getX(), e.getY())) {
                    gestor.cambiarPantalla(GestorPantallas.PANTALLA_RANKING); // Pide cambiar de pantalla
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
        g2.setFont(new Font("Arial", Font.BOLD, 60));
        g2.drawString("LODE RUNNER", 180, 120);

        btnJugar.dibujar(g2);
        btnConfig.dibujar(g2);
        btnRanking.dibujar(g2);
    }
}
