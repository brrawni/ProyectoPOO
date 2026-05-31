package spaceinvaders;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class FormacionAlien {
    private List<Escudo>        escudos      = new ArrayList<>();
    private List<ProyectilAlien> proyectiles = new ArrayList<>();
    private float velocidad              = 1.0f;
    private Alien[][] aliens;
    private int filas;
    private int columnas;
    private int direccion                = 1;
    private float multiplicadorVelocidad = 1.0f;
    private int ticksMovimiento          = 0;
    private int intervaloMovimiento      = 8;

    public FormacionAlien(int filas, int columnas, float velocidad, int yInicial) {
        this.velocidad = velocidad;
        this.filas     = filas;
        this.columnas  = columnas;
        aliens = new Alien[filas][columnas];

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                int tipo;
                if (i == 0)            tipo = Alien.PULPO;
                else if (i == filas-1) tipo = Alien.CALAMAR;
                else                   tipo = Alien.CANGREJO;
                aliens[i][j] = new Alien(tipo, 50 + j * 40, yInicial + i * 40);
            }
        }
    }

    public Alien[][] getAliens() { return aliens; }

    public void moverTodos() {
        ticksMovimiento++;
        if (ticksMovimiento < intervaloMovimiento) return;
        ticksMovimiento = 0;

        boolean cambioDireccion = false;
        for (Alien[] fila : aliens) {
            for (Alien alien : fila) {
                if (alien != null && alien.estaVivo()) {
                    alien.actualizar();
                    alien.setVelocidad(velocidad * multiplicadorVelocidad);
                    alien.setDireccion(direccion);
                    alien.mover();
                    if (alien.obtenerX() <= 0 || alien.obtenerX() >= 760) {
                        cambioDireccion = true;
                    }
                }
            }
        }

        if (cambioDireccion) {
            direccion *= -1;
            for (Alien[] fila : aliens) {
                for (Alien alien : fila) {
                    if (alien != null && alien.estaVivo()) {
                        alien.bajar(20);
                    }
                }
            }
        }
    }

    public void dibujarFormacion(Graphics2D g) {
        for (Alien[] fila : aliens) {
            for (Alien alien : fila) {
                if (alien != null && alien.estaVivo()) {
                    alien.dibujar(g);
                }
            }
        }
        g.setColor(java.awt.Color.RED);
        for (ProyectilAlien p : proyectiles) {
            p.dibujar(g);
        }
    }

    public int contarVivos() {
        int contador = 0;
        for (Alien[] fila : aliens) {
            for (Alien alien : fila) {
                if (alien != null && alien.estaVivo()) contador++;
            }
        }
        return contador;
    }

    public boolean llegoAlSuelo() {
        for (Alien[] fila : aliens) {
            for (Alien alien : fila) {
                if (alien != null && alien.estaVivo() && alien.obtenerY() >= 400) {
                    return true;
                }
            }
        }
        return false;
    }

    public void disparoAleatorio(CanonJugador jugador) {
        List<Alien> aliensVivos = new ArrayList<>();
        for (Alien[] fila : aliens) {
            for (Alien alien : fila) {
                if (alien != null && alien.estaVivo()) aliensVivos.add(alien);
            }
        }
        if (!aliensVivos.isEmpty()) {
            int indice    = (int)(Math.random() * aliensVivos.size());
            Alien tirador = aliensVivos.get(indice);
            proyectiles.add(new ProyectilAlien(
                    tirador.obtenerX() + 15,
                    tirador.obtenerY() + 30,
                    jugador,
                    escudos
            ));
        }
    }

    public void actualizarProyectiles() {
        for (int i = proyectiles.size() - 1; i >= 0; i--) {
            ProyectilAlien p = proyectiles.get(i);
            p.actualizar();
            if (!p.estaActivo()) {
                proyectiles.remove(i);
            }
        }
    }

    public void actualizarVelocidad() {
        int vivos = contarVivos();
        int total = filas * columnas;
        intervaloMovimiento = Math.max(5, 30 - (total - vivos));
    }

    public void setEscudos(List<Escudo> escudosDelNivel) {
        this.escudos = escudosDelNivel;
    }
}