package LodeRunner;

import LodeRunner.Escenario;
import LodeRunner.Oro;
import motor.Entidad;

import java.awt.*;

public abstract class PersonajeLodeRunner extends Entidad{
    protected boolean subiendoEscalera;
    protected boolean colgadoDeBarra;
    protected boolean enCaidaLibre;
    protected boolean inmovilizado;

    public PersonajeLodeRunner(int x, int y, int ancho, int alto){
        super(x, y, ancho, alto);
        super.visible = true;
    }
    public void aplicarGravedad(){

    }
    public abstract void mover();
    public int getX(){
        return super.x;
    }
    public int getY(){
        return super.y;
    }
    public int getAlto(){
        return super.alto;
    }
    public int getAncho(){
        return super.ancho;
    }
}

class Guardia extends PersonajeLodeRunner{
    private int direccion; // 0: izquierda, 1: derecha, 2: arriba, 3: abajo
    private Oro oroGuardado;

    public Guardia(int x, int y, int ancho, int alto){
        super(x, y, ancho, alto);
        super.visible = true;
    }
    @Override
    public boolean detectarColision() {
        return false;
    }
    @Override
    public void dibujar(Graphics2D g){

    }
    public void perseguir(Heroe heroe, Escenario esc){

    }
    public void mover(){
        if (direccion == 0){
            //mover izquierda
        }
        else if (direccion == 1){
            //mover derecha
        }
        else if (direccion == 2){
            //mover arriba
        }
        else if (direccion == 3){
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
    @Override
    public int getX(){
        return super.getX();
    }
    @Override
    public int getY(){
        return super.getY();
    }
    @Override
    public int getAlto(){
        return super.getAlto();
    }
    @Override
    public int getAncho(){
        return super.getAncho();
    }
}

class Heroe extends PersonajeLodeRunner{
    private int direccion; // 0: izquierda, 1: derecha, 2: arriba, 3: abajo
    private int vidas;

    public Heroe(int x, int y, int ancho, int alto){
        super(x, y, ancho, alto);
        super.visible = true;
    }
    @Override
    public boolean detectarColision() {
        return false;
    }
    @Override
    public void dibujar(Graphics2D g){

    }
    public void cavarIzquierda(){

    }
    public void cavarDerecha(){

    }
    public void recolectarOro(Oro oro){

    }
    public void mover(){
        if (direccion == 0){
            //mover izquierda
        }
        else if (direccion == 1){
            //mover derecha
        }
        else if (direccion == 2){
            //mover arriba
        }
        else if (direccion == 3){
            //mover abajo
        }
    }
    public void perderVida(){
        vidas--;
    }
}