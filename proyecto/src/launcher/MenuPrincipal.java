package launcher;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import motor.Videojuego;
import spaceinvaders.SpaceInvaders;
import spaceinvaders.GestorImagenes;
import java.awt.image.BufferedImage;

public class MenuPrincipal extends Videojuego {

    private static final int ANCHO = 800;
    private static final int ALTO  = 600;

    private BufferedImage buffer;
    private Boton[] botones;

    // Animación de aliens decorativos
    private int[] alienX    = {100, 250, 400, 550, 700};
    private int[] alienY    = {120, 100, 130, 105, 115};
    private int   alienTick = 0;
    private int   alienDir  = 1;

    // Estrellas de fondo
    private int[][] estrellas;

    public MenuPrincipal() {
        super("Space Invaders", ANCHO, ALTO);
    }

    @Override
    public void gameStartup() {
        buffer = new BufferedImage(ANCHO, ALTO, BufferedImage.TYPE_INT_ARGB);

        // Crear botones centrados
        int bAncho  = 260;
        int bAlto   = 50;
        int bX      = ANCHO / 2 - bAncho / 2;
        int espaciado = 70;
        int yInicio = 280;

        botones = new Boton[] {
                new Boton(bX, yInicio,               bAncho, bAlto, "► JUGAR"),
                new Boton(bX, yInicio + espaciado,   bAncho, bAlto, "CONFIGURACION"),
                new Boton(bX, yInicio + espaciado*2, bAncho, bAlto, "RANKING"),
                new Boton(bX, yInicio + espaciado*3, bAncho, bAlto, "SALIR")
        };

        // Generar estrellas aleatorias
        estrellas = new int[80][2];
        for (int i = 0; i < estrellas.length; i++) {
            estrellas[i][0] = (int)(Math.random() * ANCHO);
            estrellas[i][1] = (int)(Math.random() * ALTO);
        }

        // Mouse listener para clicks y hover
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                manejarClick(e.getX(), e.getY());
            }
        });

        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                for (Boton b : botones) {
                    b.setHover(b.contienePunto(e.getX(), e.getY()));
                }
            }
        });

        canvas.setFocusable(true);
        canvas.requestFocus();
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
    }

    private String texto(String t) { return t; }

    private void manejarClick(int mx, int my) {
        for (int i = 0; i < botones.length; i++) {
            if (botones[i].contienePunto(mx, my)) {
                switch (i) {
                    case 0: // Jugar
                        frame.dispose(); // cerrar ventana del menú
                        new Thread(() -> new SpaceInvaders().run()).start();
                        stop();
                        break;
                    case 1: // Configuración
                        System.out.println("Configuracion proxima");
                        break;
                    case 2: // Ranking
                        System.out.println("Ranking proximo");
                        break;
                    case 3: // Salir
                        System.exit(0);
                        break;
                }
                return; // importante: salir después del primer match
            }
        }
    }

    @Override
    public void gameUpdate(double delta) {
        int[] alienTipos = {0, 1, 2, 0, 1};
        // Animar aliens decorativos
        alienTick++;
        if (alienTick >= 20) {
            alienTick = 0;
            for (int i = 0; i < alienX.length; i++) {
                alienX[i] += alienDir * 3;
            }
            if (alienX[0] > ANCHO - 50 || alienX[0] < 50) {
                alienDir *= -1;
                for (int i = 0; i < alienY.length; i++) {
                    alienY[i] += 10;
                }
            }
        }
    }

    @Override
    public void gameDraw(Graphics2D g) {
        int[] alienTipos = {0, 1, 2, 0, 1};
        if (buffer == null) return;
        Graphics2D g2d = buffer.createGraphics();

        // Fondo negro
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, ANCHO, ALTO);

        // Estrellas
        g2d.setColor(Color.WHITE);
        for (int[] e : estrellas) {
            g2d.fillRect(e[0], e[1], 2, 2);
        }

        // Título
        g2d.setFont(new Font("Arial", Font.BOLD, 60));
        g2d.setColor(new Color(100, 200, 255));
        String titulo = "SPACE INVADERS";
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(titulo, ANCHO/2 - fm.stringWidth(titulo)/2, 180);

        // Subtítulo parpadeante
        if ((System.currentTimeMillis() / 500) % 2 == 0) {
            g2d.setFont(new Font("Arial", Font.PLAIN, 16));
            g2d.setColor(new Color(150, 150, 255));
            String sub = "INSERT COIN";
            g2d.drawString(sub, ANCHO/2 - g2d.getFontMetrics().stringWidth(sub)/2, 220);
        }

        // Aliens decorativos
        g2d.setColor(new Color(100, 255, 100));
        for (int i = 0; i < alienX.length; i++) {
            dibujarAlienSimple(g2d, alienX[i], alienY[i], alienTipos[i]);
        }

        // Botones
        for (Boton b : botones) {
            b.dibujar(g2d);
        }

        // Footer
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.setColor(new Color(80, 80, 80));
        g2d.drawString("© 2025 ProyectoPOO", ANCHO/2 - 60, ALTO - 20);

        g2d.dispose();
        g.drawImage(buffer, 0, 0, null);
    }

    private void dibujarAlienSimple(Graphics2D g, int x, int y, int tipo) {
        GestorImagenes gestor = GestorImagenes.getInstance();

        String nombreSprite;
        Color color;
        switch (tipo) {
            case 0:
                nombreSprite = "pulpo";
                color = Color.MAGENTA;
                break;
            case 1:
                nombreSprite = "cangrejo";
                color = Color.RED;
                break;
            default:
                nombreSprite = "calamar";
                color = Color.GREEN;
                break;
        }

        String ruta = "/img/spaceinvaders/" + nombreSprite + "_0.png";
        String claveCache = ruta + "_" + color.getRGB();

        BufferedImage img = gestor.cargarDeCache(claveCache);
        if (img == null) {
            img = gestor.colorear(gestor.cargar(ruta), color);
            if (img != null) gestor.guardarEnCache(claveCache, img);
        }

        if (img != null) {
            g.drawImage(img, x, y, 40, 40, null);
        } else {
            g.setColor(color);
            g.fillRect(x, y, 40, 40);
        }
    }

    @Override
    public void gameShutdown() { }
}