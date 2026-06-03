package LodeRunner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public final class GestorRecursos {
    private HashMap<String, BufferedImage> originalHeroe;
    private HashMap<String, BufferedImage> imgGuardia;
    private static GestorRecursos gestorRecursos = new GestorRecursos();

    public GestorRecursos(){
        imgGuardia = new HashMap<>();
        originalHeroe = new HashMap<>();
        try{
            BufferedImage hojaSprites = ImageIO.read(getClass().getResourceAsStream("/img/loderunner/personajes.png"));
            for (int i = 0; i < 4; i++){
                //animaciones guardia corriendo
                imgGuardia.put("corriendo" + (i + 1), hojaSprites.getSubimage(16*i, 3*16, 16, 16));
                //animaciones guardia escalera
                imgGuardia.put("escalera" + (i + 1), hojaSprites.getSubimage(16*i, 4*16, 16, 16));
                //animaciones guardia cayendo
                imgGuardia.put("cayendo" + (i + 1), hojaSprites.getSubimage(16*(i+4), 3*16, 16, 16));
                //animaciones guardia barra
                imgGuardia.put("barra" + (i + 1), hojaSprites.getSubimage(16*(i+4), 4*16, 16, 16));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void cargarSkin(String nombreSkin){

    }
    public HashMap getImgGuardia(){
        return imgGuardia;
    }
    public static GestorRecursos getInstance(){ return gestorRecursos; }
}
