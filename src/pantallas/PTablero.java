/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pantallas;

import Clases.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Random;
/**
 * Pantalla del Tablero - MEJORADA Y FUNCIONAL
 */
public class PTablero extends JFrame {
 private static final int TAMANO_TABLERO = 6;
    private Tablero tableroLogico;
    private TableroPanel tableroGUI;
    private RuletaPanel ruletaPanel;
    private JLabel lblTurno, lblPiezaSeleccionada, lblMensaje;
    private JButton btnGirarRuleta, btnRetirarse;
    private JTextArea areaLog;
    
    private HashMap<String, ImageIcon> cacheImagenes = new HashMap<>();
    
    private String piezaPermitida = null;
    private int filaSeleccionada = -1;
    private int colSeleccionada = -1;
    private boolean esperandoDestino = false;
    private int girosRestantes = 1;

    public PTablero() {
        super("🧛 Vampire Wargame");
        
        Cuenta usuario = Cuenta.getUsuarioActual();
        String jugador1 = (usuario != null) ? usuario.getUsername() : "Jugador1";
        String jugador2 = seleccionarOponente();
        
        if (jugador2 == null) {
            // Si no hay oponentes, crear "Jugador2" automático
            jugador2 = "Jugador2";
        }
        
        tableroLogico = new Tablero(jugador1, jugador2);
        precargarImagenes();
        inicializarComponentes();
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private String seleccionarOponente() {
        Cuenta usuario = Cuenta.getUsuarioActual();
        java.util.ArrayList<Cuenta> cuentas = usuario.obtenerTodasCuentas();
        
        java.util.ArrayList<String> oponentes = new java.util.ArrayList<>();
        for (Cuenta c : cuentas) {
            if (!c.getUsername().equals(usuario.getUsername())) {
                oponentes.add(c.getUsername());
            }
        }
        
        if (oponentes.isEmpty()) {
            return null; // Retornar null para usar "Jugador2"
        }
        
        String[] opciones = oponentes.toArray(new String[0]);
        String seleccionado = (String) JOptionPane.showInputDialog(
            this,
            "Selecciona tu oponente:",
            "Seleccionar Oponente",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[0]
        );
        
        return seleccionado != null ? seleccionado : null;
    }

    private void precargarImagenes() {
        String[] tipos = {"vampiro", "hombre_lobo", "muerte", "zombie"};
        String[] colores = {"blanco", "negro"};
        
        for (String tipo : tipos) {
            for (String color : colores) {
                String key = tipo + "_" + color;
                String ruta = "/imagenes/piezas/" + key + ".png";
                
                try {
                    java.net.URL imgURL = getClass().getResource(ruta);
                    if (imgURL != null) {
                        ImageIcon icono = new ImageIcon(imgURL);
                        Image img = icono.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                        cacheImagenes.put(key, new ImageIcon(img));
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ No se encontró: " + ruta);
                }
            }
        }
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(new Color(15, 15, 25));

        add(crearPanelSuperior(), BorderLayout.NORTH);
        
        tableroGUI = new TableroPanel();
        add(tableroGUI, BorderLayout.CENTER);
        
        add(crearPanelDerecho(), BorderLayout.EAST);

        actualizarInterfaz();
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
        panel.setBackground(new Color(25, 25, 40));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(139, 0, 0), 3),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        lblTurno = new JLabel("Turno: " + tableroLogico.getTurnoJugadorActual(), SwingConstants.CENTER);
        lblTurno.setFont(new Font("Arial", Font.BOLD, 22));
        lblTurno.setForeground(new Color(255, 215, 0));

        lblPiezaSeleccionada = new JLabel("🎰 Presiona GIRAR RULETA para comenzar", SwingConstants.CENTER);
        lblPiezaSeleccionada.setFont(new Font("Arial", Font.BOLD, 16));
        lblPiezaSeleccionada.setForeground(new Color(255, 100, 100));

        lblMensaje = new JLabel("", SwingConstants.CENTER);
        lblMensaje.setFont(new Font("Arial", Font.ITALIC, 14));
        lblMensaje.setForeground(new Color(100, 200, 255));

        panel.add(lblTurno);
        panel.add(lblPiezaSeleccionada);
        panel.add(lblMensaje);

        return panel;
    }

    private JPanel crearPanelDerecho() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(20, 20, 35));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(139, 0, 0), 3),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.setPreferredSize(new Dimension(320, 0));

        JLabel lblRuleta = new JLabel("🎰 RULETA", SwingConstants.CENTER);
        lblRuleta.setForeground(new Color(255, 215, 0));
        lblRuleta.setFont(new Font("Serif", Font.BOLD, 20));
        lblRuleta.setAlignmentX(Component.CENTER_ALIGNMENT);

        ruletaPanel = new RuletaPanel();
        ruletaPanel.setMaximumSize(new Dimension(270, 180));
        ruletaPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnGirarRuleta = new JButton("⚡ GIRAR RULETA");
        btnGirarRuleta.setFont(new Font("Arial", Font.BOLD, 16));
        btnGirarRuleta.setBackground(new Color(139, 0, 0));
        btnGirarRuleta.setForeground(Color.WHITE);
        btnGirarRuleta.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnGirarRuleta.setMaximumSize(new Dimension(240, 55));
        btnGirarRuleta.setFocusPainted(false);
        btnGirarRuleta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGirarRuleta.addActionListener(e -> girarRuleta());

        btnRetirarse = new JButton("🏳️ RETIRARSE");
        btnRetirarse.setFont(new Font("Arial", Font.BOLD, 14));
        btnRetirarse.setBackground(new Color(70, 0, 0));
        btnRetirarse.setForeground(Color.WHITE);
        btnRetirarse.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRetirarse.setMaximumSize(new Dimension(240, 45));
        btnRetirarse.setFocusPainted(false);
        btnRetirarse.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRetirarse.addActionListener(e -> retirarse());

        JLabel lblLog = new JLabel("📜 Historial", SwingConstants.CENTER);
        lblLog.setForeground(new Color(255, 215, 0));
        lblLog.setFont(new Font("Arial", Font.BOLD, 15));
        lblLog.setAlignmentX(Component.CENTER_ALIGNMENT);

        areaLog = new JTextArea(14, 22);
        areaLog.setEditable(false);
        areaLog.setBackground(new Color(15, 15, 25));
        areaLog.setForeground(new Color(220, 220, 220));
        areaLog.setFont(new Font("Monospaced", Font.PLAIN, 11));
        areaLog.setLineWrap(true);
        areaLog.setWrapStyleWord(true);
        JScrollPane scrollLog = new JScrollPane(areaLog);
        scrollLog.setMaximumSize(new Dimension(290, 280));
        scrollLog.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 120), 2));

        panel.add(lblRuleta);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(ruletaPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(btnGirarRuleta);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
        panel.add(btnRetirarse);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(new JSeparator(SwingConstants.HORIZONTAL));
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(lblLog);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(scrollLog);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    // ========================================
    // RULETA SIMPLIFICADA (2x3)
    // ========================================
    private class RuletaPanel extends JPanel {
        private String[] piezas = {"LOBO", "MUERTE", "VAMPIRO", "LOBO", "MUERTE", "VAMPIRO"};
        private Color[] colores = {
            new Color(150, 75, 0),    // Lobo
            new Color(100, 0, 150),   // Muerte
            new Color(180, 0, 0),     // Vampiro
            new Color(150, 75, 0),
            new Color(100, 0, 150),
            new Color(180, 0, 0)
        };
        private int indiceSeleccionado = 0;
        private boolean girando = false;
        private Random random = new Random();

        public RuletaPanel() {
            setPreferredSize(new Dimension(270, 180));
            setOpaque(false);
        }

        public void girar(Runnable onComplete) {
            if (girando) return;
            
            girando = true;
            final int[] pasos = {0};
            final int totalPasos = 20 + random.nextInt(15);
            
            Timer timer = new Timer(80, null);
            timer.addActionListener(e -> {
                indiceSeleccionado = (indiceSeleccionado + 1) % piezas.length;
                repaint();
                pasos[0]++;
                
                if (pasos[0] >= totalPasos) {
                    timer.stop();
                    girando = false;
                    onComplete.run();
                }
            });
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int cellWidth = 80;
            int cellHeight = 70;
            int startX = (getWidth() - (cellWidth * 3)) / 2;
            int startY = (getHeight() - (cellHeight * 2)) / 2;

            // Dibujar cuadrícula 2x3
            for (int i = 0; i < 6; i++) {
                int row = i / 3;
                int col = i % 3;
                int x = startX + (col * cellWidth);
                int y = startY + (row * cellHeight);

                // Color de fondo
                g2.setColor(colores[i]);
                g2.fillRoundRect(x, y, cellWidth - 5, cellHeight - 5, 15, 15);

                // Borde dorado si está seleccionado
                if (i == indiceSeleccionado) {
                    g2.setColor(new Color(255, 215, 0));
                    g2.setStroke(new BasicStroke(4));
                    g2.drawRoundRect(x, y, cellWidth - 5, cellHeight - 5, 15, 15);
                } else {
                    g2.setColor(new Color(0, 0, 0, 150));
                    g2.setStroke(new BasicStroke(2));
                    g2.drawRoundRect(x, y, cellWidth - 5, cellHeight - 5, 15, 15);
                }

                // Texto simplificado
                g2.setFont(new Font("Arial", Font.BOLD, 14));
                String texto = piezas[i];
                FontMetrics fm = g2.getFontMetrics();
                int textX = x + (cellWidth - fm.stringWidth(texto)) / 2;
                int textY = y + ((cellHeight - fm.getHeight()) / 2) + fm.getAscent();
                
                // Sombra
                g2.setColor(new Color(0, 0, 0, 180));
                g2.drawString(texto, textX + 2, textY + 2);
                
                // Texto principal
                g2.setColor(Color.WHITE);
                g2.drawString(texto, textX, textY);
            }

            // Marco exterior
            g2.setColor(new Color(255, 215, 0));
            g2.setStroke(new BasicStroke(5));
            g2.drawRoundRect(startX - 5, startY - 5, (cellWidth * 3) + 5, (cellHeight * 2) + 5, 20, 20);
        }

        public String getPiezaSeleccionada() {
            String pieza = piezas[indiceSeleccionado];
            return pieza.equals("LOBO") ? "HOMBRE_LOBO" : pieza;
        }
    }

    private void girarRuleta() {
        if (girosRestantes <= 0) {
            lblMensaje.setText("❌ No tienes giros restantes");
            return;
        }
        
        try {
            btnGirarRuleta.setEnabled(false);
            agregarLog("🎲 Girando ruleta... (" + girosRestantes + " giros restantes)");
            
            ruletaPanel.girar(() -> {
                String seleccionada = ruletaPanel.getPiezaSeleccionada();
                
                if (tienePiezaTipo(seleccionada)) {
                    piezaPermitida = seleccionada;
                    lblPiezaSeleccionada.setText("✨ Pieza: " + getNombreCompleto(seleccionada));
                    lblMensaje.setText("👆 Selecciona tu " + getNombreCompleto(seleccionada));
                    agregarLog("✅ " + getNombreCompleto(seleccionada) + " disponible");
                    girosRestantes = 0;
                } else {
                    girosRestantes--;
                    if (girosRestantes > 0) {
                        lblMensaje.setText("⚠️ No tienes esa pieza. Giros restantes: " + girosRestantes);
                        agregarLog("⚠️ Sin " + getNombreCompleto(seleccionada) + " - " + girosRestantes + " giros más");
                        btnGirarRuleta.setEnabled(true);
                    } else {
                        lblMensaje.setText("❌ Sin piezas - Pierdes turno");
                        agregarLog("❌ Turno perdido por falta de piezas");
                        Timer timer = new Timer(2000, e -> {
                            btnGirarRuleta.setEnabled(true);
                            cambiarTurno();
                        });
                        timer.setRepeats(false);
                        timer.start();
                    }
                }
            });
        } catch (Exception e) {
            mostrarError("Error en ruleta: " + e.getMessage());
            btnGirarRuleta.setEnabled(true);
        }
    }

    private boolean tienePiezaTipo(String tipo) {
        for (int i = 0; i < TAMANO_TABLERO; i++) {
            for (int j = 0; j < TAMANO_TABLERO; j++) {
                Pieza p = tableroLogico.getPieza(i, j);
                if (p != null && p.getTipo().equals(tipo) && 
                    p.getColor().equals(tableroLogico.getTurnoActual())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void retirarse() {
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro que deseas retirarte?\nEl oponente ganará automáticamente.",
                "⚠️ Confirmar Retiro",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            String ganador = tableroLogico.getTurnoActual().equals("BLANCO") 
                ? tableroLogico.getJugador2() 
                : tableroLogico.getJugador1();
            
            String perdedor = tableroLogico.getTurnoActual().equals("BLANCO") 
                ? tableroLogico.getJugador1() 
                : tableroLogico.getJugador2();
            
            tableroLogico.finalizarPartida(ganador, "RETIRO");
            
            // Registrar para AMBOS jugadores
            registrarPartidaParaAmbos(ganador, perdedor, true);
            
            JOptionPane.showMessageDialog(this,
                    "🏆 ¡" + ganador + " ha ganado por retiro!\n+3 puntos",
                    "Fin del Juego",
                    JOptionPane.INFORMATION_MESSAGE);
            
            dispose();
            new MenuPrincipal().setVisible(true);
        }
    }

    private void manejarClick(int fila, int col) {
        try {
            if (piezaPermitida == null) {
                lblMensaje.setText("⚠️ Primero gira la ruleta");
                return;
            }

            Pieza piezaEnCasilla = tableroLogico.getPieza(fila, col);

            if (!esperandoDestino) {
                if (piezaEnCasilla == null) {
                    lblMensaje.setText("❌ Casilla vacía");
                    return;
                }

                if (!piezaEnCasilla.getColor().equals(tableroLogico.getTurnoActual())) {
                    lblMensaje.setText("❌ No es tu pieza");
                    return;
                }

                if (!piezaEnCasilla.getTipo().equals(piezaPermitida)) {
                    lblMensaje.setText("❌ Debes mover " + getNombreCompleto(piezaPermitida));
                    return;
                }

                filaSeleccionada = fila;
                colSeleccionada = col;
                esperandoDestino = true;
                lblMensaje.setText("✅ Selecciona destino (🟢=mover, 🔴=atacar)");
                tableroGUI.repaint();
            } else {
                realizarAccion(fila, col);
            }
        } catch (Exception e) {
            mostrarError("Error: " + e.getMessage());
        }
    }

    private void realizarAccion(int filaDestino, int colDestino) {
        try {
            Pieza destino = tableroLogico.getPieza(filaDestino, colDestino);
            Pieza origen = tableroLogico.getPieza(filaSeleccionada, colSeleccionada);

            if (destino == null) {
                if (origen instanceof Muerte) {
                    String[] opciones = {"Mover Muerte", "Conjurar Zombie"};
                    int opcion = JOptionPane.showOptionDialog(this,
                            "¿Qué deseas hacer?", "💀 Acción de la Muerte",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null, opciones, opciones[0]);
                    
                    if (opcion == 1) {
                        String resultado = tableroLogico.conjurarZombie(
                                filaSeleccionada, colSeleccionada, filaDestino, colDestino);
                        agregarLog(resultado);
                        finalizarTurno();
                        return;
                    }
                } else if (origen instanceof Lobo) {
                    int deltaF = Math.abs(filaDestino - filaSeleccionada);
                    int deltaC = Math.abs(colDestino - colSeleccionada);
                    
                    if (deltaF == 2 || deltaC == 2) {
                        String resultado = tableroLogico.loboMover2Casillas(
                                filaSeleccionada, colSeleccionada, filaDestino, colDestino);
                        agregarLog(resultado);
                        finalizarTurno();
                        return;
                    }
                }
                
                boolean movido = tableroLogico.moverPieza(
                        filaSeleccionada, colSeleccionada, filaDestino, colDestino);
                
                if (movido) {
                    agregarLog("✅ Movimiento exitoso");
                    finalizarTurno();
                } else {
                    lblMensaje.setText("❌ Movimiento inválido");
                }
            } else if (!destino.getColor().equals(tableroLogico.getTurnoActual())) {
                String[] opciones = obtenerOpcionesAtaque(origen);
                
                int opcion = JOptionPane.showOptionDialog(this,
                        "Selecciona el tipo de ataque:", "⚔️ Atacar",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null, opciones, opciones[0]);
                
                String resultado = ejecutarAtaque(opcion, origen, filaDestino, colDestino);
                agregarLog(resultado);
                finalizarTurno();
            } else {
                lblMensaje.setText("❌ No puedes atacar tus propias piezas");
            }
        } catch (Exception e) {
            mostrarError("Error: " + e.getMessage());
        }
    }

    private String[] obtenerOpcionesAtaque(Pieza atacante) {
        if (atacante instanceof Vampiros) {
            return new String[]{"⚔️ Ataque Normal", "🩸 Chupar Sangre"};
        } else if (atacante instanceof Muerte) {
            return new String[]{"⚔️ Ataque Normal", "🗡️ Lanzar Lanza", "🧟 Ataque Zombie"};
        }
        return new String[]{"⚔️ Ataque Normal"};
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
        }
        return tableroLogico.atacarNormal(
                filaSeleccionada, colSeleccionada, filaDestino, colDestino);
    }

    private void finalizarTurno() {
        String ganador = tableroLogico.verificarGanador();
        if (ganador != null) {
            String perdedor = ganador.equals(tableroLogico.getJugador1()) 
                ? tableroLogico.getJugador2() 
                : tableroLogico.getJugador1();
            
            tableroLogico.finalizarPartida(ganador, "VICTORIA");
            
            // Registrar para AMBOS jugadores
            registrarPartidaParaAmbos(ganador, perdedor, false);
            
            JOptionPane.showMessageDialog(this,
                    "🏆 ¡" + ganador + " ha ganado la partida!\n+3 puntos",
                    "¡Victoria!",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new MenuPrincipal().setVisible(true);
            return;
        }

        filaSeleccionada = -1;
        colSeleccionada = -1;
        esperandoDestino = false;
        piezaPermitida = null;
        btnGirarRuleta.setEnabled(true);
        cambiarTurno();
    }

    private void registrarPartidaParaAmbos(String ganador, String perdedor, boolean retiro) {
        Cuenta cuentaGanadora = Cuenta.getUsuarioActual().buscarCuenta(ganador);
        Cuenta cuentaPerdedora = Cuenta.getUsuarioActual().buscarCuenta(perdedor);
        
        if (cuentaGanadora != null) {
            cuentaGanadora.registrarPartida(true); // Ganó
        }
        
        if (cuentaPerdedora != null) {
            cuentaPerdedora.registrarPartida(false); // Perdió
        }
    }

    private void cambiarTurno() {
        tableroLogico.cambiarTurno();
        
        int piezasPerdidas = 6 - contarPiezasPrincipales(tableroLogico.getTurnoActual());
        if (piezasPerdidas >= 4) {
            girosRestantes = 3;
        } else if (piezasPerdidas >= 2) {
            girosRestantes = 2;
        } else {
            girosRestantes = 1;
        }
        
        actualizarInterfaz();
    }

    private int contarPiezasPrincipales(String color) {
        int cuenta = 0;
        for (int i = 0; i < TAMANO_TABLERO; i++) {
            for (int j = 0; j < TAMANO_TABLERO; j++) {
                Pieza p = tableroLogico.getPieza(i, j);
                if (p != null && p.getColor().equals(color) && !p.getTipo().equals("ZOMBIE")) {
                    cuenta++;
                }
            }
        }
        return cuenta;
    }

    private void actualizarInterfaz() {
        String jugador = tableroLogico.getTurnoJugadorActual();
        String color = tableroLogico.getTurnoActual();
        lblTurno.setText("🎮 Turno: " + jugador + " (" + color + ")");
        lblPiezaSeleccionada.setText("🎰 Presiona GIRAR RULETA (" + girosRestantes + " giros)");
        lblMensaje.setText("");
        tableroGUI.repaint();
    }

    private void agregarLog(String mensaje) {
        String tiempo = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
        areaLog.append("[" + tiempo + "] " + mensaje + "\n");
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
    }

    private String getNombreCompleto(String tipo) {
        switch (tipo) {
            case "HOMBRE_LOBO": return "Hombre Lobo";
            case "VAMPIRO": return "Vampiro";
            case "MUERTE": return "Muerte";
            case "ZOMBIE": return "Zombie";
            default: return tipo;
        }
    }

    private void mostrarError(String mensaje) {
        System.err.println("❌ " + mensaje);
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // ========================================
    // TABLERO MEJORADO
    // ========================================
    private class TableroPanel extends JPanel {
        private CasillaButton[][] casillas;

        public TableroPanel() {
            setLayout(new GridLayout(TAMANO_TABLERO, TAMANO_TABLERO, 4, 4));
            setBackground(new Color(20, 20, 30));
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(139, 0, 0), 5),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
            ));
            
            casillas = new CasillaButton[TAMANO_TABLERO][TAMANO_TABLERO];
            crearCasillas();
        }

        private void crearCasillas() {
            for (int f = 0; f < TAMANO_TABLERO; f++) {
                for (int c = 0; c < TAMANO_TABLERO; c++) {
                    casillas[f][c] = new CasillaButton(f, c);
                    add(casillas[f][c]);
                }
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            actualizarTablero();
        }

        private void actualizarTablero() {
            for (int f = 0; f < TAMANO_TABLERO; f++) {
                for (int c = 0; c < TAMANO_TABLERO; c++) {
                    casillas[f][c].actualizar();
                }
            }
        }
    }

    // ========================================
    // CASILLA MEJORADA
    // ========================================
    private class CasillaButton extends JButton {
        private int fila, col;
        private Color colorBase;

        public CasillaButton(int fila, int col) {
            this.fila = fila;
            this.col = col;
            
            colorBase = ((fila + col) % 2 == 0) ? 
                new Color(245, 222, 179) : new Color(139, 69, 19);
            
            setPreferredSize(new Dimension(95, 95));
            setBackground(colorBase);
            setFont(new Font("Arial", Font.BOLD, 12));
            setFocusPainted(false);
            setBorderPainted(true);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setHorizontalTextPosition(SwingConstants.CENTER);
            setVerticalTextPosition(SwingConstants.BOTTOM);
            
            addActionListener(e -> manejarClick(fila, col));
        }

        public void actualizar() {
            Pieza pieza = tableroLogico.getPieza(fila, col);
            
            Color colorActual = colorBase;
            java.awt.Color bordeColor = new Color(80, 40, 10);
            int bordeTamaño = 2;
            
            if (fila == filaSeleccionada && col == colSeleccionada) {
                colorActual = new Color(255, 215, 0, 200);
                bordeColor = new Color(255, 215, 0);
                bordeTamaño = 5;
            } 
            else if (esperandoDestino && filaSeleccionada != -1) {
                Pieza seleccionada = tableroLogico.getPieza(filaSeleccionada, colSeleccionada);
                
                if (pieza == null && esMovimientoValido(seleccionada, fila, col)) {
                    colorActual = new Color(0, 255, 100, 140);
                    bordeColor = new Color(0, 200, 0);
                    bordeTamaño = 4;
                } 
                else if (pieza != null && !pieza.getColor().equals(tableroLogico.getTurnoActual()) &&
                           esAtaqueValido(seleccionada, fila, col)) {
                    colorActual = new Color(255, 50, 50, 140);
                    bordeColor = new Color(200, 0, 0);
                    bordeTamaño = 4;
                }
            }
            
            setBackground(colorActual);
            setBorder(BorderFactory.createLineBorder(bordeColor, bordeTamaño));
            
            if (pieza != null) {
                String key = pieza.getTipo().toLowerCase() + "_" + pieza.getColor().toLowerCase();
                ImageIcon icono = cacheImagenes.get(key);
                
                if (icono != null) {
                    setIcon(icono);
                    setText("");
                } else {
                    setIcon(null);
                    String emoji = getEmojiPieza(pieza);
                    setText("<html><center><font size='8'>" + emoji + "</font><br><font size='2' color='" + 
                            (pieza.getColor().equals("BLANCO") ? "blue" : "red") + "'>V:" + pieza.getVidas() + 
                            " E:" + pieza.getEscudo() + "</font></center></html>");
                }
                
                setForeground(pieza.getColor().equals("BLANCO") ? 
                    new Color(50, 50, 200) : new Color(200, 50, 50));
            } else {
                setIcon(null);
                setText("");
            }
        }

        private boolean esMovimientoValido(Pieza pieza, int destinoF, int destinoC) {
            if (pieza == null) return false;
            
            int deltaF = Math.abs(destinoF - filaSeleccionada);
            int deltaC = Math.abs(destinoC - colSeleccionada);
            
            if (deltaF <= 1 && deltaC <= 1 && !(deltaF == 0 && deltaC == 0)) {
                return true;
            }
            
            if (pieza instanceof Lobo) {
                if ((deltaF == 2 && deltaC == 0) || 
                    (deltaF == 0 && deltaC == 2) || 
                    (deltaF == 2 && deltaC == 2)) {
                    return verificarCaminoLibre(filaSeleccionada, colSeleccionada, destinoF, destinoC);
                }
            }
            
            return false;
        }

        private boolean esAtaqueValido(Pieza pieza, int destinoF, int destinoC) {
            if (pieza == null) return false;
            
            int deltaF = Math.abs(destinoF - filaSeleccionada);
            int deltaC = Math.abs(destinoC - colSeleccionada);
            
            if (deltaF <= 1 && deltaC <= 1 && !(deltaF == 0 && deltaC == 0)) {
                return true;
            }
            
            if (pieza instanceof Muerte) {
                if ((deltaF == 2 && deltaC == 0) || 
                    (deltaF == 0 && deltaC == 2) || 
                    (deltaF == 2 && deltaC == 2)) {
                    return true;
                }
                
                if (tableroLogico.hayZombieAdyacente(destinoF, destinoC, pieza.getColor())) {
                    return true;
                }
            }
            
            return false;
        }

        private boolean verificarCaminoLibre(int origenF, int origenC, int destinoF, int destinoC) {
            int pasoF = Integer.compare(destinoF, origenF);
            int pasoC = Integer.compare(destinoC, origenC);
            
            int f = origenF + pasoF;
            int c = origenC + pasoC;
            
            while (f != destinoF || c != destinoC) {
                if (tableroLogico.getPieza(f, c) != null) {
                    return false;
                }
                f += pasoF;
                c += pasoC;
            }
            
            return true;
        }

        private String getEmojiPieza(Pieza pieza) {
            switch (pieza.getTipo()) {
                case "VAMPIRO": return "🧛";
                case "HOMBRE_LOBO": return "🐺";
                case "MUERTE": return "💀";
                case "ZOMBIE": return "🧟";
                default: return "❓";
            }
        }
    }
}