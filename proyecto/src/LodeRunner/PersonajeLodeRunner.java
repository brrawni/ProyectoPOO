package LodeRunner;

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
    }
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
    public int getDireccion() { return this.direccion; }
}

class Guardia extends PersonajeLodeRunner{
    private int temporizadorPatrulla = 0;
    private Oro oroGuardado;

    public Guardia(int x, int y, int ancho, int alto, Escenario escenario){
        super(x, y, ancho, alto, escenario);
        super.visible = true;
    }
    @Override
    public boolean detectarColision() {
        enEscalera = false;
        colgadoDeBarra = false;
        boolean colision = true;
        int filaAbajo = (this.y + this.alto) / 32;
        int columnaAbajo = (this.x + this.ancho + 2) / 32;
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
        g.setColor(Color.BLUE);
        g.fillRect(this.x, this.y, this.ancho, this.alto);
    }
    public void perseguir(Heroe heroe){
        double base = (heroe.getX() + heroe.getAncho()/2) - (this.x + this.ancho/2);
        double altura = (heroe.getY() + heroe.getAlto()/2) - (this.y + this.alto/2);
        double hipotenusa = Math.sqrt(Math.pow(base, 2.0) + Math.pow(altura, 2.0));
        if (hipotenusa < 320){ //El rango de vision de los guardias esta definido en 10 bloques, y cada bloque mide 32 pixeles de largo y ancho
            if (base > 0)
                this.direccion = 1; //heroe a la derecha
            else if (base < 0)
                this.direccion = 0; //heroe a la izquierda
            if (this.y > heroe.getY()){
                if (enEscalera || colgadoDeBarra) //si encuentra soga o escalera, sube
                    this.direccion = 2; //heroe esta arriba
            }
            else if (this.y < heroe.getY()){
                if (enEscalera)
                    this.direccion = 3; //heroe esta abajo
            }
            if (detectarColision(heroe)){
                heroe.perderVida();
            }
        }
        else{
            moverAleatoriamente();
        }
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
                columnaDerecha = (this.x + this.ancho + 2) / 32; //anticipamos el siguiente paso del guardia
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
        temporizadorPatrulla++;
        if (temporizadorPatrulla >= 240){ // 60 frames son aproximadamente 1 segundo en el juego, queremos que el guardia cambie de direccion cada 4 segundos
            int nuevaDireccion = (int)(Math.random()*2); //genera numeros de 0 a 1 para cambiar de direccion
            if (!enEscalera && !colgadoDeBarra){
                this.direccion = nuevaDireccion;
            }
            temporizadorPatrulla = 0; //reseteamos el temporizador
        }
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
        boolean colision = true;
        int filaAbajo = (this.y + this.alto) / 32;
        int columnaAbajo = (this.x + this.ancho + 2) / 32;
        int tipoBloque = escenario.obtenerTipoBloqueEn(filaAbajo, columnaAbajo);
        if (tipoBloque == 0)
            colision = false;
        else{
            colision = true;
            if (tipoBloque == 3){
                enEscalera = true;
            }
            else if (tipoBloque == 4){
                colgadoDeBarra = true;
            }

        }
        return colision;
    }
    @Override
    public void dibujar(Graphics2D g){
        g.setColor(Color.RED);
        g.fillRect(this.x, this.y, this.ancho, this.alto);

    }
    public void cavarIzquierda(){

    }
    public void cavarDerecha(){

    }
    public void recolectarOro(Oro oro){
        oro.esRecolectado(null, this);

    }
    public void mover(){
        //No usamos este metodo
    }
    public void mover(int direccion){
        this.direccion = direccion;
        boolean tienePiso = detectarColision();
        if (!tienePiso && !enEscalera && !colgadoDeBarra){
            aplicarGravedad();
            return;
        }
        int columnaIzquierda, columnaDerecha, tipoBloque;
        int filaCentro = (this.y + this.alto/2) / 32; //si el centro de gravedad del heroe choca contra un bloque

        switch (direccion){
            case 0:
                columnaIzquierda = (this.x - 2) / 32; //anticipamos el siguiente paso del heroe
                tipoBloque = escenario.obtenerTipoBloqueEn(filaCentro, columnaIzquierda);
                if (tipoBloque == 0 || tipoBloque == 3 || tipoBloque == 4){
                    this.x -= 2;
                    if (tipoBloque == 3){
                        enEscalera = true;
                    }
                    else{
                        enEscalera = false;
                        colgadoDeBarra = false;
                    }
                }
                else{
                    this.x -= 0; //no se puede atravesar una pared.
                }
                break;
            case 1:
                columnaDerecha = (this.x + this.ancho + 2) / 32; //anticipamos el siguiente paso del guardia
                tipoBloque = escenario.obtenerTipoBloqueEn(filaCentro, columnaDerecha);
                if (tipoBloque == 0 || tipoBloque == 3 || tipoBloque == 4){
                    this.x += 2;
                    if (tipoBloque == 3){
                        enEscalera = true;
                    }
                    else{
                        enEscalera = false;
                        colgadoDeBarra = false;
                    }
                }
                else{
                    this.x -= 0; //no se puede atravesar la pared
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
                break;
        }
    }
    public void perderVida(){
        vidas--;
    }
}