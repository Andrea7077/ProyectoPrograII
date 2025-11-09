/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author andre
 */
public class Tablero {



    private static final int TAMANIO = 6;
    private Pieza[][] casillas;
    private String jugador1;
    private String jugador2;
    private String turnoActual; // "BLANCO" o "NEGRO"
    private int piezasPerdidasBlanco;
    private int piezasPerdidasNegro;
    private Random random;
    private InterfazGuardado storage;

    // Constructor con jugadores
    public Tablero(String jugador1, String jugador2) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.casillas = new Pieza[TAMANIO][TAMANIO];
        this.turnoActual = "BLANCO"; // Blanco inicia
        this.piezasPerdidasBlanco = 0;
        this.piezasPerdidasNegro = 0;
        this.random = new Random();
        this.storage = new Guardado(); // Para guardar logs
        inicializarTablero(jugador1, jugador2);
    }

    private void inicializarTablero(String jugador1, String jugador2) {
        try {
            // Jugador 1 (BLANCO) - fila 0
            casillas[0][0] = new Lobo("BLANCO");
            casillas[0][1] = new Vampiros("BLANCO");
            casillas[0][2] = new Muerte("BLANCO");
            casillas[0][3] = new Muerte("BLANCO");
            casillas[0][4] = new Vampiros("BLANCO");
            casillas[0][5] = new Lobo("BLANCO");

            // Actualizar posiciones
            for (int i = 0; i < TAMANIO; i++) {
                if (casillas[0][i] != null) {
                    casillas[0][i].setPosicion(0, i);
                }
            }

            // Jugador 2 (NEGRO) - fila 5
            casillas[5][0] = new Lobo("NEGRO");
            casillas[5][1] = new Vampiros("NEGRO");
            casillas[5][2] = new Muerte("NEGRO");
            casillas[5][3] = new Muerte("NEGRO");
            casillas[5][4] = new Vampiros("NEGRO");
            casillas[5][5] = new Lobo("NEGRO");

            // Actualizar posiciones
            for (int i = 0; i < TAMANIO; i++) {
                if (casillas[5][i] != null) {
                    casillas[5][i].setPosicion(5, i);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al inicializar tablero: " + e.getMessage());
        }
    }

    // ============================================
    // MÉTODOS DE ACCESO
    // ============================================

    public Pieza getPieza(int x, int y) {
        try {
            if (x < 0 || x >= TAMANIO || y < 0 || y >= TAMANIO) {
                return null;
            }
            return casillas[x][y];
        } catch (Exception e) {
            System.err.println("Error al obtener pieza: " + e.getMessage());
            return null;
        }
    }

    public Pieza obtenerPieza(int x, int y) {
        return getPieza(x, y);
    }

    public Pieza[][] getCasillas() {
        return casillas;
    }

    public String getTurnoActual() {
        return turnoActual;
    }

    public String getTurnoJugadorActual() {
        return turnoActual.equals("BLANCO") ? jugador1 : jugador2;
    }

    public String getJugador1() {
        return jugador1;
    }

    public String getJugador2() {
        return jugador2;
    }

    public static int getTamanio() {
        return TAMANIO;
    }

    // ============================================
    // SISTEMA DE TURNOS Y RULETA
    // ============================================

    public void cambiarTurno() {
        try {
            turnoActual = turnoActual.equals("BLANCO") ? "NEGRO" : "BLANCO";
        } catch (Exception e) {
            System.err.println("Error al cambiar turno: " + e.getMessage());
        }
    }

    public int vecesPermitidasGirarRuleta() {
        try {
            int piezasPerdidas = turnoActual.equals("BLANCO") ? piezasPerdidasBlanco : piezasPerdidasNegro;

            if (piezasPerdidas >= 4) {
                return 3;
            } else if (piezasPerdidas >= 2) {
                return 2;
            } else {
                return 1;
            }
        } catch (Exception e) {
            System.err.println("Error al calcular giros de ruleta: " + e.getMessage());
            return 1;
        }
    }

    public String girarRuleta() {
        try {
            // Contar piezas disponibles del jugador actual (sin zombies)
            ArrayList<String> piezasDisponibles = new ArrayList<>();

            for (int i = 0; i < TAMANIO; i++) {
                for (int j = 0; j < TAMANIO; j++) {
                    Pieza p = casillas[i][j];
                    if (p != null && p.getColor().equals(turnoActual) && !p.getTipo().equals("ZOMBIE")) {
                        String tipo = p.getTipo();
                        if (!piezasDisponibles.contains(tipo)) {
                            piezasDisponibles.add(tipo);
                        }
                    }
                }
            }

            if (piezasDisponibles.isEmpty()) {
                return null; // No hay piezas disponibles
            }

            // Tipos disponibles en la ruleta
            String[] tipos = {"HOMBRE_LOBO", "VAMPIRO", "MUERTE"};
            String seleccionada = tipos[random.nextInt(tipos.length)];

            // Verificar si el jugador tiene esa pieza
            if (piezasDisponibles.contains(seleccionada)) {
                return seleccionada;
            } else {
                // Si no tiene, devolver la primera disponible
                return piezasDisponibles.get(0);
            }
        } catch (Exception e) {
            System.err.println("Error al girar ruleta: " + e.getMessage());
            return null;
        }
    }

    // ============================================
    // MOVIMIENTO
    // ============================================

    public boolean moverPieza(int origenX, int origenY, int destinoX, int destinoY) {
        try {
            Pieza pieza = getPieza(origenX, origenY);
            Pieza destino = getPieza(destinoX, destinoY);

            // Validaciones básicas
            if (pieza == null || destino != null) {
                return false;
            }

            // Validar que sea movimiento adyacente
            if (!esMovimientoValido(pieza, origenX, origenY, destinoX, destinoY)) {
                return false;
            }

            // Mover la pieza
            casillas[destinoX][destinoY] = pieza;
            casillas[origenX][origenY] = null;
            pieza.setPosicion(destinoX, destinoY);
            return true;
        } catch (Exception e) {
            System.err.println("Error al mover pieza: " + e.getMessage());
            return false;
        }
    }

    private boolean esMovimientoValido(Pieza pieza, int origenX, int origenY, int destinoX, int destinoY) {
        try {
            int deltaX = Math.abs(destinoX - origenX);
            int deltaY = Math.abs(destinoY - origenY);

            // Movimiento adyacente (1 casilla en cualquier dirección)
            return deltaX <= 1 && deltaY <= 1;
        } catch (Exception e) {
            System.err.println("Error al validar movimiento: " + e.getMessage());
            return false;
        }
    }

    public String loboMover2Casillas(int origenX, int origenY, int destinoX, int destinoY) {
        try {
            Pieza lobo = getPieza(origenX, origenY);

            if (!(lobo instanceof Lobo)) {
                return "Error: La pieza no es un Hombre Lobo";
            }

            int deltaX = Math.abs(destinoX - origenX);
            int deltaY = Math.abs(destinoY - origenY);

            // Validar que sea movimiento de 2 casillas
            if (!((deltaX == 2 && deltaY == 0) || (deltaX == 0 && deltaY == 2) || (deltaX == 2 && deltaY == 2))) {
                return "Error: El Lobo solo puede moverse 2 casillas en línea recta o diagonal";
            }

            // Validar que el camino esté libre
            if (!caminoLibre(origenX, origenY, destinoX, destinoY)) {
                return "Error: El camino está obstruido";
            }

            // Validar que el destino esté vacío
            if (getPieza(destinoX, destinoY) != null) {
                return "Error: El destino debe estar vacío";
            }

            // Mover
            casillas[destinoX][destinoY] = lobo;
            casillas[origenX][origenY] = null;
            lobo.setPosicion(destinoX, destinoY);

            return "✅ Hombre Lobo se movió 2 casillas a (" + destinoX + "," + destinoY + ")";
        } catch (Exception e) {
            System.err.println("Error en movimiento de lobo: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

    private boolean caminoLibre(int origenX, int origenY, int destinoX, int destinoY) {
        try {
            int pasoX = Integer.compare(destinoX, origenX);
            int pasoY = Integer.compare(destinoY, origenY);

            int x = origenX + pasoX;
            int y = origenY + pasoY;

            while (x != destinoX || y != destinoY) {
                if (getPieza(x, y) != null) {
                    return false;
                }
                x += pasoX;
                y += pasoY;
            }

            return true;
        } catch (Exception e) {
            System.err.println("Error al verificar camino: " + e.getMessage());
            return false;
        }
    }

    // ============================================
    // SISTEMA DE COMBATE
    // ============================================

    public String atacarNormal(int origenX, int origenY, int destinoX, int destinoY) {
        try {
            Pieza atacante = getPieza(origenX, origenY);
            Pieza defensor = getPieza(destinoX, destinoY);

            if (atacante == null || defensor == null) {
                return "Error: Casillas inválidas";
            }

            if (atacante.getColor().equals(defensor.getColor())) {
                return "Error: No puedes atacar a tus propias piezas";
            }

            // Validar que sea ataque adyacente
            int deltaX = Math.abs(destinoX - origenX);
            int deltaY = Math.abs(destinoY - origenY);
            if (deltaX > 1 || deltaY > 1) {
                return "Error: Solo puedes atacar piezas adyacentes";
            }

            // Realizar ataque
            int dano = atacante.getPotenciaAtaque();
            return procesarDano(defensor, dano, destinoX, destinoY, "Ataque Normal", false);
        } catch (Exception e) {
            System.err.println("Error en ataque normal: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

    public String vampiroChuparSangre(int origenX, int origenY, int destinoX, int destinoY) {
        try {
            Pieza vampiro = getPieza(origenX, origenY);
            Pieza victima = getPieza(destinoX, destinoY);

            if (!(vampiro instanceof Vampiros)) {
                return "Error: La pieza no es un Vampiro";
            }

            if (victima == null || vampiro.getColor().equals(victima.getColor())) {
                return "Error: Objetivo inválido";
            }

            // Validar adyacencia
            int deltaX = Math.abs(destinoX - origenX);
            int deltaY = Math.abs(destinoY - origenY);
            if (deltaX > 1 || deltaY > 1) {
                return "Error: Debe ser adyacente para chupar sangre";
            }

            // Chupar sangre: quita 1 punto y lo restaura
            String resultado = procesarDano(victima, 1, destinoX, destinoY, "Chupar Sangre", false);
            vampiro.restaurarVida(1);

            return resultado + " | 🩸 Vampiro restauró 1 vida (ahora tiene " + vampiro.getVidas() + " vidas)";
        } catch (Exception e) {
            System.err.println("Error en chupar sangre: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

    public String muerteLanzarLanza(int origenX, int origenY, int destinoX, int destinoY) {
        try {
            Pieza muerte = getPieza(origenX, origenY);
            Pieza objetivo = getPieza(destinoX, destinoY);

            if (!(muerte instanceof Muerte)) {
                return "Error: La pieza no es una Muerte";
            }

            if (objetivo == null || muerte.getColor().equals(objetivo.getColor())) {
                return "Error: Objetivo inválido";
            }

            // Validar distancia de 2 casillas
            int deltaX = Math.abs(destinoX - origenX);
            int deltaY = Math.abs(destinoY - origenY);
            if (!((deltaX == 2 && deltaY == 0) || (deltaX == 0 && deltaY == 2) || (deltaX == 2 && deltaY == 2))) {
                return "Error: El objetivo debe estar a exactamente 2 casillas";
            }

            // Lanzar lanza: ignora escudo, hace 2 de daño (mitad de 4)
            int danoDirecto = muerte.getPotenciaAtaque() / 2;
            return procesarDano(objetivo, danoDirecto, destinoX, destinoY, "Lanzar Lanza", true);
        } catch (Exception e) {
            System.err.println("Error en lanzar lanza: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

    public String ataqueZombie(int origenX, int origenY, int destinoX, int destinoY) {
        try {
            Pieza muerte = getPieza(origenX, origenY);
            Pieza objetivo = getPieza(destinoX, destinoY);

            if (!(muerte instanceof Muerte)) {
                return "Error: Solo la Muerte puede ordenar ataques zombie";
            }

            if (objetivo == null || muerte.getColor().equals(objetivo.getColor())) {
                return "Error: Objetivo inválido";
            }

            // Verificar que haya un zombie adyacente al objetivo
            if (!hayZombieAdyacente(destinoX, destinoY, muerte.getColor())) {
                return "Error: No hay zombie adyacente al objetivo";
            }

            // Ataque zombie: 1 punto de daño
            return procesarDano(objetivo, 1, destinoX, destinoY, "Ataque Zombie", false);
        } catch (Exception e) {
            System.err.println("Error en ataque zombie: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

    public String conjurarZombie(int origenX, int origenY, int destinoX, int destinoY) {
        try {
            Pieza muerte = getPieza(origenX, origenY);

            if (!(muerte instanceof Muerte)) {
                return "Error: Solo la Muerte puede conjurar zombies";
            }

            if (getPieza(destinoX, destinoY) != null) {
                return "Error: La casilla destino no está vacía";
            }

            // Crear zombie
            Zombie zombie = new Zombie(muerte.getColor());
            casillas[destinoX][destinoY] = zombie;
            zombie.setPosicion(destinoX, destinoY);

            return "🧟 Muerte conjuró un Zombie en (" + destinoX + "," + destinoY + ")";
        } catch (Exception e) {
            System.err.println("Error al conjurar zombie: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

    private String procesarDano(Pieza defensor, int dano, int x, int y, String tipoAtaque, boolean ignorarEscudo) {
        try {
            int escudoAntes = defensor.getEscudo();
            int vidasAntes = defensor.getVidas();

            defensor.recibirDanio(dano, ignorarEscudo);

            String resultado = "⚔️ " + tipoAtaque + " | Daño: " + dano +
                    " | " + defensor.getTipo() +
                    " | Escudo: " + defensor.getEscudo() + " (antes: " + escudoAntes + ")" +
                    " | Vidas: " + defensor.getVidas() + " (antes: " + vidasAntes + ")";

            // Eliminar si murió
            if (!defensor.estaVivo()) {
                casillas[x][y] = null;
                contarPiezaPerdida(defensor.getColor());
                resultado += " | 💀 ¡PIEZA DESTRUIDA!";
            }

            return resultado;
        } catch (Exception e) {
            System.err.println("Error al procesar daño: " + e.getMessage());
            return "Error al procesar daño: " + e.getMessage();
        }
    }

    // ============================================
    // VERIFICACIÓN Y FIN DEL JUEGO
    // ============================================

    public String verificarGanador() {
        try {
            int piezasBlanco = contarPiezas("BLANCO");
            int piezasNegro = contarPiezas("NEGRO");

            if (piezasBlanco == 0) {
                return jugador2;
            } else if (piezasNegro == 0) {
                return jugador1;
            }

            return null;
        } catch (Exception e) {
            System.err.println("Error al verificar ganador: " + e.getMessage());
            return null;
        }
    }

    public void finalizarPartida(String ganador, String tipo) {
        try {
            // Otorgar 3 puntos al ganador usando el sistema de Jugador
            Jugador jugadorGanador = storage.obtenerPlayer(ganador);
            if (jugadorGanador != null) {
                jugadorGanador.agregarPuntos(3);
            }

            // Guardar log de la partida
            String mensaje;
            if (tipo.equals("RETIRO")) {
                String perdedor = ganador.equals(jugador1) ? jugador2 : jugador1;
                mensaje = perdedor + " SE HA RETIRADO, FELICIDADES " + ganador + ", HAS GANADO 3 PUNTOS";
            } else {
                String perdedor = ganador.equals(jugador1) ? jugador2 : jugador1;
                mensaje = ganador + " VENCIO A " + perdedor + ", FELICIDADES HAS GANADO 3 PUNTOS";
            }

            storage.agregarLog(mensaje);
        } catch (Exception e) {
            System.err.println("Error al finalizar partida: " + e.getMessage());
        }
    }

    // ============================================
    // MÉTODOS AUXILIARES
    // ============================================

    public boolean hayZombieAdyacente(int x, int y, String color) {
        try {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i == 0 && j == 0) continue;
                    int nx = x + i;
                    int ny = y + j;
                    Pieza p = getPieza(nx, ny);
                    if (p != null && p.getTipo().equals("ZOMBIE") && p.getColor().equals(color)) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error al buscar zombie adyacente: " + e.getMessage());
            return false;
        }
    }

    public int contarPiezas(String color) {
        try {
            int cuenta = 0;
            for (int i = 0; i < TAMANIO; i++) {
                for (int j = 0; j < TAMANIO; j++) {
                    if (casillas[i][j] != null && casillas[i][j].getColor().equals(color)) {
                        cuenta++;
                    }
                }
            }
            return cuenta;
        } catch (Exception e) {
            System.err.println("Error al contar piezas: " + e.getMessage());
            return 0;
        }
    }

    private void contarPiezaPerdida(String color) {
        try {
            if (color.equals("BLANCO")) {
                piezasPerdidasBlanco++;
            } else {
                piezasPerdidasNegro++;
            }
        } catch (Exception e) {
            System.err.println("Error al contar pieza perdida: " + e.getMessage());
        }
    }

    public void colocarPieza(Pieza pieza, int x, int y) {
        try {
            if (x >= 0 && x < TAMANIO && y >= 0 && y < TAMANIO) {
                casillas[x][y] = pieza;
                pieza.setPosicion(x, y);
            }
        } catch (Exception e) {
            System.err.println("Error al colocar pieza: " + e.getMessage());
        }
    }

    public void eliminarPieza(int x, int y) {
        try {
            casillas[x][y] = null;
        } catch (Exception e) {
            System.err.println("Error al eliminar pieza: " + e.getMessage());
        }
    }
}
