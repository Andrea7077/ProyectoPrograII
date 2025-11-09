/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * @author andre
 */
public class Lobo extends Pieza {

    public Lobo(String tipo, String color, int fila, int columna, int vidas, int escudo, int ataque) {
        super(tipo, color, fila, columna, vidas, escudo, ataque);
    }

    @Override
    public boolean puedeMoverse(int filaDestino, int colDestino, Tablero tablero) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean puedeAtaqueEspecial(int filaDestino, int colDestino, Tablero tablero, String tipoAtaque) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
