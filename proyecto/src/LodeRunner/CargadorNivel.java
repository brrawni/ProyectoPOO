package LodeRunner;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CargadorNivel {
    public static int[][] cargarMatrizNivel(String rutaArchivo) {
        List<int[]> filas = new ArrayList<>();

        try {
            // Se usa getResourceAsStream para que lea bien dentro del .jar compilado
            InputStream is = CargadorNivel.class.getResourceAsStream(rutaArchivo);

            if (is == null) {
                System.err.println("No se pudo encontrar el archivo: " + rutaArchivo);
                return null;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String linea;

            while ((linea = br.readLine()) != null) {
                linea = linea.trim(); //elimina espacios en blancos al principio, entre medio o al final.
                if (!linea.isEmpty()) {
                    int[] fila = new int[linea.length()];
                    for (int i = 0; i < linea.length(); i++) {
                        // Convierte el carácter numérico '1', '0', '3', '4' a un entero int
                        fila[i] = Character.getNumericValue(linea.charAt(i));
                    }
                    filas.add(fila);
                }
            }
            br.close();

        } catch (Exception e) {
            System.err.println("Error al cargar la matriz del nivel: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

        // Convertimos la lista de filas a una matriz int[][]
        int[][] matriz = new int[filas.size()][];
        for (int i = 0; i < filas.size(); i++) {
            matriz[i] = filas.get(i);
        }

        return matriz;
    }
}
