package motor; // O el paquete donde guardes cosas globales

import java.io.*;
import java.util.Properties;

public abstract class GestorConfiguracionBase {
    protected String rutaArchivo;
    protected boolean sonidoActivado = true;
    protected boolean pantallaCompleta = false;

    public GestorConfiguracionBase(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    // Guarda las propiedades comunes y escribe el archivo
    protected void guardarBase(Properties props) {
        props.setProperty("sonido", String.valueOf(sonidoActivado));
        props.setProperty("pantallaCompleta", String.valueOf(pantallaCompleta));
        
        try (FileOutputStream fos = new FileOutputStream(rutaArchivo)) {
            props.store(fos, "Configuracion del Juego");
        } catch (IOException e) {
            System.out.println("No se pudo guardar: " + e.getMessage());
        }
    }

    // Lee el archivo y carga las propiedades comunes
    protected Properties cargarBase() {
        Properties props = new Properties();
        File archivo = new File(rutaArchivo);
        
        if (archivo.exists()) {
            try (FileInputStream fis = new FileInputStream(archivo)) {
                props.load(fis);
                sonidoActivado = Boolean.parseBoolean(props.getProperty("sonido", "true"));
                pantallaCompleta = Boolean.parseBoolean(props.getProperty("pantallaCompleta", "false"));
            } catch (IOException e) {
                System.out.println("No se pudo cargar: " + e.getMessage());
            }
        }
        return props;
    }

    // Getters y Setters comunes
    public boolean isSonidoActivado() { return sonidoActivado; }
    public void setSonidoActivado(boolean v) { this.sonidoActivado = v; }
    
    public boolean isPantallaCompleta() { return pantallaCompleta; }
    public void setPantallaCompleta(boolean v) { this.pantallaCompleta = v; }

    // Métodos que cada juego deberá definir a su manera
    public abstract void guardar();
    public abstract void cargar();
    public abstract void restablecer();
}