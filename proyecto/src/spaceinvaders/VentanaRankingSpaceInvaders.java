package spaceinvaders;

import ranking.EntradaRanking;
import ranking.GestorRanking;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaRankingSpaceInvaders extends JDialog {
    public VentanaRankingSpaceInvaders(JFrame padre) {
        super(padre, "Ranking de Space Invaders", true);

        GestorRanking gestorRanking = new GestorRanking();
        List<EntradaRanking> entradas = gestorRanking.obtenerTop10();

        setTitle("Ranking de Space Invaders");
        setSize(600, 400);
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel labelTitulo = new JLabel("TOP 10 - Space Invaders");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        labelTitulo.setHorizontalAlignment(JLabel.CENTER);
        add(labelTitulo, BorderLayout.NORTH);

        String[] columnas = {"Posición", "Nombre", "Nivel", "Puntaje", "Fecha"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        int posicion = 1;
        for (EntradaRanking entrada : entradas) {
            Object[] fila = {
                    posicion,
                    entrada.getNombre(),
                    entrada.getNivel(),
                    entrada.getPuntaje(),
                    entrada.getFecha()
            };
            modelo.addRow(fila);
            posicion++;
        }

        if (entradas.isEmpty()) {
            modelo.addRow(new Object[]{"", "Sin registros", "", "", ""});
        }

        JTable tablaRanking = new JTable(modelo);
        tablaRanking.getColumnModel().getColumn(0).setPreferredWidth(60);
        tablaRanking.getColumnModel().getColumn(1).setPreferredWidth(150);
        tablaRanking.getColumnModel().getColumn(2).setPreferredWidth(60);
        tablaRanking.getColumnModel().getColumn(3).setPreferredWidth(80);
        tablaRanking.getColumnModel().getColumn(4).setPreferredWidth(120);
        tablaRanking.setFont(new Font("Arial", Font.PLAIN, 12));
        tablaRanking.setRowHeight(24);
        tablaRanking.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(tablaRanking);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton btnCerrar = new JButton("CERRAR");
        btnCerrar.addActionListener(e -> dispose());
        panelBotones.add(btnCerrar);
        add(panelBotones, BorderLayout.SOUTH);

        setLocationRelativeTo(padre);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
}
