package Pong;

/**
 * IA para el modo Humano vs CPU.
 *
 * Diseño según la consigna: la dificultad no se elige, se experimenta.
 * A medida que la pelota acelera (más rebotes), la IA empieza a no poder seguirla
 * porque su velocidad de paleta es fija y menor a la velocidad máxima de la pelota.
 *
 * Funcionamiento:
 * - La IA mueve la paleta hacia la posición Y de la pelota cada frame.
 * - Pero su velocidad está limitada a VELOCIDAD_IA (3.0), menor que la velocidad
 *   inicial de la pelota (4.0) y mucho menor que la velocidad máxima tras varios rebotes.
 * - Además, solo reacciona cuando la pelota viene hacia ella (velocidadX > 0),
 *   lo que le da al jugador ventana para engañarla.
 * - Tiene un pequeño margen de error aleatorio para que no sea perfecta ni al inicio.
 */
public class IA {
    private Paleta paletaCPU;

    // Margen amplio: la IA tolera estar más lejos de la pelota antes de moverse
    private static final int MARGEN = 10;

    // Error aleatorio que se recalcula frecuentemente para que falle seguido
    private int errorY = 0;
    private int contadorFrames = 0;

    private final int altoVentana;

    public IA(Paleta paletaCPU, int altoVentana) {
        this.paletaCPU   = paletaCPU;
        this.altoVentana = altoVentana;
    }
    public void actualizar(Pelota pelota) {
        if (pelota.getVelocidadX() > 0) moverHaciaPelota(pelota);
        else moverHaciaCentro(altoVentana);
    }

    // Mueve la paleta hacia la posición Y de la pelota (con velocidad limitada y error)
    private void moverHaciaPelota(Pelota pelota) {
        // Recalcular error cada 20 frames (~0.3 segundos): la IA se equivoca seguido
        contadorFrames++;
        if (contadorFrames >= 28) {
            contadorFrames = 0;
            // Error de ±35px: apunta bastante fuera del centro de la pelota
            errorY = (int) (Math.random() * 44 - 22);
        }

        int centroPaleta = paletaCPU.obtenerY() + paletaCPU.obtenerAlto() / 2;
        int objetivoY = pelota.obtenerY() + errorY;
        int diferencia = objetivoY - centroPaleta;

        // Mover solo si la diferencia supera el margen (evita vibración)
        if (diferencia > MARGEN) {
            paletaCPU.moverAbajo();
        } else if (diferencia < -MARGEN) {
            paletaCPU.moverArriba();
        }
    }

    // Cuando la pelota se aleja, la IA deriva hacia el centro de la pantalla
    private void moverHaciaCentro(int altoVentana) {
        int centroPaleta = paletaCPU.obtenerY() + paletaCPU.obtenerAlto() / 2;
        int centroVentana = altoVentana / 2;
        if (centroPaleta < centroVentana - MARGEN) {
            paletaCPU.moverAbajo();
        } else if (centroPaleta > centroVentana + MARGEN) {
            paletaCPU.moverArriba();
        }
    }
}