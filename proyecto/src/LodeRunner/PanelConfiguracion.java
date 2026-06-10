package LodeRunner;

import launcher.Boton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelConfiguracion extends JPanel implements KeyListener {
    private GestorPantallas gestor;
    private ConfiguracionLR config;
    // Declaramos todos los botones que vamos a usar
    private Boton btnSonido;
    private Boton btnMusica;
    private Boton btnPantalla;
    private Boton btnSkin;
    private Boton btnRestaurar;
    private Boton btnVolver;
    private Boton btnPista;

    public PanelConfiguracion(GestorPantallas gestor) {
        this.gestor = gestor;
        this.setBackground(Color.DARK_GRAY);

        // 1. Inicializamos y cargamos la configuración
        config = new ConfiguracionLR();
        config.cargar();

        // Si no existe el archivo previo (o está vacío), lo llenamos por defecto
        if (config.getSkin() == null) {
            config.setearPorDefecto();
        }

        // 2. Creamos los botones repartidos en la pantalla (Grilla de 2 columnas)
        // Columna Izquierda (X = 150)
        btnSonido   = new Boton(150, 180, 220, 50, obtenerTextoSonido());
        btnPantalla = new Boton(150, 260, 220, 50, obtenerTextoPantalla());

        // Columna Derecha (X = 430)
        btnMusica   = new Boton(430, 180, 220, 50, obtenerTextoMusica());
        btnSkin     = new Boton(430, 260, 220, 50, obtenerTextoSkin());

        // Botones de abajo (Centrados en X = 290)
        btnRestaurar = new Boton(150, 340, 220, 50, "RESTAURAR DEFECTO");
        btnPista = new Boton(430, 340, 220, 50, obtenerPistaMusical());
        btnVolver    = new Boton(290, 460, 220, 50, "VOLVER Y GUARDAR");

        this.setFocusable(true);
        this.addKeyListener(this);

        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                requestFocusInWindow();
            }
        });

        configurarEventosMouse();
    }

    private String obtenerTextoSonido() {
        return "Sonido: " + (config.isEfectosDeSonidoActivados() ? "ON" : "OFF");
    }
    private String obtenerTextoMusica() {
        return "Música: " + (config.isMusicaDeFondoActivada() ? "ON" : "OFF");
    }
    private String obtenerTextoPantalla() {
        return "Pantalla: " + (config.isPantallaCompleta() ? "FULL" : "VENTANA");
    }
    private String obtenerTextoSkin() {
        return "Skin: " + config.getSkin().toUpperCase();
    }
    private String obtenerPistaMusical(){ return "Pista: " + config.getPistaMusical().toUpperCase(); }

    private void refrescarTextosBotones() {
        btnSonido.setTexto(obtenerTextoSonido());
        btnMusica.setTexto(obtenerTextoMusica());
        btnPantalla.setTexto(obtenerTextoPantalla());
        btnSkin.setTexto(obtenerTextoSkin());
        btnPista.setTexto(obtenerPistaMusical());
    }

    private void configurarEventosMouse() {
        addMouseMotionListener(new MouseAdapter() {
            public void mouseMoved(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                // Actualizamos el "hover" de todos los botones
                btnSonido.setHover(btnSonido.contienePunto(x, y));
                btnMusica.setHover(btnMusica.contienePunto(x, y));
                btnPantalla.setHover(btnPantalla.contienePunto(x, y));
                btnSkin.setHover(btnSkin.contienePunto(x, y));
                btnRestaurar.setHover(btnRestaurar.contienePunto(x, y));
                btnVolver.setHover(btnVolver.contienePunto(x, y));
                btnPista.setHover(btnPista.contienePunto(x, y));
                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                // Lógica al hacer click en cada botón
                if (btnSonido.contienePunto(x, y)) {
                    config.setEfectosDeSonidoActivados(!config.isEfectosDeSonidoActivados());
                    btnSonido.setTexto(obtenerTextoSonido());

                } else if (btnMusica.contienePunto(x, y)) {
                    config.setMusicaDeFondoActivada(!config.isMusicaDeFondoActivada());
                    btnMusica.setTexto(obtenerTextoMusica());

                } else if (btnPantalla.contienePunto(x, y)) {
                    config.setPantallaCompleta(!config.isPantallaCompleta());
                    btnPantalla.setTexto(obtenerTextoPantalla());

                } else if (btnSkin.contienePunto(x, y)) {
                    // Alterna entre "original" y "alternativo"
                    String nuevaSkin = config.getSkin().equals("original") ? "alternativo" : "original";
                    config.setSkin(nuevaSkin);
                    btnSkin.setTexto(obtenerTextoSkin());

                } else if (btnRestaurar.contienePunto(x, y)) {
                    // Restaura por defecto e impacta en la pantalla
                    config.setearPorDefecto();
                    refrescarTextosBotones();

                } else if (btnVolver.contienePunto(x, y)) {
                    // 1. Guardamos el archivo .properties en la computadora
                    config.guardar();
                    // 2. Volvemos al menú principal
                    gestor.cambiarPantalla(GestorPantallas.PANTALLA_MENU);
                } else if (btnPista.contienePunto(x, y)) {
                    String nuevaPista = config.getPistaMusical().equals("original") ? "alternativa" : "original";
                    config.setPistaMusical(nuevaPista);
                    btnPista.setTexto(obtenerPistaMusical());
                }

                repaint(); // Repintamos para reflejar los cambios de texto inmediatamente
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Título de la pantalla
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 45));
        g2.drawString("AJUSTES DEL JUEGO", 170, 90);

        // Dibujamos todos los botones
        btnSonido.dibujar(g2);
        btnMusica.dibujar(g2);
        btnPantalla.dibujar(g2);
        btnSkin.dibujar(g2);
        btnPista.dibujar(g2);
        btnRestaurar.dibujar(g2);
        btnVolver.dibujar(g2);
    }
    public void keyPressed(java.awt.event.KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_Q) {
                // Guardamos la configuración antes de salir
                if (config.isEfectosDeSonidoActivados()) {
                    // Si estaba activada, la desactivamos y guardamos
                    config.setEfectosDeSonidoActivados(false);
                    btnSonido.setTexto(obtenerTextoSonido());
                    config.guardar();
                    refrescarTextosBotones();
                }
                else {
                    // Si estaba desactivado, lo activamos y guardamos
                    config.setEfectosDeSonidoActivados(true);
                    btnSonido.setTexto(obtenerTextoSonido());
                    config.guardar();
                    refrescarTextosBotones();
                }
            }
            else if (e.getKeyCode() == KeyEvent.VK_W) {
                if (config.isMusicaDeFondoActivada()) {
                    // Si estaba activada, la desactivamos y guardamos
                    config.setMusicaDeFondoActivada(false);
                    btnMusica.setTexto(obtenerTextoMusica());
                    config.guardar();
                    refrescarTextosBotones();
                }
                else {
                    // Si estaba desactivada, la activamos y guardamos
                    config.setMusicaDeFondoActivada(true);
                    btnMusica.setTexto(obtenerTextoMusica());
                    config.guardar();
                    refrescarTextosBotones();
                }
            }
    }
    public void keyReleased(java.awt.event.KeyEvent e) {} //no hace nada
    public void keyTyped(java.awt.event.KeyEvent e) {} ///no hace nada
}
