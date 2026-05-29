package spaceinvaders;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import motor.Videojuego;
import ranking.EntradaRanking;
import ranking.GestorRanking;

public class SpaceInvaders extends Videojuego {
    private ControlTeclado teclado;
    private BufferedImage buffer;
    //entidades principales del juego
    private FormacionAlien formacion;
    private CanonJugador canon;
    private List<Escudo> escudos;
    private NaveNodriza nodriza;
    private int vidasGuardadas = 3;

    //estado del juego
    private Nivel nivel;
    private int nivelActual = 1;
    private int puntaje = 0; 
    private boolean enEjecucion = true;
    private int contadorDisparos = 0;
    private int ticksNaveNodriza = 0;

    //dimensiones de pantalla
    private static final int ANCHO_PANTALLA = 800;
    private static final int ALTO_PANTALLA = 600;

    //atributos para ranking
    private GestorRanking gestorRanking = new GestorRanking();
    private boolean rankingGuardado = false;
    private String nombreJugador = "";
    private boolean ingresandoNombre = false;

    public SpaceInvaders() {
        super("Space Invaders", ANCHO_PANTALLA, ALTO_PANTALLA);
    }

    public void siguienteNivel() {
        nivelActual++;
        vidasGuardadas = Math.min(canon.obtenerVidas() + 1, 5);
        inicializarNivel();
    }

    private void inicializarNivel() {
        nivel    = new Nivel(nivelActual);
        nivel.cargar();

        int yInicial = 60 + (nivelActual - 1) * 20; //baja 20 px por nivel
        // 1. Primero crear formacion y escudos
        formacion = new FormacionAlien(nivel.obtenerFilas(), nivel.obtenerColumnas(), nivel.obtenerVelocidadAlien(), yInicial);
        escudos   = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            escudos.add(new Escudo(150 + i * 150, ALTO_PANTALLA - 150));
        }

        // 2. Después crear el canon
        canon = new CanonJugador(ANCHO_PANTALLA / 2 - 16, ALTO_PANTALLA - 80, 32, 32, vidasGuardadas);

        // 3. Setear todo al canon DESPUÉS de crearlo
        canon.setFormacion(formacion);  // ← esto debe estar
        canon.setEscudos(escudos);
        canon.setJuego(this);

        // 4. Setear escudos a la formacion
        formacion.setEscudos(escudos);

        nodriza = new NaveNodriza(nivel);
        buffer  = new BufferedImage(ANCHO_PANTALLA, ALTO_PANTALLA, BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    public void gameStartup() {
        inicializarNivel();

        //registrar control de teclado
        teclado = new ControlTeclado();
        canvas.addKeyListener(teclado);
        canvas.setFocusable(true);
        canvas.requestFocus();
        canvas.requestFocusInWindow();
    }

    @Override
    public void gameUpdate(double delta) {
        if (!enEjecucion) return;

        teclado.procesarEntrada(canon);

        // Actualizar el canon del jugador
        canon.actualizar();
        
        // Mover la formación de aliens y actualizar su velocidad
        formacion.moverTodos();
        formacion.actualizarVelocidad();
        formacion.actualizarProyectiles();

        //disparo aleatorio de aliens
        if(Math.random() < nivel.obtenerProbabilidadDisparo()){ //probabilidad de disparo de aliens (2% por frame creo)
            formacion.disparoAleatorio(canon);
        }
        
        // controlar aparición de la nave nodriza
        ticksNaveNodriza++;
        if(ticksNaveNodriza >= 1800) { // Aparece cada 10 segundos
            nodriza.aparecer(); // Aparece desde la izquierda
            ticksNaveNodriza = 0;
        }
        nodriza.actualizar();

        //verificar fin de juego
        if(verificarFinJuego()) {
            enEjecucion = false;
        }

        //verificar siguiente nivel
        if(formacion.contarVivos() == 0) {
            siguienteNivel();
        }

    }

    @Override
    public void gameDraw(Graphics2D g) {
        if (buffer == null) return;
        Graphics2D g2d = buffer.createGraphics();

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, ANCHO_PANTALLA, ALTO_PANTALLA);

        g2d.setColor(Color.WHITE);
        canon.dibujar(g2d);
        formacion.dibujarFormacion(g2d);
        nodriza.dibujar(g2d);

        for (Escudo escudo : escudos) {
            escudo.dibujar(g2d);
        }

        g2d.setColor(Color.WHITE);
        g2d.drawString("Puntaje: " + puntaje, 20, 20);
        g2d.drawString("Vidas: "   + canon.obtenerVidas(), ANCHO_PANTALLA - 100, 20);
        g2d.drawString("Nivel: "   + nivelActual, ANCHO_PANTALLA / 2 - 30, 20);

    // Game over se dibuja ANTES del dispose
        if (!enEjecucion) {
            dibujarGameOver(g2d);
        }

        g2d.dispose();
        g.drawImage(buffer, 0, 0, null);
    }

    public NaveNodriza getNaveNodriza()  { return nodriza; }
    public int getContadorDisparos()     { return contadorDisparos; }
    public void sumarPuntaje(int puntos) { puntaje += puntos; }


    @Override
    public void gameShutdown() {
        // Aquí puedes liberar recursos si es necesario y guardar el puntaje o estado del juego
    }

    public boolean verificarFinJuego() {
        return canon.obtenerVidas() <= 0 || formacion.llegoAlSuelo();
    }

    private void dibujarGameOver(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(0, 0, ANCHO_PANTALLA, ALTO_PANTALLA);

        g2d.setFont(new Font("Arial", Font.BOLD, 40));
        g2d.setColor(Color.RED);
        g2d.drawString("GAME OVER", ANCHO_PANTALLA/2 - 100, 150);

        if (!rankingGuardado) {
            g2d.setFont(new Font("Arial", Font.PLAIN, 20));
            g2d.setColor(Color.WHITE);
            g2d.drawString("Ingresá tu nombre:", ANCHO_PANTALLA/2 - 100, 220);
            g2d.drawString(teclado.getTextoIngresado() + "|", ANCHO_PANTALLA/2 - 100, 250);
            g2d.drawString("Puntaje: " + puntaje + "  Nivel: " + nivelActual, ANCHO_PANTALLA/2 - 100, 290);
            g2d.setColor(Color.YELLOW);
            g2d.drawString("Presioná ENTER para guardar", ANCHO_PANTALLA/2 - 100, 330);

            if (teclado.isEnterPresionado() && !teclado.getTextoIngresado().isEmpty()) {
                gestorRanking.agregarEntrada(
                    new EntradaRanking(teclado.getTextoIngresado(), nivelActual, puntaje)
                );
                rankingGuardado = true;
                teclado.resetEntrada();
            }
        } else {
            dibujarRanking(g2d);
        }
    }

    private void dibujarRanking(Graphics2D g2d) {
    g2d.setFont(new Font("Arial", Font.BOLD, 24));
    g2d.setColor(Color.YELLOW);
    g2d.drawString("TOP 10", ANCHO_PANTALLA/2 - 40, 180);

    g2d.setFont(new Font("Arial", Font.PLAIN, 16));
    List<EntradaRanking> top = gestorRanking.obtenerTop10();

    for (int i = 0; i < top.size(); i++) {
            EntradaRanking e = top.get(i);
            String linea = (i+1) + ". " + e.getNombre() +
                       "   Pts: " + e.getPuntaje() +
                       "   Niv: " + e.getNivel() +
                       "   "      + e.getFecha();
            g2d.setColor(i == 0 ? Color.YELLOW : Color.WHITE);
            g2d.drawString(linea, ANCHO_PANTALLA/2 - 180, 220 + i * 25);
            }
        }
}

