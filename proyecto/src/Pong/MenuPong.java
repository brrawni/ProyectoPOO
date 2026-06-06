package Pong;

import launcher.Boton;
import ranking.GestorRanking;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * Menú principal de Pong.
 * Usa JFrame propio + Timer de Swing. No extiende Videojuego.
 */
public class MenuPong extends JFrame {

    private static final int ANCHO = 800;
    private static final int ALTO  = 600;
    private static final String RANKING_PONG = "ranking_pong.txt";

    private JPanel canvas;
    private Timer timer;
    private BufferedImage buffer;

    private Boton[] botones;
    private ConfiguracionPong config;
    private GestorRanking gestorRanking;
    private JFrame launcher;

    public MenuPong(JFrame launcher) {
        super("Pong - Menu Principal");
        this.launcher      = launcher;
        this.config        = new ConfiguracionPong();
        this.gestorRanking = new GestorRanking(RANKING_PONG);

        config.cargar();
        configurarVentana();
        crearBotones();
        configurarListeners();

        timer = new Timer(16, e -> canvas.repaint());
        timer.start();
    }

    private void configurarVentana() {
        setSize(ANCHO, ALTO);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        buffer = new BufferedImage(ANCHO, ALTO, BufferedImage.TYPE_INT_ARGB);

        canvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                renderizar((Graphics2D) g);
            }
        };
        canvas.setPreferredSize(new Dimension(ANCHO, ALTO));
        canvas.setFocusable(true);
        add(canvas);
        pack();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                timer.stop();
                SwingUtilities.invokeLater(() -> launcher.setVisible(true));
            }
        });
    }

    private void crearBotones() {
        int bAncho    = 280;
        int bAlto     = 50;
        int bX        = ANCHO / 2 - bAncho / 2;
        int espaciado = 65;
        int yInicio   = 150;

        botones = new Boton[]{
            new Boton(bX, yInicio,               bAncho, bAlto, "HUMANO vs HUMANO"),
            new Boton(bX, yInicio + espaciado,   bAncho, bAlto, "HUMANO vs CPU"),
            new Boton(bX, yInicio + espaciado*2, bAncho, bAlto, "CONFIGURACION"),
            new Boton(bX, yInicio + espaciado*3, bAncho, bAlto, "RANKING"),
            new Boton(bX, yInicio + espaciado*4, bAncho, bAlto, "VOLVER")
        };
    }

    private void configurarListeners() {
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                manejarClick(e.getX(), e.getY());
            }
        });
        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                for (Boton b : botones) b.setHover(b.contienePunto(e.getX(), e.getY()));
            }
        });
    }

    private void manejarClick(int mx, int my) {
        for (int i = 0; i < botones.length; i++) {
            if (!botones[i].contienePunto(mx, my)) continue;
            switch (i) {
                case 0: lanzarPong(2); break;
                case 1: lanzarPong(1); break;
                case 2: new VentanaConfiguracionPong(config, this).setVisible(true); break;
                case 3: new VentanaRankingPong(gestorRanking, this).setVisible(true); break;
                case 4: dispose(); break;
            }
            return;
        }
    }

    private void lanzarPong(int modoJuego) {
        setVisible(false);
        timer.stop();

        Pong pong = new Pong(config.getSkinCancha(), config.getSkinBarras(), config.getSkinPelota());
        pong.setPuntuacionMaxima(config.getPuntuacionMaxima());
        pong.setModoJuego(modoJuego);
        // Pong maneja internamente el redimensionado del canvas en gameStartup()
        pong.setPantallaCompleta(config.isPantallaCompleta());
        

        pong.frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                SwingUtilities.invokeLater(() -> {
                    timer.start();
                    setVisible(true);
                });
            }
        });

        pong.run();
    }

    private void renderizar(Graphics2D g) {
        Graphics2D bg = buffer.createGraphics();
        bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        bg.setColor(new Color(20, 20, 40));
        bg.fillRect(0, 0, ANCHO, ALTO);

        bg.setColor(new Color(100, 150, 255));
        bg.setStroke(new BasicStroke(3));
        bg.drawLine(50, 80, ANCHO - 50, 80);
        bg.drawLine(50, 120, ANCHO - 50, 120);

        bg.setColor(Color.WHITE);
        bg.setFont(new Font("Arial", Font.BOLD, 48));
        String titulo = "PONG";
        FontMetrics fm = bg.getFontMetrics();
        bg.drawString(titulo, (ANCHO - fm.stringWidth(titulo)) / 2, 68);

        for (Boton b : botones) b.dibujar(bg);

        bg.setColor(new Color(150, 150, 150));
        bg.setFont(new Font("Arial", Font.PLAIN, 12));
        String pie = "Clásico juego de tenis para 2 jugadores";
        fm = bg.getFontMetrics();
        bg.drawString(pie, (ANCHO - fm.stringWidth(pie)) / 2, ALTO - 20);

        bg.dispose();
        g.drawImage(buffer, 0, 0, null);
    }
}
