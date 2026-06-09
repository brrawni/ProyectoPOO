package motor;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.*;
import javax.swing.*;

public abstract class Entidad {
    protected int x;
    protected int y;
    protected int ancho;
    protected int alto;
    protected boolean visible;

    public Entidad(int x, int y, int ancho, int alto){
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.visible = true;
    }

    public Rectangle obtenerLimites() {
        return new Rectangle(x, y, ancho, alto);
    }
    public int obtenerX() {
        return x;
    }

    public int obtenerY() {
        return y;
    }

    public int obtenerAlto(){ return alto; }

    public int obtenerAncho(){ return ancho; }

    public abstract void mover();
    
    public abstract boolean detectarColision();

    public abstract void dibujar(Graphics2D g);

}

// 2. El Bucle Puro (Clase abstracta de la cátedra)
abstract class GameLoop {
    protected boolean runFlag = false;

    public void run() {
        runFlag = true;
    startup();
    long tiempoAnterior = System.currentTimeMillis();
    
    while (runFlag) {
        long ahora = System.currentTimeMillis();
        long delta = ahora - tiempoAnterior;
        
        if (delta >= 16) { // ~60 fps
            tiempoAnterior = ahora;
            update();
            draw();
        } else {
            try {
                Thread.sleep(1); // no quemar la CPU
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            }
        }
        shutdown();
    }

    public void stop() {
        runFlag = false; // [cite: 212]
    }

    public abstract void startup(); // [cite: 213]
    public abstract void shutdown(); // [cite: 213]
    public abstract void update(); //
    public abstract void draw(); // [cite: 215]
}

// 3. JGame: Agrega la ventana, el lienzo y los periféricos de entrada
abstract class JGame extends GameLoop {
    public JFrame frame; //error de package, lo dejo público para evitarlo
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

    //sobreescribimos run() para ejecutar en hilo separado
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

