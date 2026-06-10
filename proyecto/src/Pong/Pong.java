package Pong;

import motor.Videojuego;
import motor.GestorRankingBase;
import motor.EntradaRanking;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.util.List;

/**
 * Clase principal del juego Pong. Extiende Videojuego (motor base de la cátedra).
 * Gestiona el loop del juego: paletas, pelota, colisiones, puntuación, sonido y ranking.
 *
 * Resolución lógica fija 800x600. En pantalla completa el buffer se escala para
 * llenar el canvas, de modo que la lógica (posiciones, colisiones) no cambia.
 */
public class Pong extends Videojuego {
    private static final int ANCHO_LOGICO = 800;
    private static final int ALTO_LOGICO  = 600;
    private static final String RANKING_PONG = "ranking_pong.txt";

    private Paleta paleta1;
    private Paleta paleta2;
    private Pelota pelota;

    private int puntajeJugador1 = 0;
    private int puntajeJugador2 = 0;
    private int puntuacionMaxima = 11;
    private boolean juegoTerminado = false;
    private String ganador = "";

    private ControlTeclado controlTeclado;
    private BufferedImage buffer;

    private GestorRankingBase gestorRanking;
    private boolean rankingGuardado = false;

    private GestorSonidosPong gestorSonidos;
    private TemasPong tema;
    private String pistaMusical = "original";
    private boolean sonidoActivado = true;

    private int modoJuego = 2;
    private IA ia;

    // Flag de pantalla completa: se establece desde MenuPong antes de run()
    private boolean pantallaCompleta = false;

    public Pong(String nombreTema) {
        super("Pong - ClassicGame Edition", ANCHO_LOGICO, ALTO_LOGICO);
        this.gestorRanking = new GestorRankingBase(RANKING_PONG);
        this.gestorSonidos = new GestorSonidosPong(sonidoActivado);
        this.tema = new TemasPong(nombreTema);
    }

    public Pong(String skinCancha, String skinBarras, String skinPelota) {
        super("Pong - ClassicGame Edition", ANCHO_LOGICO, ALTO_LOGICO);
        this.gestorRanking = new GestorRankingBase(RANKING_PONG);
        this.gestorSonidos = new GestorSonidosPong(sonidoActivado);
        this.tema = new TemasPong(skinCancha, skinBarras, skinPelota);
    }

    public void setModoJuego(int modo) {
        this.modoJuego = modo;
    }

    public void setPantallaCompleta(boolean pantallaCompleta) {
        this.pantallaCompleta = pantallaCompleta;
    }

    public void setPistaMusical(String pistaMusical) {
        this.pistaMusical = pistaMusical;
    }

    public void setSonidoActivado(boolean sonidoActivado) {
        this.sonidoActivado = sonidoActivado;
        this.gestorSonidos.setSonidoActivado(sonidoActivado);
    }

    @Override
    public void gameStartup() {
        // Si es pantalla completa, hacer que el canvas llene todo el frame.
        // Esto debe ocurrir antes de que el frame se muestre (invokeLater en JGame lo garantiza).
        if (pantallaCompleta) {
            // Quitamos el tamaño preferido fijo para que BorderLayout pueda
            // estirar el canvas a todo el frame. Sin esto, JPanel se queda
            // en 800x600 en la esquina aunque la ventana esté maximizada.
            canvas.setPreferredSize(null);
            canvas.setMinimumSize(null);

            // El content pane de JFrame ya usa BorderLayout por defecto.
            // Solo necesitamos re-añadir el canvas como CENTER (lo reemplaza
            // en caso de que ya estuviera añadido sin restricción de posición).
            frame.getContentPane().removeAll();
            frame.getContentPane().add(canvas, BorderLayout.CENTER);

            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
            frame.revalidate();
            frame.repaint();
        }

        // Buffer siempre en resolución lógica fija: la lógica del juego no cambia
        buffer = new BufferedImage(ANCHO_LOGICO, ALTO_LOGICO, BufferedImage.TYPE_INT_RGB);

        controlTeclado = new ControlTeclado();
        canvas.addKeyListener(controlTeclado);
        canvas.setFocusable(true);
        canvas.requestFocus();
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                cerrarPartida();
            }
        });

        gestorSonidos.cargarTodosSonidos();
        gestorSonidos.reproducirMusica(pistaMusical);

        int altoPaleta  = 100;
        int anchoPaleta = 15;
        paleta1 = new Paleta(10, ALTO_LOGICO / 2 - altoPaleta / 2, anchoPaleta, altoPaleta, true);
        paleta2 = new Paleta(ANCHO_LOGICO - 10 - anchoPaleta, ALTO_LOGICO / 2 - altoPaleta / 2, anchoPaleta, altoPaleta, false);

        paleta1.setTema(tema);
        paleta2.setTema(tema);
        paleta1.setAltoVentana(ALTO_LOGICO);
        paleta2.setAltoVentana(ALTO_LOGICO);

        pelota = new Pelota(ANCHO_LOGICO / 2 - 5, ALTO_LOGICO / 2 - 5, 10, 10, ANCHO_LOGICO, ALTO_LOGICO);
        pelota.setTema(tema);

        if (modoJuego == 1) {
            ia = new IA(paleta2);
            paleta2.setVelocidad(3.6f);
        }

        enEjecucion = true;
        juegoTerminado = false;
    }

    @Override
    public void gameUpdate(double delta) {
        if (!juegoTerminado && enEjecucion) {
            actualizarMovimientoPaletas();
            pelota.mover();

            if (pelota.detectarColisionPaleta(paleta1)) {
                pelota.rebotar(paleta1);
                gestorSonidos.reproducirRebote();
            }
            if (pelota.detectarColisionPaleta(paleta2)) {
                pelota.rebotar(paleta2);
                gestorSonidos.reproducirRebote();
            }

            if (pelota.obtenerY() <= 0 || pelota.obtenerY() + pelota.obtenerAlto() >= ALTO_LOGICO) {
                pelota.rebotar();
                gestorSonidos.reproducirRebote();
            }

            verificarAnotacion();

            if (puntajeJugador1 >= puntuacionMaxima || puntajeJugador2 >= puntuacionMaxima) {
                juegoTerminado = true;
                ganador = (puntajeJugador1 >= puntuacionMaxima) ? "Jugador 1" : "Jugador 2";
                gestorSonidos.reproducirGameOver();
                gestorSonidos.detenerMusica();
            }
        } else if (juegoTerminado && !rankingGuardado) {
            guardarRankingSiCorresponde();
        }
    }

    @Override
    public void gameDraw(Graphics2D g) {
        // ── Dibujar todo en el buffer lógico 800x600 ──────────────────────
        Graphics2D bg = buffer.createGraphics();
        bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        bg.setColor(tema.getColorFondo());
        bg.fillRect(0, 0, ANCHO_LOGICO, ALTO_LOGICO);

        bg.setColor(tema.getColorLinea());
        for (int i = 0; i < ALTO_LOGICO; i += 20) {
            bg.drawLine(ANCHO_LOGICO / 2, i, ANCHO_LOGICO / 2, i + 10);
        }

        paleta1.dibujar(bg);
        paleta2.dibujar(bg);
        pelota.dibujar(bg);

        bg.setColor(tema.getColorTexto());
        bg.setFont(new Font("Arial", Font.BOLD, 36));
        bg.drawString(String.valueOf(puntajeJugador1), ANCHO_LOGICO / 4, 50);
        bg.drawString(String.valueOf(puntajeJugador2), 3 * ANCHO_LOGICO / 4, 50);

        bg.setFont(new Font("Arial", Font.PLAIN, 14));
        bg.drawString("Jugador 1", ANCHO_LOGICO / 4 - 30, 75);
        bg.drawString(modoJuego == 1 ? "CPU" : "Jugador 2", 3 * ANCHO_LOGICO / 4 - 30, 75);

        if (juegoTerminado) {
            bg.setColor(new Color(0, 0, 0, 180));
            bg.fillRect(0, 0, ANCHO_LOGICO, ALTO_LOGICO);

            if (rankingGuardado) {
                dibujarRankingFinal(bg);
                bg.dispose();
                escalarBuffer(g);
                return;
            }

            bg.setColor(tema.getColorTexto());
            bg.setFont(new Font("Arial", Font.BOLD, 60));
            FontMetrics fm = bg.getFontMetrics();
            String txt = "GAME OVER";
            bg.drawString(txt, (ANCHO_LOGICO - fm.stringWidth(txt)) / 2, ALTO_LOGICO / 2 - 40);

            bg.setFont(new Font("Arial", Font.BOLD, 32));
            fm = bg.getFontMetrics();
            String ganadorTxt = ganador + " gana!";
            bg.drawString(ganadorTxt, (ANCHO_LOGICO - fm.stringWidth(ganadorTxt)) / 2, ALTO_LOGICO / 2 + 20);

            bg.setFont(new Font("Arial", Font.PLAIN, 20));
            bg.setColor(Color.WHITE);
            fm = bg.getFontMetrics();
            String pedirNombre = "Ingresa tu nombre:";
            bg.drawString(pedirNombre, (ANCHO_LOGICO - fm.stringWidth(pedirNombre)) / 2, ALTO_LOGICO / 2 + 70);

            String nombre = controlTeclado.getTextoIngresado() + "|";
            fm = bg.getFontMetrics();
            bg.drawString(nombre, (ANCHO_LOGICO - fm.stringWidth(nombre)) / 2, ALTO_LOGICO / 2 + 105);

            bg.setColor(Color.YELLOW);
            String guardar = "Presiona ENTER para guardar en el ranking";
            fm = bg.getFontMetrics();
            bg.drawString(guardar, (ANCHO_LOGICO - fm.stringWidth(guardar)) / 2, ALTO_LOGICO / 2 + 140);
        }

        bg.dispose();

        // ── Escalar buffer al tamaño real del canvas ──────────────────────
        // En modo ventana canvas.getWidth/Height() == 800x600 → sin cambio.
        // En pantalla completa se estira para llenar la pantalla entera.
        int w = canvas.getWidth();
        int h = canvas.getHeight();
        if (w <= 0 || h <= 0) {
            // Canvas todavía no tiene tamaño real (primer frame): dibujar 1:1
            g.drawImage(buffer, 0, 0, null);
        } else {
            g.drawImage(buffer, 0, 0, w, h, null);
        }
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

    @Override
    public void gameShutdown() {
        gestorSonidos.limpiar();
        if (buffer != null) buffer.flush();
    }

    private void cerrarPartida() {
        enEjecucion = false;
        gestorSonidos.limpiar();
        stop();
    }

    private void actualizarMovimientoPaletas() {
        if (controlTeclado.isArriba1Presionada()) paleta1.moverArriba();
        if (controlTeclado.isAbajo1Presionada())  paleta1.moverAbajo();

        if (modoJuego == 2) {
            if (controlTeclado.isArriba2Presionada()) paleta2.moverArriba();
            if (controlTeclado.isAbajo2Presionada())  paleta2.moverAbajo();
        } else {
            if (ia != null) ia.actualizar(pelota);
        }
    }

    private void verificarAnotacion() {
        if (pelota.obtenerX() < 0) {
            puntajeJugador2++;
            gestorSonidos.reproducirPunto();
            pelota.resetear(ANCHO_LOGICO / 2, ALTO_LOGICO / 2, 1);
        }
        if (pelota.obtenerX() > ANCHO_LOGICO) {
            puntajeJugador1++;
            gestorSonidos.reproducirPunto();
            pelota.resetear(ANCHO_LOGICO / 2, ALTO_LOGICO / 2, 2);
        }
    }

    private void guardarRankingSiCorresponde() {
        if (controlTeclado.isEnterPresionado()) {
            String nombre = controlTeclado.getTextoIngresado();
            if (!nombre.isEmpty()) {
                guardarEnRanking(nombre);
                controlTeclado.resetEntrada();
            } else {
                controlTeclado.resetEnter();
            }
        }
    }

    private void guardarEnRanking(String nombreJugador) {
        int puntajeFinal = Math.max(puntajeJugador1, puntajeJugador2);
        EntradaRanking entrada = new EntradaRanking(nombreJugador, 1, puntajeFinal, LocalDate.now());
        gestorRanking.agregarEntrada(entrada);
        gestorRanking.guardar();
        rankingGuardado = true;
    }

    private void dibujarRankingFinal(Graphics2D bg) {
        gestorRanking.cargar();

        bg.setColor(new Color(255, 220, 80));
        bg.setFont(new Font("Arial", Font.BOLD, 34));
        FontMetrics fm = bg.getFontMetrics();
        String titulo = "MEJORES PUNTAJES";
        bg.drawString(titulo, (ANCHO_LOGICO - fm.stringWidth(titulo)) / 2, 155);

        List<EntradaRanking> top10 = gestorRanking.obtenerTop10();
        bg.setFont(new Font("Monospaced", Font.BOLD, 20));

        if (top10.isEmpty()) {
            bg.setColor(Color.WHITE);
            bg.drawString("NO HAY PUNTAJES AUN.", 265, 270);
        } else {
            bg.setColor(new Color(170, 210, 255));
            bg.drawString("POS  NOMBRE      PUNTOS  FECHA", 150, 215);

            bg.setColor(Color.WHITE);
            int y = 250;
            for (int i = 0; i < top10.size(); i++) {
                EntradaRanking entrada = top10.get(i);
                String nombre = entrada.getNombre();
                if (nombre.length() > 10) nombre = nombre.substring(0, 10);

                String linea = String.format("%2d.  %-10s  %5d   %s",
                        i + 1,
                        nombre,
                        entrada.getPuntaje(),
                        entrada.getFecha());
                bg.drawString(linea, 150, y);
                y += 28;
            }
        }

        bg.setColor(Color.WHITE);
        bg.setFont(new Font("Arial", Font.PLAIN, 18));
        String volver = "Cerra la ventana para volver al menu";
        fm = bg.getFontMetrics();
        bg.drawString(volver, (ANCHO_LOGICO - fm.stringWidth(volver)) / 2, 555);
    }

    public int getPuntajeJugador1()    { return puntajeJugador1; }
    public int getPuntajeJugador2()    { return puntajeJugador2; }
    public int getPuntuacionMaxima()   { return puntuacionMaxima; }
    public void setPuntuacionMaxima(int puntuacion) { this.puntuacionMaxima = puntuacion; }
    public boolean isJuegoTerminado()  { return juegoTerminado; }
    public String getGanador()         { return ganador; }
    public GestorSonidosPong getGestorSonidos() { return gestorSonidos; }
    public TemasPong getTema()         { return tema; }
    public void cambiarTema(String nombreTema) {
        tema.cambiarTema(nombreTema);
        paleta1.setTema(tema);
        paleta2.setTema(tema);
        pelota.setTema(tema);
    }

    public void cambiarSkins(String skinCancha, String skinBarras, String skinPelota) {
        tema.cambiarSkinCancha(skinCancha);
        tema.cambiarSkinBarras(skinBarras);
        tema.cambiarSkinPelota(skinPelota);
        paleta1.setTema(tema);
        paleta2.setTema(tema);
        pelota.setTema(tema);
    }
}