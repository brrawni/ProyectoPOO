package Pong;

import java.awt.*;

/**
 * Gestiona las skins visuales de Pong.
 * La cancha, las barras y la pelota se eligen por separado.
 */
public class TemasPong {
    public enum Tema {
        ORIGINAL,
        MODERNO,
        OSCURO
    }

    private Color colorFondo;
    private Color colorTexto;
    private Color colorLinea;
    private Color colorLinea2;

    private String skinCancha;
    private String skinBarras;
    private String skinPelota;

    public TemasPong(String nombreTema) {
        this(nombreTema, "original", "original");
    }

    public TemasPong(String skinCancha, String skinBarras, String skinPelota) {
        cambiarSkinCancha(skinCancha);
        cambiarSkinBarras(skinBarras);
        cambiarSkinPelota(skinPelota);
    }

    private void aplicarTemaCancha(Tema tema) {
        switch (tema) {
            case ORIGINAL:
                colorFondo = Color.BLACK;
                colorTexto = Color.WHITE;
                colorLinea = new Color(100, 100, 100);
                colorLinea2 = new Color(50, 50, 50);
                break;
            case MODERNO:
                colorFondo = new Color(15, 15, 35);
                colorTexto = new Color(0, 255, 200);
                colorLinea = new Color(0, 200, 255);
                colorLinea2 = new Color(255, 0, 200);
                break;
            case OSCURO:
                colorFondo = new Color(25, 25, 35);
                colorTexto = new Color(180, 180, 180);
                colorLinea = new Color(80, 80, 100);
                colorLinea2 = new Color(60, 60, 80);
                break;
        }
    }

    public void cambiarTema(String nombreTema) {
        cambiarSkinCancha(nombreTema);
    }

    public void cambiarSkinCancha(String nombreSkin) {
        try {
            Tema tema = Tema.valueOf(nombreSkin.toUpperCase());
            skinCancha = tema.name().toLowerCase();
            aplicarTemaCancha(tema);
            System.out.println("Skin de cancha cambiada a: " + skinCancha);
        } catch (IllegalArgumentException | NullPointerException e) {
            skinCancha = "original";
            aplicarTemaCancha(Tema.ORIGINAL);
            System.out.println("Skin de cancha no valida: " + nombreSkin + ". Usando original.");
        }
    }

    public void cambiarSkinBarras(String nombreSkin) {
        if ("moderno".equals(nombreSkin) || "delgado".equals(nombreSkin)) {
            skinBarras = nombreSkin;
        } else {
            skinBarras = "original";
        }
    }

    public void cambiarSkinPelota(String nombreSkin) {
        if ("cuadrada".equals(nombreSkin) || "triangulo".equals(nombreSkin)) {
            skinPelota = nombreSkin;
        } else {
            skinPelota = "original";
        }
    }

    public Color getColorFondo() {
        return colorFondo;
    }

    public Color getColorPaleta() {
        switch (skinBarras) {
            case "moderno":
                return new Color(0, 255, 200);
            case "delgado":
                return new Color(255, 210, 70);
            default:
                return Color.WHITE;
        }
    }

    public Color getColorPelota() {
        switch (skinPelota) {
            case "cuadrada":
                return new Color(255, 210, 70);
            case "triangulo":
                return new Color(255, 100, 255);
            default:
                return Color.WHITE;
        }
    }

    public Color getColorTexto() {
        return colorTexto;
    }

    public Color getColorLinea() {
        return colorLinea;
    }

    public Color getColorLinea2() {
        return colorLinea2;
    }

    public Tema getTemaActual() {
        return Tema.valueOf(skinCancha.toUpperCase());
    }

    public String getNombreTema() {
        return skinCancha;
    }

    public String getSkinCancha() {
        return skinCancha;
    }

    public String getSkinBarras() {
        return skinBarras;
    }

    public String getSkinPelota() {
        return skinPelota;
    }

    public static String[] getTemasDisponibles() {
        return new String[]{"original", "moderno", "oscuro"};
    }
}
