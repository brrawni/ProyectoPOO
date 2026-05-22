package LodeRunner;

import java.awt.Graphics2D;
import motor.Entidad;
import motor.Plataforma;
import motor.Videojuego;
import java.util.Timer;
import java.util.TimerTask;


public class LodeRunnerMain extends Videojuego {
    private int lingotesRestantes;
    private Timer cronometro;
    
    public LodeRunner() {
        super("Lode Runner - UNLPam Edition", 800, 600);
    }

    @Override
    public void gameStartup() {
        // Acá instanciás tu Escenario, tu Héroe y tus Guardianes
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
}
}