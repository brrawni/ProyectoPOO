Package LodeRunner;

public class Oro extends Entidad{
    private int valor;

    public Oro(int x, int y, int ancho, int alto, int valor) {
        super(x, y, ancho, alto);
        this.valor = valor;
    }

    public int obtenerValor() {
        return valor;
    }
}
