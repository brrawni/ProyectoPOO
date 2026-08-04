package spaceinvaders;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class GestorImagenes {
    //singleton para cargar y almacenar imágenes en memoria, evitando cargar el mismo archivo varias veces.
    private static GestorImagenes instancia;
    //tabla hash para almacenar imágenes cargadas, con la ruta como clave
    private final Map<String, BufferedImage> cache = new HashMap<>(); 

    private GestorImagenes() { }

    public static GestorImagenes getInstance() {
        if (instancia == null) instancia = new GestorImagenes();
        return instancia;
    }

    public BufferedImage cargar(String ruta) {
        BufferedImage imagen = cache.get(ruta);

        if (imagen == null) {
            try {
                java.io.InputStream recurso = getClass().getResourceAsStream(ruta);
                if (recurso == null) {
                    System.out.println("No se pudo cargar: " + ruta);
                } else {
                    imagen = ImageIO.read(recurso);
                    cache.put(ruta, imagen);
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        return imagen;
    }

    public BufferedImage colorear(BufferedImage img, Color nuevoColor) {
        BufferedImage resultado = null;
        if (img != null) {
            resultado = new BufferedImage(
                img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB
            );
            for (int y = 0; y < img.getHeight(); y++) {
                for (int x = 0; x < img.getWidth(); x++) {
                    int pixel = img.getRGB(x, y);
                    int alpha = (pixel >> 24) & 0xFF;
                    if (alpha > 0) {
                        resultado.setRGB(x, y,
                            (alpha << 24) |
                            (nuevoColor.getRed()   << 16) |
                            (nuevoColor.getGreen() << 8)  |
                            nuevoColor.getBlue()
                        );
                    }
                }
            }
        }
        return resultado;
    }
}