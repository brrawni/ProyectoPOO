package LodeRunner;

import launcher.Launcher;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class LodeRunnerMenu extends JFrame{
    private Launcher launcher;
    public LodeRunnerMenu(Launcher launcher) {
        this.launcher = launcher;
        // Configuramos la ventana
        setTitle("Lode Runner");
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null); // Centra la ventana
        // DISPOSE_ON_CLOSE es clave para que al cerrar el juego,
        // no se cierre también el Launcher de fondo.
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Instanciamos y agregamos el gestor de los menús
        GestorPantallas gestor = new GestorPantallas(this);
        add(gestor);
    }

    public Launcher getLauncher() {
        return launcher;
    }
}
