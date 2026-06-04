package launcher;

import LodeRunner.ConfiguracionLR;
import LodeRunner.LodeRunnerMain;

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
    switch (juego) {
        case "spaceinvaders":
            spaceinvaders.MenuSpaceInvaders menu = new spaceinvaders.MenuSpaceInvaders();

            // Cuando el menú termine, volver al launcher
            menu.frame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    SwingUtilities.invokeLater(() -> setVisible(true));
                }
            });
            menu.run();
            break;
        case "loderunner":
            LodeRunnerMain lodeRunnerMain = new LodeRunnerMain(new ConfiguracionLR());
            SwingUtilities.invokeLater(() -> setVisible(true));
            lodeRunnerMain.run();
            break;
        case "pong":
            SwingUtilities.invokeLater(() -> setVisible(true));
            break;
    }
}
}