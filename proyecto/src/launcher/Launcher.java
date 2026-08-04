package launcher;

import LodeRunner.LodeRunnerMenu;
import Pong.Pong;
import javax.swing.*;

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
        switch (juego) {
            case "spaceinvaders":
                new spaceinvaders.MenuSpaceInvaders(this).run();
                break;

            case "loderunner":
                LodeRunnerMenu menuLR = new LodeRunnerMenu();
                menuLR.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        SwingUtilities.invokeLater(() -> setVisible(true));
                    }
                });
                menuLR.setVisible(true);
                break;
            case "pong":
                Pong pong = new Pong(this);
                pong.run();
                break;
        }
    }
}
