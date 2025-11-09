/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * @author andre
 */
public class Vampiros extends Pieza {

    public Vampiros(String color) {
        super(color, 4, 5, 3, "VAMPIRO");
    }

    @Override
    public void atacar(Pieza enemigo) {
        enemigo.recibirDanio(this.potenciaAtaque, false);
    }

    @Override
    public boolean puedeRealizarAtaqueEspecial(Tablero tablero, int destinoX, int destinoY) {
        // Chupar sangre: debe ser adyacente y enemigo
        Pieza objetivo = tablero.obtenerPieza(destinoX, destinoY);
        if (objetivo == null || objetivo.getColor().equals(this.color)) {
            return false;
        }
        return puedeMoverse(this.posX, this.posY, destinoX, destinoY);
    }

    @Override
    public void ataqueEspecial(Tablero tablero, int destinoX, int destinoY) {
        // Chupar sangre: quita 1 vida y restaura 1 vida propia
        Pieza enemigo = tablero.obtenerPieza(destinoX, destinoY);
        if (enemigo != null) {
            enemigo.recibirDanio(1, false);
            this.restaurarVida(1);
        }
    }
}
