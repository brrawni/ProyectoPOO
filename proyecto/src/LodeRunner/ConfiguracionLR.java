package LodeRunner;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Properties;

public class ConfiguracionLR {
    private Properties config;
    private final static String ARCHIVO_CONFIG = "config.properties";

    public ConfiguracionLR(){
        config = new Properties();
    }

    public void cargar(){
        try{
            File file = new File(ARCHIVO_CONFIG);
            if (file.exists()){
                config.load(new FileInputStream(file));
            }
        } catch (IOException e) {
            System.out.println("Error en la carga de archivo");
            throw new RuntimeException(e);
        }
    }
    public void guardar(){
        try {
            FileOutputStream out = new FileOutputStream(ARCHIVO_CONFIG);
            config.store(out, "Configuracion de Lode Runner");
            out.close();
        } catch (IOException e) {
            System.out.println("Error en el guardado de archivos");
            throw new RuntimeException(e);
        }
    }
    public void setearPorDefecto(){
        config.setProperty("pantallaCompleta", "false");
        config.setProperty("efectosDeSonido", "true");
        config.setProperty("musicaActivada", "true");
        config.setProperty("pistaMusical", "original");
        config.setProperty("skinPersonaje", "original");
        config.setProperty("teclaCavar", "SPACE");
        config.setProperty("teclaEfectos", "q");
        config.setProperty("teclaMusica", "w");
        config.setProperty("teclaIniciar", "ENTER");
        guardar();
    }

    public boolean isEfectosDeSonidoActivados() {
        return Boolean.parseBoolean(config.getProperty("efectosDeSonido"));
    }
    public void setEfectosDeSonidoActivados(boolean activados){
        config.setProperty("efectosDeSonido", String.valueOf(activados));
    }
    public boolean isMusicaDeFondoActivada() {
        return Boolean.parseBoolean(config.getProperty("musicaActivada"));
    }
    public void setMusicaDeFondoActivada(boolean musicaDeFondoActivada){
        config.setProperty("musicaActivada", String.valueOf(musicaDeFondoActivada));
    }
    public boolean isPantallaCompleta() {
        return Boolean.parseBoolean(config.getProperty("pantallaCompleta"));
    }
    public void setPantallaCompleta(boolean pantallaCompleta){
        config.setProperty("pantallaCompleta", String.valueOf(pantallaCompleta));
    }
    public String getPistaMusical(){
        return config.getProperty("pistaMusical");
    }
    public void setPistaMusical(String pistaMusical){
        config.setProperty("pistaMusical", pistaMusical);
    }
    public String getSkin(){
        return config.getProperty("skinPersonaje");
    }
    public void setSkin(String skin){
        config.setProperty("skinPersonaje", skin);
    }
    public String getTeclaCavar(){
        return config.getProperty("teclaCavar");
    }
    public void setTeclaCavar(String teclaCavar){
        config.setProperty("teclaCavar", teclaCavar);
    }
    public String getTeclaEfectos(){
        return config.getProperty("teclaEfectos");
    }
    public void setTeclaEfectos(String teclaEfectos){
        config.setProperty("teclaEfectos", teclaEfectos);
    }
    public String getTeclaMusica(){
        return config.getProperty("teclaMusica");
    }
    public void setTeclaMusica(String teclaMusica){
        config.setProperty("teclaMusica", teclaMusica);
    }
    public String getTeclaIniciar(){
        return config.getProperty("teclaIniciar");
    }
    public void setTeclaIniciar(String teclaIniciar){
        config.setProperty("teclaIniciar", teclaIniciar);
    }
}
