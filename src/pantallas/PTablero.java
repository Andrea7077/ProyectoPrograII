/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pantallas;

import Clases.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author andre
 */
public class PTablero extends JFrame {

    private static final int TAMANO_TABLERO = 6;
    private Tablero tableroLogico;
    private TableroPanel tableroGUI;
    private JLabel lblTurno, lblPiezaSeleccionada, lblMensaje;
    private JButton btnRuleta, btnRetirarse;
    private JTextArea areaLog;

    // Variables de control del juego
    private String piezaPermitida = null;
    private int filaSeleccionada = -1;
    private int colSeleccionada = -1;
    private boolean esperandoDestino = false;

    public PTablero() {
        super("Vampire Wargame");

        // Crear tablero lógico (temporalmente con usuarios de prueba)
        Cuenta usuario = Cuenta.getUsuarioActual();
        String jugador1 = (usuario != null) ? usuario.getUsername() : "Jugador1";
        String jugador2 = "Jugador2"; // Después agregarás selector

        tableroLogico = new Tablero(jugador1, jugador2);

        inicializarComponentes();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(20, 20, 30));

        // === PANEL SUPERIOR (Info del juego) ===
        JPanel panelSuperior = crearPanelSuperior();
        add(panelSuperior, BorderLayout.NORTH);

        // === PANEL CENTRAL (Tablero) ===
        tableroGUI = new TableroPanel();
        add(tableroGUI, BorderLayout.CENTER);

        // === PANEL DERECHO (Controles y Log) ===
        JPanel panelDerecho = crearPanelDerecho();
        add(panelDerecho, BorderLayout.EAST);

        actualizarInterfaz();
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
        panel.setBackground(new Color(40, 40, 50));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lblTurno = new JLabel("Turno: " + tableroLogico.getTurnoJugadorActual(), SwingConstants.CENTER);
        lblTurno.setFont(new Font("Arial", Font.BOLD, 18));
        lblTurno.setForeground(Color.WHITE);

        lblPiezaSeleccionada = new JLabel("Gira la ruleta para comenzar", SwingConstants.CENTER);
        lblPiezaSeleccionada.setFont(new Font("Arial", Font.PLAIN, 16));
        lblPiezaSeleccionada.setForeground(Color.YELLOW);

        lblMensaje = new JLabel("", SwingConstants.CENTER);
        lblMensaje.setFont(new Font("Arial", Font.ITALIC, 14));
        lblMensaje.setForeground(Color.CYAN);

        panel.add(lblTurno);
        panel.add(lblPiezaSeleccionada);
        panel.add(lblMensaje);

        return panel;
    }

    private JPanel crearPanelDerecho() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(30, 30, 40));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setPreferredSize(new Dimension(250, 0));

        // Botón Ruleta
        btnRuleta = new JButton("🎰 GIRAR RULETA");
        btnRuleta.setFont(new Font("Arial", Font.BOLD, 14));
        btnRuleta.setBackground(new Color(150, 0, 0));
        btnRuleta.setForeground(Color.WHITE);
        btnRuleta.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRuleta.setMaximumSize(new Dimension(220, 50));
        btnRuleta.addActionListener(e -> girarRuleta());

        // Botón Retirarse
        btnRetirarse = new JButton("🏳️ RETIRARSE");
        btnRetirarse.setFont(new Font("Arial", Font.BOLD, 14));
        btnRetirarse.setBackground(new Color(100, 0, 0));
        btnRetirarse.setForeground(Color.WHITE);
        btnRetirarse.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRetirarse.setMaximumSize(new Dimension(220, 40));
        btnRetirarse.addActionListener(e -> retirarse());

        // Área de Log
        JLabel lblLog = new JLabel("📜 Historial:");
        lblLog.setForeground(Color.WHITE);
        lblLog.setAlignmentX(Component.CENTER_ALIGNMENT);

        areaLog = new JTextArea(15, 20);
        areaLog.setEditable(false);
        areaLog.setBackground(new Color(20, 20, 30));
        areaLog.setForeground(Color.LIGHT_GRAY);
        areaLog.setFont(new Font("Monospaced", Font.PLAIN, 11));
        JScrollPane scrollLog = new JScrollPane(areaLog);
        scrollLog.setMaximumSize(new Dimension(230, 300));

        panel.add(btnRuleta);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnRetirarse);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(lblLog);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(scrollLog);

        return panel;
    }

    /**
     * Girar la ruleta para seleccionar pieza
     */
    private void girarRuleta() {
        try {
            int giros = tableroLogico.vecesPermitidasGirarRuleta();
            boolean piezaEncontrada = false;

            for (int i = 0; i < giros && !piezaEncontrada; i++) {
                piezaPermitida = tableroLogico.girarRuleta();
                if (piezaPermitida != null) {
                    piezaEncontrada = true;
                }
            }

            if (piezaPermitida != null) {
                lblPiezaSeleccionada.setText("🎲 Pieza seleccionada: " + getNombreCompleto(piezaPermitida));
                lblMensaje.setText("Selecciona una pieza " + getNombreCompleto(piezaPermitida) + " para mover");
                btnRuleta.setEnabled(false);
                agregarLog("Ruleta girada: " + getNombreCompleto(piezaPermitida));
            } else {
                lblMensaje.setText("❌ Pierdes el turno (no tienes piezas disponibles)");
                agregarLog("Turno perdido - Sin piezas disponibles");
                cambiarTurno();
            }
        } catch (Exception e) {
            mostrarError("Error al girar ruleta: " + e.getMessage());
        }
    }

    /**
     * Retirarse del juego
     */
    private void retirarse() {
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro que deseas retirarte?",
                "Confirmar Retiro",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            String ganador = Tablero.getTurnoActual().equals("BLANCO")
                    ? tableroLogico.getJugador2()
                    : tableroLogico.getJugador1();

            tableroLogico.finalizarPartida(ganador, "RETIRO");

            JOptionPane.showMessageDialog(this,
                    "¡" + ganador + " ha ganado por retiro!",
                    "Fin del Juego",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();
            new MenuPrincipal().setVisible(true);
        }
    }

    /**
     * Manejar click en una casilla del tablero
     */
    private void manejarClick(int fila, int col) {
        try {
            if (piezaPermitida == null) {
                lblMensaje.setText("⚠️ Primero debes girar la ruleta");
                return;
            }

            Pieza piezaEnCasilla = tableroLogico.getPieza(fila, col);

            // Primera selección: elegir pieza a mover
            if (!esperandoDestino) {
                if (piezaEnCasilla == null) {
                    lblMensaje.setText("❌ Esa casilla está vacía");
                    return;
                }

                if (!piezaEnCasilla.getColor().equals(tableroLogico.getTurnoActual())) {
                    lblMensaje.setText("❌ Esa no es tu pieza");
                    return;
                }

                if (!piezaEnCasilla.getTipo().equals(piezaPermitida)) {
                    lblMensaje.setText("❌ Debes mover una pieza " + getNombreCompleto(piezaPermitida));
                    return;
                }

                // Pieza válida seleccionada
                filaSeleccionada = fila;
                colSeleccionada = col;
                esperandoDestino = true;
                lblMensaje.setText("✅ Pieza seleccionada. Ahora elige el destino o enemigo");
                tableroGUI.repaint();
            } // Segunda selección: elegir destino o enemigo
            else {
                realizarAccion(fila, col);
            }
        } catch (Exception e) {
            mostrarError("Error al manejar click: " + e.getMessage());
        }
    }

    /**
     * Realizar acción (mover o atacar)
     */
    private void realizarAccion(int filaDestino, int colDestino) {
        try {
            Pieza destino = tableroLogico.getPieza(filaDestino, colDestino);

            // Si el destino está vacío, intentar mover
            if (destino == null) {
                // Preguntar si es movimiento especial (Muerte puede conjurar zombie)
                Pieza piezaOrigen = tableroLogico.getPieza(filaSeleccionada, colSeleccionada);

                if (piezaOrigen instanceof Muerte) {
                    String[] opciones = {"Mover", "Conjurar Zombie"};
                    int opcion = JOptionPane.showOptionDialog(this,
                            "¿Qué deseas hacer?",
                            "Acción de la Muerte",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null, opciones, opciones[0]);

                    if (opcion == 1) {
                        String resultado = tableroLogico.conjurarZombie(
                                filaSeleccionada, colSeleccionada, filaDestino, colDestino);
                        agregarLog(resultado);
                        lblMensaje.setText(resultado);
                        finalizarTurno();
                        return;
                    }
                } else if (piezaOrigen instanceof Lobo) {
                    // Preguntar si quiere moverse 2 casillas
                    int deltaFila = Math.abs(filaDestino - filaSeleccionada);
                    int deltaCol = Math.abs(colDestino - colSeleccionada);

                    if (deltaFila == 2 || deltaCol == 2) {
                        String resultado = tableroLogico.loboMover2Casillas(
                                filaSeleccionada, colSeleccionada, filaDestino, colDestino);
                        agregarLog(resultado);
                        lblMensaje.setText(resultado);
                        finalizarTurno();
                        return;
                    }
                }

                // Movimiento normal
                boolean movido = tableroLogico.moverPieza(
                        filaSeleccionada, colSeleccionada, filaDestino, colDestino);

                if (movido) {
                    agregarLog("Movimiento exitoso a (" + filaDestino + "," + colDestino + ")");
                    lblMensaje.setText("✅ Pieza movida exitosamente");
                    finalizarTurno();
                } else {
                    lblMensaje.setText("❌ Movimiento inválido");
                }
            } // Si hay una pieza enemiga, atacar
            else if (!destino.getColor().equals(tableroLogico.getTurnoActual())) {
                // Preguntar tipo de ataque
                Pieza atacante = tableroLogico.getPieza(filaSeleccionada, colSeleccionada);
                String[] opciones = obtenerOpcionesAtaque(atacante);

                int opcion = JOptionPane.showOptionDialog(this,
                        "Selecciona el tipo de ataque:",
                        "Tipo de Ataque",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null, opciones, opciones[0]);

                String resultado = ejecutarAtaque(opcion, atacante, filaDestino, colDestino);
                agregarLog(resultado);
                lblMensaje.setText(resultado);
                finalizarTurno();
            } else {
                lblMensaje.setText("❌ No puedes atacar tus propias piezas");
            }
        } catch (Exception e) {
            mostrarError("Error en acción: " + e.getMessage());
        }
    }

    private String[] obtenerOpcionesAtaque(Pieza atacante) {
        if (atacante instanceof Vampiros) {
            return new String[]{"Ataque Normal", "Chupar Sangre"};
        } else if (atacante instanceof Muerte) {
            return new String[]{"Ataque Normal", "Lanzar Lanza", "Ataque Zombie"};
        } else {
            return new String[]{"Ataque Normal"};
        }
    }

    private String ejecutarAtaque(int opcion, Pieza atacante, int filaDestino, int colDestino) {
        if (atacante instanceof Vampiros && opcion == 1) {
            return tableroLogico.vampiroChuparSangre(
                    filaSeleccionada, colSeleccionada, filaDestino, colDestino);
        } else if (atacante instanceof Muerte && opcion == 1) {
            return tableroLogico.muerteLanzarLanza(
                    filaSeleccionada, colSeleccionada, filaDestino, colDestino);
        } else if (atacante instanceof Muerte && opcion == 2) {
            return tableroLogico.ataqueZombie(
                    filaSeleccionada, colSeleccionada, filaDestino, colDestino);
        } else {
            return tableroLogico.atacarNormal(
                    filaSeleccionada, colSeleccionada, filaDestino, colDestino);
        }
    }

    private void finalizarTurno() {
        // Verificar ganador
        String ganador = tableroLogico.verificarGanador();
        if (ganador != null) {
            tableroLogico.finalizarPartida(ganador, "VICTORIA");
            JOptionPane.showMessageDialog(this,
                    "¡" + ganador + " ha ganado la partida!",
                    "Fin del Juego",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new MenuPrincipal().setVisible(true);
            return;
        }

        // Resetear selección
        filaSeleccionada = -1;
        colSeleccionada = -1;
        esperandoDestino = false;
        piezaPermitida = null;
        btnRuleta.setEnabled(true);

        // Cambiar turno
        cambiarTurno();
    }

    private void cambiarTurno() {
        tableroLogico.cambiarTurno();
        actualizarInterfaz();
    }

    private void actualizarInterfaz() {
        lblTurno.setText("Turno: " + tableroLogico.getTurnoJugadorActual());
        lblPiezaSeleccionada.setText("Gira la ruleta para seleccionar pieza");
        lblMensaje.setText("");
        tableroGUI.repaint();
    }

    private void agregarLog(String mensaje) {
        areaLog.append(mensaje + "\n");
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
    }

    private String getNombreCompleto(String tipo) {
        switch (tipo) {
            case "LOBO":
                return "Hombre Lobo";
            case "VAMP":
                return "Vampiro";
            case "MUER":
                return "Muerte";
            case "ZOMB":
                return "Zombie";
            default:
                return tipo;
        }
    }

    private void mostrarError(String mensaje) {
        System.err.println(mensaje);
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // ========================================
    // CLASE INTERNA: TableroPanel
    // ========================================
    private class TableroPanel extends JPanel {

        private JButton[][] casillas;

        public TableroPanel() {
            setLayout(new GridLayout(TAMANO_TABLERO, TAMANO_TABLERO, 2, 2));
            setBackground(new Color(50, 50, 60));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            casillas = new JButton[TAMANO_TABLERO][TAMANO_TABLERO];
            crearCasillas();
            actualizarTablero();
        }

        private void crearCasillas() {
            Color colorClaro = new Color(230, 230, 230);
            Color colorOscuro = new Color(150, 150, 150);

            for (int f = 0; f < TAMANO_TABLERO; f++) {
                for (int c = 0; c < TAMANO_TABLERO; c++) {
                    JButton boton = new JButton();
                    boton.setPreferredSize(new Dimension(80, 80));
                    boton.setFont(new Font("Arial", Font.BOLD, 12));

                    // Colores alternados
                    if ((f + c) % 2 == 0) {
                        boton.setBackground(colorClaro);
                    } else {
                        boton.setBackground(colorOscuro);
                    }

                    final int fila = f;
                    final int columna = c;

                    boton.addActionListener(e -> manejarClick(fila, columna));

                    casillas[f][c] = boton;
                    add(boton);
                }
            }
        }

        public void actualizarTablero() {
            Pieza[][] tablero = tableroLogico.getCasillas();

            for (int f = 0; f < TAMANO_TABLERO; f++) {
                for (int c = 0; c < TAMANO_TABLERO; c++) {
                    Pieza pieza = tablero[f][c];
                    JButton boton = casillas[f][c];

                    if (pieza != null) {
                        boton.setText(pieza.toString());
                        boton.setForeground(pieza.getColor().equals("BLANCO") ? Color.RED : Color.BLUE);

                        // Resaltar pieza seleccionada
                        if (f == filaSeleccionada && c == colSeleccionada) {
                            boton.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
                        } else {
                            boton.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
                        }
                    } else {
                        boton.setText("");
                        boton.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
                    }
                }
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            actualizarTablero();
        }
    }
}
