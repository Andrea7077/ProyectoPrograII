/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.util.ArrayList;
import java.time.LocalDate;

/**
 *
 * @author andre
 */
interface IGestionAlmacenamiento {

    boolean guardarCuenta(Cuenta cuenta);

    Cuenta buscarCuenta(String username);

    boolean eliminarCuenta(String username);

    ArrayList<Cuenta> obtenerTodasCuentas();

    ArrayList<Cuenta> obtenerRanking();
}

/**
 * Cuenta implementa la interfaz directamente SIN usar Collections ni Comparator
 * - Solo ciclos básicos
 */
public class Cuenta implements IGestionAlmacenamiento {

    // Atributos de la cuenta
    private String username;
    private String password;
    private int puntos;
    private String fechaCreacion;
    private boolean activo;
    private int partidasJugadas;
    private int partidasGanadas;

    // Almacenamiento estático compartido
    private static ArrayList<Cuenta> todasCuentas = new ArrayList<>();
    private static Cuenta usuarioActual = null;

    public Cuenta(String username, String password) {
        this.username = username;
        this.password = password;
        this.puntos = 0;
        this.fechaCreacion = LocalDate.now().toString();
        this.activo = true;
        this.partidasJugadas = 0;
        this.partidasGanadas = 0;
    }

    // ========== IMPLEMENTACIÓN DE INTERFAZ ==========
    @Override
    public boolean guardarCuenta(Cuenta cuenta) {
        try {
            // Verificar si ya existe
            if (buscarCuenta(cuenta.getUsername()) != null) {
                return false;
            }
            todasCuentas.add(cuenta);
            return true;
        } catch (Exception e) {
            System.err.println("Error al guardar cuenta: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Cuenta buscarCuenta(String username) {
        try {
            for (int i = 0; i < todasCuentas.size(); i++) {
                Cuenta c = todasCuentas.get(i);
                if (c.username.equalsIgnoreCase(username)) {
                    return c;
                }
            }
            return null;
        } catch (Exception e) {
            System.err.println("Error al buscar cuenta: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean eliminarCuenta(String username) {
        try {
            Cuenta cuenta = buscarCuenta(username);
            if (cuenta != null) {
                cuenta.activo = false; // Marcar como inactivo
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error al eliminar cuenta: " + e.getMessage());
            return false;
        }
    }

    @Override
    public ArrayList<Cuenta> obtenerTodasCuentas() {
        ArrayList<Cuenta> activas = new ArrayList<>();
        for (int i = 0; i < todasCuentas.size(); i++) {
            Cuenta c = todasCuentas.get(i);
            if (c.activo) {
                activas.add(c);
            }
        }
        return activas;
    }

    @Override
    public ArrayList<Cuenta> obtenerRanking() {
        try {
            ArrayList<Cuenta> ranking = obtenerTodasCuentas();

            // ORDENAMIENTO BURBUJA (Bubble Sort) - Mayor a menor
            // Lo aprendiste en estructuras básicas
            for (int i = 0; i < ranking.size() - 1; i++) {
                for (int j = 0; j < ranking.size() - i - 1; j++) {
                    Cuenta actual = ranking.get(j);
                    Cuenta siguiente = ranking.get(j + 1);

                    // Si el actual tiene MENOS puntos que el siguiente, intercambiar
                    if (actual.puntos < siguiente.puntos) {
                        ranking.set(j, siguiente);
                        ranking.set(j + 1, actual);
                    }
                }
            }

            return ranking;
        } catch (Exception e) {
            System.err.println("Error al obtener ranking: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // ========== MÉTODOS DE NEGOCIO ==========
    /**
     * Crear nueva cuenta con validaciones
     */
    public static Cuenta crearCuenta(String username, String password) {
        try {
            // Validar username no vacío
            if (username == null || username.trim().isEmpty()) {
                return null;
            }

            // Validar password: exactamente 5 caracteres
            if (password == null || password.length() != 5) {
                return null;
            }

            // Verificar que tenga al menos un carácter especial
            // (NO solo letras y números)
            boolean tieneEspecial = false;
            for (int i = 0; i < password.length(); i++) {
                char c = password.charAt(i);
                // Si NO es letra Y NO es dígito = es especial
                if (!Character.isLetter(c) && !Character.isDigit(c)) {
                    tieneEspecial = true;
                    break;
                }
            }

            if (!tieneEspecial) {
                return null; // Necesita caracteres especiales
            }

            // Verificar que username sea único
            Cuenta temp = new Cuenta("", "");
            if (temp.buscarCuenta(username) != null) {
                return null; // Ya existe
            }

            // Crear y guardar cuenta
            Cuenta nueva = new Cuenta(username, password);
            nueva.guardarCuenta(nueva);
            usuarioActual = nueva;
            return nueva;

        } catch (Exception e) {
            System.err.println("Error al crear cuenta: " + e.getMessage());
            return null;
        }
    }

    /**
     * Iniciar sesión
     */
    public static boolean iniciarSesion(String username, String password) {
        try {
            Cuenta temp = new Cuenta("", "");
            Cuenta cuenta = temp.buscarCuenta(username);

            if (cuenta != null && cuenta.password.equals(password) && cuenta.activo) {
                usuarioActual = cuenta;
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error al iniciar sesión: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cerrar sesión
     */
    public static void cerrarSesion() {
        usuarioActual = null;
    }

    /**
     * Cambiar password con validaciones
     */
    public boolean cambiarPassword(String passwordActual, String nuevaPassword) {
        try {
            // Verificar password actual
            if (!this.password.equals(passwordActual)) {
                return false;
            }

            // Validar nueva password (5 caracteres)
            if (nuevaPassword == null || nuevaPassword.length() != 5) {
                return false;
            }

            // Verificar caracteres especiales
            boolean tieneEspecial = false;
            for (int i = 0; i < nuevaPassword.length(); i++) {
                char c = nuevaPassword.charAt(i);
                if (!Character.isLetter(c) && !Character.isDigit(c)) {
                    tieneEspecial = true;
                    break;
                }
            }

            if (!tieneEspecial) {
                return false;
            }

            this.password = nuevaPassword;
            return true;
        } catch (Exception e) {
            System.err.println("Error al cambiar password: " + e.getMessage());
            return false;
        }
    }

    /**
     * Eliminar cuenta actual
     */
    public static boolean eliminarCuentaActual(String password) {
        try {
            if (usuarioActual != null && usuarioActual.password.equals(password)) {
                usuarioActual.eliminarCuenta(usuarioActual.username);
                usuarioActual = null;
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error al eliminar cuenta: " + e.getMessage());
            return false;
        }
    }

    /**
     * Registrar resultado de partida
     */
    public void registrarPartida(boolean gano) {
        try {
            partidasJugadas++;
            if (gano) {
                partidasGanadas++;
                puntos += 3;
            }
        } catch (Exception e) {
            System.err.println("Error al registrar partida: " + e.getMessage());
        }
    }

    /**
     * Calcular porcentaje de victorias
     */
    public double getPorcentajeVictorias() {
        if (partidasJugadas == 0) {
            return 0.0;
        }
        return (partidasGanadas * 100.0) / partidasJugadas;
    }

    // ========== GETTERS ==========
    public static Cuenta getUsuarioActual() {
        return usuarioActual;
    }

    public static ArrayList<Cuenta> getTodasCuentas() {
        return todasCuentas;
    }

    public String getUsername() {
        return username;
    }

    public int getPuntos() {
        return puntos;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public boolean isActivo() {
        return activo;
    }

    public int getPartidasJugadas() {
        return partidasJugadas;
    }

    public int getPartidasGanadas() {
        return partidasGanadas;
    }

    @Override
    public String toString() {
        return String.format("%s | Puntos: %d | Partidas: %d | Ganadas: %d (%.1f%%)",
                username, puntos, partidasJugadas, partidasGanadas, getPorcentajeVictorias());
    }
}
