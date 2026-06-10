package spaceinvaders;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.*;

public class GestorSonidosSpaceInvaders {
    private Clip musicaMenu;
    private Clip musicaPartida;
    private String pistaActualMenu;
    private boolean sonidoActivado;
    

    public GestorSonidosSpaceInvaders(boolean sonidoActivado) {
        this.sonidoActivado = sonidoActivado;
    }

    private Clip cargarAudio(String ruta) {
    try {
        String rutaClasspath = "/sonido/spaceinvaders/" + ruta;
        InputStream recurso = getClass().getResourceAsStream(rutaClasspath);
        
        if (recurso == null) {
            System.out.println("no encontrado en classpath: " + rutaClasspath);
            return null;
        }

        AudioInputStream audio = AudioSystem.getAudioInputStream(
            new BufferedInputStream(recurso)
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
        }); //libera recursos cuando termina de reproducirse
    }

    public void reproducirMusicaMenu() {
        if (!sonidoActivado) return;

        if (musicaMenu != null && musicaMenu.isRunning()) {
            return;
        }

        musicaMenu = cargarAudio("musicaMenu.wav");
        if (musicaMenu != null) {
            musicaMenu.loop(Clip.LOOP_CONTINUOUSLY);
            pistaActualMenu = "musicaMenu.wav";
        }
    }

    public void reproducirMusica(String pista) {
        if (!sonidoActivado) return;
        // Si ya se está reproduciendo la misma pista, no hacer nada
        if (musicaMenu != null && pista.equals(pistaActualMenu)) {
            if (musicaMenu.isRunning()) return;
            // Si existe pero está parada, volver a reproducir sin recargar
            musicaMenu.loop(Clip.LOOP_CONTINUOUSLY);
            return;
        }

        // Si hay otra pista cargada, liberarla
        if (musicaMenu != null) {
            musicaMenu.stop();
            musicaMenu.close();
            musicaMenu = null;
            pistaActualMenu = null;
        }

        musicaMenu = cargarAudio(pista);
        if (musicaMenu != null) {
            musicaMenu.loop(Clip.LOOP_CONTINUOUSLY); //reproduce la música en bucle
            pistaActualMenu = pista;
            System.out.println("reproduciendo " + pista);
        } else {
            System.out.println("No se pudo cargar pista: " + pista);
        }
    }

    public void reproducirMusicaPartida(String pista) {
        if (!sonidoActivado) return;
        if (musicaPartida != null && musicaPartida.isRunning()) {
            musicaPartida.stop();
            musicaPartida.close();
            musicaPartida = null;
        }
        musicaPartida = cargarAudio(pista);
        if (musicaPartida != null) {
            musicaPartida.loop(Clip.LOOP_CONTINUOUSLY);
            System.out.println("reproduciendo musicaPartida: " + pista);
        } else {
            System.out.println("No se pudo cargar musicaPartida: " + pista);
        }
    }

    public void detenerMusicaPartida() {
        if (musicaPartida != null && musicaPartida.isRunning()) {
            musicaPartida.stop();
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
            musicaMenu = null;
            pistaActualMenu = null;
        }
        if (musicaPartida != null) {
            musicaPartida.stop();
            musicaPartida.close();
            musicaPartida = null;
        }
    }

    public void setSonidoActivado(boolean activado) {
        this.sonidoActivado = activado;
        if (!activado) {
            detenerMusica();
            detenerMusicaPartida();
        }
    }
}
