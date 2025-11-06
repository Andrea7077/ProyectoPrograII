/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pantallas;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author andre
 */
public class PTablero extends JFrame {

    private static final int TAMANO_TABLERO = 6;
    private TableroPanel tablero;

    public PTablero() {
        
        
        super("Vampire Wargame");
        
        // Configuracion de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Crear una instancia de la clase interna TableroPanel
        tablero = new TableroPanel();
        
        // Agregar el TableroPanel al centro del JFrame
        add(tablero, BorderLayout.CENTER);

        // Opcional: Agregar un panel de controles al Este para la Ruleta/Log
        JPanel panelControles = new JPanel();
        panelControles.setLayout(new BoxLayout(panelControles, BoxLayout.Y_AXIS));
        panelControles.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelControles.add(new JLabel(" Ruleta"));
        panelControles.add(new JButton("Girar"));
        
        add(panelControles, BorderLayout.EAST); 
        
        // Finalizar configuracion
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    // --- Clase Interna (Extiende JPanel) ---
    /**
     * Esta clase representa el tablero de 6x6.
     * Es un JPanel que contiene los JButtons de las casillas.
     */
    private class TableroPanel extends JPanel {
 
        private JButton[][] casillas;

        public TableroPanel() {
            // Usar GridLayout para la cuadrícula 6x6
            setLayout(new GridLayout(TAMANO_TABLERO, TAMANO_TABLERO));
            casillas = new JButton[TAMANO_TABLERO][TAMANO_TABLERO];
            
            crearCasillas();
            colocarPiezas();
        }

        private void crearCasillas() {
            Color colorClaro = new Color(200, 200, 200);
            Color colorOscuro = new Color(150, 150, 150);
            
            for (int f = 0; f < TAMANO_TABLERO; f++) {
                for (int c = 0; c < TAMANO_TABLERO; c++) {
                    JButton boton = new JButton();
                    boton.setPreferredSize(new Dimension(75, 75));
                    
                    // Colores alternados
                    if ((f + c) % 2 == 0) {
                        boton.setBackground(colorClaro);
                    } else {
                        boton.setBackground(colorOscuro);
                    }
                    
                    final int fila = f;
                    final int columna = c;
                    
                    // Manejo del evento de click
                    boton.addActionListener(e -> manejarClick(fila, columna));
                    
                    casillas[f][c] = boton;
                    add(boton);
                }
            }
        }

        private void colocarPiezas() {
            // Orden de las piezas iniciales: LOBO, VAMP, MUERTE, MUERTE, VAMP, LOBO
            String[] orden = {"LOBO", "VAMP", "MUER", "MUER", "VAMP", "LOBO"};
            
            // Jugador 1 (Piezas Rojas - Fila 0)
            for (int c = 0; c < TAMANO_TABLERO; c++) {
                casillas[0][c].setText("R-" + orden[c]);
                casillas[0][c].setForeground(Color.RED);
            }

            // Jugador 2 (Piezas Azules - Fila 5)
            for (int c = 0; c < TAMANO_TABLERO; c++) {
                casillas[TAMANO_TABLERO - 1][c].setText("A-" + orden[c]);
                casillas[TAMANO_TABLERO - 1][c].setForeground(Color.BLUE);
            }
        }
        
        private void manejarClick(int f, int c) {
            // Aquí se conectará la lógica de tu juego (Clase Pieza, movimientos, etc.)
            System.out.println("Click: Fila " + f + ", Columna " + c + " (Pieza: " + casillas[f][c].getText() + ")");
        }
    }
}
