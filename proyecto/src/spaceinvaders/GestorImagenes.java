package spaceinvaders;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class GestorImagenes {
    // Singleton para cargar y almacenar imágenes en memoria
    private static GestorImagenes instancia;
    private Map<String, BufferedImage> cache = new HashMap<>();

    private GestorImagenes() { }

    public static GestorImagenes getInstance() {
        if (instancia == null) instancia = new GestorImagenes();
        return instancia;
    }

    public BufferedImage cargar(String ruta) {
        if (cache.containsKey(ruta)) return cache.get(ruta);
        try {
            BufferedImage img = ImageIO.read(
                getClass().getResourceAsStream(ruta)
            );
            cache.put(ruta, img);
            return img;
        } catch (Exception e) {
            System.out.println("No se pudo cargar: " + ruta);
            return null;
        }
    }

    public BufferedImage colorear(BufferedImage img, Color nuevoColor) {
        if (img == null) return null;
        BufferedImage resultado = new BufferedImage(
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
        return resultado;
    }
}