package Pong;

import motor.GestorConfiguracionBase;
import java.util.Properties;

/**
 * Configuración de Pong.
 *
 * Hereda de GestorConfiguracionBase los campos comunes:
 *   pantallaCompleta, efectosSonido, musicaFondo, pistaMusical.
 *
 * Pong, al igual que Lode Runner, muestra un único toggle "Sonido activado"
 * que en la consigna no distingue entre efectos y música. Por eso
 * VentanaConfiguracionPong usa el método de conveniencia isSonidoGeneralActivado()
 * / setSonidoGeneral(boolean), que vienen de la base.
 * Los setters individuales (setEfectosSonidoActivados / setMusicaFondoActivada)
 * también están disponibles si en el futuro se quiere separarlos.
 *
 * Alias mantenido:
 *   isSonidoActivado() / setSonidoActivado()  →  delegan al toggle unificado
 *   para no romper la VentanaConfiguracionPong actual.
 */
public class ConfiguracionPong extends GestorConfiguracionBase {

    // Campos propios de Pong
    private String skinBarras       = "original";
    private String skinCancha       = "original";
    private String skinPelota       = "original";
    private String teclaArriba1     = "UP";
    private String teclaAbajo1      = "DOWN";
    private String teclaArriba2     = "W";
    private String teclaAbajo2      = "S";
    private int    puntuacionMaxima = 11;

    public ConfiguracionPong() {
        super("config_pong.properties");
    }

    // ── Implementación de los métodos abstractos ─────────────────────

    @Override
    public void guardar() {
        Properties props = new Properties();
        // Propios de Pong
        props.setProperty("skinBarras",       skinBarras);
        props.setProperty("skinCancha",       skinCancha);
        props.setProperty("skinPelota",       skinPelota);
        props.setProperty("teclaArriba1",     teclaArriba1);
        props.setProperty("teclaAbajo1",      teclaAbajo1);
        props.setProperty("teclaArriba2",     teclaArriba2);
        props.setProperty("teclaAbajo2",      teclaAbajo2);
        props.setProperty("puntuacionMaxima", String.valueOf(puntuacionMaxima));
        // La base agrega pantallaCompleta + efectosSonido + musicaFondo
        // + pistaMusical y escribe el archivo en disco
        super.guardarBase(props);
    }

    @Override
    public void cargar() {
        // La base lee el archivo y carga sus 4 campos comunes;
        // nos devuelve el mismo Properties para que leamos los nuestros.
        Properties props = super.cargarBase();
        skinBarras       = props.getProperty("skinBarras",       "original");
        skinCancha       = props.getProperty("skinCancha",       "original");
        skinPelota       = props.getProperty("skinPelota",       "original");
        teclaArriba1     = props.getProperty("teclaArriba1",     "UP");
        teclaAbajo1      = props.getProperty("teclaAbajo1",      "DOWN");
        teclaArriba2     = props.getProperty("teclaArriba2",     "W");
        teclaAbajo2      = props.getProperty("teclaAbajo2",      "S");
        puntuacionMaxima = Integer.parseInt(
                props.getProperty("puntuacionMaxima", "11"));
    }

    @Override
    public void restablecer() {
        // Campos heredados
        pantallaCompleta = false;
        efectosSonido    = true;
        musicaFondo      = true;
        pistaMusical     = "original";
        // Campos propios
        skinBarras       = "original";
        skinCancha       = "original";
        skinPelota       = "original";
        teclaArriba1     = "UP";
        teclaAbajo1      = "DOWN";
        teclaArriba2     = "W";
        teclaAbajo2      = "S";
        puntuacionMaxima = 11;
        guardar();
    }

    /**
     * @deprecated Usar restablecer(). Mantenido por compatibilidad con VentanaConfiguracionPong.
     */
    @Deprecated
    public void setearPorDefecto() {
        restablecer();
    }

    // ── Alias de compatibilidad con VentanaConfiguracionPong ─────────
    //
    // VentanaConfiguracionPong usa config.isSonidoActivado() y
    // config.setSonidoActivado(). Esos métodos delegan al toggle
    // unificado de la base (efectosSonido + musicaFondo juntos).

    /**
     * Alias de isSonidoGeneralActivado() para compatibilidad con VentanaConfiguracionPong.
     */
    public boolean isSonidoActivado() {
        return isSonidoGeneralActivado();
    }

    /**
     * Alias de setSonidoGeneral(boolean) para compatibilidad con VentanaConfiguracionPong.
     */
    public void setSonidoActivado(boolean v) {
        setSonidoGeneral(v);
    }

    // ── Getters / Setters propios de Pong ────────────────────────────

    public String getSkinBarras()           { return skinBarras; }
    public void   setSkinBarras(String v)   { this.skinBarras = v; }

    public String getSkinCancha()           { return skinCancha; }
    public void   setSkinCancha(String v)   { this.skinCancha = v; }

    public String getSkinPelota()           { return skinPelota; }
    public void   setSkinPelota(String v)   { this.skinPelota = v; }

    public String getTeclaArriba1()         { return teclaArriba1; }
    public void   setTeclaArriba1(String v) { this.teclaArriba1 = v; }

    public String getTeclaAbajo1()          { return teclaAbajo1; }
    public void   setTeclaAbajo1(String v)  { this.teclaAbajo1 = v; }

    public String getTeclaArriba2()         { return teclaArriba2; }
    public void   setTeclaArriba2(String v) { this.teclaArriba2 = v; }

    public String getTeclaAbajo2()          { return teclaAbajo2; }
    public void   setTeclaAbajo2(String v)  { this.teclaAbajo2 = v; }

    public int  getPuntuacionMaxima()       { return puntuacionMaxima; }
    public void setPuntuacionMaxima(int v) {
        if (v != 11 && v != 15) {
            System.out.println("Puntuacion invalida, se establece 11.");
            v = 11;
        }
        this.puntuacionMaxima = v;
    }

    // pistaMusical → heredado; getPistaMusical() / setPistaMusical() ya están en la base.
}
