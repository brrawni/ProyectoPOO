package LodeRunner;


import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class GestorSonidosLodeRunner {
    private Clip musicaPartida;
    private Clip musicaPartidaAlternativa;
    private String pistaSeleccionada;
    private Clip musicaMenu;
    private boolean sonidoActivado;
    private static final String RUTA = "/sonido/loderunner/";
    private Clip efectoCaida;

    public GestorSonidosLodeRunner(boolean sonidoActivado, String pistaSeleccionada){
        this.sonidoActivado = sonidoActivado;
        this.pistaSeleccionada = pistaSeleccionada;
        //inicializar las musicas
        this.musicaMenu = cargarClip("menu.wav");
        this.musicaPartida = cargarClip("juego.wav");
        this.musicaPartidaAlternativa = cargarClip("juego_alternativo.wav");
        this.efectoCaida = cargarClip("caida.wav");
        reducirVolumen(this.musicaPartidaAlternativa, -20.0f); //bajamos el volumen de la musica alternativa por que esta muy alto
        reducirVolumen(this.musicaMenu, -15.0f); //lo mismo para la musica del menu
    }

    public void detenerMusicaMenu() {
        if (musicaMenu != null && musicaMenu.isRunning()) {
            musicaMenu.stop();
        }
    }
    public void detenerMusicaPartida() {
        if (musicaPartida != null && musicaPartida.isRunning()) {
            musicaPartida.stop();
        }
        if (musicaPartidaAlternativa != null && musicaPartidaAlternativa.isRunning()) {
            musicaPartidaAlternativa.stop();
        }
    }
    public void reproducirMusicaMenu() {
        if (sonidoActivado && musicaMenu != null) {
            detenerMusicaPartida(); // Nos aseguramos de que no se superpongan
            musicaMenu.setFramePosition(0); // Reinicia la canción al principio
            musicaMenu.loop(Clip.LOOP_CONTINUOUSLY); // Reproduce en bucle infinito
        }
    }
    public void reproducirMusicaPartida() {
        if (!sonidoActivado) return;

        detenerMusicaMenu();

        // Elegimos dinámicamente cuál clip reproducir según la configuración de la cátedra
        Clip musicaA_Reproducir = null;
        if (pistaSeleccionada.equals("original")) {
            musicaA_Reproducir = musicaPartida;
        } else {
            musicaA_Reproducir = musicaPartidaAlternativa;
        }

        // Reproducimos el elegido
        if (musicaA_Reproducir != null) {
            musicaA_Reproducir.setFramePosition(0);
            musicaA_Reproducir.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
    private void reducirVolumen(Clip clip, float decibeles) {
        // Primero verificamos que el sistema operativo soporte cambiar el volumen
        if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl controlVolumen = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            controlVolumen.setValue(decibeles);
        }
    }

    // METODOS PARA EFECTOS DE SONIDO CORTOS (SFX)
    public void reproducirEfectoCavar() {
        reproducirEfecto("cavar.wav");
    }

    public void reproducirEfectoAgarrarOro() {
        reproducirEfecto("recolectar.wav");
    }

    public void reproducirEfectoGameOver() {
        reproducirEfecto("Game_Over.wav");
    }

    public void reproducirEfectoGanarPartida() {
        reproducirEfecto("Partida_Ganada.wav");
    }
    public void reproducirEfectoCaida() {
        if (sonidoActivado && efectoCaida != null) {
            efectoCaida.setFramePosition(0); // Lo rebobinamos al principio
            efectoCaida.start();             // Le damos play
        }
    }

    public void detenerEfectoCaida() {
        if (efectoCaida != null && efectoCaida.isRunning()) {
            efectoCaida.stop();
        }
    }
    public void reproducirEfectoEscaleraActiva(){
        reproducirEfecto("escalera_salida.wav");
    }

    /*
     Este metodo carga el archivo .wav a la memoria.
     Se usa para las músicas largas que querés tener listas en las variables (musicaMenu, etc).
     */
    private Clip cargarClip(String nombreArchivo) {
        try {
            // Usamos getClass().getResourceAsStream() para que funcione incluso
            // si el juego se compila como un archivo .jar
            InputStream audioSrc = getClass().getResourceAsStream(RUTA + nombreArchivo);
            if (audioSrc == null) {
                System.out.println("No se encontró el archivo de audio: " + RUTA + nombreArchivo);
                return null;
            }

            // BufferedInputStream ayuda a que Java lea el archivo más rápido
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);

            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            return clip;

        } catch (Exception e) {
            System.out.println("Error al cargar el audio: " + nombreArchivo);
            e.printStackTrace();
            return null;
        }
    }
    /*
     Este metodo carga y reproduce un sonido en el momento.
     Ideal para efectos de sonido cortos (como agarrar oro) que pueden sonar
     varias veces superpuestos o muy rápido.
    */
    private void reproducirEfecto(String nombreArchivo) {
        if (!sonidoActivado) return;

        try {
            InputStream audioSrc = getClass().getResourceAsStream(RUTA + nombreArchivo);
            if (audioSrc != null) {
                InputStream bufferedIn = new BufferedInputStream(audioSrc);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);

                Clip clipSFX = AudioSystem.getClip();
                clipSFX.open(audioStream);
                clipSFX.addLineListener(event -> { //esto es para que cuando el clip deje de sonar, no ocupe mas memoria.
                    if (event.getType() == LineEvent.Type.STOP) {
                        clipSFX.close();
                    }
                });
                clipSFX.start(); // Reproduce una sola vez
            }
        } catch (Exception e) {
            System.out.println("No se pudo reproducir el efecto: " + nombreArchivo);
        }
    }
    public void setSonidoActivado(boolean activado) {
        this.sonidoActivado = activado;
        if (!activado) {
            // Si el jugador apaga el sonido en medio de la partida, detenemos todo
            detenerMusicaMenu();
            detenerMusicaPartida();
        }
    }

    public boolean isSonidoActivado() {
        return sonidoActivado;
    }

    public void setPistaSeleccionada(String pistaSeleccionada) {
        this.pistaSeleccionada = pistaSeleccionada;
    }
}
