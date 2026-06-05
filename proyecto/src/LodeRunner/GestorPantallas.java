package LodeRunner;

import launcher.Boton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GestorPantallas extends JPanel {
    private CardLayout cardLayout;
    private LodeRunnerMenu lodeRunnerMenu;
    // Constantes públicas para que los paneles sepan cómo se llama cada pantalla
    public static final String PANTALLA_MENU = "Menu";
    public static final String PANTALLA_CONFIG = "Config";
    public static final String PANTALLA_RANKING = "Ranking";
    public static final String PANTALLA_JUEGO = "Juego";

    public GestorPantallas(LodeRunnerMenu lodeRunnerMenu) {
        this.lodeRunnerMenu = lodeRunnerMenu;
        cardLayout = new CardLayout();
        this.setLayout(cardLayout); // El gestor en sí mismo es el contenedor

        // Instanciamos los paneles SEPARADOS y les pasamos "this" (este gestor)
        this.add(new PanelMenu(this), PANTALLA_MENU);
        this.add(new PanelConfiguracion(this), PANTALLA_CONFIG);
        this.add(new PanelRanking(this), PANTALLA_RANKING);
        // Arranca mostrando el menú
        cardLayout.show(this, PANTALLA_MENU);
    }

    // Método público que los paneles van a llamar para cambiar de vista
    public void cambiarPantalla(String nombrePantalla) {
        cardLayout.show(this, nombrePantalla);
    }
    public LodeRunnerMenu getLodeRunnerMenu(){ return lodeRunnerMenu; }
}

