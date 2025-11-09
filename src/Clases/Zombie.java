/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * @author andre
 */
public final class Zombie extends Pieza {
       public Zombie(String color) {
        super(color, 1, 0, 1, "ZOMBIE");
    }
    
    @Override
    public final void atacar(Pieza enemigo) {
        // Zombies no atacan por sí solos
        throw new UnsupportedOperationException("Los zombies no atacan por sí solos");
    }
    
    @Override
    public final boolean puedeRealizarAtaqueEspecial(Tablero tablero, int destinoX, int destinoY) {
        return false;
    }
    
    @Override
    public final void ataqueEspecial(Tablero tablero, int destinoX, int destinoY) {
        throw new UnsupportedOperationException("Los zombies no tienen ataques especiales");
    }
    
    // Zombie no se mueve
    @Override
    public final boolean puedeMoverse(int origenX, int origenY, int destinoX, int destinoY) {
        return false;
    }
}
