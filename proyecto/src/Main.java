
import launcher.Launcher;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Iniciar el launcher en el hilo de la interfaz gráfica (daba errores)
       SwingUtilities.invokeLater(() -> {
            Launcher launcher = new Launcher();
            launcher.setVisible(true);
        });
    }
}