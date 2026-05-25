package spaceinvaders;

import java.awt.Graphics2D;
import java.util.List;
import motor.Jugador;

public class CanonJugador extends Jugador {
    private List<Escudo> escudos; // Para detectar colisiones con los escudos
    private ProyectilCanon proyectil;
    private boolean puedeDisparar = true; // Controla si el jugador puede disparar

    public CanonJugador(int x, int y, int ancho, int alto, int vidas) {
        super(x, y, ancho, alto, vidas);
    }

    @Override
    public void mover() {
    }

    public void moverDerecha() {
        x += 5; // Mueve el cañón hacia la derecha
    }
    public void moverIzquierda() {
        x -= 5; // Mueve el cañón hacia la izquierda
    }

    public void disparar(FormacionAlien formacion, List<Escudo> escudos) {
    if (puedeDisparar && proyectil == null) {
        proyectil = new ProyectilCanon(x + ancho / 2, y, formacion, escudos); // Crea un nuevo proyectil en la posición del cañón
        puedeDisparar = false;
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
        
        // ¡Importante! Si el proyectil existe, el cañón también tiene que mandarlo a dibujarse
    if (proyectil != null) {
            proyectil.dibujar(g);
        }
    }
}