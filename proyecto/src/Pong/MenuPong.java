package Pong;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import launcher.Boton;
import motor.GestorRankingBase;

/**
 * Menu principal de Pong.
 * Usa JFrame propio + Timer de Swing. No extiende Videojuego.
 */
@SuppressWarnings("serial")
public class MenuPong extends JFrame {
    private static final long serialVersionUID = 1L;

    private static final int ANCHO = 800;
    private static final int ALTO  = 600;
    private static final String RANKING_PONG = "ranking_pong.txt";

    private JPanel canvas;
    private Timer timer;
    private BufferedImage buffer;

    private Boton[] botones;
    private ConfiguracionPong config;
    private GestorRankingBase gestorRanking;
    private GestorSonidosPong gestorSonidos;
    private PantallaRankingPong pantallaRanking;
    private PantallaConfiguracionPong pantallaConfiguracion;
    private JFrame launcher;
    private boolean mostrandoConfiguracion = false;
    private boolean mostrandoRanking = false;

    public MenuPong(JFrame launcher) {
        super("Pong - Menu Principal");
        this.launcher      = launcher;
        this.config        = new ConfiguracionPong();
        this.gestorRanking = new GestorRankingBase(RANKING_PONG);

        config.cargar();
        this.gestorSonidos = new GestorSonidosPong(config.isSonidoActivado());
        this.pantallaRanking = new PantallaRankingPong(ANCHO, ALTO, gestorRanking);
        this.pantallaConfiguracion = new PantallaConfiguracionPong(ANCHO, ALTO, config);
        configurarVentana();
        crearBotones();
        configurarListeners();
        actualizarSonidoMenu();

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
                gestorSonidos.limpiar();
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
                int mx = escalarX(e.getX());
                int my = escalarY(e.getY());
                if (mostrandoRanking) {
                    manejarClickRanking(mx, my);
                } else if (mostrandoConfiguracion) {
                    manejarClickConfiguracion(mx, my);
                } else {
                    manejarClickMenu(mx, my);
                }
            }
        });
        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int mx = escalarX(e.getX());
                int my = escalarY(e.getY());
                if (mostrandoRanking) {
                    actualizarHoverRanking(mx, my);
                } else if (mostrandoConfiguracion) {
                    actualizarHoverConfiguracion(mx, my);
                } else {
                    for (Boton b : botones) b.setHover(b.contienePunto(mx, my));
                }
            }
        });
    }

    /** Convierte coordenada X real del canvas a coordenada lógica 800x600 */
    private int escalarX(int x) {
        int w = canvas.getWidth();
        return (w > 0) ? x * ANCHO / w : x;
    }

    /** Convierte coordenada Y real del canvas a coordenada lógica 800x600 */
    private int escalarY(int y) {
        int h = canvas.getHeight();
        return (h > 0) ? y * ALTO / h : y;
    }

    private void manejarClickMenu(int mx, int my) {
        for (int i = 0; i < botones.length; i++) {
            if (!botones[i].contienePunto(mx, my)) continue;
            switch (i) {
                case 0: lanzarPong(2); break;
                case 1: lanzarPong(1); break;
                case 2: mostrarConfiguracion(); break;
                case 3: mostrarRanking(); break;
                case 4: dispose(); break;
            }
            return;
        }
    }

    private void mostrarRanking() {
        mostrandoRanking = true;
        pantallaRanking.iniciar();
        canvas.requestFocus();
    }

    private void mostrarConfiguracion() {
        mostrandoConfiguracion = true;
        pantallaConfiguracion.iniciar();
        canvas.requestFocus();
    }

    private void manejarClickConfiguracion(int mx, int my) {
        PantallaConfiguracionPong.Accion accion = pantallaConfiguracion.manejarClick(mx, my);
        if (accion == PantallaConfiguracionPong.Accion.GUARDAR
                || accion == PantallaConfiguracionPong.Accion.CAMBIO_PANTALLA
                || accion == PantallaConfiguracionPong.Accion.VOLVER) {
            actualizarSonidoMenu();
        }
        if (accion == PantallaConfiguracionPong.Accion.CAMBIO_PANTALLA) {
            aplicarModoPantalla();
        }
        if (accion == PantallaConfiguracionPong.Accion.VOLVER) {
            mostrandoConfiguracion = false;
        }
        canvas.requestFocus();
    }

    private void manejarClickRanking(int mx, int my) {
        if (pantallaRanking.manejarClick(mx, my)) {
            mostrandoRanking = false;
        }
        canvas.requestFocus();
    }

    private void actualizarHoverConfiguracion(int mx, int my) {
        pantallaConfiguracion.actualizarHover(mx, my);
    }

    private void actualizarHoverRanking(int mx, int my) {
        pantallaRanking.actualizarHover(mx, my);
    }

    private void aplicarModoPantalla() {
        if (pantallaConfiguracion.isPantallaCompleta()) {
            // Liberamos el tamaño fijo para que BorderLayout estire el canvas
            canvas.setPreferredSize(null);
            canvas.setMinimumSize(null);
            getContentPane().removeAll();
            getContentPane().add(canvas, BorderLayout.CENTER);
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            revalidate();
            repaint();
        } else {
            // Restauramos el tamaño fijo al volver a modo ventana
            canvas.setPreferredSize(new Dimension(ANCHO, ALTO));
            canvas.setMinimumSize(null);
            getContentPane().removeAll();
            getContentPane().add(canvas, BorderLayout.CENTER);
            setExtendedState(JFrame.NORMAL);
            pack();
            setLocationRelativeTo(null);
        }
    }

    private void lanzarPong(int modoJuego) {
        setVisible(false);
        timer.stop();
        gestorSonidos.detenerMusica();

        Pong pong = new Pong(config.getSkinCancha(), config.getSkinBarras(), config.getSkinPelota());
        pong.setPuntuacionMaxima(config.getPuntuacionMaxima());
        pong.setModoJuego(modoJuego);
        pong.setPistaMusical(config.getPistaMusical());
        pong.setSonidoActivado(config.isSonidoActivado());
        pong.setPantallaCompleta(config.isPantallaCompleta());

        pong.frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                SwingUtilities.invokeLater(() -> {
                    timer.start();
                    actualizarSonidoMenu();
                    setVisible(true);
                });
            }
        });

        pong.run();
    }

    private void renderizar(Graphics2D g) {
        if (mostrandoRanking) {
            renderizarRanking(g);
        } else if (mostrandoConfiguracion) {
            renderizarConfiguracion(g);
        } else {
            renderizarMenu(g);
        }
    }

    private void renderizarMenu(Graphics2D g) {
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
        String pie = "Clasico juego de tenis para 2 jugadores";
        fm = bg.getFontMetrics();
        bg.drawString(pie, (ANCHO - fm.stringWidth(pie)) / 2, ALTO - 20);

        bg.dispose();
        g.drawImage(buffer, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
    }

    private void renderizarRanking(Graphics2D g) {
        Graphics2D bg = buffer.createGraphics();
        pantallaRanking.renderizar(bg);
        bg.dispose();
        g.drawImage(buffer, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
    }

    private void renderizarConfiguracion(Graphics2D g) {
        Graphics2D bg = buffer.createGraphics();
        pantallaConfiguracion.renderizar(bg);
        bg.dispose();
        g.drawImage(buffer, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
    }

    private void actualizarSonidoMenu() {
        gestorSonidos.setSonidoActivado(config.isSonidoActivado());
        gestorSonidos.detenerMusica();
        if (config.isSonidoActivado()) {
            gestorSonidos.reproducirMusica("menu");
        }
    }
}
