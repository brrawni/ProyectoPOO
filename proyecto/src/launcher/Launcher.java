package launcher;

import javax.swing.*;
import java.awt.*;

public class Launcher extends JFrame {

    public Launcher() {
        setTitle("Retro Arcade");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        add(new PanelLauncher(this));
    }

    public void lanzarJuego(String juego) {
        setVisible(false);
        new Thread(() -> {
            switch (juego) {
                case "spaceinvaders":
                    new spaceinvaders.MenuSpaceInvaders().run();
                    break;
                case "loderunner":
                    System.out.println("Lode Runner proximamente");
                    break;
                case "pong":
                    System.out.println("Pong proximamente");
                    break;
            }
            SwingUtilities.invokeLater(() -> setVisible(true));
        }).start();
    }
}