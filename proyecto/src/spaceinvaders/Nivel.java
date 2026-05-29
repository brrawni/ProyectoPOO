package spaceinvaders;

public class Nivel {

    private int   numero;
    private float velocidadAlien;
    private int   filaInicio;
    private int   filas;
    private int   columnas;
    private float probabilidadDisparo;

    public Nivel(int numero) {
        this.numero = numero;
        configurar(numero);
    }
    /*
     Define los parámetros de cada nivel.
     A mayor nivel, más velocidad y los aliens empiezan más abajo.
     */
    private void configurar(int numero) {
        switch (numero) {
            case 1:
                velocidadAlien     = 2.0f;
                filas              = 3;
                columnas           = 6;
                probabilidadDisparo = 0.01f;
                break;
            case 2:
                velocidadAlien     = 2.5f;
                filas              = 4;
                columnas           = 7;
                probabilidadDisparo = 0.015f;
                break;
            case 3:
                velocidadAlien     = 3.0f;
                filas              = 4;
                columnas           = 8;
                probabilidadDisparo = 0.02f;
                break;
            case 4:
                velocidadAlien     = 3.5f;
                filas              = 5;
                columnas           = 9;
                probabilidadDisparo = 0.025f;
                break;
            default:
                // Niveles 5 en adelante escalan automáticamente
                velocidadAlien     = Math.min(2.0f + numero * 0.5f, 8.0f);
                filas              = Math.min(3 + numero, 6);
                columnas           = Math.min(6 + numero, 11);
                probabilidadDisparo = Math.min(0.01f + numero * 0.005f, 0.05f);
                break;
        }
    }

    public void cargar() { configurar(numero); }

    public int   obtenerNumero()            { return numero; }
    public float obtenerVelocidadAlien()    { return velocidadAlien; }
    public int   obtenerFilaInicio()        { return filaInicio; }
    public int   obtenerFilas()             { return filas; }
    public int   obtenerColumnas()          { return columnas; }
    public float obtenerProbabilidadDisparo(){ return probabilidadDisparo; }
}