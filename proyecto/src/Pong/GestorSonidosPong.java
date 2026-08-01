package Pong;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Clase para gestionar sonidos y musica en Pong.
 * Permite cargar y reproducir archivos de audio WAV.
 */
public class GestorSonidosPong {
    // Clips de audio para diferentes sonidos
    private Clip musicaFondo;
    private Clip sonidoRebote;
    private Clip sonidoPunto;
    private Clip sonidoGameOver;

    // Flag para saber si el sonido esta habilitado
    private boolean sonidoActivado;

    // Ruta base para archivos de audio dentro del classpath/JAR
    private static final String RUTA_AUDIO = "/sonido/Pong/";

    /**
     * Constructor: inicializa el gestor de sonidos
     * @param sonidoActivado true si el sonido debe estar activado por defecto
     */
    public GestorSonidosPong(boolean sonidoActivado) {
        this.sonidoActivado = sonidoActivado;
    }

    /**
     * Carga un archivo de audio WAV y lo devuelve como Clip
     * @param ruta Ruta relativa del archivo WAV
     * @return El Clip cargado, o null si hay error
     */
    private Clip cargarAudio(String ruta) {
        try {
            String rutaClasspath = RUTA_AUDIO + ruta;
            InputStream recursoAudio = getClass().getResourceAsStream(rutaClasspath);

            if (recursoAudio == null) {
                System.out.println("Archivo de audio no encontrado en classpath: " + rutaClasspath);
                return null;
            }

            // El buffer permite que AudioSystem lea correctamente recursos dentro de un JAR.
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                new BufferedInputStream(recursoAudio)
            );

            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            return clip;

        } catch (UnsupportedAudioFileException e) {
            System.out.println("Formato de audio no soportado: " + ruta);
            return null;
        } catch (IOException e) {
            System.out.println("Error al leer archivo de audio: " + ruta);
            return null;
        } catch (LineUnavailableException e) {
            System.out.println("Linea de audio no disponible");
            return null;
        }
    }

    /**
     * Carga todos los archivos de audio necesarios
     * Se llama durante la inicializacion del juego
     */
    public void cargarTodosSonidos() {
        if (!sonidoActivado) {
            System.out.println("Sonido desactivado en configuracion");
            return;
        }

        System.out.println("Cargando archivos de audio");

        sonidoRebote = cargarAudio("rebote_pelota.wav");
        sonidoPunto = cargarAudio("punto_anotado.wav");
        sonidoGameOver = cargarAudio("game_over.wav");

        System.out.println("Sonidos cargados (si los archivos existen)");
    }

    /**
     * Reproduce el sonido de rebote cuando la pelota golpea una paleta o borde
     */
    public void reproducirRebote() {
        if (!sonidoActivado || sonidoRebote == null) return;

        sonidoRebote.setFramePosition(0);
        sonidoRebote.start();
    }

    /**
     * Reproduce el sonido cuando alguien anota un punto
     */
    public void reproducirPunto() {
        if (!sonidoActivado || sonidoPunto == null) return;

        sonidoPunto.setFramePosition(0);
        sonidoPunto.start();
    }

    /**
     * Reproduce el sonido de Game Over al terminar la partida
     */
    public void reproducirGameOver() {
        if (!sonidoActivado || sonidoGameOver == null) return;

        sonidoGameOver.setFramePosition(0);
        sonidoGameOver.start();
    }

    /**
     * Reproduce la musica de fondo en loop
     * @param pista Nombre de la pista: "original" u otra
     */
    public void reproducirMusica(String pista) {
        if (!sonidoActivado) return;

        String archivo = "musica_pong_" + pista + ".wav";

        limpiarMusicaFondo();
        musicaFondo = cargarAudio(archivo);

        if (musicaFondo != null) {
            musicaFondo.loop(Clip.LOOP_CONTINUOUSLY);
            System.out.println("Reproduciendo musica: " + pista);
        }
    }

    /**
     * Detiene la musica de fondo
     */
    public void detenerMusica() {
        if (musicaFondo != null && musicaFondo.isRunning()) {
            musicaFondo.stop();
        }
    }

    private void limpiarMusicaFondo() {
        detenerMusica();
        if (musicaFondo != null) {
            musicaFondo.close();
            musicaFondo = null;
        }
    }

    /**
     * Activa o desactiva el sonido
     * @param activado true para activar, false para desactivar
     */
    public void setSonidoActivado(boolean activado) {
        this.sonidoActivado = activado;

        if (!activado) {
            detenerMusica();
        }
    }

    /**
     * Limpia todos los recursos de audio
     * Se llama al cerrar el juego
     */
    public void limpiar() {
        detenerMusica();

        cerrarClip(musicaFondo);
        cerrarClip(sonidoRebote);
        cerrarClip(sonidoPunto);
        cerrarClip(sonidoGameOver);

        musicaFondo = null;
        sonidoRebote = null;
        sonidoPunto = null;
        sonidoGameOver = null;
    }

    private void cerrarClip(Clip clip) {
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.close();
        }
    }
}
