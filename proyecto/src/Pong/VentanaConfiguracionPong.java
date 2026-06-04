package Pong;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana modal para configurar los parámetros de Pong.
 * Aparece cuando el usuario hace clic en "CONFIGURACION" desde el menú.
 * 
 * Permite configurar (según la consigna):
 * 1. Pantalla completa / Ventana
 * 2. Sonido activado/desactivado
 * 3. Skins (barras, cancha, pelota)
 * 4. Teclas personalizadas para ambos jugadores
 * 5. Pista musical
 * 6. Puntuación máxima (11 o 15)
 */
public class VentanaConfiguracionPong extends JDialog {
    // Referencia a la configuración compartida
    private ConfiguracionPong config;

    /**
     * Constructor: crea la ventana modal de configuración
     * @param config Objeto de configuración a modificar
     * @param padre Frame padre (la ventana del menú o del juego)
     */
    public VentanaConfiguracionPong(ConfiguracionPong config, JFrame padre) {
        // Crear diálogo modal (true = no permite interactuar con el padre hasta cerrarlo)
        super(padre, "Configuracion de Pong", true);
        this.config = config;

        // Configurar tamaño y layout
        setSize(500, 550);
        setLayout(new GridLayout(9, 1, 10, 10)); // 9 filas de configuraciones, espaciado de 10
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== 1. PANTALLA COMPLETA =====
        // Checkbox para pantalla completa (por defecto: desactivado = ventana)
        JCheckBox chkPantalla = new JCheckBox("Pantalla completa");
        chkPantalla.setSelected(config.isPantallaCompleta());
        add(chkPantalla);

        // ===== 2. SONIDO =====
        // Checkbox para sonido (por defecto: activado)
        JCheckBox chkSonido = new JCheckBox("Sonido activado");
        chkSonido.setSelected(config.isSonidoActivado());
        add(chkSonido);

        // ===== 3. SKINS - BARRAS =====
        // ComboBox para seleccionar el skin de las barras
        JPanel panelSkinBarras = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSkinBarras.add(new JLabel("Skin de barras:"));
        String[] skinsBarras = {"original", "moderno", "delgado"};
        JComboBox<String> comboSkinBarras = new JComboBox<>(skinsBarras);
        comboSkinBarras.setSelectedItem(config.getSkinBarras());
        panelSkinBarras.add(comboSkinBarras);
        add(panelSkinBarras);

        // ===== 4. SKINS - CANCHA =====
        JPanel panelSkinCancha = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSkinCancha.add(new JLabel("Skin de cancha:"));
        String[] skinsCancha = {"original", "moderno", "oscuro"};
        JComboBox<String> comboSkinCancha = new JComboBox<>(skinsCancha);
        comboSkinCancha.setSelectedItem(config.getSkinCancha());
        panelSkinCancha.add(comboSkinCancha);
        add(panelSkinCancha);

        // ===== 5. SKINS - PELOTA =====
        JPanel panelSkinPelota = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSkinPelota.add(new JLabel("Skin de pelota:"));
        String[] skinsPelota = {"original", "cuadrada", "triangulo"};
        JComboBox<String> comboSkinPelota = new JComboBox<>(skinsPelota);
        comboSkinPelota.setSelectedItem(config.getSkinPelota());
        panelSkinPelota.add(comboSkinPelota);
        add(panelSkinPelota);

        // ===== 6. PISTA MUSICAL =====
        // ComboBox para seleccionar la pista de música de fondo (por defecto: original)
        JPanel panelMusica = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelMusica.add(new JLabel("Pista musical:"));
        String[] pistas = {"original", "8-bit", "jazz", "clasica"};
        JComboBox<String> comboPistas = new JComboBox<>(pistas);
        comboPistas.setSelectedItem(config.getPistaMusical());
        panelMusica.add(comboPistas);
        add(panelMusica);

        // ===== 7. PUNTUACIÓN MÁXIMA =====
        // Spinner para elegir entre 11 o 15 puntos (según consigna)
        JPanel panelPuntuacion = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelPuntuacion.add(new JLabel("Puntuacion maxima:"));
        Integer[] puntuaciones = {11, 15};
        JComboBox<Integer> comboPuntuacion = new JComboBox<>(puntuaciones);
        comboPuntuacion.setSelectedItem(config.getPuntuacionMaxima());
        panelPuntuacion.add(comboPuntuacion);
        add(panelPuntuacion);

        // ===== 8. TECLAS PERSONALIZABLES (Informativo) =====
        // Label informativo: las teclas se pueden configurar aquí en versiones futuras
        JLabel labelTeclas = new JLabel("Teclas: J1=(Flechas arriba/abajo), J2=(W/S)");
        labelTeclas.setFont(new Font("Arial", Font.ITALIC, 11));
        add(labelTeclas);

        // ===== 9. PANEL DE BOTONES =====
        // Botones Guardar, Reset y Cancelar
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        // Botón Guardar: guarda todos los cambios
        JButton btnGuardar = new JButton("GUARDAR");
        btnGuardar.addActionListener(e -> {
            // Guardar todos los valores modificados
            config.setPantallaCompleta(chkPantalla.isSelected());
            config.setSonidoActivado(chkSonido.isSelected());
            config.setSkinBarras((String) comboSkinBarras.getSelectedItem());
            config.setSkinCancha((String) comboSkinCancha.getSelectedItem());
            config.setSkinPelota((String) comboSkinPelota.getSelectedItem());
            config.setPistaMusical((String) comboPistas.getSelectedItem());
            config.setPuntuacionMaxima((Integer) comboPuntuacion.getSelectedItem());

            // Guardar en archivo
            config.guardar();
            
            // Cerrar ventana
            dispose();
        });

        // Botón Reset: vuelve a los valores por defecto
        JButton btnReset = new JButton("RESET");
        btnReset.addActionListener(e -> {
            // Establecer valores por defecto y guardar
            config.setearPorDefecto();
            
            // Cerrar ventana (se abrirá de nuevo con valores por defecto)
            dispose();
        });

        // Botón Cancelar: cierra sin guardar
        JButton btnCancelar = new JButton("CANCELAR");
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnReset);
        panelBotones.add(btnCancelar);

        add(panelBotones);

        // Configurar posición y visibilidad
        setLocationRelativeTo(padre); // Centrar sobre el padre
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
}
