package motor;

import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

// Bucle principal reutilizable para los juegos.
abstract class GameLoop {
    protected boolean runFlag = false;

    public void run() {
        runFlag = true;
        startup();
        long tiempoAnterior = System.currentTimeMillis();

        while (runFlag) {
            long ahora = System.currentTimeMillis();
            long delta = ahora - tiempoAnterior;

            if (delta >= 16) {
                tiempoAnterior = ahora;
                update();
                draw();
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        shutdown();
    }

    public void stop() {
        runFlag = false;
    }

    public abstract void startup();
    public abstract void shutdown();
    public abstract void update();
    public abstract void draw();
}

// Base grafica Swing que adapta el bucle al ciclo de vida del juego.
abstract class JGame extends GameLoop {
    public JFrame frame;
    protected JPanel canvas;

    public JGame(String title, int ancho, int alto) {
        frame = new JFrame(title);
        canvas = new JPanel();
        canvas.setPreferredSize(new Dimension(ancho, alto));
        frame.add(canvas);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        readPropertiesFile();
    }

    @Override
    public void run() {
        Thread hiloJuego = new Thread(() -> {
            runFlag = true;
            startup();
            long tiempoAnterior = System.currentTimeMillis();

            while (runFlag) {
                long ahora = System.currentTimeMillis();
                long delta = ahora - tiempoAnterior;

                if (delta >= 16) {
                    tiempoAnterior = ahora;
                    update();
                    draw();
                } else {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
            shutdown();
        });
        hiloJuego.start();
    }

    @Override
    public void startup() {
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
        gameStartup();
    }

    @Override
    public void update() {
        double delta = 1.0 / 60.0;
        gameUpdate(delta);
    }

    @Override
    public void draw() {
        Graphics2D g = (Graphics2D) canvas.getGraphics();
        if (g != null) {
            gameDraw(g);
            g.dispose();
        }
    }

    @Override
    public void shutdown() {
        SwingUtilities.invokeLater(() -> {
            frame.setVisible(false);
            frame.dispose();
        });
        gameShutdown();
    }

    public abstract void gameStartup();
    public abstract void gameUpdate(double delta);
    public abstract void gameDraw(Graphics2D g);
    public abstract void gameShutdown();

    protected void readPropertiesFile() { }
}

public abstract class Videojuego extends JGame {
    protected int nivelActual;
    protected boolean enEjecucion;
    protected int puntaje;

    public Videojuego(String title, int ancho, int alto) {
        super(title, ancho, alto);
        this.puntaje = 0;
        this.enEjecucion = true;
        this.nivelActual = 1;
    }
}
