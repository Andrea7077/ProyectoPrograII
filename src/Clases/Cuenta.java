/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.util.ArrayList;

/**
 *
 * @author andre
 */
public class Cuenta {

    private static ArrayList<Cuenta> todasCuentas = new ArrayList<>();
    private static Cuenta usuarioActual = null;
    private String username;
    private String password;

    public Cuenta(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Crear cuenta, valida nombre y contraseña
    public static Cuenta crearCuenta(String username, String password) {
        if (username.isEmpty() || password.length() != 5 || password.matches("[a-zA-Z0-9]+")) {
            return null; 
        }
        if (buscarCuenta(username) != null) {
            return null; 
        }
        Cuenta nueva = new Cuenta(username, password);
        todasCuentas.add(nueva);
        usuarioActual = nueva; 
        return nueva;
    }

    public static Cuenta buscarCuenta(String username) {
        for (Cuenta c : todasCuentas) {
            if (c.username.equalsIgnoreCase(username)) {
                return c;
            }
        }
        return null;
    }

    public static boolean iniciarSesion(String username, String password) {
        Cuenta c = buscarCuenta(username);
        if (c != null && c.password.equals(password)) {
            usuarioActual = c;
            return true;
        }
        return false;
    }

    public static void cerrarSesion() {
        usuarioActual = null;
    }

    public void cambiarPassword(String nueva) {
        this.password = nueva;
    }

    public static void eliminarCuentaActual() {
        if (usuarioActual != null) {
            todasCuentas.remove(usuarioActual);
            usuarioActual = null;
        }
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

}
