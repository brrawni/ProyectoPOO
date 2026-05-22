package motor;

/* base abstracta para los enemigos del juego
extiende Entidad y agrega velocidad, estado de vida
y comportamiento de disparo.
*/ 

public abstract class Enemigo extends Entidad {
    protected float velocidad;
    protected boolean vivo;

    public Enemigo(int x, int y, int ancho, int alto, float velocidad) {
        super(x, y, ancho, alto);
        this.velocidad = velocidad;
        this.vivo = true;
    }

    public boolean estaVivo() {
        return vivo;
    }

    public void morir() {
        vivo = false;
    }

    public abstract void mover();
    public abstract void disparar();
    public abstract void obtenerPuntaje();
}