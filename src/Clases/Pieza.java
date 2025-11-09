/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * @author andre
 */
public abstract class Pieza {

    protected String tipo;
    protected String color; // "BLANCO" o "NEGRO"
    protected int fila;
    protected int columna;
    protected int vidas;
    protected int escudo;
    protected int ataque;

    public Pieza(String tipo, String color, int fila, int columna, int vidas, int escudo, int ataque) {
        this.tipo = tipo;
        this.color = color;
        this.fila = fila;
        this.columna = columna;
        this.vidas = vidas;
        this.escudo = escudo;
        this.ataque = ataque;
    }

    /**
     * Método abstracto para validar movimientos CUMPLE REQUISITO: Función
     * abstracta
     */
    public abstract boolean puedeMoverse(int filaDestino, int colDestino, Tablero tablero);

    /**
     * Método abstracto para ataques especiales
     */
    public abstract boolean puedeAtaqueEspecial(int filaDestino, int colDestino, Tablero tablero, String tipoAtaque);

    /**
     * Mover a casilla adyacente (regla general para todas las piezas)
     */
    public boolean esMovimientoAdyacente(int filaDestino, int colDestino) {
        int deltaFila = Math.abs(filaDestino - fila);
        int deltaCol = Math.abs(colDestino - columna);
        return deltaFila <= 1 && deltaCol <= 1 && !(deltaFila == 0 && deltaCol == 0);
    }

    /**
     * Recibir daño (primero escudo, luego vidas)
     */
    public void recibirDano(int danio, boolean ignorarEscudo) {
        if (ignorarEscudo) {
            vidas -= danio;
        } else {
            if (escudo > 0) {
                int danoRestante = danio - escudo;
                escudo = Math.max(0, escudo - danio);
                if (danoRestante > 0) {
                    vidas -= danoRestante;
                }
            } else {
                vidas -= danio;
            }
        }
        vidas = Math.max(0, vidas);
    }

    /**
     * Restaurar vida (usado por vampiros)
     */
    public void restaurarVida(int cantidad) {
        vidas += cantidad;
    }

    public boolean estaViva() {
        return vidas > 0;
    }

    // Getters y Setters
    public String getTipo() {
        return tipo;
    }

    public String getColor() {
        return color;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public int getVidas() {
        return vidas;
    }

    public int getEscudo() {
        return escudo;
    }

    public int getAtaque() {
        return ataque;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    @Override
    public String toString() {
        return color.charAt(0) + "-" + tipo;
    }

    public String getEstadoCompleto() {
        return String.format("%s: Vidas=%d, Escudo=%d, Ataque=%d",
                toString(), vidas, escudo, ataque);
    }
}
