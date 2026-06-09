package Pong;

import ranking.GestorRanking;
import ranking.EntradaRanking;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Ventana modal para visualizar el ranking de Pong.
 * Muestra los 10 mejores puntajes según la consigna:
 * "El juego debe guardar todos los puntajes obtenidos y solo visualizar los 10 mejores"
 * 
 * Cada entrada muestra:
 * - Nombre del jugador
 * - Nivel alcanzado
 * - Puntaje
 * - Fecha
 */
public class VentanaRankingPong extends JDialog {
    // Referencia al gestor de ranking (gestiona carga/guardado)
    private GestorRanking gestorRanking;
    private DefaultTableModel modelo;

    /**
     * Constructor: crea la ventana modal del ranking
     * @param gestorRanking Objeto que gestiona las entradas del ranking
     * @param padre Frame padre (la ventana del menú)
     */
    public VentanaRankingPong(GestorRanking gestorRanking, JFrame padre) {
        // Crear diálogo modal
        super(padre, "Ranking de Pong", true);
        this.gestorRanking = gestorRanking;

        // Configurar tamaño y layout
        setSize(600, 400);
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== TÍTULO =====
        JLabel labelTitulo = new JLabel("TOP 10 - MEJORES PUNTAJES");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        labelTitulo.setHorizontalAlignment(JLabel.CENTER);
        add(labelTitulo, BorderLayout.NORTH);

        // ===== TABLA DE RANKING =====
        // Columnas: Posición, Nombre, Nivel, Puntaje, Fecha
        String[] columnas = {"Posicion", "Nombre", "Nivel", "Puntaje", "Fecha"};

        // Crear modelo de tabla
        modelo = new DefaultTableModel(columnas, 0) {
            // Hacer la tabla no editable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        gestorRanking.cargar();
        // Obtener las 10 mejores entradas del ranking
        List<EntradaRanking> mejores = gestorRanking.obtenerTop10();

        // Llenar la tabla con los datos del ranking
        int posicion = 1;
        for (EntradaRanking entrada : mejores) {
            // Crear fila con: posición, nombre, nivel, puntaje, fecha
            Object[] fila = {
                    posicion,                          // Posición (1, 2, 3, ...)
                    entrada.getNombre(),               // Nombre del jugador
                    entrada.getNivel(),                // Nivel alcanzado
                    entrada.getPuntaje(),              // Puntaje obtenido
                    entrada.getFecha()                 // Fecha de la partida
            };
            modelo.addRow(fila);
            posicion++;
        }

        // Si el ranking está vacío, mostrar mensaje
        if (mejores.isEmpty()) {
            modelo.addRow(new Object[]{"", "Sin registros", "", "", ""});
        }

        // Crear tabla con el modelo
        JTable tablaRanking = new JTable(modelo);
        
        // Configurar columnas
        tablaRanking.getColumnModel().getColumn(0).setPreferredWidth(60);   // Posición
        tablaRanking.getColumnModel().getColumn(1).setPreferredWidth(120);  // Nombre
        tablaRanking.getColumnModel().getColumn(2).setPreferredWidth(60);   // Nivel
        tablaRanking.getColumnModel().getColumn(3).setPreferredWidth(80);   // Puntaje
        tablaRanking.getColumnModel().getColumn(4).setPreferredWidth(100);  // Fecha

        // Configurar apariencia de la tabla
        tablaRanking.setFont(new Font("Arial", Font.PLAIN, 12));
        tablaRanking.setRowHeight(25);
        tablaRanking.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Crear scroll para la tabla (en caso de que haya muchas entradas)
        JScrollPane scrollPane = new JScrollPane(tablaRanking);
        add(scrollPane, BorderLayout.CENTER);

        // ===== BOTONES =====
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        // Botón Limpiar Ranking: borra todos los registros
        JButton btnLimpiar = new JButton("LIMPIAR RANKING");
        btnLimpiar.addActionListener(e -> {
            int confirmacion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Estás seguro de que deseas borrar todos los registros?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirmacion == JOptionPane.YES_OPTION) {
                gestorRanking.limpiar();
                actualizarTabla();
            }
        });

        // Botón Cerrar: cierra la ventana
        JButton btnCerrar = new JButton("CERRAR");
        btnCerrar.addActionListener(e -> dispose());

        panelBotones.add(btnLimpiar);
        panelBotones.add(btnCerrar);

        add(panelBotones, BorderLayout.SOUTH);

        // Configurar posición y visibilidad
        setLocationRelativeTo(padre); // Centrar sobre el padre
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void actualizarTabla() {
        modelo.setRowCount(0);
        List<EntradaRanking> mejores = gestorRanking.obtenerTop10();

        int posicion = 1;
        for (EntradaRanking entrada : mejores) {
            modelo.addRow(new Object[]{
                    posicion,
                    entrada.getNombre(),
                    entrada.getNivel(),
                    entrada.getPuntaje(),
                    entrada.getFecha()
            });
            posicion++;
        }

        if (mejores.isEmpty()) {
            modelo.addRow(new Object[]{"", "Sin registros", "", "", ""});
        }
    }
}
