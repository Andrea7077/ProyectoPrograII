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
    private String fechaIngreso;
    private boolean activo;

    public Jugador(String username, String password) {
        this.username = username;
        this.password = password;
        this.puntos = 0;
        this.fechaIngreso = java.time.LocalDate.now().toString();
        this.activo = true;
    }

    public void agregarPuntos(int puntos) {
        this.puntos += puntos;
    }

    public boolean validarPassword(String password) {
        return this.password.equals(password);
    }

    public void cambiarPassword(String nuevoPassword) {
        this.password = nuevoPassword;
    }

    public void desactivar() {
        this.activo = false;
    }

    public String getUsername() {
        return username;
    }

    public int getPuntos() {
        return puntos;
    }

    public String getFechaIngreso() {
        return fechaIngreso;
    }

    public boolean isActivo() {
        return activo;
    }
}
