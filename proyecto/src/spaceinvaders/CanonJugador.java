package spaceinvaders;

import java.awt.Graphics2D;
import java.util.List;
import motor.Jugador;

public class CanonJugador extends Jugador {
    private SpaceInvaders juego; // Referencia al juego para acceder a la formación de aliens
    private List<Escudo> escudos; // Para detectar colisiones con los escudos
    private ProyectilCanon proyectil;
    private boolean puedeDisparar = true; // Controla si el jugador puede disparar
    private FormacionAlien formacion; // Para acceder a los aliens y detectar colisiones
    private int ticksMovimiento = 0; // Para controlar la velocidad de movimiento del cañón
    private int vida; // Vida del jugador

    public CanonJugador(int x, int y, int ancho, int alto, int vidas) {
        super(x, y, ancho, alto, vidas);
        this.vida = vidas;
    }

    @Override
    public void mover() {
    }

    
    public void moverDerecha() {
        ticksMovimiento++;
        if (ticksMovimiento >= 5) { // Controla la velocidad de movimiento
            x += 5; // Mueve el cañón hacia la derecha
            ticksMovimiento = 0;
        }
    } // Mueve el cañón hacia la derecha
    public void moverIzquierda() { 
        ticksMovimiento++;
        if (ticksMovimiento >= 5) { // Controla la velocidad de movimiento
            x -= 5; // Mueve el cañón hacia la izquierda
            ticksMovimiento = 0;
        }
     }// Mueve el cañón hacia la izquierda

    public void setEscudos(List<Escudo> escudos){ this.escudos = escudos; }
    public void setJuego(SpaceInvaders juego) { this.juego = juego; } 
    public void setFormacion(FormacionAlien formacion) { this.formacion = formacion; }
    public void setVida(int vida) { this.vida = vida; }


    public void disparar() {
    if (puedeDisparar && proyectil == null) {
        proyectil = new ProyectilCanon(x + ancho / 2, y, formacion, escudos, juego); // Crea un nuevo proyectil en la posición del cañón
        puedeDisparar = false;
        juego.getNaveNodriza().incrementarDisparos(); // Incrementa el contador de disparos para la nave nodriza
    }
}

    public void proyectilDestruido() {
        proyectil = null; // El proyectil ha sido destruido
        puedeDisparar = true; // El jugador puede disparar nuevamente
    }


    public void actualizar() {
        if (proyectil != null) {
            proyectil.actualizar(); // actualiza posición y colisiones
            if (!proyectil.estaActivo()) {
                proyectil = null;
                puedeDisparar = true;
            }
        }
    }

    @Override
    public boolean detectarColision() {
        return false;
    }
    

    @Override
    public void dibujar(Graphics2D g) {
        // Lógica para dibujar el cañón del jugador
    g.fillRect(x, y, ancho, alto); 
        
        // Si el proyectil existe, el cañón también tiene que mandarlo a dibujarse
    if (proyectil != null) {
            proyectil.dibujar(g);
        }
    }

}