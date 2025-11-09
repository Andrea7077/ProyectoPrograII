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

    private static final int TAMANO = 6;
    private Pieza[][] casillas;
    private String turnoActual; // "BLANCO" o "NEGRO"
    private String jugador1; // Username del jugador con piezas BLANCAS
    private String jugador2; // Username del jugador con piezas NEGRAS
    private ArrayList<String> historialMovimientos;
    private Random random;

    public Tablero(String jugador1, String jugador2) {
        this.casillas = new Pieza[TAMANO][TAMANO];
        this.turnoActual = "BLANCO"; // Siempre inician las blancas
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.historialMovimientos = new ArrayList<>();
        this.random = new Random();
        inicializarTablero();
    }

    /**
     * Inicializa el tablero con las piezas en sus posiciones iniciales Orden:
     * LOBO - VAMPIRO - MUERTE - MUERTE - VAMPIRO - LOBO
     */
    private void inicializarTablero() {
        try {
            // Jugador 1 (BLANCAS) - Fila 0
            casillas[0][0] = new Lobo("BLANCO", 0, 0);
            casillas[0][1] = new Vampiros("BLANCO", 0, 1);
            casillas[0][2] = new Muerte("BLANCO", 0, 2);
            casillas[0][3] = new Muerte("BLANCO", 0, 3);
            casillas[0][4] = new Vampiros("BLANCO", 0, 4);
            casillas[0][5] = new Lobo("BLANCO", 0, 5);

            // Jugador 2 (NEGRAS) - Fila 5
            casillas[5][0] = new Lobo("NEGRO", 5, 0);
            casillas[5][1] = new Vampiros("NEGRO", 5, 1);
            casillas[5][2] = new Muerte("NEGRO", 5, 2);
            casillas[5][3] = new Muerte("NEGRO", 5, 3);
            casillas[5][4] = new Vampiros("NEGRO", 5, 4);
            casillas[5][5] = new Lobo("NEGRO", 5, 5);
        } catch (Exception e) {
            System.err.println("Error al inicializar tablero: " + e.getMessage());
        }
    }

    /**
     * FUNCIÓN RECURSIVA 1: Contar piezas de un tipo y color CUMPLE REQUISITO:
     * Función recursiva
     */
    public int contarPiezasRecursivo(String tipo, String color, int fila, int col) {
        try {
            // Caso base: fuera del tablero
            if (fila >= TAMANO) {
                return 0;
            }

            if (col >= TAMANO) {
                return contarPiezasRecursivo(tipo, color, fila + 1, 0);
            }

            // Contar pieza actual si coincide
            int cuenta = 0;
            Pieza pieza = casillas[fila][col];
            if (pieza != null && pieza.getColor().equals(color)) {
                if (tipo == null || pieza.getTipo().equals(tipo)) {
                    cuenta = 1;
                }
            }

            // Recursión para siguiente columna
            return cuenta + contarPiezasRecursivo(tipo, color, fila, col + 1);
        } catch (Exception e) {
            System.err.println("Error en contarPiezasRecursivo: " + e.getMessage());
            return 0;
        }
    }

    /**
     * FUNCIÓN RECURSIVA 2: Verificar si hay camino libre entre dos puntos
     * CUMPLE REQUISITO: Segunda función recursiva
     */
    public boolean hayCaminoLibreRecursivo(int filaActual, int colActual,
            int filaFinal, int colFinal,
            int dirFila, int dirCol) {
        try {
            // Caso base: llegamos al destino
            if (filaActual == filaFinal && colActual == colFinal) {
                return true;
            }

            // Verificar si hay pieza en la posición actual
            if (casillas[filaActual][colActual] != null) {
                return false;
            }

            // Recursión: avanzar en la dirección
            return hayCaminoLibreRecursivo(filaActual + dirFila, colActual + dirCol,
                    filaFinal, colFinal, dirFila, dirCol);
        } catch (Exception e) {
            System.err.println("Error en hayCaminoLibreRecursivo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtener pieza en una posición
     */
    public Pieza getPieza(int fila, int col) {
        try {
            if (!esValidaPos(fila, col)) {
                return null;
            }
            return casillas[fila][col];
        } catch (Exception e) {
            System.err.println("Error al obtener pieza: " + e.getMessage());
            return null;
        }
    }

    /**
     * Validar si una posición está dentro del tablero
     */
    public boolean esValidaPos(int fila, int col) {
        return fila >= 0 && fila < TAMANO && col >= 0 && col < TAMANO;
    }

    /**
     * Mover pieza (sin ataque)
     */
    public boolean moverPieza(int filaOrigen, int colOrigen, int filaDestino, int colDestino) {
        try {
            Pieza pieza = getPieza(filaOrigen, colOrigen);

            if (pieza == null || !pieza.getColor().equals(turnoActual)) {
                return false;
            }

            if (!pieza.puedeMoverse(filaDestino, colDestino, this)) {
                return false;
            }

            // Ejecutar movimiento
            casillas[filaDestino][colDestino] = pieza;
            casillas[filaOrigen][colOrigen] = null;
            pieza.setFila(filaDestino);
            pieza.setColumna(colDestino);

            registrarMovimiento(String.format("%s movió %s de (%d,%d) a (%d,%d)",
                    getTurnoJugadorActual(), pieza.toString(), filaOrigen, colOrigen, filaDestino, colDestino));

            return true;
        } catch (Exception e) {
            System.err.println("Error al mover pieza: " + e.getMessage());
            return false;
        }
    }

    /**
     * Realizar ataque normal
     */
    public String atacarNormal(int filaAtacante, int colAtacante, int filaObjetivo, int colObjetivo) {
        try {
            Pieza atacante = getPieza(filaAtacante, colAtacante);
            Pieza objetivo = getPieza(filaObjetivo, colObjetivo);

            if (atacante == null || objetivo == null) {
                return "Error: Piezas no válidas";
            }

            if (!atacante.getColor().equals(turnoActual)) {
                return "Error: No es tu turno";
            }

            if (atacante.getColor().equals(objetivo.getColor())) {
                return "Error: No puedes atacar tus propias piezas";
            }

            // Debe ser adyacente para ataque normal
            if (!atacante.esMovimientoAdyacente(filaObjetivo, colObjetivo)) {
                return "Error: El ataque normal solo funciona en casillas adyacentes";
            }

            // Aplicar daño
            int dano = atacante.getAtaque();
            objetivo.recibirDano(dano, false);

            String resultado = String.format("%s atacó a %s causando %d puntos de daño.",
                    atacante.toString(), objetivo.toString(), dano);

            if (!objetivo.estaViva()) {
                resultado += String.format("\n¡%s ha sido destruido!", objetivo.toString());
                casillas[filaObjetivo][colObjetivo] = null;
            } else {
                resultado += String.format("\nA %s le quedan: Escudo=%d, Vidas=%d",
                        objetivo.toString(), objetivo.getEscudo(), objetivo.getVidas());
            }

            registrarMovimiento(resultado);
            return resultado;
        } catch (Exception e) {
            System.err.println("Error en ataque: " + e.getMessage());
            return "Error al realizar ataque";
        }
    }

    /**
     * Ataque especial: Vampiro chupa sangre
     */
    public String vampiroChuparSangre(int filaVampiro, int colVampiro, int filaObjetivo, int colObjetivo) {
        try {
            Pieza vampiro = getPieza(filaVampiro, colVampiro);
            Pieza objetivo = getPieza(filaObjetivo, colObjetivo);

            if (!(vampiro instanceof Vampiros)) {
                return "Error: Solo los vampiros pueden chupar sangre";
            }

            if (!vampiro.getColor().equals(turnoActual)) {
                return "Error: No es tu turno";
            }

            if (!vampiro.puedeAtaqueEspecial(filaObjetivo, colObjetivo, this, "CHUPAR_SANGRE")) {
                return "Error: No se puede chupar sangre en esa posición";
            }

            String resultado = ((Vampiros) vampiro).chuparSangre(objetivo);

            if (!objetivo.estaViva()) {
                casillas[filaObjetivo][colObjetivo] = null;
            }

            registrarMovimiento(resultado);
            return resultado;
        } catch (Exception e) {
            System.err.println("Error en chupar sangre: " + e.getMessage());
            return "Error al realizar ataque especial";
        }
    }

    /**
     * Ataque especial: Muerte lanza lanza
     */
    public String muerteLanzarLanza(int filaMuerte, int colMuerte, int filaObjetivo, int colObjetivo) {
        try {
            Pieza muerte = getPieza(filaMuerte, colMuerte);
            Pieza objetivo = getPieza(filaObjetivo, colObjetivo);

            if (!(muerte instanceof Muerte)) {
                return "Error: Solo la Muerte puede lanzar la lanza";
            }

            if (!muerte.getColor().equals(turnoActual)) {
                return "Error: No es tu turno";
            }

            if (!muerte.puedeAtaqueEspecial(filaObjetivo, colObjetivo, this, "LANZAR_LANZA")) {
                return "Error: No se puede lanzar la lanza a esa posición";
            }

            String resultado = ((Muerte) muerte).lanzarLanza(objetivo);

            if (!objetivo.estaViva()) {
                casillas[filaObjetivo][colObjetivo] = null;
            }

            registrarMovimiento(resultado);
            return resultado;
        } catch (Exception e) {
            System.err.println("Error al lanzar lanza: " + e.getMessage());
            return "Error al realizar ataque especial";
        }
    }

    /**
     * Conjurar zombie
     */
    public String conjurarZombie(int filaMuerte, int colMuerte, int filaZombie, int colZombie) {
        try {
            Pieza muerte = getPieza(filaMuerte, colMuerte);

            if (!(muerte instanceof Muerte)) {
                return "Error: Solo la Muerte puede conjurar zombies";
            }

            if (!muerte.getColor().equals(turnoActual)) {
                return "Error: No es tu turno";
            }

            if (!muerte.puedeAtaqueEspecial(filaZombie, colZombie, this, "CONJURAR_ZOMBIE")) {
                return "Error: No se puede conjurar zombie en esa posición";
            }

            // Crear zombie
            Zombie zombie = new Zombie(muerte.getColor(), filaZombie, colZombie);
            casillas[filaZombie][colZombie] = zombie;

            String resultado = String.format("%s conjuró un Zombie en (%d,%d)",
                    muerte.toString(), filaZombie, colZombie);

            registrarMovimiento(resultado);
            return resultado;
        } catch (Exception e) {
            System.err.println("Error al conjurar zombie: " + e.getMessage());
            return "Error al conjurar zombie";
        }
    }

    /**
     * Ataque zombie (la Muerte ordena a un zombie atacar)
     */
    public String ataqueZombie(int filaMuerte, int colMuerte, int filaObjetivo, int colObjetivo) {
        try {
            Pieza muerte = getPieza(filaMuerte, colMuerte);
            Pieza objetivo = getPieza(filaObjetivo, colObjetivo);

            if (!(muerte instanceof Muerte)) {
                return "Error: Solo la Muerte puede ordenar ataques zombie";
            }

            if (!muerte.getColor().equals(turnoActual)) {
                return "Error: No es tu turno";
            }

            if (!muerte.puedeAtaqueEspecial(filaObjetivo, colObjetivo, this, "ATAQUE_ZOMBIE")) {
                return "Error: No hay zombie adyacente al enemigo";
            }

            // El zombie ataca (daño = 1)
            objetivo.recibirDano(1, false);

            String resultado = String.format("%s ordenó ataque zombie a %s, causando 1 punto de daño.",
                    muerte.toString(), objetivo.toString());

            if (!objetivo.estaViva()) {
                resultado += String.format("\n¡%s ha sido destruido!", objetivo.toString());
                casillas[filaObjetivo][colObjetivo] = null;
            } else {
                resultado += String.format("\nA %s le quedan: Escudo=%d, Vidas=%d",
                        objetivo.toString(), objetivo.getEscudo(), objetivo.getVidas());
            }

            registrarMovimiento(resultado);
            return resultado;
        } catch (Exception e) {
            System.err.println("Error en ataque zombie: " + e.getMessage());
            return "Error al realizar ataque zombie";
        }
    }

    /**
     * Verificar si hay zombie adyacente a una posición
     */
    public boolean hayZombieAdyacente(int fila, int col, String color) {
        try {
            for (int df = -1; df <= 1; df++) {
                for (int dc = -1; dc <= 1; dc++) {
                    if (df == 0 && dc == 0) {
                        continue;
                    }

                    int nf = fila + df;
                    int nc = col + dc;

                    if (esValidaPos(nf, nc)) {
                        Pieza pieza = getPieza(nf, nc);
                        if (pieza instanceof Zombie && pieza.getColor().equals(color)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error al buscar zombie: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cambiar turno
     */
    public void cambiarTurno() {
        turnoActual = turnoActual.equals("BLANCO") ? "NEGRO" : "BLANCO";
    }

    /**
     * Verificar ganador
     */
    public String verificarGanador() {
        try {
            int piezasBlancas = contarPiezasRecursivo(null, "BLANCO", 0, 0);
            int piezasNegras = contarPiezasRecursivo(null, "NEGRO", 0, 0);

            if (piezasBlancas == 0) {
                return jugador2; // Ganó el jugador 2 (NEGRAS)
            } else if (piezasNegras == 0) {
                return jugador1; // Ganó el jugador 1 (BLANCAS)
            }

            return null; // Juego continúa
        } catch (Exception e) {
            System.err.println("Error al verificar ganador: " + e.getMessage());
            return null;
        }
    }

    /**
     * Girar ruleta para seleccionar pieza
     */
    public String girarRuleta() {
        try {
            String[] tipos = {"LOBO", "VAMP", "MUER", "LOBO", "VAMP", "MUER"};
            String tipoSeleccionado = tipos[random.nextInt(tipos.length)];

            // Verificar si el jugador tiene esa pieza
            int cantidad = contarPiezasRecursivo(tipoSeleccionado, turnoActual, 0, 0);

            if (cantidad > 0) {
                return tipoSeleccionado;
            }

            return null; // No tiene esa pieza
        } catch (Exception e) {
            System.err.println("Error al girar ruleta: " + e.getMessage());
            return null;
        }
    }

    /**
     * Calcular veces permitidas para girar ruleta según piezas perdidas
     */
    public int vecesPermitidasGirarRuleta() {
        try {
            int piezasIniciales = 6;
            int piezasActuales = contarPiezasRecursivo(null, turnoActual, 0, 0);

            // Contar solo piezas principales (no zombies)
            int piezasPrincipales = 0;
            for (int f = 0; f < TAMANO; f++) {
                for (int c = 0; c < TAMANO; c++) {
                    Pieza p = casillas[f][c];
                    if (p != null && p.getColor().equals(turnoActual) && !(p instanceof Zombie)) {
                        piezasPrincipales++;
                    }
                }
            }

            int piezasPerdidas = piezasIniciales - piezasPrincipales;

            if (piezasPerdidas >= 4) {
                return 3;
            } else if (piezasPerdidas >= 2) {
                return 2;
            } else {
                return 1;
            }
        } catch (Exception e) {
            System.err.println("Error al calcular giros: " + e.getMessage());
            return 1;
        }
    }

    /**
     * Finalizar partida y guardar log
     */
    public void finalizarPartida(String ganador, String tipoFinalizacion) {
        try {
            // Crear y guardar log
            LogPartida log = new LogPartida(jugador1, jugador2, ganador, tipoFinalizacion, 3);
            LogPartida.guardarLog(log);

            // Actualizar puntos del ganador
            Cuenta temp = new Cuenta("", "");
            Cuenta cuentaGanador = temp.buscarCuenta(ganador);
            if (cuentaGanador != null) {
                cuentaGanador.registrarPartida(true);
            }

            // Actualizar estadísticas del perdedor
            String perdedor = ganador.equals(jugador1) ? jugador2 : jugador1;
            Cuenta cuentaPerdedor = temp.buscarCuenta(perdedor);
            if (cuentaPerdedor != null) {
                cuentaPerdedor.registrarPartida(false);
            }
        } catch (Exception e) {
            System.err.println("Error al finalizar partida: " + e.getMessage());
        }
    }

    private void registrarMovimiento(String movimiento) {
        historialMovimientos.add(movimiento);
    }

    // Getters
    public String getTurnoActual() {
        return turnoActual;
    }

    public String getJugador1() {
        return jugador1;
    }

    public String getJugador2() {
        return jugador2;
    }

    public ArrayList<String> getHistorialMovimientos() {
        return historialMovimientos;
    }

    public static int getTAMANO() {
        return TAMANO;
    }

    public String getTurnoJugadorActual() {
        return turnoActual.equals("BLANCO") ? jugador1 : jugador2;
    }

    public Pieza[][] getCasillas() {
        return casillas;
    }
}
