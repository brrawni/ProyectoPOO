package spaceinvaders;

import java.awt.Graphics2D;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import motor.Videojuego;
import java.awt.event.KeyEvent;


public class SpaceInvaders extends Videojuego {
    //entidades principales del juego
    private FormacionAlien formacion;
    private CanonJugador canon;
    private List<Escudo> escudos;
    private NaveNodriza nodriza;

    //estado del juego
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

    @Override
    public void gameStartup() {
        inicializarNivel();

        //registrar control de teclado
        ControlTeclado control = new ControlTeclado(canon, formacion, this);
        canvas.addKeyListener(control);
        canvas.setFocusable(true);
        canvas.requestFocusWindow();
    }

    @Override
    public void gameUpdate(double delta) {
        if (!enEjecucion) return;

        // Actualizar el canon del jugador
        canon.actualizar();
        
        // Mover la formación de aliens y actualizar su velocidad
        formacion.moverTodos();
        formacion.actualizarVelocidad();

        //disparo aleatorio de aliens
        if(Math.random() < 0.02){ //probabilidad de disparo de aliens (2% por frame creo)
            formacion.disparoAleatorio(canon);
        }
        
        // controlar aparición de la nave nodriza
        ticksNaveNodriza++;
        if(ticksNaveNodriza >= 600) { // Aparece cada 10 segundos
            nodriza.aparecer(); // Aparece desde la izquierda
            ticksNaveNodriza = 0;
        }
        naveNodriza.actualizar();

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
        // Dibujar fondo
        g.setColor(Color.BLACK);
        g.fillRect(0,0,ANCHO_PANTALLA, ALTO_PANTALLA);

        //dibujar todo
        canon.dibujar(g);
        formacion.dibujarTodos(g);
        naveNodriza.dibujar(g);
        for(Escudo escudo : escudos) {
            escudo.dibujar(g);
        }

        //HUD puntaje y vidas
        g.setColor(Color.WHITE);
        g.drawString("Puntaje: " + puntaje, 20, 20);
        g.drawString("Vidas: " + canon.obtenerVidas(), ANCHO_PANTALLA - 100, 20);
        g.drawString("Nivel: " + nivelActual, ANCHO_PANTALLA / 2 - 30, 20);

        //pantalla game over
        if(!enEjecucion) {
            g.setColor(Color.RED);
            g.drawString("GAME OVER", ANCHO_PANTALLA / 2 - 40, ALTO_PANTALLA / 2);
        }
    }

    @Override
    public void gameShutdown() {
        // Aquí puedes liberar recursos si es necesario y guardar el puntaje o estado del juego
    }

    public void siguienteNivel() {
        nivelActual++;
        inicializarNivel(); //entidades con mas velocidad
    }

    public boolean verificarFinJuego() {
        return canon.obtenerVidas() <= 0 || formacion.llegoAlSuelo();
    }

    public void sumarPuntaje(int puntos) {
        puntaje += puntos;
    }
}
