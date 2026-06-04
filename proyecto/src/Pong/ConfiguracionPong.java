package Pong;

import java.io.*;
import java.util.Properties;

/**
 * Clase para gestionar la configuración del juego Pong.
 * Carga y guarda las preferencias en el archivo config.properties.
 * 
 * Parámetros configurables según la consigna:
 * 1. Modo de pantalla (ventana/fullscreen)
 * 2. Sonido (activado/desactivado)
 * 3. Skins (barras, cancha, pelota)
 * 4. Controles (teclas para ambos jugadores)
 * 5. Pista musical
 * 6. Puntuación máxima (11 o 15 puntos)
 */
public class ConfiguracionPong {
    // Objeto Properties para almacenar configuración en memoria
    private Properties config;
    
    // Archivo donde se persisten los datos de configuración
    private final static String ARCHIVO_CONFIG = "config_pong.properties";

    /**
     * Constructor: Inicializa el objeto Properties vacío
     */
    public ConfiguracionPong() {
        config = new Properties();
    }

    /**
     * Carga la configuración desde el archivo config_pong.properties
     * Si el archivo existe, lee todos los parámetros guardados
     */
    public void cargar() {
        try {
            File file = new File(ARCHIVO_CONFIG);
            if (file.exists()) {
                // Cargar propiedades desde archivo
                config.load(new FileInputStream(file));
            } else {
                // Si no existe, usar valores por defecto
                setearPorDefecto();
            }
        } catch (IOException e) {
            System.out.println("Error en la carga de archivo de configuración Pong");
            throw new RuntimeException(e);
        }
    }

    /**
     * Guarda la configuración actual en el archivo config_pong.properties
     * Se persiste en disco para que se mantenga entre ejecuciones
     */
    public void guardar() {
        try {
            FileOutputStream out = new FileOutputStream(ARCHIVO_CONFIG);
            config.store(out, "Configuracion de Pong");
            out.close();
        } catch (IOException e) {
            System.out.println("Error en el guardado de archivo de configuración");
            throw new RuntimeException(e);
        }
    }

    /**
     * Establece todos los parámetros a sus valores por defecto (como pide la consigna)
     * y guarda estos valores por defecto en el archivo
     */
    public void setearPorDefecto() {
        // 1. Pantalla: por defecto EN VENTANA (false = ventana, true = fullscreen)
        config.setProperty("pantallaCompleta", "false");
        
        // 2. Sonido: por defecto ACTIVADO
        config.setProperty("sonidoActivado", "true");
        
        // 3. Skins: por defecto ORIGINALES
        config.setProperty("skinBarras", "original");
        config.setProperty("skinCancha", "original");
        config.setProperty("skinPelota", "original");
        
        // 4. Controles - Jugador 1: FLECHAS por defecto (según consigna)
        config.setProperty("teclaArriba1", "UP");      // Flecha arriba
        config.setProperty("teclaAbajo1", "DOWN");     // Flecha abajo
        
        // 4. Controles - Jugador 2: W/S por defecto (según consigna)
        config.setProperty("teclaArriba2", "W");       // W
        config.setProperty("teclaAbajo2", "S");        // S
        
        // 5. Pista musical: por defecto TEMA ORIGINAL (según consigna)
        config.setProperty("pistaMusical", "original");
        
        // 6. Puntuación máxima: por defecto 11 puntos (según consigna)
        config.setProperty("puntuacionMaxima", "11");
        
        // Guardar estos valores en disco
        guardar();
    }

    // ============== GETTERS Y SETTERS ==============

    /**
     * @return true si está en pantalla completa, false si está en ventana
     */
    public boolean isPantallaCompleta() {
        return Boolean.parseBoolean(config.getProperty("pantallaCompleta", "false"));
    }

    public void setPantallaCompleta(boolean pantallaCompleta) {
        config.setProperty("pantallaCompleta", String.valueOf(pantallaCompleta));
    }

    /**
     * @return true si el sonido está activado
     */
    public boolean isSonidoActivado() {
        return Boolean.parseBoolean(config.getProperty("sonidoActivado", "true"));
    }

    public void setSonidoActivado(boolean sonidoActivado) {
        config.setProperty("sonidoActivado", String.valueOf(sonidoActivado));
    }

    /**
     * @return el skin de las barras/paletas (original, moderno, retro, etc.)
     */
    public String getSkinBarras() {
        return config.getProperty("skinBarras", "original");
    }

    public void setSkinBarras(String skin) {
        config.setProperty("skinBarras", skin);
    }

    /**
     * @return el skin de la cancha (original, moderno, retro, etc.)
     */
    public String getSkinCancha() {
        return config.getProperty("skinCancha", "original");
    }

    public void setSkinCancha(String skin) {
        config.setProperty("skinCancha", skin);
    }

    /**
     * @return el skin de la pelota (original, moderno, retro, etc.)
     */
    public String getSkinPelota() {
        return config.getProperty("skinPelota", "original");
    }

    public void setSkinPelota(String skin) {
        config.setProperty("skinPelota", skin);
    }

    /**
     * @return la tecla configurada para mover arriba al Jugador 1 (por defecto: Flecha Arriba)
     */
    public String getTeclaArriba1() {
        return config.getProperty("teclaArriba1", "UP");
    }

    public void setTeclaArriba1(String tecla) {
        config.setProperty("teclaArriba1", tecla);
    }

    /**
     * @return la tecla configurada para mover abajo al Jugador 1 (por defecto: Flecha Abajo)
     */
    public String getTeclaAbajo1() {
        return config.getProperty("teclaAbajo1", "DOWN");
    }

    public void setTeclaAbajo1(String tecla) {
        config.setProperty("teclaAbajo1", tecla);
    }

    /**
     * @return la tecla configurada para mover arriba al Jugador 2 (por defecto: W)
     */
    public String getTeclaArriba2() {
        return config.getProperty("teclaArriba2", "W");
    }

    public void setTeclaArriba2(String tecla) {
        config.setProperty("teclaArriba2", tecla);
    }

    /**
     * @return la tecla configurada para mover abajo al Jugador 2 (por defecto: S)
     */
    public String getTeclaAbajo2() {
        return config.getProperty("teclaAbajo2", "S");
    }

    public void setTeclaAbajo2(String tecla) {
        config.setProperty("teclaAbajo2", tecla);
    }

    /**
     * @return la pista musical seleccionada (por defecto: original)
     */
    public String getPistaMusical() {
        return config.getProperty("pistaMusical", "original");
    }

    public void setPistaMusical(String pista) {
        config.setProperty("pistaMusical", pista);
    }

    /**
     * @return la puntuación máxima para terminar el partido (por defecto: 11)
     * Según la consigna, puede ser 11 o 15
     */
    public int getPuntuacionMaxima() {
        return Integer.parseInt(config.getProperty("puntuacionMaxima", "11"));
    }

    public void setPuntuacionMaxima(int puntuacion) {
        // Validar que sea 11 o 15
        if (puntuacion != 11 && puntuacion != 15) {
            System.out.println("Puntuación inválida. Debe ser 11 o 15. Se establece a 11.");
            puntuacion = 11;
        }
        config.setProperty("puntuacionMaxima", String.valueOf(puntuacion));
    }
}
