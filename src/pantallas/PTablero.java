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

    private String piezaPermitida = null;
    private int filaSeleccionada = -1;
    private int colSeleccionada = -1;
    private boolean esperandoDestino = false;
    private int girosRestantes = 1;

    public PTablero() {
        super("Vampire Wargame");

        Cuenta usuario = Cuenta.getUsuarioActual();
        String jugador1 = (usuario != null) ? usuario.getUsername() : "Jugador1";
        String jugador2 = seleccionarOponente();

        if (jugador2 == null) {
            jugador2 = "Jugador2";
        }

        tableroLogico = new Tablero(jugador1, jugador2);
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
            return null;
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

        return seleccionado;
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

        lblTurno = new JLabel(" Turno: " + tableroLogico.getTurnoJugadorActual(), SwingConstants.CENTER);
        lblTurno.setFont(new Font("Arial", Font.BOLD, 22));
        lblTurno.setForeground(new Color(255, 215, 0));

        lblPiezaSeleccionada = new JLabel(" Presiona GIRAR RULETA para comenzar", SwingConstants.CENTER);
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

        JLabel lblRuleta = new JLabel("RULETA", SwingConstants.CENTER);
        lblRuleta.setForeground(new Color(255, 215, 0));
        lblRuleta.setFont(new Font("Serif", Font.BOLD, 20));
        lblRuleta.setAlignmentX(Component.CENTER_ALIGNMENT);

        ruletaPanel = new RuletaPanel();
        ruletaPanel.setPreferredSize(new Dimension(240, 240));
        ruletaPanel.setMaximumSize(new Dimension(240, 240));
        ruletaPanel.setMinimumSize(new Dimension(240, 240));
        ruletaPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnGirarRuleta = new JButton("GIRAR RULETA");
        btnGirarRuleta.setFont(new Font("Arial", Font.BOLD, 16));
        btnGirarRuleta.setBackground(new Color(139, 0, 0));
        btnGirarRuleta.setForeground(Color.WHITE);
        btnGirarRuleta.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnGirarRuleta.setMaximumSize(new Dimension(240, 55));
        btnGirarRuleta.setFocusPainted(false);
        btnGirarRuleta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGirarRuleta.addActionListener(e -> girarRuleta());

        btnRetirarse = new JButton("RETIRARSE");
        btnRetirarse.setFont(new Font("Arial", Font.BOLD, 14));
        btnRetirarse.setBackground(new Color(70, 0, 0));
        btnRetirarse.setForeground(Color.WHITE);
        btnRetirarse.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRetirarse.setMaximumSize(new Dimension(240, 45));
        btnRetirarse.setFocusPainted(false);
        btnRetirarse.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRetirarse.addActionListener(e -> retirarse());

        JLabel lblLog = new JLabel("Historial", SwingConstants.CENTER);
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
    // RULETA CIRCULAR QUE SÍ GIRA
    // ========================================
    private class RuletaPanel extends JPanel {

        private String[] piezas = {"LOBO", "VAMPIRO", "MUERTE", "LOBO", "VAMPIRO", "MUERTE"};
        private Color[] colores = {
            new Color(150, 75, 0), new Color(180, 0, 0), new Color(100, 0, 150),
            new Color(150, 75, 0), new Color(180, 0, 0), new Color(100, 0, 150)
        };
        private double anguloRotacion = 0;
        private boolean girando = false;
        private Random random = new Random();
        private int indiceSeleccionado = 0;

        public RuletaPanel() {
            setOpaque(false);
        }

        public void girar(Runnable onComplete) {
            if (girando) {
                return;
            }

            girando = true;
            final double[] velocidad = {30.0};
            final int[] frames = {0};
            final int duracionGiro = 50; // frames

            Timer timer = new Timer(16, null);
            timer.addActionListener(e -> {
                anguloRotacion += velocidad[0];
                if (anguloRotacion >= 360) {
                    anguloRotacion -= 360;
                }

                velocidad[0] *= 0.97; // Desaceleración
                frames[0]++;

                repaint();

                if (frames[0] >= duracionGiro && velocidad[0] < 1.0) {
                    timer.stop();
                    girando = false;

                    // Calcular pieza seleccionada
                    double anguloSegmento = 360.0 / piezas.length;
                    double anguloIndicador = (360 - anguloRotacion + 90) % 360;
                    indiceSeleccionado = (int) (anguloIndicador / anguloSegmento);

                    repaint();
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

            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            int radio = 100;

            // Guardar transformación original
            java.awt.geom.AffineTransform transformOriginal = g2.getTransform();

            // Aplicar rotación
            g2.rotate(Math.toRadians(anguloRotacion), centerX, centerY);

            // Dibujar segmentos
            int numSegmentos = piezas.length;
            double anguloSegmento = 360.0 / numSegmentos;

            for (int i = 0; i < numSegmentos; i++) {
                double anguloInicio = i * anguloSegmento;

                g2.setColor(colores[i]);
                g2.fillArc(centerX - radio, centerY - radio, radio * 2, radio * 2,
                        (int) anguloInicio, (int) anguloSegmento);

                g2.setColor(new Color(0, 0, 0, 100));
                g2.setStroke(new BasicStroke(2));
                g2.drawArc(centerX - radio, centerY - radio, radio * 2, radio * 2,
                        (int) anguloInicio, (int) anguloSegmento);

                // Texto
                double anguloMedio = Math.toRadians(anguloInicio + anguloSegmento / 2);
                int textX = centerX + (int) (Math.cos(anguloMedio) * radio * 0.65);
                int textY = centerY + (int) (Math.sin(anguloMedio) * radio * 0.65);

                g2.setFont(new Font("Arial", Font.BOLD, 11));
                String texto = piezas[i];
                FontMetrics fm = g2.getFontMetrics();

                g2.setColor(new Color(0, 0, 0, 180));
                g2.drawString(texto, textX - fm.stringWidth(texto) / 2 + 1, textY + 1);

                g2.setColor(Color.WHITE);
                g2.drawString(texto, textX - fm.stringWidth(texto) / 2, textY);
            }

            // Restaurar transformación
            g2.setTransform(transformOriginal);

            // Círculo exterior dorado
            g2.setColor(new Color(255, 215, 0));
            g2.setStroke(new BasicStroke(6));
            g2.drawOval(centerX - radio - 5, centerY - radio - 5, (radio + 5) * 2, (radio + 5) * 2);

            // Indicador (triángulo en la parte superior)
            int[] xPoints = {centerX, centerX - 15, centerX + 15};
            int[] yPoints = {centerY - radio - 20, centerY - radio - 5, centerY - radio - 5};
            g2.setColor(new Color(255, 215, 0));
            g2.fillPolygon(xPoints, yPoints, 3);
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2));
            g2.drawPolygon(xPoints, yPoints, 3);

            // Círculo central
            g2.setColor(new Color(50, 50, 50));
            g2.fillOval(centerX - 25, centerY - 25, 50, 50);
            g2.setColor(new Color(255, 215, 0));
            g2.setStroke(new BasicStroke(3));
            g2.drawOval(centerX - 25, centerY - 25, 50, 50);
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
            agregarLog("Girando ruleta... (" + girosRestantes + " giros restantes)");

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
                        lblMensaje.setText("No tienes esa pieza. Giros restantes: " + girosRestantes);
                        agregarLog("Sin " + getNombreCompleto(seleccionada) + " - " + girosRestantes + " giros más");
                        btnGirarRuleta.setEnabled(true);
                    } else {
                        lblMensaje.setText("❌ Sin piezas - Pierdes turno");
                        agregarLog("❌ Turno perdido por falta de piezas");
                        Timer timer = new Timer(2000, ev -> {
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
                if (p != null && p.getTipo().equals(tipo)
                        && p.getColor().equals(tableroLogico.getTurnoActual())) {
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
                lblMensaje.setText("✅ Selecciona destino");
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
                            "¿Qué deseas hacer?", "Acción de la Muerte",
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
                    agregarLog("Movimiento exitoso");
                    finalizarTurno();
                } else {
                    lblMensaje.setText("❌ Movimiento inválido");
                }
            } else if (!destino.getColor().equals(tableroLogico.getTurnoActual())) {
                String[] opciones = obtenerOpcionesAtaque(origen, filaDestino, colDestino);

                int opcion = JOptionPane.showOptionDialog(this,
                        "Selecciona el tipo de ataque:", "⚔️ Atacar",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null, opciones, opciones[0]);

                String resultado = ejecutarAtaque(opcion, origen, filaDestino, colDestino);
                agregarLog(resultado);
                finalizarTurno();
            } else {
                lblMensaje.setText("❌No puedes atacar tus propias piezas");
            }
        } catch (Exception e) {
            mostrarError("Error: " + e.getMessage());
        }
    }

    private String[] obtenerOpcionesAtaque(Pieza atacante, int destinoF, int destinoC) {
        java.util.ArrayList<String> opciones = new java.util.ArrayList<>();
        opciones.add("Ataque Normal");

        if (atacante instanceof Vampiros) {
            int deltaF = Math.abs(destinoF - filaSeleccionada);
            int deltaC = Math.abs(destinoC - colSeleccionada);
            if (deltaF <= 1 && deltaC <= 1) {
                opciones.add("Chupar Sangre");
            }
        } else if (atacante instanceof Muerte) {
            int deltaF = Math.abs(destinoF - filaSeleccionada);
            int deltaC = Math.abs(destinoC - colSeleccionada);

            if ((deltaF == 2 && deltaC == 0) || (deltaF == 0 && deltaC == 2) || (deltaF == 2 && deltaC == 2)) {
                if (verificarCaminoLibre(filaSeleccionada, colSeleccionada, destinoF, destinoC)) {
                    opciones.add("Lanzar Lanza");
                }
            }

            if (tableroLogico.hayZombieAdyacente(destinoF, destinoC, atacante.getColor())) {
                opciones.add("Ataque Zombie");
            }
        }

        return opciones.toArray(new String[0]);
    }

    private String ejecutarAtaque(int opcion, Pieza atacante, int filaDestino, int colDestino) {
        String[] opciones = obtenerOpcionesAtaque(atacante, filaDestino, colDestino);

        if (opcion < 0 || opcion >= opciones.length) {
            return "Ataque cancelado";
        }

        String opcionSeleccionada = opciones[opcion];

        if (opcionSeleccionada.contains("Chupar Sangre")) {
            return tableroLogico.vampiroChuparSangre(
                    filaSeleccionada, colSeleccionada, filaDestino, colDestino);
        } else if (opcionSeleccionada.contains("Lanzar Lanza")) {
            return tableroLogico.muerteLanzarLanza(
                    filaSeleccionada, colSeleccionada, filaDestino, colDestino);
        } else if (opcionSeleccionada.contains("Ataque Zombie")) {
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
            registrarPartidaParaAmbos(ganador, perdedor, false);

            JOptionPane.showMessageDialog(this,
                    "¡" + ganador + " ha ganado la partida!\n+3 puntos",
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
        Cuenta usuario = Cuenta.getUsuarioActual();
        Cuenta cuentaGanadora = usuario.buscarCuenta(ganador);
        Cuenta cuentaPerdedora = usuario.buscarCuenta(perdedor);

        if (cuentaGanadora != null) {
            cuentaGanadora.registrarPartida(true);
        }

        if (cuentaPerdedora != null) {
            cuentaPerdedora.registrarPartida(false);
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

    private void actualizarInterfaz() {
        String jugador = tableroLogico.getTurnoJugadorActual();
        String color = tableroLogico.getTurnoActual();
        lblTurno.setText("Turno: " + jugador + " (" + color + ")");
        lblPiezaSeleccionada.setText("Presiona GIRAR RULETA (" + girosRestantes + " giros)");
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
            case "HOMBRE_LOBO":
                return "lobo";
            case "VAMPIRO":
                return "vampiro";
            case "MUERTE":
                return "muerte";
            case "ZOMBIE":
                return "zombie";
            default:
                return tipo;
        }
    }

    private void mostrarError(String mensaje) {
        System.err.println("❌ " + mensaje);
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // ========================================
    // TABLERO CON IMÁGENES
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
    // CASILLA CON IMÁGENES Y OVERLAY DE COLORES
    // ========================================
    private class CasillaButton extends JButton {

        private int fila, col;
        private Color colorBase;
        private ImageIcon imagenActual = null;

        public CasillaButton(int fila, int col) {
            this.fila = fila;
            this.col = col;

            colorBase = ((fila + col) % 2 == 0)
                    ? new Color(245, 222, 179) : new Color(139, 69, 19);

            setPreferredSize(new Dimension(95, 95));
            setBackground(colorBase);
            setFont(new Font("Arial", Font.BOLD, 10));
            setFocusPainted(false);
            setBorderPainted(true);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setContentAreaFilled(false);
            setOpaque(true);

            addActionListener(e -> manejarClick(fila, col));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Dibujar fondo base
            g2.setColor(colorBase);
            g2.fillRect(0, 0, getWidth(), getHeight());

            // Aplicar overlay de color si está seleccionada o es destino válido
            Color overlay = obtenerColorOverlay();
            if (overlay != null) {
                g2.setColor(overlay);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }

            // Dibujar imagen de la pieza si existe
            if (imagenActual != null) {
                int x = (getWidth() - imagenActual.getIconWidth()) / 2;
                int y = (getHeight() - imagenActual.getIconHeight()) / 2;
                imagenActual.paintIcon(this, g2, x, y);
            }

            // Dibujar estadísticas si hay pieza
            Pieza pieza = tableroLogico.getPieza(fila, col);
            if (pieza != null) {
                g2.setFont(new Font("Arial", Font.BOLD, 10));
                String stats = "V:" + pieza.getVidas() + " E:" + pieza.getEscudo();
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(stats);

                // Fondo semitransparente para el texto
                g2.setColor(new Color(0, 0, 0, 150));
                g2.fillRect((getWidth() - textWidth) / 2 - 3, getHeight() - 18, textWidth + 6, 16);

                // Texto
                Color colorTexto = pieza.getColor().equals("BLANCO")
                        ? new Color(100, 150, 255) : new Color(255, 100, 100);
                g2.setColor(colorTexto);
                g2.drawString(stats, (getWidth() - textWidth) / 2, getHeight() - 5);
            }

            // Dibujar borde
            Color bordeColor = obtenerColorBorde();
            int bordeTamaño = obtenerTamañoBorde();
            g2.setColor(bordeColor);
            g2.setStroke(new BasicStroke(bordeTamaño));
            g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        }

        private Color obtenerColorOverlay() {
            // Casilla seleccionada (dorada)
            if (fila == filaSeleccionada && col == colSeleccionada) {
                return new Color(255, 215, 0, 120);
            }

            // Casillas de destino
            if (esperandoDestino && filaSeleccionada != -1) {
                Pieza seleccionada = tableroLogico.getPieza(filaSeleccionada, colSeleccionada);
                Pieza actual = tableroLogico.getPieza(fila, col);

                // MUERTE: Puede conjurar zombie en CUALQUIER casilla vacía
                if (seleccionada instanceof Muerte && actual == null) {
                    return new Color(138, 43, 226, 100); // Morado para zombies
                } // Movimiento normal (verde)
                else if (actual == null && esMovimientoValido(seleccionada, fila, col)) {
                    return new Color(0, 255, 100, 100);
                } // Ataque (rojo)
                else if (actual != null && !actual.getColor().equals(tableroLogico.getTurnoActual())
                        && esAtaqueValido(seleccionada, fila, col)) {
                    return new Color(255, 50, 50, 100);
                }
            }

            return null;
        }

        private Color obtenerColorBorde() {
            if (fila == filaSeleccionada && col == colSeleccionada) {
                return new Color(255, 215, 0);
            }
            return new Color(80, 40, 10);
        }

        private int obtenerTamañoBorde() {
            if (fila == filaSeleccionada && col == colSeleccionada) {
                return 5;
            }

            if (esperandoDestino && filaSeleccionada != -1) {
                Pieza seleccionada = tableroLogico.getPieza(filaSeleccionada, colSeleccionada);
                Pieza actual = tableroLogico.getPieza(fila, col);

                if ((seleccionada instanceof Muerte && actual == null)
                        || (actual == null && esMovimientoValido(seleccionada, fila, col))
                        || (actual != null && !actual.getColor().equals(tableroLogico.getTurnoActual())
                        && esAtaqueValido(seleccionada, fila, col))) {
                    return 4;
                }
            }

            return 2;
        }

        public void actualizar() {
            Pieza pieza = tableroLogico.getPieza(fila, col);
            if (pieza != null) {
                String tipo = pieza.getTipo().toLowerCase();
                String color = pieza.getColor().toLowerCase();
                String ruta = "/imagenes/" + tipo + "_" + color + ".png"; // ejemplo: /imagenes/lobo_blanco.png
                java.net.URL url = getClass().getResource(ruta);
                if (url != null) {
                    imagenActual = new ImageIcon(url);
                } else {
                    imagenActual = null; // si no encuentra imagen
                }
            } else {
                imagenActual = null;
            }
            repaint();
        }

        private String obtenerNombreImagen(Pieza pieza) {
            String tipo = pieza.getTipo().toLowerCase();
            String color = pieza.getColor().toLowerCase();

            // Mapear nombres de tipos
            if (tipo.equals("HOMBRE_LOBO")) {
                tipo = "lobo";
            }

            return tipo + "_" + color + ".png";
        }

        private boolean esMovimientoValido(Pieza pieza, int destinoF, int destinoC) {
            if (pieza == null) {
                return false;
            }

            int deltaF = Math.abs(destinoF - filaSeleccionada);
            int deltaC = Math.abs(destinoC - colSeleccionada);

            if (deltaF <= 1 && deltaC <= 1 && !(deltaF == 0 && deltaC == 0)) {
                return true;
            }

            if (pieza instanceof Lobo) {
                if ((deltaF == 2 && deltaC == 0)
                        || (deltaF == 0 && deltaC == 2)
                        || (deltaF == 2 && deltaC == 2)) {
                    return verificarCaminoLibre(filaSeleccionada, colSeleccionada, destinoF, destinoC);
                }
            }

            return false;
        }

        private boolean esAtaqueValido(Pieza pieza, int destinoF, int destinoC) {
            if (pieza == null) {
                return false;
            }

            int deltaF = Math.abs(destinoF - filaSeleccionada);
            int deltaC = Math.abs(destinoC - colSeleccionada);

            if (deltaF <= 1 && deltaC <= 1 && !(deltaF == 0 && deltaC == 0)) {
                return true;
            }

            if (pieza instanceof Muerte) {
                if ((deltaF == 2 && deltaC == 0)
                        || (deltaF == 0 && deltaC == 2)
                        || (deltaF == 2 && deltaC == 2)) {
                    return verificarCaminoLibre(filaSeleccionada, colSeleccionada, destinoF, destinoC);
                }

                if (tableroLogico.hayZombieAdyacente(destinoF, destinoC, pieza.getColor())) {
                    return true;
                }
            }

            return false;
        }
    }
}
