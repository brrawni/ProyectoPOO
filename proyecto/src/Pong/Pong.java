package Pong;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import motor.GestorRankingBase;
import motor.Videojuego;

/**
 * Coordinador principal del videojuego Pong.
 * Administra la ventana, el canvas, los estados y delega cada pantalla.
 */
public class Pong extends Videojuego {
    private enum EstadoPong {
        MENU,
        CONFIGURACION,
        RANKING,
        JUGANDO
    }

    private static final int ANCHO_LOGICO = 800;
    private static final int ALTO_LOGICO = 600;
    private static final String RANKING_PONG = "ranking_pong.txt";

    private final JFrame launcher;
    private final ConfiguracionPong config;
    private final GestorRankingBase gestorRanking;
    private final PantallaMenuPong pantallaMenu;
    private final PantallaConfiguracionPong pantallaConfiguracion;
    private final PantallaRankingPong pantallaRanking;

    private GestorSonidosPong gestorSonidos;
    private BufferedImage buffer;
    private EstadoPong estadoActual = EstadoPong.MENU;
    private PartidaPong partida;

    public Pong(JFrame launcher) {
        super("Pong - ClassicGame Edition", ANCHO_LOGICO, ALTO_LOGICO);
        this.launcher = launcher;
        this.config = new ConfiguracionPong();
        this.config.cargar();
        this.gestorRanking = new GestorRankingBase(RANKING_PONG);
        this.pantallaMenu = new PantallaMenuPong(ANCHO_LOGICO, ALTO_LOGICO);
        this.pantallaConfiguracion = new PantallaConfiguracionPong(ANCHO_LOGICO, ALTO_LOGICO, config);
        this.pantallaRanking = new PantallaRankingPong(ANCHO_LOGICO, ALTO_LOGICO, gestorRanking);
        this.gestorSonidos = new GestorSonidosPong(config.isSonidoActivado());
    }

    @Override
    public void gameStartup() {
        buffer = new BufferedImage(ANCHO_LOGICO, ALTO_LOGICO, BufferedImage.TYPE_INT_RGB);
        configurarListeners();
        aplicarModoPantalla(config.isPantallaCompleta());
        actualizarSonidoMenu();
        enEjecucion = true;
        canvas.setFocusable(true);
        canvas.requestFocus();
    }

    @Override
    public void gameUpdate(double delta) {
        if (estadoActual == EstadoPong.JUGANDO && partida != null) {
            partida.actualizar(delta);
            if (partida.debeVolverAlMenu()) {
                volverAlMenuDesdePartida();
            }
        }
    }

    @Override
    public void gameDraw(Graphics2D g) {
        if (buffer == null) return;

        Graphics2D bg = buffer.createGraphics();
        bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        switch (estadoActual) {
            case MENU:
                pantallaMenu.renderizar(bg);
                break;
            case CONFIGURACION:
                pantallaConfiguracion.renderizar(bg);
                break;
            case RANKING:
                pantallaRanking.renderizar(bg);
                break;
            case JUGANDO:
                if (partida != null) {
                    partida.renderizar(bg);
                }
                break;
        }

        bg.dispose();
        escalarBuffer(g);
    }

    @Override
    public void gameShutdown() {
        limpiarPartidaActual();
        if (gestorSonidos != null) gestorSonidos.limpiar();
        if (buffer != null) buffer.flush();
        if (launcher != null) {
            SwingUtilities.invokeLater(() -> launcher.setVisible(true));
        }
    }

    private void configurarListeners() {
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mx = escalarX(e.getX());
                int my = escalarY(e.getY());
                delegarClick(mx, my);
            }
        });

        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int mx = escalarX(e.getX());
                int my = escalarY(e.getY());
                actualizarHover(mx, my);
            }
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cerrarPong();
            }
        });
    }

    private void delegarClick(int mx, int my) {
        switch (estadoActual) {
            case MENU:
                procesarAccionMenu(pantallaMenu.manejarClick(mx, my));
                break;
            case CONFIGURACION:
                procesarAccionConfiguracion(pantallaConfiguracion.manejarClick(mx, my));
                break;
            case RANKING:
                procesarAccionRanking(pantallaRanking.manejarClick(mx, my));
                break;
            case JUGANDO:
                break;
        }
        canvas.requestFocus();
    }

    private void procesarAccionMenu(PantallaMenuPong.Accion accion) {
        switch (accion) {
            case HUMANO_VS_HUMANO:
                iniciarPartida(2);
                break;
            case HUMANO_VS_CPU:
                iniciarPartida(1);
                break;
            case CONFIGURACION:
                mostrarConfiguracion();
                break;
            case RANKING:
                mostrarRanking();
                break;
            case VOLVER:
                cerrarPong();
                break;
            default:
                break;
        }
    }

    private void procesarAccionConfiguracion(PantallaConfiguracionPong.Accion accion) {
        if (accion == PantallaConfiguracionPong.Accion.GUARDAR
                || accion == PantallaConfiguracionPong.Accion.CAMBIO_PANTALLA
                || accion == PantallaConfiguracionPong.Accion.VOLVER) {
            actualizarSonidoMenu();
        }
        if (accion == PantallaConfiguracionPong.Accion.CAMBIO_PANTALLA) {
            aplicarModoPantalla(config.isPantallaCompleta());
        }
        if (accion == PantallaConfiguracionPong.Accion.VOLVER) {
            estadoActual = EstadoPong.MENU;
        }
    }

    private void procesarAccionRanking(boolean volverAlMenu) {
        if (volverAlMenu) {
            estadoActual = EstadoPong.MENU;
            actualizarSonidoMenu();
        }
    }

    private void actualizarHover(int mx, int my) {
        switch (estadoActual) {
            case MENU:
                pantallaMenu.actualizarHover(mx, my);
                break;
            case CONFIGURACION:
                pantallaConfiguracion.actualizarHover(mx, my);
                break;
            case RANKING:
                pantallaRanking.actualizarHover(mx, my);
                break;
            case JUGANDO:
                break;
        }
    }

    private void mostrarConfiguracion() {
        estadoActual = EstadoPong.CONFIGURACION;
        pantallaConfiguracion.iniciar();
    }

    private void mostrarRanking() {
        estadoActual = EstadoPong.RANKING;
        pantallaRanking.iniciar();
    }

    private void iniciarPartida(int modoJuego) {
        limpiarPartidaActual();
        gestorSonidos.limpiar();
        gestorSonidos.setSonidoActivado(config.isSonidoActivado());
        gestorSonidos.cargarTodosSonidos();
        gestorSonidos.reproducirMusica(config.getPistaMusical());

        TemasPong tema = new TemasPong(config.getSkinCancha(), config.getSkinBarras(), config.getSkinPelota());
        partida = new PartidaPong(
                modoJuego,
                config.getPuntuacionMaxima(),
                tema,
                gestorSonidos,
                gestorRanking,
                pantallaRanking);
        partida.iniciar(canvas);
        estadoActual = EstadoPong.JUGANDO;
    }

    private void volverAlMenuDesdePartida() {
        limpiarPartidaActual();
        estadoActual = EstadoPong.MENU;
        actualizarSonidoMenu();
    }

    private void limpiarPartidaActual() {
        if (partida != null) {
            partida.limpiar();
            partida = null;
        }
    }

    private void cerrarPong() {
        enEjecucion = false;
        stop();
    }

    private void actualizarSonidoMenu() {
        gestorSonidos.setSonidoActivado(config.isSonidoActivado());
        gestorSonidos.detenerMusica();
        if (config.isSonidoActivado() && estadoActual != EstadoPong.JUGANDO) {
            gestorSonidos.reproducirMusica("menu");
        }
    }

    private void aplicarModoPantalla(boolean pantallaCompleta) {
        if (pantallaCompleta) {
            canvas.setPreferredSize(null);
            canvas.setMinimumSize(null);
            frame.getContentPane().removeAll();
            frame.getContentPane().add(canvas, BorderLayout.CENTER);
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
            frame.revalidate();
            frame.repaint();
        } else {
            canvas.setPreferredSize(new Dimension(ANCHO_LOGICO, ALTO_LOGICO));
            canvas.setMinimumSize(null);
            frame.getContentPane().removeAll();
            frame.getContentPane().add(canvas, BorderLayout.CENTER);
            frame.setExtendedState(Frame.NORMAL);
            frame.pack();
            frame.setLocationRelativeTo(null);
        }
    }

    private int escalarX(int x) {
        int w = canvas.getWidth();
        return (w > 0) ? x * ANCHO_LOGICO / w : x;
    }

    private int escalarY(int y) {
        int h = canvas.getHeight();
        return (h > 0) ? y * ALTO_LOGICO / h : y;
    }

    private void escalarBuffer(Graphics2D g) {
        int w = canvas.getWidth();
        int h = canvas.getHeight();
        if (w <= 0 || h <= 0) {
            g.drawImage(buffer, 0, 0, null);
        } else {
            g.drawImage(buffer, 0, 0, w, h, null);
        }
    }
}
