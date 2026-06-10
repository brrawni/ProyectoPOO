package Pong;

import java.awt.*;
import java.util.List;
import launcher.Boton;
import motor.EntradaRanking;
import motor.GestorRankingBase;

public class PantallaRankingPong {
    private final int ancho;
    private final int alto;
    private final GestorRankingBase gestorRanking;

    private Boton btnLimpiarRanking;
    private Boton btnVolverRanking;

    public PantallaRankingPong(int ancho, int alto, GestorRankingBase gestorRanking) {
        this.ancho = ancho;
        this.alto = alto;
        this.gestorRanking = gestorRanking;
    }

    public void iniciar() {
        gestorRanking.cargar();
        btnLimpiarRanking = new Boton(230, 500, 150, 45, "LIMPIAR");
        btnVolverRanking = new Boton(420, 500, 150, 45, "VOLVER");
    }

    public boolean manejarClick(int mx, int my) {
        if (btnLimpiarRanking.contienePunto(mx, my)) {
            gestorRanking.limpiar();
        }
        return btnVolverRanking.contienePunto(mx, my);
    }

    public void actualizarHover(int mx, int my) {
        btnLimpiarRanking.setHover(btnLimpiarRanking.contienePunto(mx, my));
        btnVolverRanking.setHover(btnVolverRanking.contienePunto(mx, my));
    }

    public void renderizar(Graphics2D bg) {
        bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        bg.setColor(new Color(15, 25, 35));
        bg.fillRect(0, 0, ancho, alto);

        bg.setColor(new Color(255, 220, 80));
        bg.setFont(new Font("Arial", Font.BOLD, 42));
        String titulo = "MEJORES PUNTAJES";
        FontMetrics fm = bg.getFontMetrics();
        bg.drawString(titulo, (ancho - fm.stringWidth(titulo)) / 2, 70);

        dibujarTablaRanking(bg, 130);

        btnLimpiarRanking.dibujar(bg);
        btnVolverRanking.dibujar(bg);
    }

    private void dibujarTablaRanking(Graphics2D bg, int yInicial) {
        List<EntradaRanking> top10 = gestorRanking.obtenerTop10();
        bg.setFont(new Font("Monospaced", Font.BOLD, 22));

        if (top10.isEmpty()) {
            bg.setColor(Color.WHITE);
            bg.drawString("NO HAY PUNTAJES AUN.", 250, 270);
            bg.drawString("SE EL PRIMERO!", 300, 310);
            return;
        }

        bg.setColor(new Color(170, 210, 255));
        bg.drawString("POS  NOMBRE      PUNTOS  FECHA", 150, yInicial);

        bg.setColor(Color.WHITE);
        int y = yInicial + 40;
        for (int i = 0; i < top10.size(); i++) {
            EntradaRanking entrada = top10.get(i);
            String nombre = entrada.getNombre();
            if (nombre.length() > 10) nombre = nombre.substring(0, 10);

            String linea = String.format("%2d.  %-10s  %5d   %s",
                    i + 1,
                    nombre,
                    entrada.getPuntaje(),
                    entrada.getFecha());
            bg.drawString(linea, 150, y);
            y += 32;
        }
    }
}
