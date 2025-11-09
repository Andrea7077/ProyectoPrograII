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

     public Lobo(String color) {
        super(color, 6, 3, 5, "HOMBRE_LOBO");
    }
    
    @Override
    public void atacar(Pieza enemigo) {
        enemigo.recibirDanio(this.potenciaAtaque, false);
    }
    
    @Override
    public boolean puedeRealizarAtaqueEspecial(Tablero tablero, int destinoX, int destinoY) {
        // Movimiento especial: hasta 2 casillas vacías
        int difX = Math.abs(destinoX - this.posX);
        int difY = Math.abs(destinoY - this.posY);
        
        // Debe ser máximo 2 casillas en una dirección
        if (difX > 2 || difY > 2) return false;
        if (difX == 0 && difY == 0) return false;
        
        // Verificar que el destino esté vacío
        return tablero.obtenerPieza(destinoX, destinoY) == null;
    }
    
    @Override
    public void ataqueEspecial(Tablero tablero, int destinoX, int destinoY) {
        // Mover hasta 2 casillas (ya validado)
        tablero.moverPieza(this.posX, this.posY, destinoX, destinoY);
    }
    
}
