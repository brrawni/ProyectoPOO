package LodeRunner;

import motor.Videojuego;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class LodeRunnerMain extends Videojuego {
    private BufferedImage buffer;
    //Imagen para cancelar parpadeos
    private int lingotesRestantes;
    private Timer cronometro;
    private Escenario escenario;
    private Heroe heroe;
    private ArrayList<Guardia> guardias;
    private ArrayList<Oro> lingotes;

    public LodeRunnerMain() {
        super("Lode Runner - UNLPam Edition", 800, 600);
    }

    @Override
    public void gameStartup() {
        buffer = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        // Acá instanciás tu Escenario, tu Héroe y tus Guardianes
        escenario = new Escenario(32, 32, 1); // Ejemplo de creación del escenario
        heroe = new Heroe(1*32, 1*32, 32, 64, escenario); // Ejemplo de creación del héroe
        guardias = new ArrayList<>();
        lingotes = new ArrayList<>();
        cronometro = new Timer();
        // 1. Spawneo inteligente de Guardias
        int guardiasCreados = 0;
        while (guardiasCreados < 10) {
            int colRand = (int)(Math.random() * 14); // Columnas de 0 a 13
            int filaRand = (int)(Math.random() * 8); // Filas de 0 a 7 (evitamos el fondo)

            // Verificamos los 3 bloques involucrados en el cuerpo del guardia
            int bloqueCabeza = escenario.obtenerTipoBloqueEn(filaRand, colRand);
            int bloquePies = escenario.obtenerTipoBloqueEn(filaRand + 1, colRand);
            int bloquePiso = escenario.obtenerTipoBloqueEn(filaRand + 2, colRand);

            // Regla: Cabeza en el aire, pies en el aire, y apoyado en un ladrillo (1) o escalera (3)
            if (bloqueCabeza == 0 && bloquePies == 0 && (bloquePiso == 1 || bloquePiso == 3)) {
                guardias.add(new Guardia(colRand * 32, filaRand * 32, 32, 64, escenario));
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

            // Regla de oro: El casillero actual debe ser Aire (0) y el de abajo Ladrillo (1)
            if (bloqueActual == 0 && bloqueDeAbajo == 1) {
                lingotes.add(new Oro(columnaRand * 32, filaRand * 32, 32, 32));
                orosCreados++;
            }
        }
    }

    @Override
    public void gameUpdate(double delta) {
        // Acá ejecutas las colisiones de las hitboxes y los movimientos
        for (Guardia g : guardias){
            g.perseguir(heroe);
            g.mover();
            for (Oro o : lingotes){
                g.robarOro(o);
                o.mover();
            }
        }
        try {
            Thread.sleep(32);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gameDraw(Graphics2D g) {
        if (buffer == null)
            return;
        // 1. Limpiamos la pantalla entera pintándola de negro
        Graphics2D g2 = buffer.createGraphics();
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, 800, 600); // Ajustá al ancho y alto real de la ventana
        // Dibujamos guardias en azul
        escenario.dibujar(g2);
        g2.setColor(Color.RED); //Heroe de rojo
        heroe.dibujar(g2);
        for (Guardia guardia : guardias){
            g2.setColor(Color.BLUE);
            guardia.dibujar(g2);
        }
        for (Oro o : lingotes){
            g2.setColor(Color.YELLOW);
            o.dibujar(g2);
        }
        //lo descartamos
        g2.dispose();
        //pegamos la imagen en la pantalla para que no haya parpadeos
        g.drawImage(buffer, 0, 0, null);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 800, 600);
        //comenzar a dibujar
        escenario.dibujar(g);
        g.setColor(Color.RED);
        heroe.dibujar(g);
        for (Guardia guardia : guardias){
            g.setColor(Color.BLUE);
            guardia.dibujar(g);
        }
        for (Oro o : lingotes){
            g.setColor(Color.YELLOW);
            o.dibujar(g);
        }
    }

    @Override
    public void gameShutdown() {
        // Código de cierre
    }
    public void iniciarCronometro() {
        cronometro.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Lógica para actualizar el cronómetro cada segundo
            }
        }, 0, 1000);
    }
}
