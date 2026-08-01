package Pong;


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
        boolean pelotaVieneHaciaCPU = paletaCPU.isJugador1()
                ? pelota.getVelocidadX() < 0
                : pelota.getVelocidadX() > 0;

        if (pelotaVieneHaciaCPU) moverHaciaPelota(pelota);
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
