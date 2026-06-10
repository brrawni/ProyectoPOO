package Pong;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.*;
import launcher.Boton;
import motor.EntradaRanking;
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
    private JFrame launcher;
    private boolean mostrandoConfiguracion = false;
    private boolean mostrandoRanking = false;

    private final String[] skinsBarras = {"original", "moderno", "delgado"};
    private final String[] skinsCancha = {"original", "moderno", "oscuro"};
    private final String[] skinsPelota = {"original", "cuadrada", "triangulo"};
    private final String[] pistas = {"original", "piano", "lofi"};
    private final int[] puntuaciones = {11, 15};

    private int indiceSkinBarras;
    private int indiceSkinCancha;
    private int indiceSkinPelota;
    private int indicePista;
    private int indicePuntuacion;
    private boolean sonidoActivado;
    private boolean pantallaCompleta;

    private Boton btnSonido;
    private Boton btnPantalla;
    private Boton btnSkinBarras;
    private Boton btnSkinCancha;
    private Boton btnSkinPelota;
    private Boton btnPista;
    private Boton btnPuntuacion;
    private Boton btnGuardar;
    private Boton btnReset;
    private Boton btnVolver;
    private Boton btnVolverRanking;
    private Boton btnLimpiarRanking;

    public MenuPong(JFrame launcher) {
        super("Pong - Menu Principal");
        this.launcher      = launcher;
        this.config        = new ConfiguracionPong();
        this.gestorRanking = new GestorRankingBase(RANKING_PONG);

        config.cargar();
        this.gestorSonidos = new GestorSonidosPong(config.isSonidoActivado());
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
                if (mostrandoRanking) {
                    manejarClickRanking(e.getX(), e.getY());
                } else if (mostrandoConfiguracion) {
                    manejarClickConfiguracion(e.getX(), e.getY());
                } else {
                    manejarClickMenu(e.getX(), e.getY());
                }
            }
        });
        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (mostrandoRanking) {
                    actualizarHoverRanking(e.getX(), e.getY());
                } else if (mostrandoConfiguracion) {
                    actualizarHoverConfiguracion(e.getX(), e.getY());
                } else {
                    for (Boton b : botones) b.setHover(b.contienePunto(e.getX(), e.getY()));
                }
            }
        });
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
        gestorRanking.cargar();
        btnLimpiarRanking = new Boton(230, 500, 150, 45, "LIMPIAR");
        btnVolverRanking = new Boton(420, 500, 150, 45, "VOLVER");
        canvas.requestFocus();
    }

    private void mostrarConfiguracion() {
        mostrandoConfiguracion = true;
        cargarValoresConfiguracion();
        crearBotonesConfiguracion();
        canvas.requestFocus();
    }

    private void cargarValoresConfiguracion() {
        sonidoActivado = config.isSonidoActivado();
        pantallaCompleta = config.isPantallaCompleta();
        indiceSkinBarras = indiceDe(skinsBarras, config.getSkinBarras());
        indiceSkinCancha = indiceDe(skinsCancha, config.getSkinCancha());
        indiceSkinPelota = indiceDe(skinsPelota, config.getSkinPelota());
        indicePista = indiceDe(pistas, config.getPistaMusical());
        indicePuntuacion = config.getPuntuacionMaxima() == 15 ? 1 : 0;
    }

    private int indiceDe(String[] opciones, String valor) {
        for (int i = 0; i < opciones.length; i++) {
            if (opciones[i].equals(valor)) return i;
        }
        return 0;
    }

    private void crearBotonesConfiguracion() {
        btnSonido = new Boton(100, 150, 240, 45, "");
        btnPantalla = new Boton(100, 215, 240, 45, "");
        btnPista = new Boton(100, 280, 240, 45, "");
        btnPuntuacion = new Boton(100, 345, 240, 45, "");

        btnSkinCancha = new Boton(460, 150, 240, 45, "");
        btnSkinBarras = new Boton(460, 215, 240, 45, "");
        btnSkinPelota = new Boton(460, 280, 240, 45, "");

        btnGuardar = new Boton(190, 500, 130, 45, "GUARDAR");
        btnReset = new Boton(335, 500, 130, 45, "RESET");
        btnVolver = new Boton(480, 500, 130, 45, "VOLVER");
        actualizarTextosConfiguracion();
    }

    private void actualizarTextosConfiguracion() {
        btnSonido.setTexto("Sonido: " + (sonidoActivado ? "ON" : "OFF"));
        btnPantalla.setTexto("Pantalla: " + (pantallaCompleta ? "FULL" : "VENTANA"));
        btnPista.setTexto("Musica: " + pistas[indicePista]);
        btnPuntuacion.setTexto("Puntos: " + puntuaciones[indicePuntuacion]);
        btnSkinCancha.setTexto("Cancha: " + skinsCancha[indiceSkinCancha]);
        btnSkinBarras.setTexto("Barras: " + skinsBarras[indiceSkinBarras]);
        btnSkinPelota.setTexto("Pelota: " + skinsPelota[indiceSkinPelota]);
    }

    private void manejarClickConfiguracion(int mx, int my) {
        boolean pantallaAnterior = pantallaCompleta;

        if (btnSonido.contienePunto(mx, my)) sonidoActivado = !sonidoActivado;
        if (btnPantalla.contienePunto(mx, my)) pantallaCompleta = !pantallaCompleta;
        if (btnPista.contienePunto(mx, my)) indicePista = (indicePista + 1) % pistas.length;
        if (btnPuntuacion.contienePunto(mx, my)) indicePuntuacion = (indicePuntuacion + 1) % puntuaciones.length;
        if (btnSkinCancha.contienePunto(mx, my)) indiceSkinCancha = (indiceSkinCancha + 1) % skinsCancha.length;
        if (btnSkinBarras.contienePunto(mx, my)) indiceSkinBarras = (indiceSkinBarras + 1) % skinsBarras.length;
        if (btnSkinPelota.contienePunto(mx, my)) indiceSkinPelota = (indiceSkinPelota + 1) % skinsPelota.length;

        if (btnGuardar.contienePunto(mx, my)) {
            guardarConfiguracion();
        }
        if (btnReset.contienePunto(mx, my)) {
            config.restablecer();
            cargarValoresConfiguracion();
        }
        if (btnVolver.contienePunto(mx, my)) {
            guardarConfiguracion();
            mostrandoConfiguracion = false;
        }

        actualizarTextosConfiguracion();
        if (pantallaCompleta != pantallaAnterior) {
            aplicarModoPantalla();
        }
        canvas.requestFocus();
    }

    private void manejarClickRanking(int mx, int my) {
        if (btnLimpiarRanking.contienePunto(mx, my)) {
            gestorRanking.limpiar();
        }
        if (btnVolverRanking.contienePunto(mx, my)) {
            mostrandoRanking = false;
        }
        canvas.requestFocus();
    }

    private void actualizarHoverConfiguracion(int mx, int my) {
        Boton[] botonesConfig = {
            btnSonido, btnPantalla, btnPista, btnPuntuacion,
            btnSkinCancha, btnSkinBarras, btnSkinPelota,
            btnGuardar, btnReset, btnVolver
        };
        for (Boton boton : botonesConfig) {
            boton.setHover(boton.contienePunto(mx, my));
        }
    }

    private void actualizarHoverRanking(int mx, int my) {
        btnLimpiarRanking.setHover(btnLimpiarRanking.contienePunto(mx, my));
        btnVolverRanking.setHover(btnVolverRanking.contienePunto(mx, my));
    }

    private void guardarConfiguracion() {
        config.setSonidoActivado(sonidoActivado);
        config.setPantallaCompleta(pantallaCompleta);
        config.setSkinBarras(skinsBarras[indiceSkinBarras]);
        config.setSkinCancha(skinsCancha[indiceSkinCancha]);
        config.setSkinPelota(skinsPelota[indiceSkinPelota]);
        config.setPistaMusical(pistas[indicePista]);
        config.setPuntuacionMaxima(puntuaciones[indicePuntuacion]);
        config.guardar();
        actualizarSonidoMenu();
    }

    private void aplicarModoPantalla() {
        if (pantallaCompleta) {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            setExtendedState(JFrame.NORMAL);
            setSize(ANCHO, ALTO);
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
        g.drawImage(buffer, 0, 0, null);
    }

    private void renderizarRanking(Graphics2D g) {
        Graphics2D bg = buffer.createGraphics();
        bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        bg.setColor(new Color(15, 25, 35));
        bg.fillRect(0, 0, ANCHO, ALTO);

        bg.setColor(new Color(255, 220, 80));
        bg.setFont(new Font("Arial", Font.BOLD, 42));
        String titulo = "MEJORES PUNTAJES";
        FontMetrics fm = bg.getFontMetrics();
        bg.drawString(titulo, (ANCHO - fm.stringWidth(titulo)) / 2, 70);

        dibujarTablaRanking(bg, 130);

        btnLimpiarRanking.dibujar(bg);
        btnVolverRanking.dibujar(bg);

        bg.dispose();
        g.drawImage(buffer, 0, 0, null);
    }

    private void dibujarTablaRanking(Graphics2D bg, int yInicial) {
        List<EntradaRanking> top10 = gestorRanking.obtenerTop10();
        bg.setFont(new Font("Monospaced", Font.BOLD, 22));

        if (top10.isEmpty()) {
            bg.setColor(Color.WHITE);
            bg.drawString("NO HAY PUNTAJES AUN.", 250, 270);
            bg.drawString("SE EL PRIMERO!", 300, 310);
            return;
        }

        bg.setColor(new Color(170, 210, 255));
        bg.drawString("POS  NOMBRE      PUNTOS  FECHA", 150, yInicial);

        bg.setColor(Color.WHITE);
        int y = yInicial + 40;
        for (int i = 0; i < top10.size(); i++) {
            EntradaRanking entrada = top10.get(i);
            String nombre = entrada.getNombre();
            if (nombre.length() > 10) nombre = nombre.substring(0, 10);

            String linea = String.format("%2d.  %-10s  %5d   %s",
                    i + 1,
                    nombre,
                    entrada.getPuntaje(),
                    entrada.getFecha());
            bg.drawString(linea, 150, y);
            y += 32;
        }
    }

    private void renderizarConfiguracion(Graphics2D g) {
        Graphics2D bg = buffer.createGraphics();
        bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        bg.setColor(new Color(25, 25, 35));
        bg.fillRect(0, 0, ANCHO, ALTO);

        bg.setColor(Color.WHITE);
        bg.setFont(new Font("Arial", Font.BOLD, 38));
        String titulo = "CONFIGURACION";
        FontMetrics fm = bg.getFontMetrics();
        bg.drawString(titulo, (ANCHO - fm.stringWidth(titulo)) / 2, 70);

        bg.setFont(new Font("Arial", Font.PLAIN, 20));
        bg.drawString("General", 100, 125);
        bg.drawString("Apariencia", 460, 125);

        btnSonido.dibujar(bg);
        btnPantalla.dibujar(bg);
        btnPista.dibujar(bg);
        btnPuntuacion.dibujar(bg);
        btnSkinCancha.dibujar(bg);
        btnSkinBarras.dibujar(bg);
        btnSkinPelota.dibujar(bg);
        btnGuardar.dibujar(bg);
        btnReset.dibujar(bg);
        btnVolver.dibujar(bg);

        bg.dispose();
        g.drawImage(buffer, 0, 0, null);
    }

    private void actualizarSonidoMenu() {
        gestorSonidos.setSonidoActivado(config.isSonidoActivado());
        gestorSonidos.detenerMusica();
        if (config.isSonidoActivado()) {
            gestorSonidos.reproducirMusica("menu");
        }
    }
}