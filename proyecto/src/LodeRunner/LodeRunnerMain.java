package LodeRunner;

import motor.Videojuego;
import LodeRunner.Escenario;
import LodeRunner.PersonajeLodeRunner;
import LodeRunner.Oro;

import java.awt.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class LodeRunnerMain extends Videojuego {
    private int lingotesRestantes;
    private Timer cronometro;
    private Escenario escenario;
    private Heroe heroe;
    private ArrayList<Guardia> guardianes;
    private ArrayList<Oro> lingotes;

    public LodeRunnerMain() {
        super("Lode Runner - UNLPam Edition", 800, 600);
    }

    @Override
    public void gameStartup() {
        // Acá instanciás tu Escenario, tu Héroe y tus Guardianes
        escenario = new Escenario(32, 32, 1); // Ejemplo de creación del escenario
        heroe = new Heroe(100, 100, 32, 32, escenario); // Ejemplo de creación del héroe
        guardianes = new ArrayList<>();
        lingotes = new ArrayList<>();
        cronometro = new Timer();
    }

    @Override
    public void gameUpdate(double delta) {
        // Acá ejecutas las colisiones de las hitboxes y los movimientos
    }

    @Override
    public void gameDraw(Graphics2D g) {
        // Acá dibujas el mapa y los personajes usando el objeto 'g'
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
