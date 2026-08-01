package Pong;

import java.awt.*;
import launcher.Boton;

public class PantallaConfiguracionPong {
    public enum Accion {
        NINGUNA,
        GUARDAR,
        CAMBIO_PANTALLA,
        VOLVER
    }

    private final int ancho;
    private final int alto;
    private final ConfiguracionPong config;

    private final String[] skinsBarras = {"original", "moderno", "delgado"};
    private final String[] skinsCancha = {"original", "moderno", "oscuro"};
    private final String[] skinsPelota = {"original", "cuadrada", "triangulo"};
    private final String[] pistas = {"original", "piano", "lofi"};
    private final int[] puntuaciones = {11, 15};

    private int indiceSkinBarras;
    private int indiceSkinCancha;
    private int indiceSkinPelota;
    private int indicePista;
    private int indicePuntuacion;
    private boolean sonidoActivado;
    private boolean pantallaCompleta;

    private Boton btnSonido;
    private Boton btnPantalla;
    private Boton btnSkinBarras;
    private Boton btnSkinCancha;
    private Boton btnSkinPelota;
    private Boton btnPista;
    private Boton btnPuntuacion;
    private Boton btnGuardar;
    private Boton btnReset;
    private Boton btnVolver;

    public PantallaConfiguracionPong(int ancho, int alto, ConfiguracionPong config) {
        this.ancho = ancho;
        this.alto = alto;
        this.config = config;
    }

    public void iniciar() {
        cargarValoresConfiguracion();
        crearBotones();
    }

    public Accion manejarClick(int mx, int my) {
        boolean pantallaAnterior = pantallaCompleta;
        Accion accion = Accion.NINGUNA;

        if (btnSonido.contienePunto(mx, my)) sonidoActivado = !sonidoActivado;
        if (btnPantalla.contienePunto(mx, my)) pantallaCompleta = !pantallaCompleta;
        if (btnPista.contienePunto(mx, my)) indicePista = (indicePista + 1) % pistas.length;
        if (btnPuntuacion.contienePunto(mx, my)) indicePuntuacion = (indicePuntuacion + 1) % puntuaciones.length;
        if (btnSkinCancha.contienePunto(mx, my)) indiceSkinCancha = (indiceSkinCancha + 1) % skinsCancha.length;
        if (btnSkinBarras.contienePunto(mx, my)) indiceSkinBarras = (indiceSkinBarras + 1) % skinsBarras.length;
        if (btnSkinPelota.contienePunto(mx, my)) indiceSkinPelota = (indiceSkinPelota + 1) % skinsPelota.length;

        if (btnGuardar.contienePunto(mx, my)) {
            guardarConfiguracion();
            accion = Accion.GUARDAR;
        }
        if (btnReset.contienePunto(mx, my)) {
            config.restablecer();
            cargarValoresConfiguracion();
            accion = Accion.GUARDAR;
        }
        if (btnVolver.contienePunto(mx, my)) {
            guardarConfiguracion();
            accion = Accion.VOLVER;
        }

        actualizarTextos();
        if (pantallaCompleta != pantallaAnterior && accion != Accion.VOLVER) {
            accion = Accion.CAMBIO_PANTALLA;
        }
        return accion;
    }

    public void actualizarHover(int mx, int my) {
        Boton[] botonesConfig = {
            btnSonido, btnPantalla, btnPista, btnPuntuacion,
            btnSkinCancha, btnSkinBarras, btnSkinPelota,
            btnGuardar, btnReset, btnVolver
        };
        for (Boton boton : botonesConfig) {
            boton.setHover(boton.contienePunto(mx, my));
        }
    }

    public void renderizar(Graphics2D bg) {
        bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        bg.setColor(new Color(25, 25, 35));
        bg.fillRect(0, 0, ancho, alto);

        bg.setColor(Color.WHITE);
        bg.setFont(new Font("Arial", Font.BOLD, 38));
        String titulo = "CONFIGURACION";
        FontMetrics fm = bg.getFontMetrics();
        bg.drawString(titulo, (ancho - fm.stringWidth(titulo)) / 2, 70);

        bg.setFont(new Font("Arial", Font.PLAIN, 20));
        bg.drawString("General", 100, 125);
        bg.drawString("Apariencia", 460, 125);

        btnSonido.dibujar(bg);
        btnPantalla.dibujar(bg);
        btnPista.dibujar(bg);
        btnPuntuacion.dibujar(bg);
        btnSkinCancha.dibujar(bg);
        btnSkinBarras.dibujar(bg);
        btnSkinPelota.dibujar(bg);
        btnGuardar.dibujar(bg);
        btnReset.dibujar(bg);
        btnVolver.dibujar(bg);
    }

    private void cargarValoresConfiguracion() {
        sonidoActivado = config.isSonidoActivado();
        pantallaCompleta = config.isPantallaCompleta();
        indiceSkinBarras = indiceDe(skinsBarras, config.getSkinBarras());
        indiceSkinCancha = indiceDe(skinsCancha, config.getSkinCancha());
        indiceSkinPelota = indiceDe(skinsPelota, config.getSkinPelota());
        indicePista = indiceDe(pistas, config.getPistaMusical());
        indicePuntuacion = config.getPuntuacionMaxima() == 15 ? 1 : 0;
    }

    private int indiceDe(String[] opciones, String valor) {
        int indice = 0;
        for (int i = 0; i < opciones.length; i++) {
            if (opciones[i].equals(valor)) {
                indice = i;
            }
        }
        return indice;
    }

    private void crearBotones() {
        btnSonido = new Boton(100, 150, 240, 45, "");
        btnPantalla = new Boton(100, 215, 240, 45, "");
        btnPista = new Boton(100, 280, 240, 45, "");
        btnPuntuacion = new Boton(100, 345, 240, 45, "");

        btnSkinCancha = new Boton(460, 150, 240, 45, "");
        btnSkinBarras = new Boton(460, 215, 240, 45, "");
        btnSkinPelota = new Boton(460, 280, 240, 45, "");

        btnGuardar = new Boton(190, 500, 130, 45, "GUARDAR");
        btnReset = new Boton(335, 500, 130, 45, "RESET");
        btnVolver = new Boton(480, 500, 130, 45, "VOLVER");

        actualizarTextos();
    }

    private void actualizarTextos() {
        btnSonido.setTexto("Sonido: " + (sonidoActivado ? "ON" : "OFF"));
        btnPantalla.setTexto("Pantalla: " + (pantallaCompleta ? "FULL" : "VENTANA"));
        btnPista.setTexto("Musica: " + pistas[indicePista]);
        btnPuntuacion.setTexto("Puntos: " + puntuaciones[indicePuntuacion]);
        btnSkinCancha.setTexto("Cancha: " + skinsCancha[indiceSkinCancha]);
        btnSkinBarras.setTexto("Barras: " + skinsBarras[indiceSkinBarras]);
        btnSkinPelota.setTexto("Pelota: " + skinsPelota[indiceSkinPelota]);
    }

    private void guardarConfiguracion() {
        config.setSonidoActivado(sonidoActivado);
        config.setPantallaCompleta(pantallaCompleta);
        config.setSkinBarras(skinsBarras[indiceSkinBarras]);
        config.setSkinCancha(skinsCancha[indiceSkinCancha]);
        config.setSkinPelota(skinsPelota[indiceSkinPelota]);
        config.setPistaMusical(pistas[indicePista]);
        config.setPuntuacionMaxima(puntuaciones[indicePuntuacion]);
        config.guardar();
    }
}
