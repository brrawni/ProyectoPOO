package spaceinvaders;

import configuracion.GestorConfiguracion;
import motor.Videojuego;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import launcher.Boton;

public class PantallaConfiguracion extends Videojuego {
    private static final int ANCHO = 800;
    private static final int ALTO  = 600;

    private BufferedImage buffer;
    private GestorConfiguracion config;

    //opciones
    private String[] opcionVelocidad = {"LENTA", "MEDIA", "RAPIDA"};
    private int indiceVelocidad = 1; // Por defecto MEDIA

    private boolean sonidoActivado = true;

    //teclas
    private int teclaIzquierda;
    private int teclaDerecha;
    private int teclaDisparo;

    //estado de edicion de teclas
    private String esperandoTecla = null; //izquierda, derecha, disparo

    //botones de opciones
    private Boton btnVelIzq, btnVelDer;
    private Boton btnSonido;
    private Boton btnTeclaIzq, btnTeclaDer, btnTeclaDisparo;

    //botones inferiores
    private Boton btnGuardar, btnVolver, btnReset;

    public PantallaConfiguracion() {
        super("Configuracion Space Invaders", ANCHO, ALTO);
    }

    @Override
    public void gameStartup() {
        buffer = new BufferedImage(ANCHO, ALTO, BufferedImage.TYPE_INT_ARGB);
        config = GestorConfiguracion.getInstance();

        //cargar valores actuales
        sonidoActivado = config.isSonidoActivado();
        teclaIzquierda = config.getTeclaIzquierda();
        teclaDerecha   = config.getTeclaDerecha();
        teclaDisparo   = config.getTeclaDisparo();

        switch (config.getVelocidad()) {
            case "LENTA": indiceVelocidad = 0; break;
            case "RAPIDA": indiceVelocidad = 2; break;
            default: indiceVelocidad = 1; break;
        }

        //botones de velocidad
        btnVelIzq = new Boton(300, 150, 50, 40, "<");
        btnVelDer = new Boton(450, 150, 50, 40, ">");

        //boton de sonido
        btnSonido = new Boton(300, 250, 200, 40, "Sonido: " + (sonidoActivado ? "ACTIVADO" : "DESACTIVADO"));

        //botones de teclas
        btnTeclaIzq    = new Boton(300, 350, 200, 40, "Izquierda: " + KeyEvent.getKeyText(teclaIzquierda));
        btnTeclaDer    = new Boton(300, 410, 200, 40, "Derecha: " + KeyEvent.getKeyText(teclaDerecha));
        btnTeclaDisparo = new Boton(300, 470, 200, 40, "Disparo: " + KeyEvent.getKeyText(teclaDisparo));

        //botones inferiores
        btnGuardar = new Boton(250, 520, 100, 40, "GUARDAR");
        btnVolver  = new Boton(450, 520, 100, 40, "VOLVER");
        btnReset  = new Boton(350, 520, 100, 40, "RESET");

        //mouse click
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                manejarClick(e.getX(), e.getY());
            }
        });

        //captura de tecla para reasignar controles
        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (esperandoTecla != null) {
                    int codigo = e.getKeyCode();
                    switch (esperandoTecla) {
                        case "izq":
                            teclaIzquierda = codigo;
                            btnTeclaIzq = new Boton(430, 330, 120, 32,
                                KeyEvent.getKeyText(codigo));
                            break;
                        case "der":
                            teclaDerecha = codigo;
                            btnTeclaDer = new Boton(430, 375, 120, 32,
                                KeyEvent.getKeyText(codigo));
                            break;
                        case "disp":
                            teclaDisparo = codigo;
                            btnTeclaDisp = new Boton(430, 420, 120, 32,
                                KeyEvent.getKeyText(codigo));
                            break;
                    }
                    esperandoTecla = null;
                }
            }
        });

        canvas.setFocusable(true);
        canvas.requestFocus();
    }

    private void manejarClick(int x, int y) {
        //velocidad izquierda
        if (btnVelIzq.contienePunto(x, y)) {
            indiceVelocidad = Math.max(0, indiceVelocidad - 1);
        }
        //velocidad derecha
        if(btnVelDer.contienePunto(x, y)) {
            indiceVelocidad = Math.min(2, indiceVelocidad + 1);
        }
        //sonido
        if(btnSonido.contienePunto(x, y)) {
            sonidoActivado = !sonidoActivado;
            btnSonido.setTexto("Sonido: " + (sonidoActivado ? "ACTIVADO" : "DESACTIVADO"));
        }
        //teclas
        if(btnTeclaIzq.contienePunto(x, y)) {
            esperandoTecla = "izquierda";
            canvas.requestFocus(); // Asegurarse de que el canvas tenga el foco para capturar la tecla
        }
        if(btnTeclaDer.contienePunto(x, y)) {
            esperandoTecla = "derecha";
            canvas.requestFocus(); // Asegurarse de que el canvas tenga el foco para capturar la tecla
        }
        if(btnTeclaDisparo.contienePunto(x, y)) {
            esperandoTecla = "disparo";
            canvas.requestFocus(); // Asegurarse de que el canvas tenga el foco para capturar la tecla
        }
        //guardar
        if(btnGuardar.contienePunto(x, y)) {
            config.setSonidoActivado(sonidoActivado);
            config.setVelocidad(opcionVelocidad[indiceVelocidad]);
            config.setTeclaIzquierda(teclaIzquierda);
            config.setTeclaDerecha(teclaDerecha);
            config.setTeclaDisparo(teclaDisparo);
            config.guardar();
        }
        //reset
        if(btnReset.contienePunto(x, y)) {
            config.restablecer();
            //recargar valores
            sonidoActivado = config.isSonidoActivado();
            teclaIzquierda = config.getTeclaIzquierda();
            teclaDerecha   = config.getTeclaDerecha();
            teclaDisparo   = config.getTeclaDisparo();
            indiceVelocidad = 1;
            btnSonido = new Boton(300, 250, 200, 40, "Sonido: " + (sonidoActivado ? "ACTIVADO" : "DESACTIVADO"));
            btnTeclaIzq = new Boton(300, 350, 200, 40, "Izquierda: " + KeyEvent.getKeyText(teclaIzquierda));
            btnTeclaDer = new Boton(300, 410, 200, 40, "Derecha: " + KeyEvent.getKeyText(teclaDerecha));
            btnTeclaDisparo = new Boton(300, 470, 200, 40, "Disparo: " + KeyEvent.getKeyText(teclaDisparo));
        }
        //volver
        if (btnVolver.contienePunto(x, y)) {
            stop();
        }
    }

    @Override
    public void gameUpdate(double delta) {
        // No hay animaciones
    }
    
    @Override
    public void gameDraw(Graphics2D g) {
        if (buffer == null) return;
        Graphics2D g2d = buffer.createGraphics();

        //fondo
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(0, 0, ANCHO, ALTO);

        //titulo
        g2d.setFont(new Font("Arial", Font.BOLD, 36));
        g2d.setColor(Color.WHITE);
        String titulo = "CONFIGURACION";
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(titulo, 400 - fm.stringWidth(titulo)/2, 80);

        //opciones
        String[] velocidades = {"LENTA", "MEDIA", "RAPIDA"};
        g2d.setFont(new Font("Arial", Font.PLAIN, 24));
        g2d.drawString("Velocidad de juego:", 300, 130);
        btnVelIzq.dibujar(g2d);
        btnVelDer.dibujar(g2d);
        g2d.drawString(velocidades[indiceVelocidad], 375 - fm.stringWidth(velocidades[indiceVelocidad])/2, 180);

        btnSonido.dibujar(g2d);
        btnTeclaIzq.dibujar(g2d);
        btnTeclaDer.dibujar(g2d);
        btnTeclaDisparo.dibujar(g2d);

        //botones inferiores
        btnGuardar.dibujar(g2d);
        btnVolver.dibujar(g2d);
        btnReset.dibujar(g2d);

        //mostrar buffer
        g.drawImage(buffer, 0, 0, null);
    }
    
    @Override
    public void gameShutdown() {
        new MenuSpaceInvaders().run(); // Volver al menú de Space Invaders después de cerrar la configuración
    }
}