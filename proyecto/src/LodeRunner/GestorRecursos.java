package LodeRunner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

public class GestorRecursos {
    private static final int TAMANIO_SPRITE = 16;
    private static final int COLUMNA_SKIN_ALTERNATIVO = 7;

    private HashMap<String, BufferedImage> imgHeroe;
    private HashMap<String, BufferedImage> imgGuardia;
    private BufferedImage hojaSprites;
    private BufferedImage hojaSpritesAlternativo;
    private static GestorRecursos gestorRecursos = new GestorRecursos();

    public GestorRecursos(){
        imgGuardia = new HashMap<>();
        imgHeroe = new HashMap<>();
        try{
            hojaSprites = ImageIO.read(getClass().getResourceAsStream("/img/loderunner/original.png"));
            hojaSpritesAlternativo = ImageIO.read(getClass().getResourceAsStream("/img/loderunner/alternativo.png"));
            for (int i = 0; i < 4; i++){
                imgGuardia.put("corriendo" + (i + 1), recortarSprite(hojaSprites, i, 3));
                imgGuardia.put("escalera" + (i + 1), recortarSprite(hojaSprites, i, 4));
                imgGuardia.put("cayendo" + (i + 1), recortarSprite(hojaSprites, i + 4, 3));
                imgGuardia.put("barra" + (i + 1), recortarSprite(hojaSprites, i + 4, 4));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Método original (para el skin base y los guardias, que sí respetan la grilla 16x16)
    private BufferedImage recortarSprite(BufferedImage hoja, int columna, int fila) {
        return hoja.getSubimage(
                columna * TAMANIO_SPRITE,
                fila * TAMANIO_SPRITE,
                TAMANIO_SPRITE,
                TAMANIO_SPRITE
        );
    }

    // NUEVO MÉTODO: Recibe Y, ancho y alto a medida. 
    // Mantenemos X como (columna * 16) asumiendo que la separación horizontal sigue siendo cada 16 px.
    private BufferedImage recortarSpriteAlternativo(BufferedImage hoja, int columna, int y, int ancho, int alto) {
        return hoja.getSubimage(
                columna * TAMANIO_SPRITE,
                y,
                ancho,
                alto
        );
    }

    public void cargarSkin(String nombreSkin){
        if ("original".equals(nombreSkin)){
            imgHeroe.put("idle1" + "_" + nombreSkin, recortarSprite(hojaSprites, 2, 2));
            for (int i = 0; i < 4; i++){
                imgHeroe.put("corriendo" + (i + 1) + "_" + nombreSkin, recortarSprite(hojaSprites, i, 0));
                imgHeroe.put("escalera" + (i + 1) + "_" + nombreSkin, recortarSprite(hojaSprites, i, 1));
                imgHeroe.put("cayendo" + (i + 1) + "_" + nombreSkin, recortarSprite(hojaSprites, i + 4, 0));
                imgHeroe.put("barra" + (i + 1) + "_" + nombreSkin, recortarSprite(hojaSprites, i + 4, 1));
            }
        } else {
            imgHeroe.put("idle1" + "_" + nombreSkin, recortarSpriteAlternativo(hojaSpritesAlternativo, COLUMNA_SKIN_ALTERNATIVO, 0, 16, 17));

            for (int i = 0; i < 4; i++){
                if (i < 3)
                    // CORRIENDO: Y=24, Ancho=16, Alto=16
                    imgHeroe.put("corriendo" + (i + 1) + "_" + nombreSkin, recortarSpriteAlternativo(hojaSpritesAlternativo, COLUMNA_SKIN_ALTERNATIVO + i, 24, 16, 16));
                if (i < 2)
                    // ESCALANDO: Y=48, Ancho=15, Alto=20
                    imgHeroe.put("escalera" + (i + 1) + "_" + nombreSkin, recortarSpriteAlternativo(hojaSpritesAlternativo, COLUMNA_SKIN_ALTERNATIVO + i, 48, 15, 20));
                // CAYENDO: Y=96, Ancho=16, Alto=19.
                // Como solo tiene 2 frames, usamos (i % 2) para alternar entre la columna base + 0 y la columna base + 1.
                int columnaCayendo = COLUMNA_SKIN_ALTERNATIVO + (i % 2);
                imgHeroe.put("cayendo" + (i + 1) + "_" + nombreSkin, recortarSpriteAlternativo(hojaSpritesAlternativo, columnaCayendo, 96, 16, 19));

                // COLGADO (Barra): Y=96, Ancho=16, Alto=19.
                // Empieza justo DESPUÉS de los 2 frames de cayendo, por eso le sumamos 2 a la columna base.
                int columnaBarra = COLUMNA_SKIN_ALTERNATIVO + 2 + i;
                imgHeroe.put("barra" + (i + 1) + "_" + nombreSkin, recortarSpriteAlternativo(hojaSpritesAlternativo, columnaBarra, 96, 16, 19));
            }
        }
    }

    public HashMap getImgGuardia(){ return imgGuardia; }
    public static GestorRecursos getInstance(){ return gestorRecursos; }
    public HashMap getImgHeroe(){ return imgHeroe; }
}