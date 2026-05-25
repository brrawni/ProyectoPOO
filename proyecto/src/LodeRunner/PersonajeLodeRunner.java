package LodeRunner;

import LodeRunner.Escenario;
import LodeRunner.Oro;
import motor.Entidad;

import java.awt.*;

public abstract class PersonajeLodeRunner extends Entidad{
    protected boolean enEscalera = false;
    protected boolean colgadoDeBarra = false;
    protected boolean enCaidaLibre = false;
    protected boolean inmovilizado = false;
    protected int direccion; //0: izquierda, 1: derecha, 2: arriba, 3: abajo
    protected Escenario escenario;

    public PersonajeLodeRunner(int x, int y, int ancho, int alto, Escenario escenario){
        super(x, y, ancho, alto);
        super.visible = true;
        this.escenario = escenario;
    }
    public void aplicarGravedad(){
        this.y += 4; // Cae por gravedad
        this.direccion = 3; // Mirando hacia abajo
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
    private Oro oroGuardado;

    public Guardia(int x, int y, int ancho, int alto, Escenario escenario){
        super(x, y, ancho, alto, escenario);
        super.visible = true;
    }
    @Override
    public boolean detectarColision() {
        boolean colision = true;
        int filaAbajo = (this.y + this.alto) / 32;
        int columnaAbajo = (this.x + this.ancho) / 32;
        int tipoBloque = escenario.obtenerTipoBloqueEn(filaAbajo, columnaAbajo);
        if (tipoBloque == 0)
            colision = false;
        else{
            colision = true;
            if (tipoBloque == 3){
                enEscalera = true;
                direccion = 3;
            }
            else if (tipoBloque == 4){
                colgadoDeBarra = true;
                direccion = 3;
            }

        }
        return colision;
    }
    public boolean detectarColision(Entidad entidad){
        return new Rectangle(this.x, this.y, this.ancho, this.alto).intersects(new Rectangle(entidad.obtenerX(), entidad.obtenerY(), entidad.obtenerAncho(), entidad.obtenerAlto()));
    }
    @Override
    public void dibujar(Graphics2D g){

    }
    public void perseguir(Heroe heroe, Escenario esc){

    }
    public void mover(){
        boolean tienePiso = detectarColision();
        if (!tienePiso && !enEscalera && !colgadoDeBarra){
            aplicarGravedad();
            return;
        }
        int columnaIzquierda, columnaDerecha, tipoBloque;
        int filaCentro = (this.y + this.alto/2) / 32; //si el centro de gravedad del guardia choca contra un bloque

        switch (direccion){
            case 0:
                columnaIzquierda = (this.x - 2) / 32; //anticipamos el siguiente paso del guardia
                tipoBloque = escenario.obtenerTipoBloqueEn(filaCentro, columnaIzquierda);
                if (tipoBloque == 0 || tipoBloque == 3 || tipoBloque == 4){
                    this.x -= 2;
                    if (tipoBloque == 3){
                        enEscalera = true;
                        direccion = 2;
                    }
                    else{
                        enEscalera = false;
                        colgadoDeBarra = false;
                    }
                }
                else{
                    direccion = 1; //cambiar de direccion a la derecha
                }
                break;
            case 1:
                columnaDerecha = (this.x + 2) / 32; //anticipamos el siguiente paso del guardia
                tipoBloque = escenario.obtenerTipoBloqueEn(filaCentro, columnaDerecha);
                if (tipoBloque == 0 || tipoBloque == 3 || tipoBloque == 4){
                    this.x += 2;
                    if (tipoBloque == 3){
                        enEscalera = true;
                        direccion = 2;
                    }
                    else{
                        enEscalera = false;
                        colgadoDeBarra = false;
                    }
                }
                else{
                    direccion = 0; //cambiar de direccion a la izquierda
                }
                break;
            case 2:
                if (enEscalera){
                    this.y -= 2;
                }
                break;
            case 3:
                if (enEscalera || colgadoDeBarra)
                    this.y += 2;
                else
                    aplicarGravedad();
                break;
        }
    }
    public void moverAleatoriamente(){

    }
    
    public void robarOro(Oro oro){
        if (oro != null && detectarColision(oro)){
            oroGuardado = oro;
            oroGuardado.esRecolectado(this, null);
        }
    }
    public void soltarOro(){
        oroGuardado.serSoltado();
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
    private int vidas;

    public Heroe(int x, int y, int ancho, int alto, Escenario escenario){
        super(x, y, ancho, alto, escenario);
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