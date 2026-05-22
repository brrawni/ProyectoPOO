Package LodeRunner;

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

    public void perseguir(Heroe heroe, Escenario esc){

    }
    public void mover(){

    }
}

class Heroe extends PersonajeLodeRunner{
    private int vidas;

    public void cavarIzquierda(){

    }
    public void cavarDerecha(){

    }
    public void recolectarOro(Oro oro){

    }
    public void mover(){

    }
    public void perderVida(){
        vidas--;
    }
}