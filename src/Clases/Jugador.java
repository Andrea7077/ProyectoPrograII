/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author andre
 */
public class Jugador {

    private String username;
    private String password;
    private int puntos;
    private String fecha;
    private boolean activo;

    private static ArrayList<Jugador> listaJugadores = new ArrayList<>();

    public Jugador() {
    }

    public Jugador(String username, String password) {
        this.username = username;
        this.password = password;
        this.puntos = 0;
        this.fecha = LocalDate.now().toString();
        this.activo = true;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public static ArrayList<Jugador> getListaJugadores() {
        return listaJugadores;
    }

    // --- Métodos de gestión (como los de la interfaz) ---
    public static void crearJugador(String username, String password) {
        Jugador nuevo = new Jugador(username, password);
        listaJugadores.add(nuevo);
    }

    public static Jugador buscarJugador(String username) {
        for (Jugador j : listaJugadores) {
            if (j.getUsername().equalsIgnoreCase(username)) {
                return j;
            }
        }
        return null;
    }

    public static boolean eliminarJugador(String username) {
        Jugador j = buscarJugador(username);
        if (j != null) {
            listaJugadores.remove(j);
            return true;
        }
        return false;
    }

    public static boolean cambiarPassword(String username, String oldPass, String newPass) {
        Jugador j = buscarJugador(username);
        if (j != null && j.getPassword().equals(oldPass)) {
            j.setPassword(newPass);
            return true;
        }
        return false;
    }

    public static void listarJugadores() {
        System.out.println("---- Lista de Jugadores ----");
        for (Jugador j : listaJugadores) {
            System.out.println(j);
        }
    }

    @Override
    public String toString() {
        return username + " | Puntos: " + puntos + " | Fecha: " + fecha + " | Activo: " + activo;
    }
}
