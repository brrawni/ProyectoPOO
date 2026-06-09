package spaceinvaders;

import motor.Videojuego;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import launcher.Boton;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import launcher.Launcher;

public class MenuSpaceInvaders extends Videojuego {

    private static final int ANCHO = 800;
    private static final int ALTO  = 600;

    private BufferedImage buffer;
    private Boton[] botones;
    private GestorSonidosSpaceInvaders gestorSonidos;
    private GestorConfiguracionSpaceInvaders config;

    private int[][] estrellas;
    private int siguientePantalla = 0; // 0=salir, 1=jugar
    private Launcher launcher;
    private boolean mostrandoConfiguracion = false;

    private String[] opcionVelocidad = {"LENTA", "MEDIA", "RAPIDA"};
    private String[] opcionSkins = {"original", "alternativa"};
    private String[] opcionMusica = {"original", "alternativa"};

    private int indiceVelocidad = 1;
    private int indiceSkinNave = 0;
    private int indiceSkinInv = 0;
    private int indiceSkinProy = 0;
    private int indiceMusica = 0;

    private boolean sonidoActivado = true;
    private boolean pantallaCompleta = false;

    private int teclaIzquierda;
    private int teclaDerecha;
    private int teclaDisparo;
    private String esperandoTecla = null;

    private Boton btnVelIzq, btnVelDer;
    private Boton btnSonido, btnPantalla, btnMusica;
    private Boton btnTeclaIzq, btnTeclaDer, btnTeclaDisparo;
    private Boton btnSkinNave, btnSkinInv, btnSkinProy;
    private Boton btnGuardar, btnVolver, btnReset;

    public MenuSpaceInvaders(Launcher launcher) {
        super("Space Invaders", ANCHO, ALTO);
        this.launcher = launcher;
    }

    @Override
    public void gameStartup() {
        buffer = new BufferedImage(ANCHO, ALTO, BufferedImage.TYPE_INT_ARGB);

        config = GestorConfiguracionSpaceInvaders.getInstance();
        cargarValoresConfiguracion();
        gestorSonidos = new GestorSonidosSpaceInvaders(config.isSonidoGeneralActivado());
        if (config.isSonidoGeneralActivado()) {
            gestorSonidos.reproducirMusica(archivoPistaSeleccionada());
        }

        configurarModoPantallaInicial();
        inicializarEstrellas();
        inicializarBotonesMenu();
        configurarEventos();

        canvas.setFocusable(true);
        canvas.requestFocus();
    }

    private void configurarModoPantallaInicial() {
        if (config.isPantallaCompleta()) {
            SwingUtilities.invokeLater(() -> {
                frame.dispose();
                frame.setUndecorated(true);
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                frame.setVisible(true);
                canvas.requestFocus();
            });
        } else {
            SwingUtilities.invokeLater(() -> {
                frame.setSize(ANCHO, ALTO);
                frame.setLocationRelativeTo(null);
                canvas.requestFocus();
            });
        }
    }

    private void inicializarEstrellas() {
        estrellas = new int[80][3];
        for (int i = 0; i < estrellas.length; i++) {
            estrellas[i][0] = (int)(Math.random() * ANCHO);
            estrellas[i][1] = (int)(Math.random() * ALTO);
            estrellas[i][2] = Math.random() < 0.3 ? 2 : 1;
        }
    }

    private void inicializarBotonesMenu() {
        int bAncho = 260, bAlto = 50;
        int bX = 400 - bAncho / 2;
        int espaciado = 65, yInicio = 220;

        botones = new Boton[] {
            new Boton(bX, yInicio,               bAncho, bAlto, "JUGAR"),
            new Boton(bX, yInicio + espaciado,   bAncho, bAlto, "CONFIGURACION"),
            new Boton(bX, yInicio + espaciado*2, bAncho, bAlto, "RANKING"),
            new Boton(bX, yInicio + espaciado*3, bAncho, bAlto, "VOLVER"),
        };
    }

    private void configurarEventos() {
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int xLogico = (int)(e.getX() * ((double)ANCHO / canvas.getWidth()));
                int yLogico = (int)(e.getY() * ((double)ALTO / canvas.getHeight()));
                if (mostrandoConfiguracion) {
                    manejarClickConfiguracion(xLogico, yLogico);
                } else {
                    manejarClickMenu(xLogico, yLogico);
                }
            }
        });

        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int xLogico = (int)(e.getX() * ((double)ANCHO / canvas.getWidth()));
                int yLogico = (int)(e.getY() * ((double)ALTO / canvas.getHeight()));
                if (mostrandoConfiguracion) {
                    actualizarHoverConfiguracion(xLogico, yLogico);
                } else {
                    for (Boton b : botones) {
                        b.setHover(b.contienePunto(xLogico, yLogico));
                    }
                }
            }
        });

        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!mostrandoConfiguracion || esperandoTecla == null) return;
                int codigo = e.getKeyCode();
                switch (esperandoTecla) {
                    case "izquierda": teclaIzquierda = codigo; break;
                    case "derecha":   teclaDerecha = codigo; break;
                    case "disparo":   teclaDisparo = codigo; break;
                }
                actualizarTextosConfiguracion();
                esperandoTecla = null;
            }
        });
    }

    private void manejarClickMenu(int mx, int my) {
        for (int i = 0; i < botones.length; i++) {
            if (botones[i].contienePunto(mx, my)) {
                switch (i) {
                    case 0:
                        siguientePantalla = 1;
                        stop();
                        break;
                    case 1:
                        mostrarConfiguracion();
                        break;
                    case 2:
                        SwingUtilities.invokeLater(() -> new VentanaRankingSpaceInvaders(frame).setVisible(true));
                        break;
                    case 3:
                        siguientePantalla = 0;
                        stop();
                        break;
                }
                return;
            }
        }
    }

    private void mostrarConfiguracion() {
        mostrandoConfiguracion = true;
        cargarValoresConfiguracion();
        inicializarBotonesConfiguracion();
        canvas.requestFocus();
    }

    private void cargarValoresConfiguracion() {
        sonidoActivado   = config.isSonidoGeneralActivado();
        pantallaCompleta = config.isPantallaCompleta();
        teclaIzquierda   = config.getTeclaIzquierda();
        teclaDerecha     = config.getTeclaDerecha();
        teclaDisparo     = config.getTeclaDisparo();

        indiceVelocidad = ("LENTA".equals(config.getVelocidad())) ? 0 : ("RAPIDA".equals(config.getVelocidad())) ? 2 : 1;
        indiceMusica    = ("alternativa".equals(config.getPistaMusical())) ? 1 : 0;
        indiceSkinNave  = ("alternativa".equals(config.getSkinNave())) ? 1 : 0;
        indiceSkinInv   = ("alternativa".equals(config.getSkinInvasores())) ? 1 : 0;
        indiceSkinProy  = ("alternativa".equals(config.getSkinProyectil())) ? 1 : 0;
    }

    private void inicializarBotonesConfiguracion() {
        btnVelIzq = new Boton(100, 150, 50, 40, "<");
        btnVelDer = new Boton(250, 150, 50, 40, ">");
        btnGuardar = new Boton(250, 520, 100, 40, "GUARDAR");
        btnReset = new Boton(350, 520, 100, 40, "RESET");
        btnVolver = new Boton(450, 520, 100, 40, "VOLVER");
        actualizarTextosConfiguracion();
    }

    private void actualizarTextosConfiguracion() {
        btnSonido = new Boton(100, 220, 200, 40, "Sonido: " + (sonidoActivado ? "ON" : "OFF"));
        btnPantalla = new Boton(100, 290, 200, 40, "Pantalla: " + (pantallaCompleta ? "FULL" : "VENTANA"));
        btnMusica = new Boton(100, 360, 200, 40, "Musica: " + opcionMusica[indiceMusica]);

        btnTeclaIzq = new Boton(450, 150, 250, 40, "Izquierda: " + KeyEvent.getKeyText(teclaIzquierda));
        btnTeclaDer = new Boton(450, 200, 250, 40, "Derecha: " + KeyEvent.getKeyText(teclaDerecha));
        btnTeclaDisparo = new Boton(450, 250, 250, 40, "Disparo: " + KeyEvent.getKeyText(teclaDisparo));

        btnSkinNave = new Boton(450, 320, 250, 40, "Skin Nave: " + opcionSkins[indiceSkinNave]);
        btnSkinInv = new Boton(450, 370, 250, 40, "Skin Alien: " + opcionSkins[indiceSkinInv]);
        btnSkinProy = new Boton(450, 420, 250, 40, "Skin Laser: " + opcionSkins[indiceSkinProy]);
    }

    private void manejarClickConfiguracion(int x, int y) {
        boolean previaPantallaCompleta = pantallaCompleta;

        if (btnVelIzq.contienePunto(x, y)) indiceVelocidad = Math.max(0, indiceVelocidad - 1);
        if (btnVelDer.contienePunto(x, y)) indiceVelocidad = Math.min(2, indiceVelocidad + 1);

        if (btnSonido.contienePunto(x, y)) {
            sonidoActivado = !sonidoActivado;
            gestorSonidos.setSonidoActivado(sonidoActivado);
            if (sonidoActivado) gestorSonidos.reproducirMusica(archivoPistaSeleccionada());
            else gestorSonidos.detenerMusica();
        }
        if (btnPantalla.contienePunto(x, y)) pantallaCompleta = !pantallaCompleta;
        if (btnMusica.contienePunto(x, y)) {
            indiceMusica = (indiceMusica + 1) % 2;
            if (sonidoActivado) gestorSonidos.reproducirMusica(archivoPistaSeleccionada());
        }

        if (btnTeclaIzq.contienePunto(x, y)) { esperandoTecla = "izquierda"; canvas.requestFocus(); }
        if (btnTeclaDer.contienePunto(x, y)) { esperandoTecla = "derecha"; canvas.requestFocus(); }
        if (btnTeclaDisparo.contienePunto(x, y)) { esperandoTecla = "disparo"; canvas.requestFocus(); }

        if (btnSkinNave.contienePunto(x, y)) indiceSkinNave = (indiceSkinNave + 1) % 2;
        if (btnSkinInv.contienePunto(x, y)) indiceSkinInv = (indiceSkinInv + 1) % 2;
        if (btnSkinProy.contienePunto(x, y)) indiceSkinProy = (indiceSkinProy + 1) % 2;

        if (btnGuardar.contienePunto(x, y)) {
            aplicarConfiguracionActual();
        }
        if (btnReset.contienePunto(x, y)) {
            config.restablecer();
            cargarValoresConfiguracion();
        }
        if (btnVolver.contienePunto(x, y)) {
            aplicarConfiguracionActual();
            mostrandoConfiguracion = false;
            esperandoTecla = null;
        }

        actualizarTextosConfiguracion();
        if (pantallaCompleta != previaPantallaCompleta) {
            aplicarModoPantalla();
        }
        canvas.requestFocus();
    }

    private void actualizarHoverConfiguracion(int x, int y) {
        Boton[] botonesConfiguracion = {
            btnVelIzq, btnVelDer, btnSonido, btnPantalla, btnMusica,
            btnTeclaIzq, btnTeclaDer, btnTeclaDisparo,
            btnSkinNave, btnSkinInv, btnSkinProy,
            btnGuardar, btnReset, btnVolver
        };
        for (Boton boton : botonesConfiguracion) {
            if (boton != null) boton.setHover(boton.contienePunto(x, y));
        }
    }

    private void aplicarConfiguracionActual() {
        config.setSonidoGeneral(sonidoActivado);
        config.setPantallaCompleta(pantallaCompleta);
        config.setVelocidad(opcionVelocidad[indiceVelocidad]);
        config.setTeclaIzquierda(teclaIzquierda);
        config.setTeclaDerecha(teclaDerecha);
        config.setTeclaDisparo(teclaDisparo);
        config.setPistaMusical(opcionMusica[indiceMusica]);
        config.setSkinNave(opcionSkins[indiceSkinNave]);
        config.setSkinInvasores(opcionSkins[indiceSkinInv]);
        config.setSkinProyectil(opcionSkins[indiceSkinProy]);
        config.guardar();

        gestorSonidos.setSonidoActivado(sonidoActivado);
        if (sonidoActivado) gestorSonidos.reproducirMusica(archivoPistaSeleccionada());
        else gestorSonidos.detenerMusica();
    }

    private void aplicarModoPantalla() {
        frame.dispose();
        if (pantallaCompleta) {
            frame.setUndecorated(true);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            frame.setUndecorated(false);
            frame.setExtendedState(JFrame.NORMAL);
            frame.setSize(ANCHO, ALTO);
            frame.setLocationRelativeTo(null);
        }
        frame.setVisible(true);
    }

    private String archivoPistaSeleccionada() {
        return "alternativa".equals(opcionMusica[indiceMusica]) ? "musicaMenu_alternativa.wav" : "musicaMenu.wav";
    }

    @Override
    public void gameUpdate(double delta) { }

    @Override
    public void gameDraw(Graphics2D g) {
        if (buffer == null) return;
        if (mostrandoConfiguracion) {
            dibujarConfiguracion(g);
        } else {
            dibujarMenu(g);
        }
    }

    private void dibujarMenu(Graphics2D g) {
        Graphics2D g2d = buffer.createGraphics();

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, ANCHO, ALTO);

        g2d.setFont(new Font("Arial", Font.BOLD, 48));
        g2d.setColor(Color.WHITE);
        String titulo = "SPACE INVADERS";
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(titulo, 400 - fm.stringWidth(titulo)/2, 150);

        for (Boton b : botones) b.dibujar(g2d);

        for (int[] e : estrellas) {
            int brillo = 100 + (int)(Math.random() * 155);
            g2d.setColor(new Color(brillo, brillo, brillo));
            g2d.fillRect(e[0], e[1], e[2], e[2]);
        }

        g2d.dispose();
        g.drawImage(buffer, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
    }

    private void dibujarConfiguracion(Graphics2D g) {
        Graphics2D g2d = buffer.createGraphics();

        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(0, 0, ANCHO, ALTO);

        g2d.setFont(new Font("Arial", Font.BOLD, 36));
        g2d.setColor(Color.WHITE);
        String titulo = "CONFIGURACION";
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(titulo, 400 - fm.stringWidth(titulo)/2, 60);

        g2d.setFont(new Font("Arial", Font.PLAIN, 20));
        g2d.drawString("Velocidad:", 100, 130);
        g2d.drawString("Controles:", 450, 130);
        g2d.drawString("Apariencia:", 450, 310);

        btnVelIzq.dibujar(g2d);
        btnVelDer.dibujar(g2d);
        g2d.drawString(opcionVelocidad[indiceVelocidad], 175 - fm.stringWidth(opcionVelocidad[indiceVelocidad])/2, 178);
        btnSonido.dibujar(g2d);
        btnPantalla.dibujar(g2d);
        btnMusica.dibujar(g2d);

        btnTeclaIzq.dibujar(g2d);
        btnTeclaDer.dibujar(g2d);
        btnTeclaDisparo.dibujar(g2d);
        btnSkinNave.dibujar(g2d);
        btnSkinInv.dibujar(g2d);
        btnSkinProy.dibujar(g2d);

        if (esperandoTecla != null) {
            g2d.setColor(Color.YELLOW);
            g2d.drawString("Presiona una tecla para: " + esperandoTecla, 450, 100);
        }

        btnGuardar.dibujar(g2d);
        btnReset.dibujar(g2d);
        btnVolver.dibujar(g2d);

        g2d.dispose();
        g.drawImage(buffer, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
    }

    @Override
    public void gameShutdown() {
        if (gestorSonidos != null) {
            gestorSonidos.limpiar();
        }
        switch (siguientePantalla) {
            case 0:
                SwingUtilities.invokeLater(() -> launcher.setVisible(true));
                break;
            case 1:
                new SpaceInvaders(launcher).run();
                break;
        }
    }
}
