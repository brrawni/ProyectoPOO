package motor;

/* 
Representa al jugador controlado por el usuario.
Extiende Entidad y agrega el concepto de vidas.
*/

public abstract class Jugador extends Entidad {
    protected int vidas;

    public Jugador(int x, int y, int ancho, int alto, int vidas) {
        super(x, y, ancho, alto);
        this.vidas = vidas;
    }

    public int obtenerVidas() {
        return vidas;
    }

    public void perderVida() {
        if (vidas > 0) {
            vidas--;
        }
    }

    public abstract void mover();
}