package spaceinvaders;

import motor.GestorConfiguracionBase;
import java.awt.event.KeyEvent;
import java.util.Properties;

/**
 * Configuración de Space Invaders.
 *
 * Hereda de GestorConfiguracionBase los campos comunes:
 *   pantallaCompleta, efectosSonido, musicaFondo, pistaMusical.
 *
 * Space Invaders expone un único toggle "Sonido activado/desactivado" que
 * controla efectos Y música al mismo tiempo. Para eso la UI usa:
 *   config.isSonidoGeneralActivado()   → true si ambos canales están ON
 *   config.setSonidoGeneral(boolean)   → activa/desactiva ambos a la vez
 * Ambos métodos están definidos en GestorConfiguracionBase.
 *
 * Nota: el campo pistaMusical ya no se declara aquí; viene heredado de la base.
 */
public class GestorConfiguracionSpaceInvaders extends GestorConfiguracionBase {

    private static GestorConfiguracionSpaceInvaders instancia;

    // Campos propios de Space Invaders
    private String velocidad       = "MEDIA";
    private int    teclaIzquierda  = KeyEvent.VK_LEFT;
    private int    teclaDerecha    = KeyEvent.VK_RIGHT;
    private int    teclaDisparo    = KeyEvent.VK_SPACE;
    private String skinNave        = "original";
    private String skinInvasores   = "original";
    private String skinProyectiles = "original";

    // Constructor privado (Singleton)
    private GestorConfiguracionSpaceInvaders() {
        super("config_spaceinvaders.properties");
        cargar();
    }

    public static GestorConfiguracionSpaceInvaders getInstance() {
        if (instancia == null) instancia = new GestorConfiguracionSpaceInvaders();
        return instancia;
    }

    // ── Implementación de los métodos abstractos ─────────────────────

    @Override
    public void guardar() {
        Properties props = new Properties();
        // Propios de Space Invaders
        props.setProperty("velocidad",       velocidad);
        props.setProperty("teclaIzquierda",  String.valueOf(teclaIzquierda));
        props.setProperty("teclaDerecha",    String.valueOf(teclaDerecha));
        props.setProperty("teclaDisparo",    String.valueOf(teclaDisparo));
        props.setProperty("skinNave",        skinNave);
        props.setProperty("skinInvasores",   skinInvasores);
        props.setProperty("skinProyectiles", skinProyectiles);
        // La base agrega pantallaCompleta + efectosSonido + musicaFondo
        // + pistaMusical y escribe el archivo en disco
        super.guardarBase(props);
    }

    @Override
    public void cargar() {
        // La base lee el archivo y carga sus 4 campos comunes;
        // nos devuelve el mismo Properties para que leamos los nuestros.
        Properties props = super.cargarBase();
        velocidad       = props.getProperty("velocidad",       "MEDIA");
        teclaIzquierda  = Integer.parseInt(props.getProperty("teclaIzquierda", String.valueOf(KeyEvent.VK_LEFT)));
        teclaDerecha    = Integer.parseInt(props.getProperty("teclaDerecha",   String.valueOf(KeyEvent.VK_RIGHT)));
        teclaDisparo    = Integer.parseInt(props.getProperty("teclaDisparo",   String.valueOf(KeyEvent.VK_SPACE)));
        skinNave        = props.getProperty("skinNave",        "original");
        skinInvasores   = props.getProperty("skinInvasores",   "original");
        skinProyectiles = props.getProperty("skinProyectiles", "original");
    }

    @Override
    public void restablecer() {
        // Campos heredados
        pantallaCompleta = false;
        efectosSonido    = true;
        musicaFondo      = true;
        pistaMusical     = "original";
        // Campos propios
        velocidad        = "MEDIA";
        teclaIzquierda   = KeyEvent.VK_LEFT;
        teclaDerecha     = KeyEvent.VK_RIGHT;
        teclaDisparo     = KeyEvent.VK_SPACE;
        skinNave         = "original";
        skinInvasores    = "original";
        skinProyectiles  = "original";
        guardar();
    }

    // ── Getters / Setters propios de Space Invaders ──────────────────

    public String getVelocidad()                    { return velocidad; }
    public void   setVelocidad(String v)            { this.velocidad = v; }

    public int  getTeclaIzquierda()                 { return teclaIzquierda; }
    public void setTeclaIzquierda(int v)            { this.teclaIzquierda = v; }

    public int  getTeclaDerecha()                   { return teclaDerecha; }
    public void setTeclaDerecha(int v)              { this.teclaDerecha = v; }

    public int  getTeclaDisparo()                   { return teclaDisparo; }
    public void setTeclaDisparo(int v)              { this.teclaDisparo = v; }

    public String getSkinNave()                     { return skinNave; }
    public void   setSkinNave(String v)             { this.skinNave = v; }

    public String getSkinInvasores()                { return skinInvasores; }
    public void   setSkinInvasores(String v)        { this.skinInvasores = v; }

    public String getSkinProyectil()                { return skinProyectiles; }
    public void   setSkinProyectil(String v)        { this.skinProyectiles = v; }

    // pistaMusical → heredado; getPistaMusical() / setPistaMusical() ya están en la base.
}