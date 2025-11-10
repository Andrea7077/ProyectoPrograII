/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * @author andre
 */
public class Jugar {

    private Tablero tablero;
    private Jugador jugador1;
    private Jugador jugador2;
    private String turnoActual; 
    private String[] tiposPiezas = {"VAMPIRO", "HOMBRE_LOBO", "MUERTE"};
    private InterfazGuardado storage;

    public Jugar(Jugador j1, Jugador j2, InterfazGuardado storage) {
        try {
            this.jugador1 = j1;
            this.jugador2 = j2;
            this.storage = storage;

            this.tablero = new Tablero(j1.getUsername(), j2.getUsername());
            this.turnoActual = "BLANCO"; // Blancas inician

        } catch (Exception e) {
            System.err.println("Error al crear Jugar: " + e.getMessage());
        }
    }

    public String seleccionarPiezaRuleta() {
        try {
            java.util.Random rand = new java.util.Random();
            return tiposPiezas[rand.nextInt(tiposPiezas.length)];
        } catch (Exception e) {
            System.err.println("Error en ruleta: " + e.getMessage());
            return "VAMPIRO";
        }
    }

    public int contarPiezasPerdidas(String color) {
        try {
            int piezasActuales = 0;
            for (int i = 0; i < Tablero.getTamanio(); i++) {
                for (int j = 0; j < Tablero.getTamanio(); j++) {
                    Pieza p = tablero.obtenerPieza(i, j);
                    if (p != null && p.getColor().equals(color) && !p.getTipo().equals("ZOMBIE")) {
                        piezasActuales++;
                    }
                }
            }
            return 6 - piezasActuales;
        } catch (Exception e) {
            System.err.println("Error al contar piezas perdidas: " + e.getMessage());
            return 0;
        }
    }

    public int calcularGirosRuleta() {
        try {
            int perdidas = contarPiezasPerdidas(turnoActual);
            if (perdidas >= 4) {
                return 3;
            }
            if (perdidas >= 2) {
                return 2;
            }
            return 1;
        } catch (Exception e) {
            System.err.println("Error al calcular giros: " + e.getMessage());
            return 1;
        }
    }

    public boolean tienePiezaTipo(String tipo, String color) {
        try {
            return contarPiezasTipoRecursivo(tipo, color, 0, 0) > 0;
        } catch (Exception e) {
            System.err.println("Error al verificar tipo de pieza: " + e.getMessage());
            return false;
        }
    }

    private int contarPiezasTipoRecursivo(String tipo, String color, int fila, int col) {
        if (fila >= Tablero.getTamanio()) {
            return 0;
        }

        try {
            int siguiente_fila = fila;
            int siguiente_col = col + 1;

            if (siguiente_col >= Tablero.getTamanio()) {
                siguiente_col = 0;
                siguiente_fila++;
            }

            Pieza p = tablero.obtenerPieza(fila, col);
            int cuenta = 0;

            if (p != null && p.getTipo().equals(tipo) && p.getColor().equals(color)) {
                cuenta = 1;
            }

            return cuenta + contarPiezasTipoRecursivo(tipo, color, siguiente_fila, siguiente_col);
        } catch (Exception e) {
            System.err.println("Error en recursión de contar piezas: " + e.getMessage());
            return 0;
        }
    }

    public boolean validarMovimiento(int origenX, int origenY, int destinoX, int destinoY, String tipoPieza) {
        try {
            Pieza pieza = tablero.obtenerPieza(origenX, origenY);

            if (pieza == null) {
                return false;
            }
            if (!pieza.getColor().equals(turnoActual)) {
                return false;
            }
            if (!pieza.getTipo().equals(tipoPieza)) {
                return false;
            }

            return true;
        } catch (Exception e) {
            System.err.println("Error al validar movimiento: " + e.getMessage());
            return false;
        }
    }

    public String realizarAccion(int origenX, int origenY, int destinoX, int destinoY,
            String tipoAccion, String tipoAtaque) {
        try {
            Pieza pieza = tablero.obtenerPieza(origenX, origenY);
            Pieza destino = tablero.obtenerPieza(destinoX, destinoY);

            StringBuilder resultado = new StringBuilder();

            if (tipoAccion.equals("MOVER")) {
                if (destino == null) {
                    tablero.moverPieza(origenX, origenY, destinoX, destinoY);
                    resultado.append("Pieza movida exitosamente");
                }
            } else if (tipoAccion.equals("ATACAR")) {
                if (tipoAtaque.equals("NORMAL")) {
                    int escudoAntes = destino.getEscudo();
                    int vidasAntes = destino.getVidas();

                    pieza.atacar(destino);

                    resultado.append("Ataque normal realizado. ");
                    resultado.append("Daño: ").append(pieza.getPotenciaAtaque());
                    resultado.append(", Escudo restante: ").append(destino.getEscudo());
                    resultado.append(", Vidas restantes: ").append(destino.getVidas());

                    if (!destino.estaVivo()) {
                        tablero.eliminarPieza(destinoX, destinoY);
                        resultado.append(". ¡Pieza destruida!");
                    }
                } else if (tipoAtaque.equals("ESPECIAL")) {
                    realizarAtaqueEspecial(pieza, destinoX, destinoY, resultado);
                }
            }

            return resultado.toString();
        } catch (Exception e) {
            System.err.println("Error al realizar acción: " + e.getMessage());
            return "Error en la acción: " + e.getMessage();
        }
    }

    private void realizarAtaqueEspecial(Pieza pieza, int destinoX, int destinoY,
            StringBuilder resultado) {
        try {
            if (pieza instanceof Vampiros) {
                ((Vampiros) pieza).ataqueEspecial(tablero, destinoX, destinoY);
                resultado.append("¡Vampiro chupó sangre! +1 vida restaurada");
            } else if (pieza instanceof Lobo) {
                ((Lobo) pieza).ataqueEspecial(tablero, destinoX, destinoY);
                resultado.append("¡Hombre Lobo se movió 2 casillas!");
            } else if (pieza instanceof Muerte) {
                Muerte muerte = (Muerte) pieza;
                resultado.append("Ataque especial de Muerte ejecutado");
            }
        } catch (Exception e) {
            System.err.println("Error en ataque especial: " + e.getMessage());
            resultado.append("Error en ataque especial");
        }
    }

    public boolean verificarFinJuego() {
        try {
            return tablero.contarPiezas("BLANCO") == 0 || tablero.contarPiezas("NEGRO") == 0;
        } catch (Exception e) {
            System.err.println("Error al verificar fin de juego: " + e.getMessage());
            return false;
        }
    }

    public void cambiarTurno() {
        try {
            turnoActual = turnoActual.equals("BLANCO") ? "NEGRO" : "BLANCO";
        } catch (Exception e) {
            System.err.println("Error al cambiar turno: " + e.getMessage());
        }
    }

    public void finalizarJuego(String ganador, boolean retiro) {
        try {
            Jugador jugadorGanador = ganador.equals(jugador1.getUsername()) ? jugador1 : jugador2;
            Jugador jugadorPerdedor = ganador.equals(jugador1.getUsername()) ? jugador2 : jugador1;

            jugadorGanador.agregarPuntos(3);

            String log;
            if (retiro) {
                log = jugadorPerdedor.getUsername() + " se ha retirado. "
                        + "¡Felicidades " + jugadorGanador.getUsername() + ", has ganado 3 puntos!";
            } else {
                log = jugadorGanador.getUsername() + " venció a " + jugadorPerdedor.getUsername()
                        + ". ¡Felicidades, has ganado 3 puntos!";
            }

            storage.agregarLog(log);
        } catch (Exception e) {
            System.err.println("Error al finalizar juego: " + e.getMessage());
        }
    }

    // Getters
    public Tablero getTablero() {
        return tablero;
    }

    public String getTurnoActual() {
        return turnoActual;
    }

    public Jugador getJugador1() {
        return jugador1;
    }

    public Jugador getJugador2() {
        return jugador2;
    }
}
