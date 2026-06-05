package launcher;

import LodeRunner.GestorPantallas;
import LodeRunner.LodeRunnerMenu;
import Pong.MenuPong;
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
                LodeRunnerMenu menuLR = new LodeRunnerMenu(this);
                menuLR.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        SwingUtilities.invokeLater(() -> setVisible(true));
                    }
                });
                menuLR.setVisible(true);
                break;
            case "pong":
                // MenuPong maneja su propio JFrame y Timer; lo mostramos y listo.
                // Cuando el usuario vuelva o cierre, MenuPong se encarga de
                // llamar launcher.setVisible(true) por su cuenta.
                MenuPong menuPong = new MenuPong(this);
                menuPong.setVisible(true);
                break;
        }
    }
}