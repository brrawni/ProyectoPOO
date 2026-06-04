package Pong;

import java.awt.*;

/**
 * Clase para gestionar los diferentes temas visuales (skins) de Pong.
 * Permite cambiar colores y estilos según la configuración.
 * 
 * Temas disponibles (según consigna):
 * - original: Estilo clásico blanco y negro
 * - moderno: Colores vibrantes y degradados
 * - oscuro: Tema oscuro para ojos cansados
 * 
 * Cada tema define:
 * - Color de fondo
 * - Color de paletas
 * - Color de pelota
 * - Color de texto/HUD
 * - Color de líneas decorativas
 */
public class TemasPong {
    // Enumeración de temas disponibles
    public enum Tema {
        ORIGINAL,
        MODERNO,
        OSCURO
    }

    // Colores del tema actual
    private Color colorFondo;
    private Color colorPaleta;
    private Color colorPelota;
    private Color colorTexto;
    private Color colorLinea;
    private Color colorLinea2; // Para efectos de luz

    // Tema actualmente seleccionado
    private Tema temaActual;

    /**
     * Constructor: inicializa con un tema específico
     * @param nombreTema Nombre del tema: "original", "moderno", "oscuro"
     */
    public TemasPong(String nombreTema) {
        try {
            this.temaActual = Tema.valueOf(nombreTema.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Si el tema no existe, usar original
            System.out.println("⚠️ Tema no encontrado: " + nombreTema + ". Usando tema original.");
            this.temaActual = Tema.ORIGINAL;
        }

        // Aplicar colores del tema
        aplicarTema(this.temaActual);
    }

    /**
     * Aplica los colores según el tema seleccionado
     * @param tema El tema a aplicar
     */
    private void aplicarTema(Tema tema) {
        switch (tema) {
            case ORIGINAL:
                // Tema clásico: blanco y negro
                this.colorFondo = Color.BLACK;
                this.colorPaleta = Color.WHITE;
                this.colorPelota = Color.WHITE;
                this.colorTexto = Color.WHITE;
                this.colorLinea = new Color(100, 100, 100);
                this.colorLinea2 = new Color(50, 50, 50);
                break;

            case MODERNO:
                // Tema moderno: colores vibrantes
                this.colorFondo = new Color(15, 15, 35); // Azul muy oscuro
                this.colorPaleta = new Color(0, 255, 200); // Cyan fluorescente
                this.colorPelota = new Color(255, 100, 255); // Magenta
                this.colorTexto = new Color(0, 255, 200); // Cyan
                this.colorLinea = new Color(0, 200, 255); // Azul cyan
                this.colorLinea2 = new Color(255, 0, 200); // Magenta
                break;

            case OSCURO:
                // Tema oscuro: suave y relajante
                this.colorFondo = new Color(25, 25, 35); // Gris muy oscuro
                this.colorPaleta = new Color(200, 200, 200); // Gris claro
                this.colorPelota = new Color(220, 220, 220); // Gris más claro
                this.colorTexto = new Color(180, 180, 180); // Gris medio
                this.colorLinea = new Color(80, 80, 100); // Azul grisáceo
                this.colorLinea2 = new Color(60, 60, 80); // Azul más oscuro
                break;
        }
    }

    /**
     * Cambia el tema actual
     * @param nombreTema Nombre del tema: "original", "moderno", "oscuro"
     */
    public void cambiarTema(String nombreTema) {
        try {
            Tema nuevoTema = Tema.valueOf(nombreTema.toUpperCase());
            this.temaActual = nuevoTema;
            aplicarTema(nuevoTema);
            System.out.println("🎨 Tema cambiado a: " + nombreTema);
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Tema no válido: " + nombreTema);
        }
    }

    // ============== GETTERS DE COLORES ==============

    /**
     * @return Color de fondo de la pantalla
     * Se usa en Pong.gameDraw() para llenar el rectángulo de fondo
     */
    public Color getColorFondo() {
        return colorFondo;
    }

    /**
     * @return Color de las paletas (raquetas)
     * Se usa en Paleta.dibujar() para pintar el rectángulo
     */
    public Color getColorPaleta() {
        return colorPaleta;
    }

    /**
     * @return Color de la pelota
     * Se usa en Pelota.dibujar() para pintar el círculo
     */
    public Color getColorPelota() {
        return colorPelota;
    }

    /**
     * @return Color del texto (puntuación, etc.)
     * Se usa en Pong.gameDraw() para dibujar la puntuación
     */
    public Color getColorTexto() {
        return colorTexto;
    }

    /**
     * @return Color de líneas decorativas (línea central, bordes)
     * Se usa en Pong.gameDraw() para dibujar línea punteada central
     */
    public Color getColorLinea() {
        return colorLinea;
    }

    /**
     * @return Color secundario para efectos (luz, sombras)
     * Se usa en Pelota.dibujar() para efecto de brillo
     */
    public Color getColorLinea2() {
        return colorLinea2;
    }

    /**
     * @return El tema actualmente seleccionado
     */
    public Tema getTemaActual() {
        return temaActual;
    }

    /**
     * @return El nombre del tema actual en minúsculas
     */
    public String getNombreTema() {
        return temaActual.name().toLowerCase();
    }

    /**
     * Obtiene todos los temas disponibles
     * Se usa en VentanaConfiguracionPong para llenar el ComboBox
     * @return Array de nombres de temas
     */
    public static String[] getTemasDisponibles() {
        return new String[]{"original", "moderno", "oscuro"};
    }
}
