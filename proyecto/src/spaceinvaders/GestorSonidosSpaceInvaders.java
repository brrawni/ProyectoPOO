package spaceinvaders;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;

public class GestorSonidosSpaceInvaders {
    private Clip musicaMenu;
    private boolean sonidoActivado;
    private static final String RUTA_AUDIO = "proyecto/resources/sonido/spaceinvaders/";

    public GestorSonidosSpaceInvaders(boolean sonidoActivado) {
        this.sonidoActivado = sonidoActivado;
    }

    private Clip cargarAudio(String ruta) {
    try {
        String rutaClasspath = "/sonido/spaceinvaders/" + ruta;
        java.io.InputStream recurso = getClass().getResourceAsStream(rutaClasspath);
        
        if (recurso == null) {
            System.out.println("no encontrado en classpath: " + rutaClasspath);
            return null;
        }

        AudioInputStream audio = AudioSystem.getAudioInputStream(
            new java.io.BufferedInputStream(recurso)
        );
        Clip clip = AudioSystem.getClip();
        clip.open(audio);
        return clip;

    } catch (UnsupportedAudioFileException e) {
        System.out.println("formato no soportado: " + ruta);
        return null;
    } catch (IOException e) {
        System.out.println("error leyendo: " + ruta);
        return null;
    } catch (LineUnavailableException e) {
        System.out.println("linea de audio no disponible");
        return null;
    }
}

    public void reproducirEfecto(String ruta) {
        if (!sonidoActivado) return;

        Clip clip = cargarAudio(ruta);
        if (clip == null) return;

        clip.setFramePosition(0);
        clip.start();
        clip.addLineListener(event -> {
            if (event.getType() == LineEvent.Type.STOP) {
                clip.close();
            }
        });
    }

    public void reproducirMusicaMenu() {
        if (!sonidoActivado) return;

        if (musicaMenu != null && musicaMenu.isRunning()) {
            return;
        }

        musicaMenu = cargarAudio("musicaMenu.wav");
        if (musicaMenu != null) {
            musicaMenu.loop(Clip.LOOP_CONTINUOUSLY);
            System.out.println("reproduciendo musicaMenu.wav");
        }
    }

    public void detenerMusica() {
        if (musicaMenu != null && musicaMenu.isRunning()) {
            musicaMenu.stop();
        }
    }

    public void limpiar() {
        detenerMusica();
        if (musicaMenu != null) {
            musicaMenu.close();
        }
    }

    public void setSonidoActivado(boolean activado) {
        this.sonidoActivado = activado;
        if (!activado) {
            detenerMusica();
        }
    }
}
