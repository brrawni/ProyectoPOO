package spaceinvaders;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class FormacionAlien {
    private List<ProyectilAlien> proyectiles = new ArrayList<>();
    private float velocidad = 1.0f; // Velocidad base de movimiento de la formación
    private Alien[][] aliens;
    private int filas;
    private int columnas;
    private int direccion = 1; // 1 para derecha, -1 para izquierda
    private float multiplicadorVelocidad = 1.0f; // Velocidad de movimiento de la formación
     // Dirección inicial del movimiento


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
        boolean cambioDireccion = false;
        // Lógica para mover toda la formación de aliens
        for (Alien[] fila : aliens) {
            for (Alien alien : fila) {
                if (alien != null && alien.estaVivo()) {
                    alien.setVelocidad(velocidad * multiplicadorVelocidad); // Ajusta la velocidad de cada alien
                    alien.mover();
                    if (alien.obtenerX() <= 0 || alien.obtenerX() >=760) { // Suponiendo que el ancho de la pantalla es 760
                        cambioDireccion = true;
                    }
                }
            }
        }
        if (cambioDireccion){
            direccion *= -1; // Cambia la dirección
            for (Alien[] fila : aliens) {
                for (Alien alien : fila) {
                    if (alien != null && alien.estaVivo()) {
                        alien.setDireccion(direccion); // Mueve el alien en la nueva dirección
                        alien.mover(); // Mueve el alien en la nueva dirección
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
        // Lógica para que un alien dispare un proyectil de forma aleatoria
        List<Alien> aliensVivos = new ArrayList<>();
        for(Alien[] fila : aliens) {
            for (Alien alien : fila) {
                if (alien != null && alien.estaVivo()) {
                    aliensVivos.add(alien);
                }
            }
        }
        if(!aliensVivos.isEmpty()){
            int indiceAleatorio = (int)(Math.random() * aliensVivos.size());
            Alien tirador = aliensVivos.get(indiceAleatorio);
            proyectiles.add(new ProyectilAlien(tirador.obtenerX()+15, tirador.obtenerY()+30, jugador));
        }
    }

    public void actualizarProyectiles() {
        // Recorremos la lista al revés para poder borrar proyectiles sin que salte error
        for (int i = proyectiles.size() - 1; i >= 0; i--) {
            ProyectilAlien p = proyectiles.get(i);
            p.actualizar(); // Esto lo mueve y chequea si te dio a vos

            if (!p.estaActivo()) {
                proyectiles.remove(i); // Si chocó o salió de pantalla, lo borramos
            }
        }
    }

    public void actualizarVelocidad() {
        int vivos = contarVivos();
        int total = filas * columnas;
        multiplicadorVelocidad = 1.0f + (total - vivos) * 0.1f; // Aumenta la velocidad a medida que quedan menos aliens
    }
}