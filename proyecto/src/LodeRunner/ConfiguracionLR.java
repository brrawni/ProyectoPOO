package LodeRunner;

import motor.GestorConfiguracionBase;
import java.util.Properties;

/**
 * Configuración de Lode Runner.
 *
 * Hereda de GestorConfiguracionBase los campos comunes:
 *   pantallaCompleta, efectosSonido, musicaFondo, pistaMusical.
 *
 * Agrega los campos propios de LR:
 *   skinPersonaje, teclaCavar, teclaEfectos, teclaMusica, teclaIniciar.
 *
 * Nota sobre el sonido:
 *   LR controla efectos y música por separado, por lo que usa directamente
 *   isEfectosSonidoActivados() / isMusicaFondoActivada() de la base.
 *   Los alias isEfectosDeSonidoActivados() / isMusicaDeFondoActivada() se
 *   mantienen por compatibilidad con PanelConfiguracion.
 */
public class ConfiguracionLR extends GestorConfiguracionBase {

    // Campos propios de Lode Runner
    private String skinPersonaje = "original";
    private String teclaCavar    = "SPACE";
    private String teclaEfectos  = "Q";
    private String teclaMusica   = "W";
    private String teclaIniciar  = "ENTER";

    public ConfiguracionLR() {
        super("config_loderunner.properties");
    }

    // ── Implementación de los métodos abstractos ─────────────────────

    @Override
    public void guardar() {
        Properties props = new Properties();
        // Propios de LR
        props.setProperty("skinPersonaje", skinPersonaje);
        props.setProperty("teclaCavar",    teclaCavar);
        props.setProperty("teclaEfectos",  teclaEfectos);
        props.setProperty("teclaMusica",   teclaMusica);
        props.setProperty("teclaIniciar",  teclaIniciar);
        // La base agrega pantallaCompleta + efectosSonido + musicaFondo
        // + pistaMusical y escribe el archivo en disco
        super.guardarBase(props);
    }

    @Override
    public void cargar() {
        // La base lee el archivo y carga sus 4 campos comunes;
        // nos devuelve el mismo Properties para que leamos los nuestros.
        Properties props = super.cargarBase();
        skinPersonaje = props.getProperty("skinPersonaje", "original");
        teclaCavar    = props.getProperty("teclaCavar",    "SPACE");
        teclaEfectos  = props.getProperty("teclaEfectos",  "Q");
        teclaMusica   = props.getProperty("teclaMusica",   "W");
        teclaIniciar  = props.getProperty("teclaIniciar",  "ENTER");
    }

    @Override
    public void restablecer() {
        // Campos heredados
        pantallaCompleta = false;
        efectosSonido    = true;
        musicaFondo      = true;
        pistaMusical     = "original";
        // Campos propios
        skinPersonaje = "original";
        teclaCavar    = "SPACE";
        teclaEfectos  = "Q";
        teclaMusica   = "W";
        teclaIniciar  = "ENTER";
        guardar();
    }

    /**
     * @deprecated Usar restablecer(). Mantenido por compatibilidad con PanelConfiguracion.
     */
    @Deprecated
    public void setearPorDefecto() {
        restablecer();
    }

    // ── Aliases de compatibilidad con PanelConfiguracion ─────────────

    /**
     * Alias de isEfectosSonidoActivados() para compatibilidad con el código existente.
     */
    public boolean isEfectosDeSonidoActivados() {
        return isEfectosSonidoActivados();
    }

    public void setEfectosDeSonidoActivados(boolean v) {
        setEfectosSonidoActivados(v);
    }

    /**
     * Alias de isMusicaFondoActivada() para compatibilidad con el código existente.
     */
    public boolean isMusicaDeFondoActivada() {
        return isMusicaFondoActivada();
    }

    public void setMusicaDeFondoActivada(boolean v) {
        setMusicaFondoActivada(v);
    }

    // ── Getters / Setters propios de LR ──────────────────────────────

    public String getSkin()                      { return skinPersonaje; }
    public void   setSkin(String v)              { this.skinPersonaje = v; }

    public String getTeclaCavar()                { return teclaCavar; }
    public void   setTeclaCavar(String v)        { this.teclaCavar = v; }

    public String getTeclaEfectos()              { return teclaEfectos; }
    public void   setTeclaEfectos(String v)      { this.teclaEfectos = v; }

    public String getTeclaMusica()               { return teclaMusica; }
    public void   setTeclaMusica(String v)       { this.teclaMusica = v; }

    public String getTeclaIniciar()              { return teclaIniciar; }
    public void   setTeclaIniciar(String v)      { this.teclaIniciar = v; }
}