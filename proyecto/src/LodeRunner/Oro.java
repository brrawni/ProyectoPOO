package LodeRunner;

import motor.Entidad;

import java.awt.*;


public class Oro extends Entidad{
    private static final int valor = 250;
    private boolean recolectado; 

    public Oro(int x, int y, int ancho, int alto) {
        super(x, y, ancho, alto);
        recolectado = false;
    }
    
    @Override
    public void dibujar(Graphics2D g) {
        if (!recolectado) {
            // Aquí dibujas el oro usando el objeto 'g'
        }
    }
    public void recolectar() {
        recolectado = true;
    }
    public boolean estaRecolectado() {
        return recolectado;
    }
    public void setX(int x) {
        super.x = x;
    }
    public void setY(int y) {
        super.y = y;
    }
    public int obtenerValor() {
        return valor;
    }
}
