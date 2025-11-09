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
    
    public Tablero() {
        casillas = new Pieza[TAMANIO][TAMANIO];
    }
    
    public void inicializarTablero(String jugador1, String jugador2) {
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
    }
    
    public Pieza obtenerPieza(int x, int y) {
        if (x < 0 || x >= TAMANIO || y < 0 || y >= TAMANIO) {
            return null;
        }
        return casillas[x][y];
    }
    
    public void colocarPieza(Pieza pieza, int x, int y) {
        if (x >= 0 && x < TAMANIO && y >= 0 && y < TAMANIO) {
            casillas[x][y] = pieza;
            pieza.setPosicion(x, y);
        }
    }
    
    public boolean moverPieza(int origenX, int origenY, int destinoX, int destinoY) {
        Pieza pieza = obtenerPieza(origenX, origenY);
        if (pieza == null || obtenerPieza(destinoX, destinoY) != null) {
            return false;
        }
        
        casillas[destinoX][destinoY] = pieza;
        casillas[origenX][origenY] = null;
        pieza.setPosicion(destinoX, destinoY);
        return true;
    }
    
    public void eliminarPieza(int x, int y) {
        casillas[x][y] = null;
    }
    
    public boolean hayZombieAdyacente(int x, int y, String color) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                int nx = x + i;
                int ny = y + j;
                Pieza p = obtenerPieza(nx, ny);
                if (p != null && p.getTipo().equals("ZOMBIE") && p.getColor().equals(color)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public int contarPiezas(String color) {
        int cuenta = 0;
        for (int i = 0; i < TAMANIO; i++) {
            for (int j = 0; j < TAMANIO; j++) {
                if (casillas[i][j] != null && casillas[i][j].getColor().equals(color)) {
                    cuenta++;
                }
            }
        }
        return cuenta;
    }
    
    public static int getTamanio() {
        return TAMANIO;
    }

}
