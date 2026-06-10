package motor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Clase base para rankings de los juegos.
 * Gestiona archivo, carga, guardado, orden y limite de mejores puntajes.
 */
public class GestorRankingBase {
    protected List<EntradaRanking> entradas = new ArrayList<>();
    private final String ruta;
    private final int maxEntradas;

    public GestorRankingBase(String ruta) {
        this(ruta, 10);
    }

    public GestorRankingBase(String ruta, int maxEntradas) {
        this.ruta = ruta;
        this.maxEntradas = maxEntradas;
        cargar();
    }

    public void agregarEntrada(EntradaRanking entrada) {
        entradas.add(entrada);
        entradas.sort(Comparator.comparingInt(EntradaRanking::getPuntaje).reversed());

        if (entradas.size() > maxEntradas) {
            entradas = new ArrayList<>(entradas.subList(0, maxEntradas));
        }

        guardar();
    }

    public List<EntradaRanking> obtenerTop10() {
        return Collections.unmodifiableList(entradas);
    }

    public void limpiar() {
        entradas.clear();
        guardar();
    }

    public void cargar() {
        entradas.clear();
        File archivo = new File(ruta);
        if (!archivo.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                EntradaRanking entrada = crearEntradaDesdeLinea(linea);
                if (entrada != null) {
                    entradas.add(entrada);
                }
            }
            entradas.sort(Comparator.comparingInt(EntradaRanking::getPuntaje).reversed());
        } catch (IOException e) {
            System.out.println("No se pudo cargar el ranking: " + e.getMessage());
        }
    }

    public void guardar() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ruta))) {
            for (EntradaRanking e : entradas) {
                pw.println(convertirEntradaALinea(e));
            }
        } catch (IOException e) {
            System.out.println("No se pudo guardar el ranking: " + e.getMessage());
        }
    }

    protected EntradaRanking crearEntradaDesdeLinea(String linea) {
        String[] partes = linea.split(",");
        if (partes.length != 4) return null;

        try {
            return new EntradaRanking(
                    partes[0],
                    Integer.parseInt(partes[1]),
                    Integer.parseInt(partes[2]),
                    LocalDate.parse(partes[3])
            );
        } catch (Exception ex) {
            System.out.println("Linea corrupta ignorada en el ranking.");
            return null;
        }
    }

    protected String convertirEntradaALinea(EntradaRanking entrada) {
        return entrada.toString();
    }
}
