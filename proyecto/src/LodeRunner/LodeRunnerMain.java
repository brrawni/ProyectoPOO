package LodeRunner;

import launcher.Launcher;
import motor.Videojuego;
import ranking.EntradaRanking;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
//Cada sprite mide 16x16

public class LodeRunnerMain extends Videojuego implements KeyListener{
    private GestorPantallas gestorPantallas;
    //Imagen para cancelar parpadeos
    private BufferedImage buffer;
    //configuracion del juego
    private ConfiguracionLR config;
    //componentes del juego
    private Escenario escenario;
    private Heroe heroe;
    private ArrayList<Guardia> guardias;
    private ArrayList<Oro> lingotes;
    private int vidasHeroe;
    //variables de control
    private boolean mirandoIzq;
    private boolean mirandoDer;
    private int lingotesRestantes;
    //temporizador
    private int temporizador;
    private int frames = 0;
    private int puntajePorMin = 300;
    //atributos para ranking
    private RankingLR ranking;
    private boolean rankingGuardado = false;
    private String nombreJugador = "";
    private boolean enterPresionado = false;


    public LodeRunnerMain(GestorPantallas gestorPantallas) {
        super("Lode Runner - UNLPam edition", 800, 600);
        config = new ConfiguracionLR();
        ranking = new RankingLR();
        this.gestorPantallas = gestorPantallas;
    }

    @Override
    public void gameStartup() {
        buffer = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        enEjecucion = true;
        canvas.addKeyListener(this);
        canvas.setFocusable(true);
        canvas.requestFocus();
        canvas.requestFocusInWindow();
        nivelActual = 1;
        vidasHeroe = 5;
        config.cargar();
        iniciarNivel();
    }
    public void iniciarNivel(){
        escenario = new Escenario(32, 32, nivelActual); // Ejemplo de creación del escenario
        heroe = new Heroe(32, 32, 32, 32, vidasHeroe, escenario); // Ejemplo de creación del héroe
        guardias = new ArrayList<>();
        lingotes = new ArrayList<>();
        //iniciar temporizador en 3 minutos
        temporizador = 180;
        // 1. Spawneo inteligente de Guardias
        guardias.clear(); //para no sobrecargar la memoria ram
        int guardiasCreados = 0;
        int limiteGuardias = 0;
        for (int i = 0; i < nivelActual; i++){
            limiteGuardias += 2;
        }
        while (guardiasCreados < limiteGuardias) {
            int colRand = (int)(Math.random() * 14); // Columnas de 0 a 13
            int filaRand = (int)(Math.random() * 8); // Filas de 0 a 7 (evitamos el fondo)

            // Verificamos los 3 bloques involucrados en el cuerpo del guardia
            int bloqueCabeza = escenario.obtenerTipoBloqueEn(filaRand, colRand);
            int bloquePies = escenario.obtenerTipoBloqueEn(filaRand + 1, colRand);
            int bloquePiso = escenario.obtenerTipoBloqueEn(filaRand + 2, colRand);

            // Regla: Cabeza en el aire, pies en el aire, y apoyado en un ladrillo (1) o escalera (3)
            if (bloqueCabeza == 0 && bloquePies == 0 && (bloquePiso == 1 || bloquePiso == 3)) {
                guardias.add(new Guardia(colRand * 32, filaRand * 32, 32, 32, escenario));
                guardiasCreados++;
            }
        }
        //2.Spawneo de lingotes
        //Añadir lingotes
        int orosCreados = 0;
        while (orosCreados < 15) {
            int columnaRand = (int)(Math.random() * 14); // Columnas del mapa
            int filaRand = (int)(Math.random() * 9);    // Filas del mapa (sin llegar al fondo)

            int bloqueActual = escenario.obtenerTipoBloqueEn(filaRand, columnaRand);
            int bloqueDeAbajo = escenario.obtenerTipoBloqueEn(filaRand + 1, columnaRand);

            //El casillero actual debe ser Aire (0) y el de abajo Ladrillo (1)
            if (bloqueActual == 0 && bloqueDeAbajo == 1) {
                lingotes.add(new Oro(columnaRand * 32, filaRand * 32, 16, 16));
                orosCreados++;
            }
        }
    }
    @Override
    public void gameUpdate(double delta) {
                if (!enEjecucion)
                    return;
                if (config.isPantallaCompleta()){
                    super.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                    Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize(); //esto nos ayuda a obtener la resolucion de la pantalla en la que se eejcuta el juego

                    canvas.setSize(pantalla);
                    canvas.setPreferredSize(pantalla);
                }else{
                    frame.setSize(800, 600);
                    canvas.setSize(800, 600);
                    frame.setLocationRelativeTo(null);
                }
                frames++;
                if (frames == 30){ // 35 frames son aproximadamente un segundo
                    frames = 0;
                    temporizador--;
                    if (temporizador % 60 == 0){ //ya transcurrio un minuto
                        puntajePorMin -= 100; //se descuentan 100 puntos del puntaje por minutos (500) por cada minuto transcurrido
                    }
                }
                boolean heroeArriba = false;
                heroe.skin = config.getSkin();
                escenario.actualizarPozos();
                // Este for es para verificar si el heroe esta pisando la cabeza de un guardia
                for (Guardia g : guardias) {
                    boolean alineadosEnX = Math.abs(heroe.getX() - g.getX()) < 32;
                    boolean tocandoCabeza = (heroe.getY() + heroe.getAlto() >= g.getY()) &&
                            (heroe.getY() + heroe.getAlto() <= g.getY() + 4);
                    if (alineadosEnX && tocandoCabeza) {
                        heroeArriba = true; // No usamos break para que siga revisando a los demás
                    }
                }
                // Le pasamos el estado al héroe de entrada
                heroe.setArribaDeGuardia(heroeArriba);
                //logica de persecucion
                for (Guardia g : guardias){
                    g.perseguir(heroe);
                    g.mover();

                    // Si lo toca, y NO le está pisando la cabeza, el héroe muere
                    if (g.detectarColision(heroe)){
                        if (!heroe.isArribaDeGuardia()){
                            vidasHeroe--;
                            reiniciarNivel();
                        }
                    }
                    // Chequeo de pozo y paredes
                    int filaCentro = (g.getY() + g.getAlto() / 2) / 32;
                    int colCentro = (g.getX() + g.getAncho() / 2) / 32;
                    int bloqueCuerpo = escenario.obtenerTipoBloqueEn(filaCentro, colCentro);

                    // 1ro: Si el bloque es un 1 sólido, significa que el pozo se cerró (o se bugeó en la pared). Reaparece.
                    if (bloqueCuerpo == 1) {
                        puntaje += 150;
                        g.reaparecer();
                    }
                    // 2do: Si no se cerró, pero está en un pozo activo
                    else if (g.estaEnPozo()){
                        Oro oroRobado = g.getOroGuardado();

                        if (oroRobado != null) {
                            oroRobado.setX(g.getX());
                            oroRobado.setY(g.getY() - 16);
                            g.soltarOro();
                        }
                    }
                }

                if (escenario.obtenerTipoBloqueEn((heroe.getY() + heroe.getAlto() / 2) / 32, (heroe.getX() + heroe.getAncho() / 2) / 32) == 1){
                    //si el heroe queda atrapado en un pozo y este se cierra, pierde una vida y se reinicia el nivel
                    vidasHeroe--;
                    reiniciarNivel();
                }
                heroe.mover();

                for (Oro o : lingotes){
                    // Los guardias intentan robar la plata que esté tirada
                    for(Guardia g : guardias){
                        g.robarOro(o);
                    }

                    o.mover(); // Si el oro está en manos de un guardia, lo sigue. Si no, se queda quieto.
                    heroe.recolectarOro(o);
                    if (o.isRecolectadoPorHeroe())
                        puntaje += Oro.obtenerValor();
                }

                lingotes.removeIf(o -> o.isRecolectadoPorHeroe());
                lingotesRestantes = lingotes.size();
                if (lingotesRestantes == 0){
                    escenario.setEscaleraSalidaActiva(true);
                    escenario.activarEscalera(nivelActual);
                    if (heroe.getY() <= 0){ //el heroe cruzo la escalera
                        puntaje += 200;
                        puntaje += puntajePorMin;
                        vidasHeroe++; //Si el heroe completa el nivel, se le otorga una vida extra
                        nivelActual++; //siguiente nivel
                        escenario.setEscaleraSalidaActiva(false);
                        iniciarNivel();
                    }
                }
                //si el heroe pierde todas sus vidas, game over
                if (vidasHeroe == 0){
                    enEjecucion = false;
                }

                try {
                    Thread.sleep(32);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        // AL PRINCIPIO DEL UPDATE

    }

    @Override
    public void gameDraw(Graphics2D g) {
        if (buffer == null)
            return;
        int windowWidth = frame.getContentPane().getWidth();
        int windowHeight = frame.getContentPane().getHeight();
        Graphics2D g2 = buffer.createGraphics();
        // mantener aspect ratio
        double scaleX = (double) windowWidth / 800;
        double scaleY = (double) windowHeight / 600;
        double scale = Math.min(scaleX, scaleY);

        int newWidth = (int)(800 * scale);
        int newHeight = (int)(600 * scale);

        // centrar
        int x = (windowWidth - newWidth) / 2;
        int y = (windowHeight - newHeight) / 2;

        //para que los sprites no se vean borrosos
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                // 1. Limpiamos la pantalla entera pintándola de negro
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, 800, 600); // Ajustá al ancho y alto real de la ventana
                // Dibujamos guardias en azul
                escenario.dibujar(g2);
                if (!enEjecucion){
                    finDeJuego(g2);
                }
                heroe.actualizarAnimacion();
                heroe.dibujar(g2);
                for (Guardia guardia : guardias){
                    guardia.actualizarAnimacion();
                    guardia.dibujar(g2);
                }
                for (Oro o : lingotes){
                    g2.setColor(Color.YELLOW);
                    o.dibujar(g2);
                }
                g2.setColor(Color.yellow);
                g2.drawString("Puntaje: " + puntaje, 20, 580);
                g2.drawString("Nivel: "   + nivelActual, 800 / 2 - 30, 580);
                g2.drawString("Tiempo: "  + temporizador, 535, 580);
                g2.drawString("Vidas: "   + vidasHeroe, 800 - 100, 580);
        g2.dispose();
        g.drawImage(buffer, x, y, newWidth, newHeight, null);
    }

    @Override
    public void gameShutdown() {
        // Código de cierre
        if (frame != null)
            frame.dispose();
    }
    public void reiniciarNivel(){
        iniciarNivel();
    }
    public void finDeJuego(Graphics2D g){
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, 800, 600);

        g.setFont(new Font("Times New Roman", Font.BOLD, 40));
        g.setColor(Color.RED);
        g.drawString("GAME OVER", 800/2 - 100, 150);

        if (!rankingGuardado) {
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.setColor(Color.WHITE);
            g.drawString("Nombre:", 800/2 - 100, 220);
            g.drawString(nombreJugador + "|", 800/2 - 100, 250);
            g.drawString("Puntaje obtenido: " + puntaje + "  Nivel: " + nivelActual, 800/2 - 100, 290);
            g.setColor(Color.YELLOW);
            g.drawString("Presiona ENTER para guardar", 800/2 - 100, 330);

            if (enterPresionado && !nombreJugador.isEmpty()) {
                ranking.agregarEntrada(
                        new EntradaRanking(nombreJugador, nivelActual, puntaje)
                );
                rankingGuardado = true;
                nombreJugador = "";
            }
        } else {
            dibujarRanking(g);
        }
    }
    private void dibujarRanking(Graphics2D g2d) {
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.setColor(Color.YELLOW);
        g2d.drawString("TOP 10", 800/2 - 40, 180);

        g2d.setFont(new Font("Arial", Font.PLAIN, 16));
        List<EntradaRanking> top = ranking.obtenerTop10();

        for (int i = 0; i < top.size(); i++) {
            EntradaRanking e = top.get(i);
            String linea = (i+1) + ". " + e.getNombre() +
                    "   Pts: " + e.getPuntaje() +
                    "   Niv: " + e.getNivel() +
                    "   "      + e.getFecha();
            g2d.setColor(i == 0 ? Color.YELLOW : Color.WHITE);
            g2d.drawString(linea, 800/2 - 180, 220 + i * 25);
        }
        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.drawString("Presione ESCAPE para volver al menu principal", 800/2 - 200, 520);
    }
    public void keyPressed(KeyEvent e){
        switch(e.getKeyCode()){
            case KeyEvent.VK_ENTER:
                enterPresionado = true;
                break;
            case KeyEvent.VK_SPACE:
                if (mirandoIzq) //heroe esta mirando a la izquierda
                    heroe.cavarIzquierda();
                else if (mirandoDer)// heroe esta mirando a la derecha
                    heroe.cavarDerecha();
                break;
            case KeyEvent.VK_UP:
                heroe.setDireccion(2);
                break;
            case KeyEvent.VK_DOWN:
                heroe.setDireccion(3);
                break;
            case KeyEvent.VK_LEFT:
                heroe.setDireccion(0);
                break;
            case KeyEvent.VK_RIGHT:
                heroe.setDireccion(1);
                break;
            case KeyEvent.VK_BACK_SPACE:
                if (nombreJugador.length() > 0 && !rankingGuardado)
                    nombreJugador = nombreJugador.substring(0, nombreJugador.length() - 1);
                break;
            case KeyEvent.VK_ESCAPE:
                if (rankingGuardado){
                    stop();
                }
            default:
                break;
        }
    }
    public void keyTyped(KeyEvent e){
        if (!rankingGuardado) {
            char letra = e.getKeyChar();

            // Evitamos que guarde caracteres raros (como el Enter o el retroceso que ya manejamos)
            // Y le ponemos un límite de 10 caracteres para que no te rompa la tabla visual
            if (letra != '\b' && letra != '\n' && letra != '\r' && nombreJugador.length() < 10) {
                nombreJugador += letra;
            }
        }
    }
    public void keyReleased(KeyEvent e){
        switch(e.getKeyCode()){
            case KeyEvent.VK_ENTER:
                enterPresionado = false;
                break;
            case KeyEvent.VK_UP:
                heroe.setDireccion(-1); //para que el heroe no "patine" cuando se mueve
                break;
            case KeyEvent.VK_DOWN:
                heroe.setDireccion(-1);
                break;
            case KeyEvent.VK_LEFT:
                heroe.setDireccion(-1);
                mirandoIzq = true;
                mirandoDer = false;
                break;
            case KeyEvent.VK_RIGHT:
                heroe.setDireccion(-1);
                mirandoDer = true;
                mirandoIzq = false;
                break;
            default:
                break;
        }
    }
}
