# Retro Launcher — Proyecto POO 2026

Plataforma de videojuegos retro desarrollada en Java con interfaz gráfica Swing, usando los principios de la Programación Orientada a Objetos: encapsulamiento, herencia, polimorfismo y abstracción.

Incluye tres juegos clásicos: Space Invaders, Lode Runner y Pong.

---

## Requisitos del sistema

- Java JDK: 17 o superior
- Sistema operativo: Windows, macOS o Linux
- No requiere instalación de dependencias externas

---

## Cómo compilar y ejecutar

### Desde IntelliJ IDEA

1. Clonar o descomprimir el proyecto
2. Abrir IntelliJ IDEA → 'File -> Open' -> seleccionar la carpeta 'proyecto/'
3. Verificar que el JDK esté configurado: 'File -> Project Structure -> SDK'
4. Marcar 'proyecto/resources' como Resources Root: click derecho -> `Mark Directory as -> Resources Root`
5. Ejecutar la clase 'Main.java'

### Desde línea de comandos

bash
# Desde la raíz del proyecto
cd proyecto

# Compilar
javac -d out/production/proyecto -sourcepath src src/Main.java

# Ejecutar
java -cp out/production/proyecto:resources Main
```

### Ejecutar el JAR

```bash
java -jar RetroArcade.jar
```

## Cómo jugar

Al iniciar la aplicación se abre el Launcher, desde donde podés seleccionar cualquiera de los tres juegos.

---

### Space Invaders

Eliminá todas las oleadas de aliens antes de que lleguen al suelo.

Controles por defecto:

| Acción | Tecla |
|--------|-------|
| Mover izquierda | ← Flecha izquierda |
| Mover derecha | → Flecha derecha |
| Disparar | Barra espaciadora |

Mecánicas:
- Solo podés tener un proyectil activo a la vez
- Cuatro escudos destructibles te protegen (se degradan entre niveles)
- Los aliens se aceleran a medida que quedan menos
- Una nave nodriza aparece periódicamente en la parte superior
- Cada nivel la formación de aliens empieza una fila más abajo

Puntuación:

| Alien | Puntos |
|-------|--------|
| Calamar (fila inferior) | 10 pts |
| Cangrejo (filas centrales) | 20 pts |
| Pulpo (filas superiores) | 30 pts |
| Nave nodriza | 50 a 300 pts |

---

### Lode Runner

Recolectá todo el oro del nivel mientras evitás a los guardias.

**Controles por defecto:**

| Acción | Tecla |
|--------|-------|
| Mover | Flechas direccionales |
| Cavar izquierda | Q |
| Cavar derecha | W |
| Iniciar juego | Enter |

**Mecánicas:**
- El personaje no puede saltar
- Cavá agujeros para atrapar a los guardias temporalmente
- Los agujeros se cierran después de 5 segundos
- Al recoger todo el oro aparece una escalera oculta para completar el nivel
- Mínimo 3 niveles con diseños únicos

**Puntuación:**
- Recolectar oro
- Atrapar guardias
- Completar el nivel
- Tiempo sobrante al terminar

---

### Pong

Tenis de mesa clásico para dos jugadores (humano vs humano o humano vs CPU).

**Controles por defecto:**

| Jugador | Arriba | Abajo |
|---------|--------|-------|
| Jugador 1 | ↑ Flecha arriba | ↓ Flecha abajo |
| Jugador 2 | W | S |

**Mecánicas:**
- Las paletas se mueven en el eje vertical
- La pelota se acelera progresivamente con cada rebote
- El ángulo de rebote depende de dónde golpee la paleta
- La partida termina cuando un jugador alcanza el puntaje límite configurado

---

## Configuración

Cada juego tiene su propia pantalla de configuración accesible desde su menú. Los valores se guardan automáticamente en archivos `.properties`:

| Archivo | Juego |
|---------|-------|
| `config_spaceinvaders.properties` | Space Invaders |
| `config_loderunner.properties` | Lode Runner |
| `config_pong.properties` | Pong |

**Parámetros configurables por juego:**

- Modo ventana / pantalla completa
- Sonido activado / desactivado
- Skins de personajes y elementos
- Teclas de control personalizables
- Selección de pista musical
- Velocidad de los invasores (Space Invaders)
- Puntaje límite de la partida (Pong)
- Botón **RESET** para restaurar valores por defecto

---

## Ranking

Cada juego guarda los 10 mejores puntajes en archivos de texto:

| Archivo | Juego |
|---------|-------|
| `ranking_spaceinvaders.txt` | Space Invaders |
| `ranking_pong.txt` | Pong |

Cada entrada contiene: **nombre del jugador**, **nivel alcanzado**, **puntaje** y **fecha**.

El ranking se puede consultar desde el menú de cada juego, antes de comenzar la partida y al finalizar.

---

## Arquitectura del sistema

El proyecto aplica una arquitectura en capas basada en herencia:

```
GameLoop          ← bucle puro
  └── JGame       ← agrega ventana Swing y periféricos
        └── Videojuego    ← clase base de todos los juegos
              ├── SpaceInvaders
              ├── MenuSpaceInvaders
              ├── LodeRunnerMain
              └── Pong

Entidad           ← base de todos los objetos del juego
  ├── Jugador     ← CanonJugador, PersonajeLodeRunner, Paleta
  ├── Enemigo     ← Alien, NaveNodriza
  └── Proyectil   ← ProyectilCanon, ProyectilAlien

GestorConfiguracionBase   ← configuración base compartida
  ├── GestorConfiguracionSpaceInvaders
  ├── ConfiguracionLR
  └── ConfiguracionPong
```

---

## Integrantes

| Integrante | Juego |
|-----------|-------|
| Bruno | Space Invaders |
| [Compañero 2] | Lode Runner |
| [Compañero 3] | Pong |

---

## Tecnologías utilizadas

- **Java** — lenguaje de programación
- **Java Swing** — interfaz gráfica
- **javax.sound** — reproducción de audio
- **Java AWT** — gráficos 2D y manejo de eventos
- **Properties API** — persistencia de configuración
- **Git / GitHub** — control de versiones

---

## 📄 Licencia

Proyecto académico desarrollado para la materia **Programación Orientada a Objetos — 2026**.