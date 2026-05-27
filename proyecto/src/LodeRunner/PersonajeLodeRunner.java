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
        this.x = ((this.x + 16)/32)*32;
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
    private boolean persiguiendo;
    private boolean heroeArriba;

    public Guardia(int x, int y, int ancho, int alto, Escenario escenario){
        super(x, y, ancho, alto, escenario);
        super.visible = true;
    }
    @Override
    public boolean detectarColision() {
        // 1. Olvidar si estaba en escalera o colgado de barra
        enEscalera = false;
        colgadoDeBarra = false;
        boolean hayPiso = true;

        // 2. SENSOR DE CUERPO: ¿Estamos tocando una escalera o barra con el torso?
        int filaCentro = (this.y + this.alto / 2 ) / 32;
        int columnaCentro = (this.x + this.ancho / 2 ) / 32;
        int bloqueCuerpo = escenario.obtenerTipoBloqueEn(filaCentro, columnaCentro);

        if (bloqueCuerpo == 3) {
            enEscalera = true;
        } else if (bloqueCuerpo == 4) {
            colgadoDeBarra = true;
        }

        // 3. SENSOR DE PIES: ¿Tenemos suelo firme debajo para no caer por gravedad?
        int filaAbajo = (this.y + this.alto) / 32;
        int bloquePies = escenario.obtenerTipoBloqueEn(filaAbajo, columnaCentro);

        if (bloquePies == 0) {
            hayPiso = false; // Hay aire, nos caemos
        } else {
            hayPiso = true; // Hay piso (ladrillo, escalera, etc.)
            // Si el piso debajo de mis pies es la punta de una escalera, también cuenta
            if (bloquePies == 3) {
                enEscalera = true;
            }
        }

        return hayPiso;
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
        if (hipotenusa < 64){ //El rango de vision de los guardias esta definido en 10 bloques, y cada bloque mide 32 pixeles de largo y ancho
            persiguiendo = true;
            if (base > 0)
                this.direccion = 1; //heroe a la derecha
            else if (base < 0)
                this.direccion = 0; //heroe a la izquierda
            if (this.y > heroe.getY()){
                if (enEscalera || colgadoDeBarra){ //si encuentra soga o escalera, sube
                    this.direccion = 2; //heroe esta arriba

                }
            }
            else if (this.y < heroe.getY()){
                // Le damos "ojos" a la IA para mirar los pies
                int filaPies = (this.y + this.alto) / 32;
                int colCentro = (this.x + this.ancho / 2) / 32;
                int bloqueAbajo = escenario.obtenerTipoBloqueEn(filaPies, colCentro);
                int bloqueEnPies = escenario.obtenerTipoBloqueEn(filaPies + 1, colCentro);
                // Solo le da la orden de bajar (3) si NO hay un ladrillo.
                // Si hay un ladrillo sólido (1), esto da falso.
                // Al dar falso, el guardia mantiene la dirección horizontal (0 o 1) y sigue de largo.
                if (bloqueEnPies == 3 || bloqueAbajo == 3) {
                    this.direccion = 3;
                    return; //este return sirve para que en cada frame no se "Pise" la direccion actual del guardia
                }
            }
            if (detectarColision(heroe)){
                heroe.perderVida();
            }
        }
        else{
            persiguiendo = false;
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
                    this.x = ((this.x + 16)/32)*32; //para que no desfase de la escalera
                    this.y -= 2;
                }
                break;
            case 3:
                if (enEscalera || colgadoDeBarra){
                    this.x = ((this.x + 16)/32)*32;
                    this.y += 2;
                }
                else{
                    aplicarGravedad();
                }
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
        if (oroGuardado == null && detectarColision(oro)){
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
        // 1. Olvidar si estaba en escalera o colgado de barra
        enEscalera = false;
        colgadoDeBarra = false;
        boolean hayPiso = true;

        // 2. SENSOR DE CUERPO: ¿Estamos tocando una escalera o barra con el torso?
        int filaCentro = (this.y + this.alto / 2 ) / 32;
        int columnaCentro = (this.x + this.ancho / 2 ) / 32;
        int bloqueCuerpo = escenario.obtenerTipoBloqueEn(filaCentro, columnaCentro);

        if (bloqueCuerpo == 3) {
            enEscalera = true;
        } else if (bloqueCuerpo == 4) {
            colgadoDeBarra = true;
        }

        // 3. SENSOR DE PIES: ¿Tenemos suelo firme debajo para no caer por gravedad?
        int filaAbajo = (this.y + this.alto) / 32;
        int bloquePies = escenario.obtenerTipoBloqueEn(filaAbajo, columnaCentro);

        if (bloquePies == 0) {
            hayPiso = false; // Hay aire, nos caemos
        } else {
            hayPiso = true; // Hay piso (ladrillo, escalera, etc.)
            // Si el piso debajo de mis pies es la punta de una escalera, también cuenta
            if (bloquePies == 3) {
                enEscalera = true;
            }
        }
        return hayPiso;
    }
    @Override
    public void dibujar(Graphics2D g){
        g.setColor(Color.RED);
        g.fillRect(this.x, this.y, this.ancho, this.alto);

    }
    public void cavarIzquierda(){
        int columnaCentro = (this.x + this.ancho / 2) / 32;
        int filaAbajo = (this.y + this.alto) / 32;
        if (escenario.obtenerTipoBloqueEn(filaAbajo, columnaCentro - 1) == 1)
            escenario.setBloque(filaAbajo, columnaCentro - 1, 0);
    }
    public void cavarDerecha(){
        int columnaCentro = (this.x + this.ancho / 2) / 32;
        int filaAbajo = (this.y + this.alto) / 32;
        if (escenario.obtenerTipoBloqueEn(filaAbajo, columnaCentro + 1) == 1)
            escenario.setBloque(filaAbajo, columnaCentro + 1, 0);
    }
    public void recolectarOro(Oro oro){
        oro.esRecolectado(null, this);
    }
    @Override
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
                    this.x -= 4;
                    if (tipoBloque == 3){
                        enEscalera = true;
                    }
                    else{
                        enEscalera = false;
                        colgadoDeBarra = false;
                    }
                }
                else{
                    this.x += 0; //no se puede atravesar la pared
                }
                break;
            case 1:
                columnaDerecha = (this.x + this.ancho + 2) / 32; //anticipamos el siguiente paso del guardia
                tipoBloque = escenario.obtenerTipoBloqueEn(filaCentro, columnaDerecha);
                if (tipoBloque == 0 || tipoBloque == 3 || tipoBloque == 4){
                    this.x += 4;
                    if (tipoBloque == 3){
                        enEscalera = true;
                    }
                    else{
                        enEscalera = false;
                        colgadoDeBarra = false;
                    }
                }
                else{
                    this.x += 0; //no se puede atravesar la pared
                }
                break;
            case 2:
                if (enEscalera){
                    this.x = ((this.x + 16)/32)*32; //para que no desfase de la escalera
                    // PROTECCIÓN DE TECHO (CASCO)
                    int filaCabeza = (this.y - 2) / 32;
                    int colCentroAlineado = this.x / 32;
                    int bloqueArriba = escenario.obtenerTipoBloqueEn(filaCabeza, colCentroAlineado);

                    // Si arriba hay aire (0) o escalera (3), sube. Si hay ladrillo, choca y no hace nada.
                    if (bloqueArriba != 1) {
                        this.y -= 2;
                    }
                }
                break;
            case 3:
                if (enEscalera || colgadoDeBarra) {
                    this.x = ((this.x + 16) / 32) * 32; // Alineación

                    // PROTECCIÓN ANTI-ENTIERRO
                    int filaSuelo = (this.y + this.alto + 2) / 32;
                    int colSuelo = this.x / 32;
                    int bloqueAbajo = escenario.obtenerTipoBloqueEn(filaSuelo, colSuelo);

                    if (bloqueAbajo != 1) {
                        this.y += 2; // Si no hay ladrillo, sigue bajando
                    }
                }
                else
                    aplicarGravedad();
                break;
            default:
                break;
        }
    }
    public void perderVida(){
        vidas--;
    }
    public void setDireccion(int direccion){
        super.direccion = direccion;
    }
}