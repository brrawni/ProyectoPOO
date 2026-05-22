Package LodeRunner;

import LodeRunner.Escenario;
import LodeRunner.Oro;
import motor.Entidad;

public abstract class PersonajeLodeRunner extends Entidad{
    protected boolean subiendoEscalera;
    protected boolean colgadoDeBarra;
    protected boolean enCaidaLibre;
    protected boolean inmovilizado;

    public void aplicarGravedad(){

    }
    public abstract void mover();
}

class Guardia extends PersonajeLodeRunner{
    private int direccion; // 0: izquierda, 1: derecha, 2: arriba, 3: abajo
    private Oro oroGuardado;

    public void perseguir(Heroe heroe, Escenario esc){

    }
    public void mover(){
        if (direccion = 0){
            //mover izquierda
        }
        else if (direccion = 1){
            //mover derecha
        }
        else if (direccion = 2){
            //mover arriba
        }
        else if (direccion = 3){
            //mover abajo
        }
    }
    public void moverAleatoriamente(Escenario esc){

    }
    
    public void robarOro(Oro oro){
        oroGuardado = oro;
    }
    public void soltarOro(){
        oroGuardado = null;
    }
}

class Heroe extends PersonajeLodeRunner{
    private int direccion; // 0: izquierda, 1: derecha, 2: arriba, 3: abajo
    private int vidas;

    public void cavarIzquierda(){

    }
    public void cavarDerecha(){

    }
    public void recolectarOro(Oro oro){

    }
    public void mover(){
        if (direccion = 0){
            //mover izquierda
        }
        else if (direccion = 1){
            //mover derecha
        }
        else if (direccion = 2){
            //mover arriba
        }
        else if (direccion = 3){
            //mover abajo
        }
    }
    public void perderVida(){
        vidas--;
    }
}