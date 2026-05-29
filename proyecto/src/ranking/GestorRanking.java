package ranking;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GestorRanking {

    private List<EntradaRanking> entradas = new ArrayList<>();
    private static final String RUTA     = "ranking_spaceinvaders.txt";
    private static final int    MAX      = 10;

    public GestorRanking() {
        cargar();
    }

    public void agregarEntrada(EntradaRanking entrada) {
        entradas.add(entrada);
        entradas.sort(Comparator.comparingInt(EntradaRanking::getPuntaje).reversed());
        if (entradas.size() > MAX) {
            entradas = entradas.subList(0, MAX);
        }
        guardar();
    }

    public List<EntradaRanking> obtenerTop10() {
        return entradas;
    }

    public void cargar() {
        entradas.clear();
        File archivo = new File(RUTA);
        if (!archivo.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 4) {
                    entradas.add(new EntradaRanking(
                        partes[0],
                        Integer.parseInt(partes[1]),
                        Integer.parseInt(partes[2]),
                        LocalDate.parse(partes[3])
                    ));
                }
            }
        } catch (IOException e) {
            System.out.println("No se pudo cargar el ranking: " + e.getMessage());
        }
    }

    public void guardar() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA))) {
            for (EntradaRanking e : entradas) {
                pw.println(e.toString());
            }
        } catch (IOException e) {
            System.out.println("No se pudo guardar el ranking: " + e.getMessage());
        }
    }
}