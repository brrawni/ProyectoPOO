package spaceinvaders;

import motor.GestorConfiguracionBase;
import java.awt.event.KeyEvent;
import java.util.Properties;

public class GestorConfiguracionSpaceInvaders extends GestorConfiguracionBase {
    private static GestorConfiguracionSpaceInvaders instancia;

    // Valores específicos de Space Invaders
    private String velocidad       = "MEDIA";
    private int teclaIzquierda     = KeyEvent.VK_LEFT;
    private int teclaDerecha       = KeyEvent.VK_RIGHT;
    private int teclaDisparo       = KeyEvent.VK_SPACE;
    
    // Nuevos valores requeridos
    private String skinNave        = "original";
    private String skinInvasores   = "original";
    private String skinProyectiles = "original";
    private String pistaMusical    = "original";

    // Constructor privado: le pasa el nombre correcto del archivo al padre
    private GestorConfiguracionSpaceInvaders() {
        super("config_spaceinvaders.properties");
        cargar();
    }

    public static GestorConfiguracionSpaceInvaders getInstance() {
        if (instancia == null) instancia = new GestorConfiguracionSpaceInvaders();
        return instancia;
    }

    @Override
    public void guardar() {
        Properties props = new Properties();
        props.setProperty("velocidad", velocidad);
        props.setProperty("teclaIzquierda", String.valueOf(teclaIzquierda));
        props.setProperty("teclaDerecha", String.valueOf(teclaDerecha));
        props.setProperty("teclaDisparo", String.valueOf(teclaDisparo));
        props.setProperty("skinNave", skinNave);
        props.setProperty("skinInvasores", skinInvasores);
        props.setProperty("skinProyectiles", skinProyectiles);
        props.setProperty("pistaMusical", pistaMusical);
        
        // El padre guarda sus variables y escribe todo en el disco
        super.guardarBase(props);
    }

    @Override
    public void cargar() {
        // El padre lee el archivo y nos devuelve las propiedades
        Properties props = super.cargarBase(); 
        
        velocidad       = props.getProperty("velocidad", "MEDIA");
        teclaIzquierda  = Integer.parseInt(props.getProperty("teclaIzquierda", String.valueOf(KeyEvent.VK_LEFT)));
        teclaDerecha    = Integer.parseInt(props.getProperty("teclaDerecha", String.valueOf(KeyEvent.VK_RIGHT)));
        teclaDisparo    = Integer.parseInt(props.getProperty("teclaDisparo", String.valueOf(KeyEvent.VK_SPACE)));
        skinNave        = props.getProperty("skinNave", "original");
        skinInvasores   = props.getProperty("skinInvasores", "original");
        skinProyectiles = props.getProperty("skinProyectiles", "original");
        pistaMusical    = props.getProperty("pistaMusical", "original");
    }

    @Override
    public void restablecer() {
        sonidoActivado   = true;
        pantallaCompleta = false;
        velocidad        = "MEDIA";
        teclaIzquierda   = KeyEvent.VK_LEFT;
        teclaDerecha     = KeyEvent.VK_RIGHT;
        teclaDisparo     = KeyEvent.VK_SPACE;
        skinNave         = "original";
        skinInvasores    = "original";
        skinProyectiles  = "original";
        pistaMusical     = "original";
        guardar();
    }

    // Faltaría agregar los Getters y Setters para las variables de velocidad, teclas, skins y música...
    // Getters y Setters de Velocidad
    public String getVelocidad() { return velocidad; }
    public void setVelocidad(String velocidad) { this.velocidad = velocidad; }

    // Getters y Setters de Teclas
    public int getTeclaIzquierda() { return teclaIzquierda; }
    public void setTeclaIzquierda(int teclaIzquierda) { this.teclaIzquierda = teclaIzquierda; }

    public int getTeclaDerecha() { return teclaDerecha; }
    public void setTeclaDerecha(int teclaDerecha) { this.teclaDerecha = teclaDerecha; }

    public int getTeclaDisparo() { return teclaDisparo; }
    public void setTeclaDisparo(int teclaDisparo) { this.teclaDisparo = teclaDisparo; }

    // Getters y Setters de Skins y Música
    public String getSkinNave() { return skinNave; }
    public void setSkinNave(String skinNave) { this.skinNave = skinNave; }

    public String getSkinInvasores() { return skinInvasores; }
    public void setSkinInvasores(String skinInvasores) { this.skinInvasores = skinInvasores; }

    public String getSkinProyectil() { return skinProyectiles; }
    public void setSkinProyectiles(String skinProyectiles) { this.skinProyectiles = skinProyectiles; }

    public String getPistaMusical() { return pistaMusical; }
    public void setPistaMusical(String pistaMusical) { this.pistaMusical = pistaMusical; }
}