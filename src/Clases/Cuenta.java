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
interface GestionCuenta {

    boolean guardarCuenta(Cuenta cuenta);

    Cuenta buscarCuenta(String username);

    boolean eliminarCuenta(String username);

    ArrayList<Cuenta> obtenerTodasCuentas();

    ArrayList<Cuenta> obtenerRanking();
}


public class Cuenta implements GestionCuenta {

    private String username;
    private String password;
    private int puntos;
    private String fechaCreacion;
    private boolean activo;
    private int partidasJugadas;
    private int partidasGanadas;

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

    @Override
    public boolean guardarCuenta(Cuenta cuenta) {
           try {
        if (cuenta.username == null || cuenta.username.trim().isEmpty()) {
            return false;
        }

        for (Cuenta c : todasCuentas) {
            if (c.username.equalsIgnoreCase(cuenta.username) && c.activo) {
                return false;
            }
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
            for (Cuenta c : todasCuentas) {
                if (c.username.equalsIgnoreCase(username) && c.activo) {
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
                cuenta.activo = false; 
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

            for (int i = 0; i < ranking.size() - 1; i++) {
                for (int j = 0; j < ranking.size() - i - 1; j++) {
                    Cuenta actual = ranking.get(j);
                    Cuenta siguiente = ranking.get(j + 1);

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

   
    public static Cuenta crearCuenta(String username, String password) {
         try {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }
        if (password == null || password.length() != 5) {
            return null;
        }

        for (char c : password.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                return null;
            }
        }

        for (Cuenta c : todasCuentas) {
            if (c.username.equalsIgnoreCase(username) && c.activo) {
                return null; 
            }
        }

        Cuenta nueva = new Cuenta(username, password);
        nueva.guardarCuenta(nueva);
        usuarioActual = nueva;
        return nueva;

    } catch (Exception e) {
        System.err.println("Error al crear cuenta: " + e.getMessage());
        return null;
    }    
    }

    public static boolean iniciarSesion(String username, String password) {
        try {
        Cuenta cuenta = null;
        for (Cuenta c : todasCuentas) {
            if (c.username.equalsIgnoreCase(username) && c.activo) {
                cuenta = c;
                break;
            }
        }

        if (cuenta != null && cuenta.password.equals(password)) {
            usuarioActual = cuenta;
            return true;
        }
        return false;

    } catch (Exception e) {
        System.err.println("Error al iniciar sesión: " + e.getMessage());
        return false;
    }
    }

   
    public static void cerrarSesion() {
        usuarioActual = null;
    }

  
    public boolean cambiarPassword(String passwordActual, String nuevaPassword) {
        try {
            if (!this.password.equals(passwordActual)) {
                return false;
            }

            if (nuevaPassword == null || nuevaPassword.length() != 5) {
                return false;
            }

            for (int i = 0; i < nuevaPassword.length(); i++) {
                char c = nuevaPassword.charAt(i);
                if (Character.isLetterOrDigit(c)) {
                    return false;
                }
            }

            this.password = nuevaPassword;
            return true;
        } catch (Exception e) {
            System.err.println("Error al cambiar password: " + e.getMessage());
            return false;
        }
    }

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

   
    public double getPorcentajeVictorias() {
        if (partidasJugadas == 0) {
            return 0.0;
        }
        return (partidasGanadas * 100.0) / partidasJugadas;
    }

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
