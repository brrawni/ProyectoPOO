package spaceinvaders;

public class Nivel {
    private int numero;
    private float velocidadAlien;
    private int filaInicio;

    public Nivel(int numero) {
        this.numero = numero;
        configurar(numero);
    }
    /*
     Define los parámetros de cada nivel.
     A mayor nivel, más velocidad y los aliens empiezan más abajo.
     */  
    private void configurar(int numero) {
        switch(numero){
            case 1:
                velocidadAlien = 1.0f;
                filaInicio = 1;
                break;
            case 2:
                velocidadAlien = 1.5f;
                filaInicio = 2;
                break;
            case 3:
                velocidadAlien = 2.0f;
                filaInicio = 3;
                break;
            default:
                velocidadAlien = 1.0f + (numero - 0.5f);;
                filaInicio = Math.min(numero, 5); // No empezar más abajo del nivel 5
                break;
        }
    }

    public void cargar(){
        configurar(numero);
    }

    public int obtenerNumero() {
        return numero;
    }
    public float obtenerVelocidadAlien() {
        return velocidadAlien;
    }
    public int obtenerFilaInicio() {
        return filaInicio;
    }
}