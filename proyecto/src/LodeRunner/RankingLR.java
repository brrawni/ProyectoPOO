package LodeRunner;

import ranking.EntradaRanking;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RankingLR {
    private List<EntradaRanking> entradas = new ArrayList<>();
    private static final String RUTA     = "ranking_loderunner.txt";
    private static final int    MAX      = 10;

    public RankingLR() {
        cargar();
    }

    public void agregarEntrada(EntradaRanking entrada) {
        entradas.add(entrada);
        entradas.sort(Comparator.comparingInt(EntradaRanking::getPuntaje).reversed());

        if (entradas.size() > MAX) {
            // FIX: Envuelto en un ArrayList nuevo para evitar bugs de memoria con "subList"
            entradas = new ArrayList<>(entradas.subList(0, MAX));
        }
        guardar();
    }

    public List<EntradaRanking> obtenerTop10() {
        // Devolver una lista inmodificable para que nadie
        // desde afuera pueda hacer obtenerTop10().clear() y borrarte los datos sin querer.
        return Collections.unmodifiableList(entradas);
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
                    try {
                        // FIX: Try-catch interno. Si alguien editó mal el TXT, esta línea
                        // falla en silencio pero el resto de puntajes se cargan bien.
                        entradas.add(new EntradaRanking(
                                partes[0],
                                Integer.parseInt(partes[1]),
                                Integer.parseInt(partes[2]),
                                LocalDate.parse(partes[3])
                        ));
                    } catch (Exception ex) {
                        System.out.println("Línea corrupta ignorada en el ranking.");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("No se pudo cargar el ranking: " + e.getMessage());
        }
    }

    public void guardar() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA))) {
            for (EntradaRanking e : entradas) {
                pw.println(e.toString()); // Usa el CSV limpio
            }
        } catch (IOException e) {
            System.out.println("No se pudo guardar el ranking: " + e.getMessage());
        }
    }
}
