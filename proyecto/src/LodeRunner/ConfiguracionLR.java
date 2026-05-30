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
            file.delete();
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

class VentanaConfiguracion extends JDialog{
    private ConfiguracionLR config;

    public VentanaConfiguracion(ConfiguracionLR config, JFrame padre){
        super(padre, "Configuracion", true); //modal hace que se superponga sobre el juego
        this.config = config;

        setSize(400, 400); //ventana de 400 ancho y 400 de largo
        setLayout(new GridLayout(6, 1));
        //1. CheckBox Pantalla Completa
        JCheckBox chkPantalla = new JCheckBox("Pantalla completa");
        chkPantalla.setSelected(config.isPantallaCompleta());
        add(chkPantalla);
        //2. CheckBox sonido
        JCheckBox chkSonido = new JCheckBox("Efectos de sonido");
        chkSonido.setSelected(config.isEfectosDeSonidoActivados());
        add(chkSonido);
        //3. CheckBox musica
        JCheckBox chkMusica = new JCheckBox("Musica de fondo");
        chkMusica.setSelected(config.isMusicaDeFondoActivada());
        //4. ComboBox skins
        JPanel panelSkin = new JPanel();
        panelSkin.add(new JLabel("Skin de personaje: "));
        String[] skins = {"original", "moderno", "retro"};
        JComboBox<String> comboSkins = new JComboBox<>(skins);
        comboSkins.setSelectedItem(config.getSkin());
        panelSkin.add(comboSkins);
        add(panelSkin);
        //5. ComboBox pista musical
        JPanel panelMusica = new JPanel();
        panelMusica.add(new JLabel("Pista musical: "));
        String[] pistas = {"original", "8-bit", "retro"};
        JComboBox<String> comboPistas = new JComboBox<>(pistas);
        comboPistas.setSelectedItem(config.getPistaMusical());
        panelMusica.add(comboPistas);
        add(panelMusica);
        //6. Teclas

        //7. Botones
        JPanel panelBotones = new JPanel();
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> {
            config.setPantallaCompleta(chkPantalla.isSelected());
            config.setEfectosDeSonidoActivados(chkSonido.isSelected());
            config.setMusicaDeFondoActivada(chkMusica.isSelected());
            config.setSkin(comboSkins.getSelectedItem().toString());
            config.setPistaMusical(comboPistas.getSelectedItem().toString());
            config.guardar();
            dispose();
        });
        JButton btnReset = new JButton("Reset");
        btnReset.addActionListener(e -> {
            config.setearPorDefecto();
        });
        panelBotones.add(btnGuardar);
        panelBotones.add(btnReset);
        add(panelBotones);
    }
}