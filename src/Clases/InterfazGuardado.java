/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * @author andre
 */
public interface InterfazGuardado {

    boolean agregarPlayer(Jugador player);

    Jugador obtenerPlayer(String username);

    boolean existeUsername(String username);

    void eliminarPlayer(String username);

    Jugador[] obtenerTodosPlayers();

    void agregarLog(String log);

    String[] obtenerLogs(String username);

    Jugador[] obtenerRanking();
}
