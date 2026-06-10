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
                //animaciones guardia corriendo
                imgGuardia.put("corriendo" + (i + 1), recortarSprite(hojaSprites, i, 3));
                //animaciones guardia escalera
                imgGuardia.put("escalera" + (i + 1), recortarSprite(hojaSprites, i, 4));
                //animaciones guardia cayendo
                imgGuardia.put("cayendo" + (i + 1), recortarSprite(hojaSprites, i + 4, 3));
                //animaciones guardia barra
                imgGuardia.put("barra" + (i + 1), recortarSprite(hojaSprites, i + 4, 4));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private BufferedImage recortarSprite(BufferedImage hoja, int columna, int fila) {
        return hoja.getSubimage(
                columna * TAMANIO_SPRITE,
                fila * TAMANIO_SPRITE,
                TAMANIO_SPRITE,
                TAMANIO_SPRITE
        );
    }

    public void cargarSkin(String nombreSkin){
        if ("original".equals(nombreSkin)){
            imgHeroe.put("idle1" + "_" + nombreSkin, recortarSprite(hojaSprites, 2, 2));
            for (int i = 0; i < 4; i++){
                //animaciones heroe corriendo
                imgHeroe.put("corriendo" + (i + 1) + "_" + nombreSkin, recortarSprite(hojaSprites, i, 0));
                //animaciones heroe escalera
                imgHeroe.put("escalera" + (i + 1) + "_" + nombreSkin, recortarSprite(hojaSprites, i, 1));
                //animaciones heroe cayendo
                imgHeroe.put("cayendo" + (i + 1) + "_" + nombreSkin, recortarSprite(hojaSprites, i + 4, 0));
                //animaciones heroe barra
                imgHeroe.put("barra" + (i + 1) + "_" + nombreSkin, recortarSprite(hojaSprites, i + 4, 1));
            }
        }else {
            imgHeroe.put("idle1" + "_" + nombreSkin, recortarSprite(hojaSpritesAlternativo, COLUMNA_SKIN_ALTERNATIVO, 0));
            for (int i = 0; i < 4; i++){
                //animaciones heroe corriendo
                imgHeroe.put("corriendo" + (i + 1) + "_" + nombreSkin, recortarSprite(hojaSpritesAlternativo, COLUMNA_SKIN_ALTERNATIVO + i, 1));
                //animaciones heroe escalera
                imgHeroe.put("escalera" + (i + 1) + "_" + nombreSkin, recortarSprite(hojaSpritesAlternativo, COLUMNA_SKIN_ALTERNATIVO + i, 2));
                //animaciones heroe cayendo
                imgHeroe.put("cayendo" + (i + 1) + "_" + nombreSkin, recortarSprite(hojaSpritesAlternativo, COLUMNA_SKIN_ALTERNATIVO + i, 4));
                //animaciones heroe barra (usando la misma fila que cayendo, pero diferentes columnas)
                imgHeroe.put("barra" + (i + 1) + "_" + nombreSkin, recortarSprite(hojaSpritesAlternativo, COLUMNA_SKIN_ALTERNATIVO + i, 3));
            }
        }
    }
    public HashMap getImgGuardia(){
        return imgGuardia;
    }
    public static GestorRecursos getInstance(){ return gestorRecursos; }
    public HashMap getImgHeroe(){ return imgHeroe; }
}
