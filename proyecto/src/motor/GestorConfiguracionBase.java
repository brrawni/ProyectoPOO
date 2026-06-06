package motor;

import java.io.*;
import java.util.Properties;

/**
 * Clase base abstracta para la configuración de todos los videojuegos.
 *
 * Campos comunes a los tres juegos:
 *   - pantallaCompleta  (todos)
 *   - efectosSonido     (todos; LR y Pong lo controlan por separado de la música)
 *   - musicaFondo       (todos; Space Invaders lo expone como "sonido general" unificado)
 *   - pistaMusical      (todos)
 *
 * Notas de diseño:
 *   · Space Invaders muestra un único toggle "Sonido activado/desactivado" que
 *     controla ambos flags a la vez. Para eso su subclase usa el método utilitario
 *     setSonidoGeneral(boolean) que se define aquí.
 *   · Lode Runner y Pong muestran dos toggles independientes (efectos / música),
 *     por lo que usan los setters individuales directamente.
 */
public abstract class GestorConfiguracionBase {

    protected boolean pantallaCompleta = false;
    protected boolean efectosSonido    = true;
    protected boolean musicaFondo      = true;
    protected String  pistaMusical     = "original";

    protected String rutaArchivo;

    public GestorConfiguracionBase(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    // ── Helpers internos ────────────────────────────────────────────

    /**
     * Agrega las 4 propiedades comunes al objeto Properties recibido
     * y luego escribe el archivo en disco.
     * Las subclases deben llamar a este método al final de su guardar().
     */
    protected void guardarBase(Properties props) {
        props.setProperty("pantallaCompleta", String.valueOf(pantallaCompleta));
        props.setProperty("efectosSonido",    String.valueOf(efectosSonido));
        props.setProperty("musicaFondo",      String.valueOf(musicaFondo));
        props.setProperty("pistaMusical",     pistaMusical);
        try (FileOutputStream fos = new FileOutputStream(rutaArchivo)) {
            props.store(fos, "Configuracion del Juego");
        } catch (IOException e) {
            System.out.println("No se pudo guardar: " + e.getMessage());
        }
    }

    /**
     * Lee el archivo de propiedades, carga las 4 propiedades comunes
     * y devuelve el objeto Properties para que la subclase cargue el resto.
     */
    protected Properties cargarBase() {
        Properties props = new Properties();
        File archivo = new File(rutaArchivo);
        if (archivo.exists()) {
            try (FileInputStream fis = new FileInputStream(archivo)) {
                props.load(fis);
                pantallaCompleta = Boolean.parseBoolean(props.getProperty("pantallaCompleta", "false"));
                efectosSonido    = Boolean.parseBoolean(props.getProperty("efectosSonido",    "true"));
                musicaFondo      = Boolean.parseBoolean(props.getProperty("musicaFondo",      "true"));
                pistaMusical     = props.getProperty("pistaMusical", "original");
            } catch (IOException e) {
                System.out.println("No se pudo cargar: " + e.getMessage());
            }
        }
        return props;
    }

    // ── Getters / Setters comunes ────────────────────────────────────

    public boolean isPantallaCompleta()              { return pantallaCompleta; }
    public void    setPantallaCompleta(boolean v)    { this.pantallaCompleta = v; }

    public boolean isEfectosSonidoActivados()        { return efectosSonido; }
    public void    setEfectosSonidoActivados(boolean v) { this.efectosSonido = v; }

    public boolean isMusicaFondoActivada()           { return musicaFondo; }
    public void    setMusicaFondoActivada(boolean v) { this.musicaFondo = v; }

    public String  getPistaMusical()                 { return pistaMusical; }
    public void    setPistaMusical(String v)         { this.pistaMusical = v; }

    /**
     * Método de conveniencia para juegos (como Space Invaders) que muestran
     * un único toggle "Sonido activado/desactivado" que afecta efectos Y música.
     */
    public void setSonidoGeneral(boolean v) {
        this.efectosSonido = v;
        this.musicaFondo   = v;
    }

    /**
     * Devuelve true solo si AMBOS canales de audio están activos.
     * Útil para el toggle unificado de Space Invaders.
     */
    public boolean isSonidoGeneralActivado() {
        return efectosSonido && musicaFondo;
    }

    // ── Métodos abstractos que cada juego debe implementar ───────────

    public abstract void guardar();
    public abstract void cargar();
    public abstract void restablecer();
}