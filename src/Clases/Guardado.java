/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * @author andre
 */
public class Guardado implements InterfazGuardado {
private Jugador[] players;
    private String[] logs;
    private int cantidadPlayers;
    private int cantidadLogs;
    
    public Guardado() {
        players = new Jugador[100]; // Capacidad inicial
        logs = new String[1000];
        cantidadPlayers = 0;
        cantidadLogs = 0;
    }
    
    @Override
    public boolean agregarPlayer(Jugador player) {
        try {
            if (existeUsername(player.getUsername())) {
                return false;
            }
            if (cantidadPlayers >= players.length) {
                expandirArrayPlayers();
            }
            players[cantidadPlayers++] = player;
            return true;
        } catch (Exception e) {
            System.err.println("Error al agregar player: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public Jugador obtenerPlayer(String username) {
        try {
            return buscarPlayerRecursivo(username, 0);
        } catch (Exception e) {
            System.err.println("Error al obtener player: " + e.getMessage());
            return null;
        }
    }
    
    // FUNCIÓN RECURSIVA 1: Buscar player por username
    private Jugador buscarPlayerRecursivo(String username, int indice) {
        // Caso base 1: llegamos al final del arreglo
        if (indice >= cantidadPlayers) {
            return null;
        }
        
        // Caso base 2: encontramos el player
        if (players[indice] != null && players[indice].getUsername().equals(username)) {
            return players[indice];
        }
        
        // Caso recursivo: seguir buscando
        return buscarPlayerRecursivo(username, indice + 1);
    }
    
    @Override
    public boolean existeUsername(String username) {
        return obtenerPlayer(username) != null;
    }
    
    @Override
    public void eliminarPlayer(String username) {
        try {
            Jugador player = obtenerPlayer(username);
            if (player != null) {
                player.desactivar();
            }
        } catch (Exception e) {
            System.err.println("Error al eliminar player: " + e.getMessage());
        }
    }
    
    @Override
    public Jugador[] obtenerTodosPlayers() {
        try {
            Jugador[] activos = new Jugador[cantidadPlayers];
            int cuenta = 0;
            for (int i = 0; i < cantidadPlayers; i++) {
                if (players[i] != null && players[i].isActivo()) {
                    activos[cuenta++] = players[i];
                }
            }
            // Retornar array del tamaño exacto
            Jugador[] resultado = new Jugador[cuenta];
            System.arraycopy(activos, 0, resultado, 0, cuenta);
            return resultado;
        } catch (Exception e) {
            System.err.println("Error al obtener todos los players: " + e.getMessage());
            return new Jugador[0];
        }
    }
    
    @Override
    public void agregarLog(String log) {
        try {
            if (cantidadLogs >= logs.length) {
                expandirArrayLogs();
            }
            logs[cantidadLogs++] = log;
        } catch (Exception e) {
            System.err.println("Error al agregar log: " + e.getMessage());
        }
    }
    
    @Override
    public String[] obtenerLogs(String username) {
        try {
            String[] logsUsuario = new String[cantidadLogs];
            int cuenta = 0;
            
            for (int i = cantidadLogs - 1; i >= 0; i--) { // Del más reciente al más viejo
                if (logs[i] != null && logs[i].contains(username)) {
                    logsUsuario[cuenta++] = logs[i];
                }
            }
            
            String[] resultado = new String[cuenta];
            System.arraycopy(logsUsuario, 0, resultado, 0, cuenta);
            return resultado;
        } catch (Exception e) {
            System.err.println("Error al obtener logs: " + e.getMessage());
            return new String[0];
        }
    }
    
    @Override
    public Jugador[] obtenerRanking() {
        try {
            Jugador[] activos = obtenerTodosPlayers();
            // Ordenar usando QuickSort recursivo
            quickSortRecursivo(activos, 0, activos.length - 1);
            return activos;
        } catch (Exception e) {
            System.err.println("Error al obtener ranking: " + e.getMessage());
            return new Jugador[0];
        }
    }
    
    // FUNCIÓN RECURSIVA 2: QuickSort para ordenar ranking
    private void quickSortRecursivo(Jugador[] arr, int bajo, int alto) {
        // Caso base: si bajo >= alto, ya está ordenado
        if (bajo >= alto) {
            return;
        }
        
        try {
            // Particionar el arreglo
            int indicePivote = particion(arr, bajo, alto);
            
            // Caso recursivo: ordenar las dos mitades
            quickSortRecursivo(arr, bajo, indicePivote - 1);
            quickSortRecursivo(arr, indicePivote + 1, alto);
        } catch (Exception e) {
            System.err.println("Error en quicksort: " + e.getMessage());
        }
    }
    
    private int particion(Jugador[] arr, int bajo, int alto) {
        int pivote = arr[alto].getPuntos();
        int i = bajo - 1;
        
        for (int j = bajo; j < alto; j++) {
            // Ordenar de mayor a menor puntos
            if (arr[j].getPuntos() > pivote) {
                i++;
                Jugador temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        
        Jugador temp = arr[i + 1];
        arr[i + 1] = arr[alto];
        arr[alto] = temp;
        
        return i + 1;
    }
    
    private void expandirArrayPlayers() {
        Jugador[] nuevo = new Jugador[players.length * 2];
        System.arraycopy(players, 0, nuevo, 0, cantidadPlayers);
        players = nuevo;
    }
    
    private void expandirArrayLogs() {
        String[] nuevo = new String[logs.length * 2];
        System.arraycopy(logs, 0, nuevo, 0, cantidadLogs);
        logs = nuevo;
    }
}

