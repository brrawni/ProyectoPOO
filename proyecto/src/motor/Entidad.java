package motor;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;

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

    public abstract void mover();
    
    public abstract boolean detectarColision();

    public abstract void dibujar(Graphics2D g);

}


// 1. La Plataforma: El punto de arranque de toda la aplicación
class Plataforma extends JFrame {

    public Plataforma() {
        setTitle("Consola de Videojuegos Retro");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        // Acá irían tus layouts y botones del catálogo
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Plataforma menu = new Plataforma();
            menu.setVisible(true);
        });
    }
}

// 2. El Bucle Puro (Clase abstracta de la cátedra)
abstract class GameLoop {
    private boolean runFlag = false;

    public void run() {
        runFlag = true;
        startup();
        while(runFlag) {
            update();
            draw();
        }
        shutdown(); // [cite: 208]
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
    protected JFrame frame;
    protected JPanel canvas;
    protected KeyListener keyboard;
    protected MouseListener mouse;
    protected MouseWheelListener mouseWheel;

    public JGame(String title, int ancho, int alto) {
        // Inicialización básica del entorno visual de Java Swing
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
    public void startup() {
        frame.setVisible(true); // Hacemos visible la ventana al arrancar
        gameStartup(); // [cite: 221]
    }

    @Override
    public void update() {
        // Simulación didáctica de delta: en un motor real aca calculás el tiempo real por frame
        double delta = 1.0 / 60.0;
        gameUpdate(delta); // Le pasamos el delta a la lógica del juego [cite: 227]
    }

    @Override
    public void draw() {
        // Obtenemos el gráfico del lienzo de Swing de forma segura
        Graphics2D g = (Graphics2D) canvas.getGraphics();
        if (g != null) {
            Graphics2D g2d = (Graphics2D) g;
            gameDraw(g2d); // Llamamos al dibujo pasándole el objeto gráfico
            g.dispose();   // Liberamos los recursos del sistema operativo
        }
    }

    @Override
    public void shutdown() {
        frame.setVisible(false);
        frame.dispose();
        gameShutdown();
    }

    public abstract void gameStartup(); // [cite: 235]
    public abstract void gameUpdate(double delta); //
    public abstract void gameDraw(Graphics2D g); //
    public abstract void gameShutdown(); // [cite: 238]

    protected void readPropertiesFile() {
        //Busca leer el archivo jgame.properties
    }
}

