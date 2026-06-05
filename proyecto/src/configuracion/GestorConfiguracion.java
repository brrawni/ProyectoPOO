package configuracion;

import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Properties;

public class GestorConfiguracion {

    private static GestorConfiguracion instancia;
    private static final String RUTA = "config.properties";

    // Valores configurables
    private boolean sonidoActivado  = true;
    private String  velocidad       = "MEDIA";
    private int     teclaIzquierda  = KeyEvent.VK_LEFT;
    private int     teclaDerecha    = KeyEvent.VK_RIGHT;
    private int     teclaDisparo    = KeyEvent.VK_SPACE;

    private GestorConfiguracion() {
        cargar();
    }

    public static GestorConfiguracion getInstance() {
        if (instancia == null) instancia = new GestorConfiguracion();
        return instancia;
    }

    public void guardar() {
        Properties props = new Properties();
        props.setProperty("sonido",         String.valueOf(sonidoActivado));
        props.setProperty("velocidad",      velocidad);
        props.setProperty("teclaIzquierda", String.valueOf(teclaIzquierda));
        props.setProperty("teclaDerecha",   String.valueOf(teclaDerecha));
        props.setProperty("teclaDisparo",   String.valueOf(teclaDisparo));

        try (FileOutputStream fos = new FileOutputStream(RUTA)) {
            props.store(fos, "Configuracion Space Invaders");
        } catch (IOException e) {
            System.out.println("No se pudo guardar: " + e.getMessage());
        }
    }

    public void cargar() {
        Properties props = new Properties();
        File archivo = new File(RUTA);
        if (!archivo.exists()) return;

        try (FileInputStream fis = new FileInputStream(archivo)) {
            props.load(fis);
            sonidoActivado = Boolean.parseBoolean(
                props.getProperty("sonido",    "true"));
            velocidad      = props.getProperty("velocidad", "MEDIA");
            teclaIzquierda = Integer.parseInt(
                props.getProperty("teclaIzquierda",
                    String.valueOf(KeyEvent.VK_LEFT)));
            teclaDerecha   = Integer.parseInt(
                props.getProperty("teclaDerecha",
                    String.valueOf(KeyEvent.VK_RIGHT)));
            teclaDisparo   = Integer.parseInt(
                props.getProperty("teclaDisparo",
                    String.valueOf(KeyEvent.VK_SPACE)));
        } catch (IOException e) {
            System.out.println("No se pudo cargar: " + e.getMessage());
        }
    }

    public void restablecer() {
        sonidoActivado = true;
        velocidad      = "MEDIA";
        teclaIzquierda = KeyEvent.VK_LEFT;
        teclaDerecha   = KeyEvent.VK_RIGHT;
        teclaDisparo   = KeyEvent.VK_SPACE;
        guardar();
    }

    //Getters y Setters
    public boolean isSonidoActivado()           { return sonidoActivado; }
    public void    setSonidoActivado(boolean v) { this.sonidoActivado = v; }

    public String  getVelocidad()               { return velocidad; }
    public void    setVelocidad(String v)       { this.velocidad = v; }

    public int     getTeclaIzquierda()          { return teclaIzquierda; }
    public void    setTeclaIzquierda(int v)     { this.teclaIzquierda = v; }

    public int     getTeclaDerecha()            { return teclaDerecha; }
    public void    setTeclaDerecha(int v)       { this.teclaDerecha = v; }

    public int     getTeclaDisparo()            { return teclaDisparo; }
    public void    setTeclaDisparo(int v)       { this.teclaDisparo = v; }
}