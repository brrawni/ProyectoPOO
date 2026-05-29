package spaceinvaders;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class FormacionAlien {
    private List<Escudo> escudos = new ArrayList<>(); // Para detectar colisiones con los escudos
    private List<ProyectilAlien> proyectiles = new ArrayList<>();
    private float velocidad = 1.0f; // Velocidad base de movimiento de la formación
    private Alien[][] aliens;
    private int filas;
    private int columnas;
    private int direccion = 1; // 1 para derecha, -1 para izquierda
    private float multiplicadorVelocidad = 1.0f; // Velocidad de movimiento de la formación
    
    //correccion de movimiento 
    private int ticksMovimiento = 0;
    private int intervaloMovimiento = 8; // mueve cada 8 frames (ajustable para dificultad)


    public FormacionAlien(int filas, int columnas, float velocidad) {
        this.velocidad = velocidad;
        this.filas = filas;
        this.columnas = columnas;
        aliens = new Alien[filas][columnas];
        // Inicializar la formación con aliens de diferentes tipos
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                int tipo = (i % 3); // Alternar entre los tipos de alien
                aliens[i][j] = new Alien(tipo, 50+(j * 40),60 +(i * 40)); // Posicionar cada alien
            }
        }
    }


    public Alien[][] getAliens() {
        return aliens;
    }

    public void moverTodos() {
    //agregamos un sistema de ticks para controlar la velocidad de movimiento de la formación
    ticksMovimiento++;
    if (ticksMovimiento < intervaloMovimiento) {
        return; // No mover aún
    }
    ticksMovimiento = 0; // Reiniciar contador
    boolean cambioDireccion = false;

    for (Alien[] fila : aliens) {
        for (Alien alien : fila) {
            if (alien != null && alien.estaVivo()) {
                alien.setVelocidad(velocidad * multiplicadorVelocidad);
                alien.setDireccion(direccion); // siempre seteamos la dirección actual
                alien.mover();
                if (alien.obtenerX() <= 0 || alien.obtenerX() >= 760) {
                    cambioDireccion = true;
                }
            }
        }
    }

    // Solo cambiar dirección y bajar, sin mover de nuevo
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
        // Lógica para dibujar toda la formación de aliens
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
                if (alien != null && alien.estaVivo()) {
                    contador++;
                }
            }
        }
        return contador;
    }

    public boolean llegoAlSuelo(){
        for (Alien[] fila : aliens) {
            for (Alien alien : fila) {
                if (alien != null && alien.estaVivo() && alien.obtenerY() >= 400) { // Suponiendo que el suelo está en y=400
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
                if (alien != null && alien.estaVivo()) {
                    aliensVivos.add(alien);
                }
            }
        }
        if (!aliensVivos.isEmpty()) {
            int indice = (int)(Math.random() * aliensVivos.size());
            Alien tirador = aliensVivos.get(indice);
            proyectiles.add(new ProyectilAlien(
                    tirador.obtenerX() + 15,
                    tirador.obtenerY() + 30,
                    jugador,
                    escudos  // ← la lista ya la tiene la formación como atributo
            ));
        }
    }

    public void actualizarProyectiles() {
        // Recorremos la lista al revés para poder borrar proyectiles sin que salte error
        for (int i = proyectiles.size() - 1; i >= 0; i--) {
            ProyectilAlien p = proyectiles.get(i);
            p.actualizar(); // Esto lo mueve y chequea si te dio a vos

            //verificar que golpeo en escudo
        for (Escudo escudo : escudos) {
            if (p.estaActivo() && p.obtenerLimites().intersects(escudo.obtenerLimites())) { // Usamos el centro del proyectil, no su esquina superior izquierda
        
                int px = p.obtenerX() + p.obtenerAncho() / 2;
                int py = p.obtenerY() + p.obtenerAlto()  / 2;
                escudo.recibirImpacto(px, py);
                p.desactivar();
                break;
            }
        }

             if (!p.estaActivo()) {
                proyectiles.remove(i); // Si chocó o salió de pantalla, lo borramos
            }
        }
    }

    public void actualizarVelocidad() {
        int vivos = contarVivos();
        int total = filas * columnas;
        intervaloMovimiento = Math.max(5, 30 - (total - vivos)); // Aumenta la velocidad a medida que quedan menos aliens
    }

    public void setEscudos(List<Escudo> escudosDelNivel) {
        this.escudos = escudosDelNivel;
    }
}