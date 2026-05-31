package launcher;

import java.awt.*;

public class Boton {
    private int x, y, ancho, alto;
    private String texto;
    private boolean hover = false;

    private static final Color COLOR_NORMAL    = new Color(30, 30, 60);
    private static final Color COLOR_HOVER     = new Color(60, 60, 120);
    private static final Color COLOR_BORDE     = new Color(100, 100, 255);
    private static final Color COLOR_TEXTO     = Color.WHITE;

    public Boton(int x, int y, int ancho, int alto, String texto) {
        this.x     = x;
        this.y     = y;
        this.ancho = ancho;
        this.alto  = alto;
        this.texto = texto;
    }

    public void dibujar(Graphics2D g) {
        // Fondo
        g.setColor(hover ? COLOR_HOVER : COLOR_NORMAL);
        g.fillRoundRect(x, y, ancho, alto, 12, 12);

        // Borde
        g.setColor(COLOR_BORDE);
        g.setStroke(new BasicStroke(2));
        g.drawRoundRect(x, y, ancho, alto, 12, 12);
        g.setStroke(new BasicStroke(1));

        // Texto centrado
        g.setColor(COLOR_TEXTO);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        FontMetrics fm = g.getFontMetrics();
        int tx = x + (ancho - fm.stringWidth(texto)) / 2;
        int ty = y + (alto  - fm.getHeight()) / 2 + fm.getAscent();
        g.drawString(texto, tx, ty);
    }

    public boolean contienePunto(int px, int py) {
        return px >= x && px <= x + ancho && py >= y && py <= y + alto;
    }

    public void setHover(boolean hover) { this.hover = hover; }
    public String getTexto()            { return texto; }
}