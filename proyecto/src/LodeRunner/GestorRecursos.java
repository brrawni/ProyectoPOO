package LodeRunner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

public class GestorRecursos {
    private HashMap<String, BufferedImage> imgHeroe;
    private HashMap<String, BufferedImage> imgGuardia;
    private BufferedImage hojaSprites;
    private static GestorRecursos gestorRecursos = new GestorRecursos();

    public GestorRecursos(){
        imgGuardia = new HashMap<>();
        imgHeroe = new HashMap<>();
        try{
            hojaSprites = ImageIO.read(getClass().getResourceAsStream("/img/loderunner/personajes.png"));
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
        imgHeroe.put("idle1" + "_" + nombreSkin, hojaSprites.getSubimage(32, 32, 16, 16));
        for (int i = 0; i < 4; i++){
            //animaciones heroe corriendo
            imgHeroe.put("corriendo" + (i + 1) + "_" + nombreSkin, hojaSprites.getSubimage(16*i, 0, 16, 16));
            //animaciones heroe escalera
            imgHeroe.put("escalera" + (i + 1) + "_" + nombreSkin, hojaSprites.getSubimage(16*i, 16, 16, 16));
            //animaciones heroe cayendo
            imgHeroe.put("cayendo" + (i + 1) + "_" + nombreSkin, hojaSprites.getSubimage(16*(i+4), 0, 16, 16));
            //animaciones heroe barra
            imgHeroe.put("barra" + (i + 1) + "_" + nombreSkin, hojaSprites.getSubimage(16*(i+4), 16, 16, 16));
        }
    }
    public HashMap getImgGuardia(){
        return imgGuardia;
    }
    public static GestorRecursos getInstance(){ return gestorRecursos; }
    public HashMap getImgHeroe(){ return imgHeroe; }
}
