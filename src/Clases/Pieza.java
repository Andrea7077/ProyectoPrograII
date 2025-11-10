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

    protected String color; 
    protected int vidas;
    protected int escudo;
    protected int potenciaAtaque;
    protected int posX;
    protected int posY;
    protected String tipo; 
    public Pieza(String color, int vidas, int escudo, int potenciaAtaque, String tipo) {
        this.color = color;
        this.vidas = vidas;
        this.escudo = escudo;
        this.potenciaAtaque = potenciaAtaque;
        this.tipo = tipo;
    }

    public abstract void atacar(Pieza enemigo);

    public abstract boolean puedeRealizarAtaqueEspecial(Tablero tablero, int destinoX, int destinoY);

    public abstract void ataqueEspecial(Tablero tablero, int destinoX, int destinoY);

    public void recibirDanio(int danio, boolean ignorarEscudo) {
        if (ignorarEscudo) {
            vidas -= danio;
        } else {
            if (escudo > 0) {
                int danioRestante = danio - escudo;
                escudo = Math.max(0, escudo - danio);
                if (danioRestante > 0) {
                    vidas -= danioRestante;
                }
            } else {
                vidas -= danio;
            }
        }
        vidas = Math.max(0, vidas);
    }

    public boolean estaVivo() {
        return vidas > 0;
    }

    public void restaurarVida(int cantidad) {
        vidas += cantidad;
    }

    public boolean puedeMoverse(int origenX, int origenY, int destinoX, int destinoY) {
        int difX = Math.abs(destinoX - origenX);
        int difY = Math.abs(destinoY - origenY);
        return difX <= 1 && difY <= 1 && !(difX == 0 && difY == 0);
    }

    public String getColor() {
        return color;
    }

    public int getVidas() {
        return vidas;
    }

    public int getEscudo() {
        return escudo;
    }

    public int getPotenciaAtaque() {
        return potenciaAtaque;
    }

    public String getTipo() {
        return tipo;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosicion(int x, int y) {
        this.posX = x;
        this.posY = y;
    }
}


