package spaceinvaders;

import motor.Videojuego;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import launcher.Boton;
import javax.swing.JFrame;
import launcher.Launcher;
import motor.GestorConfiguracionBase;

public class PantallaConfiguracion extends Videojuego {
    private static final int ANCHO = 800;
    private static final int ALTO  = 600;

    private BufferedImage buffer;
    private GestorConfiguracionSpaceInvaders config;
    private GestorSonidosSpaceInvaders gestorSonidos;
    private Launcher launcher;

    // Arrays de opciones
    private String[] opcionVelocidad = {"LENTA", "MEDIA", "RAPIDA"};
    private String[] opcionSkins = {"original", "alternativa"};
    private String[] opcionMusica = {"original", "remix"};

    // Índices y estados
    private int indiceVelocidad = 1;
    private int indiceSkinNave = 0;
    private int indiceSkinInv = 0;
    private int indiceSkinProy = 0;
    private int indiceMusica = 0;
    
    private boolean sonidoActivado = true;
    private boolean pantallaCompleta = false;

    // Teclas
    private int teclaIzquierda;
    private int teclaDerecha;
    private int teclaDisparo;
    private String esperandoTecla = null;

    // Botones Columna Izquierda (Generales)
    private Boton btnVelIzq, btnVelDer;
    private Boton btnSonido;
    private Boton btnPantalla;
    private Boton btnMusica;

    // Botones Columna Derecha (Controles y Skins)
    private Boton btnTeclaIzq, btnTeclaDer, btnTeclaDisparo;
    private Boton btnSkinNave, btnSkinInv, btnSkinProy;

    // Botones inferiores
    private Boton btnGuardar, btnVolver, btnReset;

    public PantallaConfiguracion(Launcher launcher) {
        super("Configuracion Space Invaders", ANCHO, ALTO);
        this.launcher = launcher;
    }

    @Override
    public void gameStartup() {
        buffer = new BufferedImage(ANCHO, ALTO, BufferedImage.TYPE_INT_ARGB);
        config = GestorConfiguracionSpaceInvaders.getInstance();
        gestorSonidos = new GestorSonidosSpaceInvaders(config.isSonidoActivado());
        gestorSonidos.reproducirMusicaMenu();

        cargarValoresDesdeGestor();
        inicializarBotones();

        // Mouse click
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int xLogico = (int)(e.getX() * ((double)ANCHO / canvas.getWidth()));
                int yLogico = (int)(e.getY() * ((double)ALTO / canvas.getHeight()));
                manejarClick(xLogico, yLogico);
            }
        });

        // Captura de tecla para reasignar controles
        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (esperandoTecla == null) return;
                int codigo = e.getKeyCode();
                switch (esperandoTecla) {
                    case "izquierda": teclaIzquierda = codigo; break;
                    case "derecha":   teclaDerecha = codigo; break;
                    case "disparo":   teclaDisparo = codigo; break;
                }
                actualizarTextosBotones();
                esperandoTecla = null;
            }
        });

        canvas.setFocusable(true);
        canvas.requestFocus();
    }

    private void cargarValoresDesdeGestor() {
        sonidoActivado   = config.isSonidoActivado();
        pantallaCompleta = config.isPantallaCompleta();
        teclaIzquierda   = config.getTeclaIzquierda();
        teclaDerecha     = config.getTeclaDerecha();
        teclaDisparo     = config.getTeclaDisparo();

        indiceVelocidad = ("LENTA".equals(config.getVelocidad())) ? 0 : ("RAPIDA".equals(config.getVelocidad())) ? 2 : 1;
        indiceMusica    = ("remix".equals(config.getPistaMusical())) ? 1 : 0;
        indiceSkinNave  = ("alternativa".equals(config.getSkinNave())) ? 1 : 0;
        indiceSkinInv   = ("alternativa".equals(config.getSkinInvasores())) ? 1 : 0;
        indiceSkinProy  = ("alternativa".equals(config.getSkinProyectil())) ? 1 : 0;
    }

    private void inicializarBotones() {
        //columna izquierda (X = 100)
        btnVelIzq   = new Boton(100, 150, 50, 40, "<");
        btnVelDer   = new Boton(250, 150, 50, 40, ">");
        btnSonido   = new Boton(100, 220, 200, 40, "");
        btnPantalla = new Boton(100, 290, 200, 40, "");
        btnMusica   = new Boton(100, 360, 200, 40, "");

        // COLUMNA DERECHA (X = 450)
        btnTeclaIzq     = new Boton(450, 150, 250, 40, "");
        btnTeclaDer     = new Boton(450, 200, 250, 40, "");
        btnTeclaDisparo = new Boton(450, 250, 250, 40, "");
        btnSkinNave     = new Boton(450, 320, 250, 40, "");
        btnSkinInv      = new Boton(450, 370, 250, 40, "");
        btnSkinProy     = new Boton(450, 420, 250, 40, "");

        // INFERIORES
        btnGuardar = new Boton(250, 520, 100, 40, "GUARDAR");
        btnReset   = new Boton(350, 520, 100, 40, "RESET");
        btnVolver  = new Boton(450, 520, 100, 40, "VOLVER");

        actualizarTextosBotones();
    }

    private void actualizarTextosBotones() {
        btnSonido = new Boton(100, 220, 200, 40, "Sonido: " + (sonidoActivado ? "ON" : "OFF"));
        btnPantalla = new Boton(100, 290, 200, 40, "Pantalla: " + (pantallaCompleta ? "FULL" : "VENTANA"));
        btnMusica = new Boton(100, 360, 200, 40, "Música: " + opcionMusica[indiceMusica]);

        btnTeclaIzq = new Boton(450, 150, 250, 40, "Izquierda: " + KeyEvent.getKeyText(teclaIzquierda));
        btnTeclaDer = new Boton(450, 200, 250, 40, "Derecha: " + KeyEvent.getKeyText(teclaDerecha));
        btnTeclaDisparo = new Boton(450, 250, 250, 40, "Disparo: " + KeyEvent.getKeyText(teclaDisparo));

        btnSkinNave = new Boton(450, 320, 250, 40, "Skin Nave: " + opcionSkins[indiceSkinNave]);
        btnSkinInv = new Boton(450, 370, 250, 40, "Skin Alien: " + opcionSkins[indiceSkinInv]);
        btnSkinProy = new Boton(450, 420, 250, 40, "Skin Laser: " + opcionSkins[indiceSkinProy]);
    }

    private void manejarClick(int x, int y) {
        // Guardar estado previo para evitar recrear la ventana sin necesidad
        boolean previaPantallaCompleta = pantallaCompleta;

        // Columna Izquierda
        if (btnVelIzq.contienePunto(x, y)) indiceVelocidad = Math.max(0, indiceVelocidad - 1);
        if (btnVelDer.contienePunto(x, y)) indiceVelocidad = Math.min(2, indiceVelocidad + 1);

        if (btnSonido.contienePunto(x, y)) {
            sonidoActivado = !sonidoActivado;
            if (gestorSonidos != null) {
                gestorSonidos.setSonidoActivado(sonidoActivado);
                if (sonidoActivado) gestorSonidos.reproducirMusicaMenu();
            }
        }
        if (btnPantalla.contienePunto(x, y)) pantallaCompleta = !pantallaCompleta;
        if (btnMusica.contienePunto(x, y)) indiceMusica = (indiceMusica + 1) % 2;

        // Columna Derecha (Teclas)
        if (btnTeclaIzq.contienePunto(x, y)) { esperandoTecla = "izquierda"; canvas.requestFocus(); }
        if (btnTeclaDer.contienePunto(x, y)) { esperandoTecla = "derecha"; canvas.requestFocus(); }
        if (btnTeclaDisparo.contienePunto(x, y)) { esperandoTecla = "disparo"; canvas.requestFocus(); }

        // Columna Derecha (Skins)
        if (btnSkinNave.contienePunto(x, y)) indiceSkinNave = (indiceSkinNave + 1) % 2;
        if (btnSkinInv.contienePunto(x, y)) indiceSkinInv = (indiceSkinInv + 1) % 2;
        if (btnSkinProy.contienePunto(x, y)) indiceSkinProy = (indiceSkinProy + 1) % 2;

        actualizarTextosBotones();

        // Botones Inferiores
        if (btnGuardar.contienePunto(x, y)) {
            aplicarConfiguracionActual();
        }
        
        if (btnReset.contienePunto(x, y)) {
            config.restablecer();
            cargarValoresDesdeGestor();
            actualizarTextosBotones();
        }
        
        if (btnVolver.contienePunto(x, y)) {
            aplicarConfiguracionActual();
            stop();
        }

        //solo ajustar la ventana si cambió el modo pantalla completa
        if (pantallaCompleta != previaPantallaCompleta) {
            frame.dispose();
            if (pantallaCompleta) {
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                frame.setUndecorated(true);
            } else {
                frame.setUndecorated(false);
                frame.setSize(ANCHO, ALTO);
                frame.setLocationRelativeTo(null);
            }
            frame.setVisible(true);
        }
        canvas.requestFocus();
    }

    private void aplicarConfiguracionActual() {
        config.setSonidoActivado(sonidoActivado);
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
    }

    @Override
    public void gameUpdate(double delta) { }
    
    @Override
    public void gameDraw(Graphics2D g) {
        if (buffer == null) return;
        Graphics2D g2d = buffer.createGraphics();

        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(0, 0, ANCHO, ALTO);

        g2d.setFont(new Font("Arial", Font.BOLD, 36));
        g2d.setColor(Color.WHITE);
        String titulo = "CONFIGURACION";
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(titulo, 400 - fm.stringWidth(titulo)/2, 60);

        g2d.setFont(new Font("Arial", Font.PLAIN, 20));
        
        // Títulos de columnas
        g2d.drawString("Velocidad:", 100, 130);
        g2d.drawString("Controles:", 450, 130);
        g2d.drawString("Apariencia:", 450, 310);

        // Dibujar botones Columna Izquierda
        btnVelIzq.dibujar(g2d);
        btnVelDer.dibujar(g2d);
        g2d.drawString(opcionVelocidad[indiceVelocidad], 175 - fm.stringWidth(opcionVelocidad[indiceVelocidad])/2, 178);
        btnSonido.dibujar(g2d);
        btnPantalla.dibujar(g2d);
        btnMusica.dibujar(g2d);

        // Dibujar botones Columna Derecha
        btnTeclaIzq.dibujar(g2d);
        btnTeclaDer.dibujar(g2d);
        btnTeclaDisparo.dibujar(g2d);
        btnSkinNave.dibujar(g2d);
        btnSkinInv.dibujar(g2d);
        btnSkinProy.dibujar(g2d);

        // Si está esperando tecla, mostrar aviso
        if (esperandoTecla != null) {
            g2d.setColor(Color.YELLOW);
            g2d.drawString("Presiona una tecla para: " + esperandoTecla, 450, 100);
        }

        // Dibujar botones Inferiores
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
        new MenuSpaceInvaders(launcher).run();
    }
}