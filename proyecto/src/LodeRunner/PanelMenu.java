package LodeRunner;

import launcher.Boton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class PanelMenu extends JPanel implements KeyListener {
    private GestorSonidosLodeRunner gestorSonidosLodeRunner;
    private GestorPantallas gestor; // Referencia al gestor para pedirle cambios de pantalla
    private Boton btnJugar;
    private Boton btnConfig;
    private Boton btnRanking;
    private Boton btnVolver;

    public PanelMenu(GestorPantallas gestor) {
        ConfiguracionLR config = new ConfiguracionLR();
        config.cargar();
        gestorSonidosLodeRunner = new GestorSonidosLodeRunner(config.isSonidoGeneralActivado(), config.isMusicaDeFondoActivada(), config.getPistaMusical());
        gestorSonidosLodeRunner.reproducirMusicaMenu();
        this.gestor = gestor;
        this.setBackground(Color.BLACK);
        gestor.getLodeRunnerMenu().addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                gestorSonidosLodeRunner.detenerMusicaMenu();
            }
        });

        gestor.getLodeRunnerMenu().addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            @Override
            public void windowGainedFocus(java.awt.event.WindowEvent e) {
                // El juego se cerró y el jugador volvió a hacer click o mirar el menú
                if (gestorSonidosLodeRunner != null) {
                    gestorSonidosLodeRunner.reproducirMusicaMenu();
                }
            }

            @Override
            public void windowLostFocus(java.awt.event.WindowEvent e) {
                // Cuando el menú pierde el foco (ej: se abre el juego), no hacemos nada
            }
        });

        btnJugar   = new Boton(300, 200, 200, 50, "JUGAR");
        btnConfig  = new Boton(300, 280, 200, 50, "CONFIGURACIÓN");
        btnRanking = new Boton(300, 360, 200, 50, "RANKING");
        btnVolver = new Boton(300, 440, 200, 50, "VOLVER");

        configurarEventosMouse();

        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(this);
    }

    private void configurarEventosMouse() {
        addMouseMotionListener(new MouseAdapter() {
            public void mouseMoved(MouseEvent e) {
                btnJugar.setHover(btnJugar.contienePunto(e.getX(), e.getY()));
                btnConfig.setHover(btnConfig.contienePunto(e.getX(), e.getY()));
                btnRanking.setHover(btnRanking.contienePunto(e.getX(), e.getY()));
                btnVolver.setHover(btnVolver.contienePunto(e.getX(), e.getY()));
                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (btnJugar.contienePunto(e.getX(), e.getY())) {
                    gestorSonidosLodeRunner.detenerMusicaMenu();
                    LodeRunnerMain juego = new LodeRunnerMain();
                    juego.run();
                } else if (btnConfig.contienePunto(e.getX(), e.getY())) {
                    gestor.cambiarPantalla(GestorPantallas.PANTALLA_CONFIG); // Pide cambiar de pantalla
                } else if (btnRanking.contienePunto(e.getX(), e.getY())) {
                    gestor.cambiarPantalla(GestorPantallas.PANTALLA_RANKING); // Pide cambiar de pantalla
                } else if (btnVolver.contienePunto(e.getX(), e.getY())){
                    gestorSonidosLodeRunner.detenerMusicaMenu();
                    gestor.getLodeRunnerMenu().dispose();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("Times New Roman", Font.BOLD, 60));
        g2.drawString("LODE RUNNER", 180, 120);

        btnJugar.dibujar(g2);
        btnConfig.dibujar(g2);
        btnRanking.dibujar(g2);
        btnVolver.dibujar(g2);
    }
    public void keyPressed(java.awt.event.KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            // Detenemos la música del menú antes de iniciar el juego
            gestorSonidosLodeRunner.detenerMusicaMenu();
            LodeRunnerMain juego = new LodeRunnerMain();
            juego.run();
        }
    }
    public void keyReleased(java.awt.event.KeyEvent e) {} //no hace nada
    public void keyTyped(java.awt.event.KeyEvent e) {} ///no hace nada
}
