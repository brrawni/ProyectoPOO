package LodeRunner;

import motor.Entidad;

import java.awt.*;


public class Oro extends Entidad{
    private static final int valor = 250;
    private boolean recolectadoPorGuardia;
    private boolean recolectadoPorHeroe;
    private Guardia guardiaQueLoLleva;

    public Oro(int x, int y, int ancho, int alto) {
        super(x, y, ancho, alto);
        recolectadoPorGuardia = false;
        recolectadoPorHeroe = false;
    }
    @Override
    public void mover(){
        if (recolectadoPorGuardia){
            //Logica para que el oro "siga" al guardia
            this.x = guardiaQueLoLleva.getX();
            this.y = guardiaQueLoLleva.getY() + 4;
        }
    }
    @Override
    public void dibujar(Graphics2D g) {
        if (!recolectadoPorHeroe) {
            // Dibujar oro usando el objeto 'g'
            g.setColor(Color.YELLOW);
            g.fillOval(super.x, super.y, super.ancho, super.alto);
        }
    }
    public boolean detectarColision(PersonajeLodeRunner p){
        return new Rectangle(this.x, this.y, this.ancho, this.alto).intersects(new Rectangle(p.getX(), p.getY(), p.getAncho(), p.getAlto()));
    }

    @Override
    public boolean detectarColision() {
        return false;
    }

    public void esRecolectado(Guardia guardia, Heroe heroe) {
        if (guardia != null && guardiaQueLoLleva == null && detectarColision(guardia) ){
            this.guardiaQueLoLleva = guardia;
            recolectadoPorGuardia = true;
        }
        else if (heroe != null && detectarColision(heroe) && !recolectadoPorGuardia){
            recolectadoPorHeroe = true;
        }
    }
    public void setX(int x) {
        super.x = x;
    }
    public void setY(int y) {
        super.y = y;
    }
    public static int obtenerValor() {
        return valor;
    }
    public void serSoltado() {
        this.recolectadoPorGuardia = false;
        this.guardiaQueLoLleva = null;
    }
    public boolean isRecolectadoPorHeroe(){
        return recolectadoPorHeroe;
    }
}
