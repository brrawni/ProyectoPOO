package spaceinvaders;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import motor.Videojuego;



public class SpaceInvaders extends Videojuego {
    private ControlTeclado teclado;
    private BufferedImage buffer;
    //entidades principales del juego
    private FormacionAlien formacion;
    private CanonJugador canon;
    private List<Escudo> escudos;
    private NaveNodriza nodriza;

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

    public SpaceInvaders() {
        super("Space Invaders", ANCHO_PANTALLA, ALTO_PANTALLA);
    }

    private void inicializarNivel(){
        buffer = new BufferedImage(ANCHO_PANTALLA, ALTO_PANTALLA, BufferedImage.TYPE_INT_ARGB);
        nivel = new Nivel(nivelActual);
        nivel.cargar();

        canon = new CanonJugador(ANCHO_PANTALLA / 2 - 16, ALTO_PANTALLA - 80, 32, 32, 3);

        formacion = new FormacionAlien(4, 8, 0.5f);

        nodriza = new NaveNodriza(nivel);
        escudos = new ArrayList<>();
        int numEscudos = 4;
        for(int i=0; i<numEscudos; i++) {
            escudos.add(new Escudo(150 + i * 150, ALTO_PANTALLA - 150));
        }
        formacion.setEscudos(escudos);
        canon.setEscudos(escudos);
        canon.setJuego(this);
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

        teclado.procesarEntrada(canon, formacion);

        // Actualizar el canon del jugador
        canon.actualizar();
        
        // Mover la formación de aliens y actualizar su velocidad
        formacion.moverTodos();
        formacion.actualizarVelocidad();
        formacion.actualizarProyectiles();

        //disparo aleatorio de aliens
        if(Math.random() < 0.02){ //probabilidad de disparo de aliens (2% por frame creo)
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

        // 1. Dibujar fondo en el buffer
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0,0,ANCHO_PANTALLA, ALTO_PANTALLA);

        // 2. Pintamos entidades en el buffer
        g2d.setColor(Color.WHITE);
        canon.dibujar(g2d);
        formacion.dibujarFormacion(g2d);
        nodriza.dibujar(g2d);

        // 3. Los escudos
        for(Escudo escudo : escudos) {
            escudo.dibujar(g2d);
        }

        // 4. HUD
        g2d.setColor(Color.WHITE);
        g2d.drawString("Puntaje: " + puntaje, 20, 20);
        g2d.drawString("Vidas: " + canon.obtenerVidas(), ANCHO_PANTALLA - 100, 20);
        g2d.drawString("Nivel: " + nivelActual, ANCHO_PANTALLA / 2 - 30, 20);

        if(!enEjecucion) {
            g2d.setColor(Color.RED);
            g2d.drawString("GAME OVER", ANCHO_PANTALLA / 2 - 40, ALTO_PANTALLA / 2);
        }

        // 5. Descartamos el pincel temporal
        g2d.dispose();

        // 6. ¡Pegamos la imagen terminada en la pantalla de una sola vez!
        g.drawImage(buffer, 0, 0, null);

    }

    public NaveNodriza getNaveNodriza()  { return nodriza; }
    public int getContadorDisparos()     { return contadorDisparos; }
    public void sumarPuntaje(int puntos) { puntaje += puntos; }


    @Override
    public void gameShutdown() {
        // Aquí puedes liberar recursos si es necesario y guardar el puntaje o estado del juego
    }

    public void siguienteNivel() {
        nivelActual++;
        int vidasActuales = canon.obtenerVidas()+1; // recompensa con una vida extra
        int vidaMaxima = 5;
        if(vidasActuales > vidaMaxima) vidasActuales = vidaMaxima; // límite de vidas

        inicializarNivel(); //entidades con mas velocidad
        canon = new CanonJugador(ANCHO_PANTALLA / 2 - 16, ALTO_PANTALLA - 80, 32, 32, vidasActuales);
        canon.setEscudos(escudos);
    }

    public boolean verificarFinJuego() {
        return canon.obtenerVidas() <= 0 || formacion.llegoAlSuelo();
    }


}
