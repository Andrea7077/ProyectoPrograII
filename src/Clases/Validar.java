/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * @author andre
 */

public final class Validar {
    
    public static final boolean validarPassword(String password) {
        try {
            if (password == null || password.length() != 5) {
                return false;
            }
            
            return contieneCaracterEspecial(password, 0);
        } catch (Exception e) {
            System.err.println("Error al validar password: " + e.getMessage());
            return false;
        }
    }
    
    private static boolean contieneCaracterEspecial(String password, int indice) {
        if (indice >= password.length()) {
            return false;
        }
        
        try {
            char c = password.charAt(indice);
            
            if (!Character.isLetterOrDigit(c)) {
                return true;
            }
            
            return contieneCaracterEspecial(password, indice + 1);
        } catch (Exception e) {
            System.err.println("Error en validación recursiva: " + e.getMessage());
            return false;
        }
    }
    
    public static final boolean validarUsername(String username) {
        try {
            return username != null && !username.trim().isEmpty();
        } catch (Exception e) {
            System.err.println("Error al validar username: " + e.getMessage());
            return false;
        }
    }
    
    public static final boolean validarCoordenadas(int x, int y) {
        try {
            return x >= 0 && x < 6 && y >= 0 && y < 6;
        } catch (Exception e) {
            System.err.println("Error al validar coordenadas: " + e.getMessage());
            return false;
        }
    }
}