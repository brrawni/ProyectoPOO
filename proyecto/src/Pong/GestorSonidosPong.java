package Pong;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Clase para gestionar sonidos y música en Pong.
 * Permite cargar y reproducir archivos de audio WAV.
 
 */
public class GestorSonidosPong {
    // Clips de audio para diferentes sonidos
    private Clip musicaFondo;
    private Clip sonidoRebote;
    private Clip sonidoPunto;
    private Clip sonidoGameOver;

    // Flag para saber si el sonido está habilitado
    private boolean sonidoActivado;

    // Ruta base para archivos de audio
    private static final String RUTA_AUDIO = "proyecto/resources/sonido/Pong/";

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
            // Buscar archivo de audio
            File archivoAudio = new File(RUTA_AUDIO + ruta);
            
            // Si el archivo no existe, imprimir advertencia y devolver null
            if (!archivoAudio.exists()) {
                System.out.println("Archivo de audio no encontrado: " + ruta);
                return null;
            }

            // Obtener información del archivo de audio
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(archivoAudio);
            
            // Crear clip de audio
            Clip clip = AudioSystem.getClip();
            
            // Abrir el clip con el stream de audio
            clip.open(audioInputStream);
            
            return clip;

        } catch (UnsupportedAudioFileException e) {
            System.out.println("Formato de audio no soportado: " + ruta);
            return null;
        } catch (IOException e) {
            System.out.println("Error al leer archivo de audio: " + ruta);
            return null;
        } catch (LineUnavailableException e) {
            System.out.println("Línea de audio no disponible");
            return null;
        }
    }

    /**
     * Carga todos los archivos de audio necesarios
     * Se llama durante la inicialización del juego
     */
    public void cargarTodosSonidos() {
        if (!sonidoActivado) {
            System.out.println("Sonido desactivado en configuración");
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
        
        // Restablecer posición al inicio
        sonidoRebote.setFramePosition(0);
        // Reproducir
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
     * Reproduce la música de fondo en loop
     * @param pista Nombre de la pista: "original" u otra
     */
    public void reproducirMusica(String pista) {
        if (!sonidoActivado) return;

        // Determinar archivo según la pista seleccionada
        String archivo = "musica_pong_" + pista + ".wav";
        
        limpiarMusicaFondo();
        musicaFondo = cargarAudio(archivo);
        
        if (musicaFondo != null) {
            // Reproducir en loop infinito (valor negativo = infinito)
            musicaFondo.loop(Clip.LOOP_CONTINUOUSLY);
            System.out.println("Reproduciendo música: " + pista);
        }
    }

    /**
     * Detiene la música de fondo
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
            // Detener todo si se desactiva
            detenerMusica();
        }
    }

    /**
     * @return true si el sonido está activado
     */
    public boolean isSonidoActivado() {
        return sonidoActivado;
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
