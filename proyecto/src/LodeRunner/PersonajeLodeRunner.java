package LodeRunner;

import motor.Entidad;

import java.awt.*;
import java.util.HashMap;

public abstract class PersonajeLodeRunner extends Entidad{
    protected boolean enEscalera = false;
    protected boolean colgadoDeBarra = false;
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
    public abstract boolean estaEnPozo();
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

        // 2. SENSOR DE CUERPO: Verificar si se esta tocando una escalera o barra con el torso
        int filaCentro = (this.y + this.alto / 2 ) / 32;
        int columnaCentro = (this.x + this.ancho / 2 ) / 32;
        int bloqueCuerpo = escenario.obtenerTipoBloqueEn(filaCentro, columnaCentro);

        if (bloqueCuerpo == 3) {
            enEscalera = true;
        } else if (bloqueCuerpo == 4) {
            colgadoDeBarra = true;
        }

        // 3. SENSOR DE PIES: verificar si tenemos suelo firme debajo para no caer por gravedad
        int filaAbajo = (this.y + this.alto) / 32;
        int bloquePies = escenario.obtenerTipoBloqueEn(filaAbajo, columnaCentro);

        if (bloquePies == 0 || bloquePies == 4) {
            hayPiso = false; // Hay aire, caemos
        } else {
            hayPiso = true; // Hay piso (ladrillo, escalera, etc.)
            // Si el piso debajo de los pies es la punta de una escalera, también cuenta
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
        if (hipotenusa < 32*7){ //El rango de vision de los guardias esta definido en 10 bloques, y cada bloque mide 32 pixeles de largo y ancho
            if (base > 0)
                this.direccion = 1; //heroe a la derecha
            else if (base < 0)
                this.direccion = 0; //heroe a la izquierda
            if (this.y > heroe.getY()){
                if (enEscalera){ //si encuentra soga o escalera, sube
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
                    //return; //este return sirve para que en cada frame no se "Pise" la direccion actual del guardia
                }
                else if (colgadoDeBarra && Math.abs(base) <= 16 && bloqueAbajo != 1) {
                    this.direccion = 3; // Le mandamos la orden de bajar
                }
            }
            if (detectarColision(heroe)){
                if (!heroe.isArribaDeGuardia())
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
        if (tienePiso && !enEscalera && !colgadoDeBarra) {
            int filaSuelo = (this.y + this.alto) / 32;
            this.y = (filaSuelo * 32) - this.alto; // Aterrizaje perfecto
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
                        colgadoDeBarra = false;
                    }
                    else if (tipoBloque == 4){
                        this.y = ((this.y + 16) / 32) * 32; //alinear el eje y
                        enEscalera = false;
                        colgadoDeBarra = true;
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
                        colgadoDeBarra = false;
                    }
                    else if (tipoBloque == 4){
                        this.y = ((this.y + 16) / 32) * 32; //alinear el eje y
                        colgadoDeBarra = true;
                        enEscalera = false;
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
                    int filaPies = (this.y + this.alto - 1) / 32;
                    int colCentro = this.x / 32;
                    int bloquePies = escenario.obtenerTipoBloqueEn(filaPies, colCentro);

                    // Si al bajar, los pies entraron adentro de un ladrillo sólido (1)
                    if (bloquePies == 1) {
                        // Lo empujamos hacia arriba forzando su Y para que quede clavado perfecto sobre el piso
                        this.y = (filaPies * 32) - this.alto;
                    }
                }
                else{
                    aplicarGravedad();
                }
                break;
        }
    }
    public void moverAleatoriamente(){
        temporizadorPatrulla++;
        int nuevaDireccion = (int)(Math.random()*2); //genera numeros de 0 a 1 para cambiar de direccion
        if (enEscalera)
            temporizadorPatrulla += 3;
        if (temporizadorPatrulla >= 60*2){ // 60 frames son aproximadamente 1 segundo en el juego, queremos que el guardia cambie de direccion cada 4 segundos

            this.direccion = nuevaDireccion;
            if (enEscalera){
                int colCentro = (this.x + this.ancho / 2) / 32;
                this.direccion = (int)(Math.random() * 2) + 2; // 2 o 3

                if (this.direccion == 2) {
                    // Para subir: verificar que no haya techo sólido
                    int filaArriba = (this.y - 2) / 32;
                    if (escenario.obtenerTipoBloqueEn(filaArriba, colCentro) == 1 || escenario.obtenerTipoBloqueEn(filaArriba, colCentro) == 0) {
                        this.direccion = nuevaDireccion;
                    }
                } else {
                    // Para bajar: verificar que haya escalera debajo de los pies
                    int filaPies = (this.y + this.alto) / 32;
                    int bloqueAbajo = escenario.obtenerTipoBloqueEn(filaPies, colCentro);
                    if (bloqueAbajo != 3) { // si no hay escalera abajo, no puede bajar
                        this.direccion = nuevaDireccion;
                    }
                }
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
    public boolean estaEnPozo(){
        HashMap<Point, Long> hashMap = escenario.obtenerMapaPozos();
        for (Point p : hashMap.keySet()){
            if ((this.x + this.ancho/2)/32 == p.x && (this.y + this.alto/2)/32 == p.y){ //si el torso del guardia esta dentro del pozo, es valido
                return true;
            }
        }
        return false;
    }
    public void reaparecer(){
        boolean respawnExitoso = false;

        while (!respawnExitoso) {
            // Asumiendo tu mapa grande de 25x16
            int colRand = (int)(Math.random() * 25);

            // Buscamos hasta la fila 14, para que al revisar "filaRand + 1" (el piso) no nos salgamos del mapa
            int filaRand = (int)(Math.random() * 15);

            // Al medir 32x32, solo nos importan 2 bloques: el cuerpo y el piso
            int bloqueCuerpo = escenario.obtenerTipoBloqueEn(filaRand, colRand);
            int bloquePiso = escenario.obtenerTipoBloqueEn(filaRand + 1, colRand);

            // Regla: Cuerpo en el aire (0) y apoyado en un ladrillo (1) o escalera (3)
            if (bloqueCuerpo == 0 && (bloquePiso == 1 || bloquePiso == 3)) {

                // ¡Encontramos el lugar perfecto! Lo teletransportamos.
                this.x = colRand * 32;
                this.y = filaRand * 32;

                // Cortamos el bucle infinito
                respawnExitoso = true;
            }
        }
    }
    public Oro getOroGuardado(){
        return oroGuardado;
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
    private boolean arribaDeGuardia;

    public Heroe(int x, int y, int ancho, int alto, int vidas , Escenario escenario){
        super(x, y, ancho, alto, escenario);
        this.vidas = vidas;
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

        if (bloquePies == 0 || bloquePies == 4 ) {
            hayPiso = false; // Hay aire, nos caemos
        } else {
            hayPiso = true; // Hay piso (ladrillo, escalera, etc.)
            // Si el piso debajo de mis pies es la punta de una escalera, también cuenta
            if (bloquePies == 3) {
                enEscalera = true;
            }
        }
        if (arribaDeGuardia){
            hayPiso = true;
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
            escenario.romperBloque(filaAbajo, columnaCentro - 1);
    }
    public void cavarDerecha(){
        int columnaCentro = (this.x + this.ancho / 2) / 32;
        int filaAbajo = (this.y + this.alto) / 32;
        if (escenario.obtenerTipoBloqueEn(filaAbajo, columnaCentro + 1) == 1)
            escenario.romperBloque(filaAbajo, columnaCentro + 1);
    }
    @Override
    public void mover(){

        boolean tienePiso = detectarColision();
        if (!tienePiso && !enEscalera && !colgadoDeBarra){
            aplicarGravedad();
            return;
        }
        if (tienePiso && !enEscalera && !colgadoDeBarra) {
            int filaSuelo = (this.y + this.alto) / 32;
            this.y = (filaSuelo * 32) - this.alto; // Aterrizaje perfecto
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
                        colgadoDeBarra = false;
                    }
                    else if (tipoBloque == 4){
                        this.y = ((this.y + 16) / 32) * 32; //alinear el eje y
                        colgadoDeBarra = true;
                        enEscalera = false;
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
                        colgadoDeBarra = false;
                    }
                    else if (tipoBloque == 4){
                        enEscalera = false;
                        colgadoDeBarra = true;
                        this.y = ((this.y + 16) / 32) * 32; // alinear el eje y
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

                    // para que el heroe no se entierre cuando baja una escalera
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
    public boolean estaEnPozo(){
        HashMap<Point, Long> hashMap = escenario.obtenerMapaPozos();
        for (Point p : hashMap.keySet()){
            if ((this.x + this.ancho/2)/32 == p.x && (this.y + this.alto/2)/32 == p.y){ //si el torso del heroe esta dentro del pozo, es valido
                return true;
            }
        }
        return false;
    }
    public void recolectarOro(Oro oro){
        oro.esRecolectado(null, this);
    }
    public void perderVida(){
        vidas--;
    }
    public int getVidas(){ return vidas; }
    public void incrementarVida(){ vidas++; }
    public void setDireccion(int direccion){
        super.direccion = direccion;
    }
    public void setArribaDeGuardia(boolean arribaDeGuardia){ this.arribaDeGuardia = arribaDeGuardia; }
    public boolean isArribaDeGuardia(){ return arribaDeGuardia; }
}