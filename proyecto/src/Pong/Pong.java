package Pong;

import motor.Videojuego;
import ranking.GestorRanking;
import ranking.EntradaRanking;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalDate;

/**
 * Clase principal del juego Pong. Extiende Videojuego (motor base de la cátedra).
 * Gestiona el loop del juego: paletas, pelota, colisiones, puntuación, sonido y ranking.
 */
public class Pong extends Videojuego {
    private static final int ANCHO_PANTALLA = 800;
    private static final int ALTO_PANTALLA = 600;

    // Entidades del juego (definidas en el paquete Pong)
    private Paleta paleta1;
    private Paleta paleta2;
    private Pelota pelota;

    // Estado de la partida
    private int puntajeJugador1 = 0;
    private int puntajeJugador2 = 0;
    private int puntuacionMaxima = 11; // Configurable desde MenuPong (11 o 15)
    private boolean juegoTerminado = false;
    private String ganador = "";

    // Control de teclado (captura teclas de ambos jugadores)
    private ControlTeclado controlTeclado;

    // Buffer para double buffering: evita el parpadeo al dibujar
    private BufferedImage buffer;

    // Ranking: guarda el resultado al cerrar el juego
    private GestorRanking gestorRanking;
    private boolean rankingGuardado = false;

    // Sonido: carga y reproduce música y efectos (falla silenciosamente si no hay archivos)
    private GestorSonidosPong gestorSonidos;

    // Tema visual: define colores de fondo, paletas y pelota (original = blanco/negro)
    private TemasPong tema;

    // Modo de juego: 1 = Humano vs CPU, 2 = Humano vs Humano (viene desde MenuPong)
    private int modoJuego = 2;
    private IA ia; // Solo se usa si modoJuego == 1

    public Pong() {
        super("Pong - ClassicGame Edition", ANCHO_PANTALLA, ALTO_PANTALLA);
        this.gestorRanking = new GestorRanking();
        this.gestorSonidos = new GestorSonidosPong(true);
        this.tema = new TemasPong("original");
    }

    // Llamado desde Launcher antes de run() para establecer el modo elegido en el menú
    public void setModoJuego(int modo) {
        this.modoJuego = modo;
    }

    // gameStartup(): se ejecuta una sola vez al iniciar, equivale al constructor del juego
    @Override
    public void gameStartup() {
        // Crear buffer para double buffering (arregla el parpadeo)
        buffer = new BufferedImage(ANCHO_PANTALLA, ALTO_PANTALLA, BufferedImage.TYPE_INT_RGB);

        // Registrar el listener de teclado en el canvas del motor
        controlTeclado = new ControlTeclado();
        canvas.addKeyListener(controlTeclado);
        canvas.setFocusable(true);
        canvas.requestFocus();

        // Cargar sonidos (si no existen los archivos WAV, continúa sin error)
        gestorSonidos.cargarTodosSonidos();
        gestorSonidos.reproducirMusica("original");

        // Crear paletas: J1 a la izquierda, J2 a la derecha
        int altoPaleta = 100;
        int anchoPaleta = 15;
        paleta1 = new Paleta(10, ALTO_PANTALLA / 2 - altoPaleta / 2, anchoPaleta, altoPaleta, true);
        paleta2 = new Paleta(ANCHO_PANTALLA - 10 - anchoPaleta, ALTO_PANTALLA / 2 - altoPaleta / 2, anchoPaleta, altoPaleta, false);

        paleta1.setTema(tema);
        paleta2.setTema(tema);
        paleta1.setAltoVentana(ALTO_PANTALLA);
        paleta2.setAltoVentana(ALTO_PANTALLA);

        // Crear pelota en el centro
        pelota = new Pelota(ANCHO_PANTALLA / 2 - 5, ALTO_PANTALLA / 2 - 5, 10, 10, ANCHO_PANTALLA, ALTO_PANTALLA);
        pelota.setTema(tema);

        // Si es 1P, crear la IA que controla la paleta derecha
        if (modoJuego == 1) {
            ia = new IA(paleta2); // La dificultad es progresiva, no se elige
            paleta2.setVelocidad(3.6f); // Más lenta que el jugador (que tiene 5.0)
        }

        enEjecucion = true;
        juegoTerminado = false;
    }

    // gameUpdate(): lógica del juego, se llama ~60 veces por segundo desde el motor
    @Override
    public void gameUpdate(double delta) {
        if (!juegoTerminado && enEjecucion) {
            actualizarMovimientoPaletas();
            pelota.mover();

            // Colisiones pelota-paleta: rebotar y reproducir sonido
            if (pelota.detectarColisionPaleta(paleta1)) {
                pelota.rebotar(paleta1);
                gestorSonidos.reproducirRebote();
            }
            if (pelota.detectarColisionPaleta(paleta2)) {
                pelota.rebotar(paleta2);
                gestorSonidos.reproducirRebote();
            }

            // Colisión con bordes superior e inferior
            if (pelota.obtenerY() <= 0 || pelota.obtenerY() + pelota.obtenerAlto() >= ALTO_PANTALLA) {
                pelota.rebotar();
                gestorSonidos.reproducirRebote();
            }

            verificarAnotacion();

            // Verificar fin de partida
            if (puntajeJugador1 >= puntuacionMaxima || puntajeJugador2 >= puntuacionMaxima) {
                juegoTerminado = true;
                ganador = (puntajeJugador1 >= puntuacionMaxima) ? "Jugador 1" : "Jugador 2";
                gestorSonidos.reproducirGameOver();
                gestorSonidos.detenerMusica();
            }
        }
    }

    // gameDraw(): renderizado, se llama luego de cada gameUpdate()
    @Override
    public void gameDraw(Graphics2D g) {
        // Dibujamos todo en el buffer primero (double buffering)
        Graphics2D bg = buffer.createGraphics();
        bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fondo
        bg.setColor(tema.getColorFondo());
        bg.fillRect(0, 0, ANCHO_PANTALLA, ALTO_PANTALLA);

        // Línea central punteada
        bg.setColor(tema.getColorLinea());
        for (int i = 0; i < ALTO_PANTALLA; i += 20) {
            bg.drawLine(ANCHO_PANTALLA / 2, i, ANCHO_PANTALLA / 2, i + 10);
        }

        paleta1.dibujar(bg);
        paleta2.dibujar(bg);
        pelota.dibujar(bg);

        // Marcador
        bg.setColor(tema.getColorTexto());
        bg.setFont(new Font("Arial", Font.BOLD, 36));
        bg.drawString(String.valueOf(puntajeJugador1), ANCHO_PANTALLA / 4, 50);
        bg.drawString(String.valueOf(puntajeJugador2), 3 * ANCHO_PANTALLA / 4, 50);

        // Labels de jugadores
        bg.setFont(new Font("Arial", Font.PLAIN, 14));
        bg.drawString("Jugador 1", ANCHO_PANTALLA / 4 - 30, 75);
        bg.drawString(modoJuego == 1 ? "CPU" : "Jugador 2", 3 * ANCHO_PANTALLA / 4 - 30, 75);

        // Pantalla de Game Over
        if (juegoTerminado) {
            bg.setColor(new Color(0, 0, 0, 180));
            bg.fillRect(0, 0, ANCHO_PANTALLA, ALTO_PANTALLA);

            bg.setColor(tema.getColorTexto());
            bg.setFont(new Font("Arial", Font.BOLD, 60));
            FontMetrics fm = bg.getFontMetrics();
            String txt = "GAME OVER";
            bg.drawString(txt, (ANCHO_PANTALLA - fm.stringWidth(txt)) / 2, ALTO_PANTALLA / 2 - 40);

            bg.setFont(new Font("Arial", Font.BOLD, 32));
            fm = bg.getFontMetrics();
            String ganadorTxt = ganador + " gana!";
            bg.drawString(ganadorTxt, (ANCHO_PANTALLA - fm.stringWidth(ganadorTxt)) / 2, ALTO_PANTALLA / 2 + 20);

            bg.setFont(new Font("Arial", Font.PLAIN, 18));
            fm = bg.getFontMetrics();
            String cerrar = "Cerrá la ventana para volver al menú";
            bg.setColor(new Color(180, 180, 180));
            bg.drawString(cerrar, (ANCHO_PANTALLA - fm.stringWidth(cerrar)) / 2, ALTO_PANTALLA / 2 + 70);
        }

        bg.dispose();

        // Volcar el buffer completo a la pantalla (un solo drawImage = sin parpadeo)
        g.drawImage(buffer, 0, 0, null);
    }

    // gameShutdown(): se ejecuta al cerrar la ventana, libera recursos
    @Override
    public void gameShutdown() {
        if (!rankingGuardado && !ganador.isEmpty()) {
            guardarEnRanking();
        }
        gestorSonidos.limpiar();
        if (buffer != null) buffer.flush();
    }

    // Mueve las paletas según las teclas presionadas o la IA
    private void actualizarMovimientoPaletas() {
        if (controlTeclado.isArriba1Presionada()) paleta1.moverArriba();
        if (controlTeclado.isAbajo1Presionada())  paleta1.moverAbajo();

        if (modoJuego == 2) {
            // 2P: el segundo jugador usa W/S
            if (controlTeclado.isArriba2Presionada()) paleta2.moverArriba();
            if (controlTeclado.isAbajo2Presionada())  paleta2.moverAbajo();
        } else {
            // 1P: la IA calcula y aplica el movimiento de la paleta derecha
            if (ia != null) ia.actualizar(pelota);
        }
    }

    // Detecta si la pelota salió por algún lado y actualiza el marcador
    private void verificarAnotacion() {
        if (pelota.obtenerX() < 0) {
            puntajeJugador2++;
            gestorSonidos.reproducirPunto();
            pelota.resetear(ANCHO_PANTALLA / 2, ALTO_PANTALLA / 2, 1);
        }
        if (pelota.obtenerX() > ANCHO_PANTALLA) {
            puntajeJugador1++;
            gestorSonidos.reproducirPunto();
            pelota.resetear(ANCHO_PANTALLA / 2, ALTO_PANTALLA / 2, 2);
        }
    }

    // Guarda el resultado en el archivo de ranking (GestorRanking)
    private void guardarEnRanking() {
        int puntajeFinal = Math.max(puntajeJugador1, puntajeJugador2);
        EntradaRanking entrada = new EntradaRanking(ganador, 1, puntajeFinal, LocalDate.now());
        gestorRanking.agregarEntrada(entrada);
        gestorRanking.guardar();
        rankingGuardado = true;
    }

    // Getters usados desde Launcher y MenuPong
    public int getPuntajeJugador1() { return puntajeJugador1; }
    public int getPuntajeJugador2() { return puntajeJugador2; }
    public int getPuntuacionMaxima() { return puntuacionMaxima; }
    public void setPuntuacionMaxima(int puntuacion) { this.puntuacionMaxima = puntuacion; }
    public boolean isJuegoTerminado() { return juegoTerminado; }
    public String getGanador() { return ganador; }
    public GestorSonidosPong getGestorSonidos() { return gestorSonidos; }
    public TemasPong getTema() { return tema; }
    public void cambiarTema(String nombreTema) {
        tema.cambiarTema(nombreTema);
        paleta1.setTema(tema);
        paleta2.setTema(tema);
        pelota.setTema(tema);
    }
}