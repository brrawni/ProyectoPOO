package LodeRunner;

import javax.swing.*;

public class LodeRunnerMenu extends JFrame{
    public LodeRunnerMenu() {
        // Configuramos la ventana
        setTitle("Lode Runner");
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null); // Centra la ventana

        // DISPOSE_ON_CLOSE es clave para que al cerrar el juego,
        // no se cierre también el Launcher de fondo.
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Instanciamos y agregamos el gestor de los menús
        GestorPantallas gestor = new GestorPantallas();
        add(gestor);
    }
}
