/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * @author andre
 */
public class Muerte extends Pieza {

    public Muerte(String color) {
        super(color, 5, 7, 4, "MUERTE");
    }

    @Override
    public void atacar(Pieza enemigo) {
        enemigo.recibirDanio(this.potenciaAtaque, false);
    }

    @Override
    public boolean puedeRealizarAtaqueEspecial(Tablero tablero, int destinoX, int destinoY) {
        // Tiene múltiples ataques especiales, se valida en el método
        return true;
    }

    @Override
    public void ataqueEspecial(Tablero tablero, int destinoX, int destinoY) {
        // Este método no se usa directamente, ver métodos específicos abajo
    }

    // Ataque especial 1: Lanzar lanza a 2 casillas
    public boolean puedeLanzarLanza(Tablero tablero, int destinoX, int destinoY) {
        int difX = Math.abs(destinoX - this.posX);
        int difY = Math.abs(destinoY - this.posY);

        // Debe estar exactamente a 2 casillas en una dirección
        boolean dosHorizontal = (difX == 2 && difY == 0);
        boolean dosVertical = (difX == 0 && difY == 2);
        boolean dosDiagonal = (difX == 2 && difY == 2);

        if (!dosHorizontal && !dosVertical && !dosDiagonal) {
            return false;
        }

        Pieza objetivo = tablero.obtenerPieza(destinoX, destinoY);
        return objetivo != null && !objetivo.getColor().equals(this.color);
    }

    public void lanzarLanza(Tablero tablero, int destinoX, int destinoY) {
        Pieza enemigo = tablero.obtenerPieza(destinoX, destinoY);
        if (enemigo != null) {
            // Ignora escudo y hace la mitad del daño (4/2 = 2)
            enemigo.recibirDanio(this.potenciaAtaque / 2, true);
        }
    }

    // Ataque especial 2: Conjurar zombie
    public boolean puedeConjurarZombie(Tablero tablero, int destinoX, int destinoY) {
        return tablero.obtenerPieza(destinoX, destinoY) == null;
    }

    public void conjurarZombie(Tablero tablero, int destinoX, int destinoY) {
        Zombie zombie = new Zombie(this.color);
        tablero.colocarPieza(zombie, destinoX, destinoY);
    }

    // Ataque especial 3: Ataque mediante zombie
    public boolean puedeAtacarConZombie(Tablero tablero, int destinoX, int destinoY) {
        Pieza objetivo = tablero.obtenerPieza(destinoX, destinoY);
        if (objetivo == null || objetivo.getColor().equals(this.color)) {
            return false;
        }

        // Verificar si hay un zombie adyacente al objetivo
        return tablero.hayZombieAdyacente(destinoX, destinoY, this.color);
    }

    public void atacarConZombie(Tablero tablero, int destinoX, int destinoY) {
        Pieza enemigo = tablero.obtenerPieza(destinoX, destinoY);
        if (enemigo != null) {
            enemigo.recibirDanio(1, false); // Zombie hace 1 de daño
        }
    }
}
