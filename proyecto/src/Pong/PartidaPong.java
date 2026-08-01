package Pong;

import java.awt.*;
import java.time.LocalDate;
import javax.swing.JPanel;
import motor.EntradaRanking;
import motor.GestorRankingBase;

/**
 * Logica y dibujo de una partida individual de Pong.
 * Pong coordina la ventana y los estados; esta clase se ocupa solo de jugar.
 */
public class PartidaPong {
    private static final int ANCHO_LOGICO = 800;
    private static final int ALTO_LOGICO = 600;

    private final int modoJuego;
    private final int puntuacionMaxima;
    private final TemasPong tema;
    private final GestorSonidosPong gestorSonidos;
    private final GestorRankingBase gestorRanking;
    private final PantallaRankingPong pantallaRanking;

    private Paleta paleta1;
    private Paleta paleta2;
    private Pelota pelota;
    private ControlTeclado controlTeclado;
    private IA ia;
    private JPanel canvas;

    private int puntajeJugador1 = 0;
    private int puntajeJugador2 = 0;
    private boolean juegoTerminado = false;
    private boolean rankingGuardado = false;
    private boolean volverAlMenu = false;
    private String ganador = "";

    public PartidaPong(int modoJuego, int puntuacionMaxima, TemasPong tema,
                       GestorSonidosPong gestorSonidos, GestorRankingBase gestorRanking,
                       PantallaRankingPong pantallaRanking) {
        this.modoJuego = modoJuego;
        this.puntuacionMaxima = puntuacionMaxima;
        this.tema = tema;
        this.gestorSonidos = gestorSonidos;
        this.gestorRanking = gestorRanking;
        this.pantallaRanking = pantallaRanking;
    }

    public void iniciar(JPanel canvas) {
        this.canvas = canvas;
        controlTeclado = new ControlTeclado();
        canvas.addKeyListener(controlTeclado);
        canvas.setFocusable(true);
        canvas.requestFocus();

        int altoPaleta = 100;
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
            ia = new IA(paleta1, ALTO_LOGICO);
            paleta1.setVelocidad(3.6f);
        }
    }

    public void actualizar(double delta) {
        if (!juegoTerminado) {
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
            verificarFinPartida();
        } else if (!rankingGuardado) {
            guardarRankingSiCorresponde();
        } else if (controlTeclado.isEnterPresionado()) {
            volverAlMenu = true;
            controlTeclado.resetEntrada();
        }
    }

    public void renderizar(Graphics2D bg) {
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
        bg.drawString(String.valueOf(puntajeJugador2), ANCHO_LOGICO / 4, 50);
        bg.drawString(String.valueOf(puntajeJugador1), 3 * ANCHO_LOGICO / 4, 50);

        bg.setFont(new Font("Arial", Font.PLAIN, 14));
        bg.drawString(modoJuego == 1 ? "CPU" : "Jugador 2", ANCHO_LOGICO / 4 - 30, 75);
        bg.drawString("Jugador 1", 3 * ANCHO_LOGICO / 4 - 30, 75);

        if (juegoTerminado) {
            renderizarGameOver(bg);
        }
    }

    public boolean debeVolverAlMenu() {
        return volverAlMenu;
    }

    public void limpiar() {
        if (canvas != null && controlTeclado != null) {
            canvas.removeKeyListener(controlTeclado);
        }
        canvas = null;
    }

    private void actualizarMovimientoPaletas() {
        if (controlTeclado.isArriba1Presionada()) paleta2.moverArriba();
        if (controlTeclado.isAbajo1Presionada()) paleta2.moverAbajo();

        if (modoJuego == 2) {
            if (controlTeclado.isArriba2Presionada()) paleta1.moverArriba();
            if (controlTeclado.isAbajo2Presionada()) paleta1.moverAbajo();
        } else if (ia != null) {
            ia.actualizar(pelota);
        }
    }

    private void verificarAnotacion() {
        if (pelota.obtenerX() < 0) {
            puntajeJugador1++;
            gestorSonidos.reproducirPunto();
            pelota.resetear(ANCHO_LOGICO / 2, ALTO_LOGICO / 2, 1);
        }
        if (pelota.obtenerX() > ANCHO_LOGICO) {
            puntajeJugador2++;
            gestorSonidos.reproducirPunto();
            pelota.resetear(ANCHO_LOGICO / 2, ALTO_LOGICO / 2, 2);
        }
    }

    private void verificarFinPartida() {
        if (puntajeJugador1 >= puntuacionMaxima || puntajeJugador2 >= puntuacionMaxima) {
            juegoTerminado = true;
            ganador = (puntajeJugador1 >= puntuacionMaxima) ? "Jugador 1" : "Jugador 2";
            gestorSonidos.reproducirGameOver();
            gestorSonidos.detenerMusica();
            controlTeclado.resetEntrada();
        }
    }

    private void renderizarGameOver(Graphics2D bg) {
        bg.setColor(new Color(0, 0, 0, 180));
        bg.fillRect(0, 0, ANCHO_LOGICO, ALTO_LOGICO);

        if (rankingGuardado) {
            pantallaRanking.renderizarPostPartida(bg, ANCHO_LOGICO, ALTO_LOGICO);
            bg.setColor(Color.YELLOW);
            bg.setFont(new Font("Arial", Font.PLAIN, 18));
            FontMetrics fm = bg.getFontMetrics();
            String volver = "Presiona ENTER para volver al menu";
            bg.drawString(volver, (ANCHO_LOGICO - fm.stringWidth(volver)) / 2, ALTO_LOGICO - 20);
        } else {
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
            if (modoJuego == 1 && ganador.equals("Jugador 2")) {
                String msg = "La CPU gano esta vez...";
                bg.drawString(msg, (ANCHO_LOGICO - fm.stringWidth(msg)) / 2, ALTO_LOGICO / 2 + 90);
            } else {
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
        }
    }

    private void guardarRankingSiCorresponde() {
        if (modoJuego == 1 && ganador.equals("Jugador 2")) {
            guardarEnRanking("CPU");
        } else if (controlTeclado.isEnterPresionado()) {
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
        int puntajeGanador = Math.max(puntajeJugador1, puntajeJugador2);
        int puntajePerdedor = Math.min(puntajeJugador1, puntajeJugador2);
        int puntajeRanking = (puntajeGanador - puntajePerdedor) * 10;
        EntradaRanking entrada = new EntradaRanking(nombreJugador, puntajePerdedor, puntajeRanking, LocalDate.now());
        gestorRanking.agregarEntrada(entrada);
        gestorRanking.guardar();
        rankingGuardado = true;
    }
}
