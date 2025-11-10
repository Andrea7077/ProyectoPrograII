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
 * 
 */
public class PTablero extends JFrame {


    private static final int TAMANO_TABLERO = 6;
    
    private static final Color FONDO_PRINCIPAL = new Color(18, 8, 15);
    private static final Color SANGRE_OSCURA = new Color(120, 0, 20);
    private static final Color SANGRE_BRILLANTE = new Color(180, 0, 30);
    private static final Color MORADO_OSCURO = new Color(70, 20, 80);
    private static final Color GRIS_PIEDRA = new Color(45, 40, 50);
    private static final Color ORO_ANTIGUO = new Color(200, 170, 100);
    
    private Tablero tableroLogico;
    private TableroPanel tableroGUI;
    private RuletaPanel ruletaPanel;
    private JLabel lblTurno, lblPiezaSeleccionada, lblMensaje;
    private JLabel lblPiezasBlanco, lblPiezasNegro;
    private JButton btnGirarRuleta, btnRetirarse;

    private String piezaPermitida = null;
    private int filaSeleccionada = -1;
    private int colSeleccionada = -1;
    private boolean esperandoDestino = false;
    private int girosRestantes = 1;

    public PTablero() {
        super("Vampire Wargame - Batalla Nocturna");

        Cuenta usuario = Cuenta.getUsuarioActual();
        String jugador1 = (usuario != null) ? usuario.getUsername() : "Jugador1";
        String jugador2 = seleccionarOponente();

        if (jugador2 == null) {
            jugador2 = "Jugador2";
        }

        tableroLogico = new Tablero(jugador1, jugador2);
        inicializarComponentes();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1280, 820);
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
                "Selecciona tu adversario para la batalla nocturna:",
                "Seleccionar Oponente",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        return seleccionado;
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(FONDO_PRINCIPAL);

        add(crearPanelSuperior(), BorderLayout.NORTH);

        tableroGUI = new TableroPanel();
        add(tableroGUI, BorderLayout.CENTER);

        add(crearPanelDerecho(), BorderLayout.EAST);

        actualizarInterfaz();
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout(15, 5));
        panel.setBackground(GRIS_PIEDRA);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SANGRE_OSCURA, 4),
                BorderFactory.createEmptyBorder(12, 18, 12, 18)
        ));

        JPanel panelInfo = new JPanel(new GridLayout(3, 1, 5, 5));
        panelInfo.setOpaque(false);

        lblTurno = new JLabel("Turno: " + tableroLogico.getTurnoJugadorActual(), SwingConstants.LEFT);
        lblTurno.setFont(new Font("Cinzel", Font.BOLD, 24));
        lblTurno.setForeground(ORO_ANTIGUO);

        lblPiezaSeleccionada = new JLabel("Gira la ruleta", SwingConstants.LEFT);
        lblPiezaSeleccionada.setFont(new Font("Arial", Font.BOLD, 16));
        lblPiezaSeleccionada.setForeground(new Color(220, 120, 120));

        lblMensaje = new JLabel("", SwingConstants.LEFT);
        lblMensaje.setFont(new Font("Arial", Font.ITALIC, 14));
        lblMensaje.setForeground(new Color(150, 200, 255));

        panelInfo.add(lblTurno);
        panelInfo.add(lblPiezaSeleccionada);
        panelInfo.add(lblMensaje);

        JPanel panelContadores = new JPanel(new GridLayout(2, 1, 5, 10));
        panelContadores.setOpaque(false);
        panelContadores.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));

        lblPiezasBlanco = new JLabel("", SwingConstants.CENTER);
        lblPiezasBlanco.setFont(new Font("Arial", Font.BOLD, 15));
        lblPiezasBlanco.setForeground(new Color(220, 220, 255));
        lblPiezasBlanco.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 200), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        lblPiezasBlanco.setOpaque(true);
        lblPiezasBlanco.setBackground(new Color(30, 30, 50));

        lblPiezasNegro = new JLabel("", SwingConstants.CENTER);
        lblPiezasNegro.setFont(new Font("Arial", Font.BOLD, 15));
        lblPiezasNegro.setForeground(new Color(255, 180, 180));
        lblPiezasNegro.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SANGRE_BRILLANTE, 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        lblPiezasNegro.setOpaque(true);
        lblPiezasNegro.setBackground(new Color(50, 20, 20));

        panelContadores.add(lblPiezasBlanco);
        panelContadores.add(lblPiezasNegro);

        panel.add(panelInfo, BorderLayout.CENTER);
        panel.add(panelContadores, BorderLayout.EAST);

        return panel;
    }

    private JPanel crearPanelDerecho() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(25, 15, 30));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SANGRE_OSCURA, 4),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        panel.setPreferredSize(new Dimension(380, 0));

        JLabel lblRuleta = new JLabel("RULETA DEL DESTINO", SwingConstants.CENTER);
        lblRuleta.setForeground(ORO_ANTIGUO);
        lblRuleta.setFont(new Font("Cinzel", Font.BOLD, 22));
        lblRuleta.setAlignmentX(Component.CENTER_ALIGNMENT);

        ruletaPanel = new RuletaPanel();
        ruletaPanel.setPreferredSize(new Dimension(260, 260));
        ruletaPanel.setMaximumSize(new Dimension(260, 260));
        ruletaPanel.setMinimumSize(new Dimension(260, 260));
        ruletaPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnGirarRuleta = crearBotonEstilizado("GIRAR RULETA", SANGRE_OSCURA, 20);
        btnGirarRuleta.setMaximumSize(new Dimension(320, 65));
        btnGirarRuleta.addActionListener(e -> girarRuleta());

        btnRetirarse = crearBotonEstilizado("RETIRARSE", new Color(60, 30, 40), 16);
        btnRetirarse.setMaximumSize(new Dimension(320, 55));
        btnRetirarse.addActionListener(e -> retirarse());

        panel.add(lblRuleta);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        panel.add(ruletaPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(btnGirarRuleta);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(btnRetirarse);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JButton crearBotonEstilizado(String texto, Color fondo, int tamañoFuente) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, tamañoFuente));
        btn.setBackground(fondo);
        btn.setForeground(new Color(255, 240, 220));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ORO_ANTIGUO, 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(fondo.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(fondo);
            }
        });

        return btn;
    }

    private class RuletaPanel extends JPanel {
        private String[] piezas = {"VAMPIRO", "LOBO", "MUERTE", "VAMPIRO", "LOBO", "MUERTE"};
        private Color[] colores = {
            new Color(140, 0, 30),    
            new Color(80, 60, 40),    
            new Color(90, 30, 110),    
            new Color(140, 0, 30),     
            new Color(80, 60, 40),     
            new Color(90, 30, 110)     
        };
        private double anguloRotacion = 0;
        private boolean girando = false;
        private Random random = new Random();
        private int indiceSeleccionado = 0;

        public RuletaPanel() {
            setOpaque(false);
        }

        public void girar(Runnable onComplete) {
            if (girando) return;

            girando = true;
            final double[] velocidad = {35.0};
            final int[] frames = {0};
            final int duracionGiro = 55;

            Timer timer = new Timer(16, null);
            timer.addActionListener(e -> {
                anguloRotacion += velocidad[0];
                if (anguloRotacion >= 360) {
                    anguloRotacion -= 360;
                }

                velocidad[0] *= 0.96;
                frames[0]++;

                repaint();

                if (frames[0] >= duracionGiro && velocidad[0] < 1.0) {
                    timer.stop();
                    girando = false;

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
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            int radio = 110;

            java.awt.geom.AffineTransform transformOriginal = g2.getTransform();

            g2.rotate(Math.toRadians(anguloRotacion), centerX, centerY);

            int numSegmentos = piezas.length;
            double anguloSegmento = 360.0 / numSegmentos;

            for (int i = 0; i < numSegmentos; i++) {
                double anguloInicio = i * anguloSegmento;

                g2.setColor(colores[i]);
                g2.fillArc(centerX - radio, centerY - radio, radio * 2, radio * 2,
                        (int) anguloInicio, (int) anguloSegmento);

                g2.setColor(new Color(0, 0, 0, 200));
                g2.setStroke(new BasicStroke(3));
                g2.drawArc(centerX - radio, centerY - radio, radio * 2, radio * 2,
                        (int) anguloInicio, (int) anguloSegmento);
            }

            g2.setTransform(transformOriginal);

            for (int i = 0; i < numSegmentos; i++) {
                double anguloInicio = i * anguloSegmento;
                double anguloMedio = Math.toRadians(anguloInicio + anguloSegmento / 2 + anguloRotacion);
                
                int distanciaTexto = (int) (radio * 0.65);
                int textX = centerX + (int) (Math.cos(anguloMedio) * distanciaTexto);
                int textY = centerY + (int) (Math.sin(anguloMedio) * distanciaTexto);

                String texto = piezas[i];
                g2.setFont(new Font("Arial", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                int anchoTexto = fm.stringWidth(texto);
                int altoTexto = fm.getHeight();

                java.awt.geom.AffineTransform savedTransform = g2.getTransform();
                
                g2.translate(textX, textY);
                g2.rotate(anguloMedio + Math.PI/2);

                g2.setColor(new Color(0, 0, 0, 255));
                g2.drawString(texto, -anchoTexto / 2 + 1, altoTexto / 4 + 1);

                g2.setColor(new Color(255, 250, 230));
                g2.drawString(texto, -anchoTexto / 2, altoTexto / 4);

                g2.setTransform(savedTransform);
            }

            g2.setColor(ORO_ANTIGUO);
            g2.setStroke(new BasicStroke(6));
            g2.drawOval(centerX - radio - 5, centerY - radio - 5, (radio + 5) * 2, (radio + 5) * 2);

            int[] xPoints = {centerX, centerX - 18, centerX + 18};
            int[] yPoints = {centerY - radio - 25, centerY - radio - 5, centerY - radio - 5};
            g2.setColor(ORO_ANTIGUO);
            g2.fillPolygon(xPoints, yPoints, 3);
            g2.setColor(new Color(50, 0, 0));
            g2.setStroke(new BasicStroke(2));
            g2.drawPolygon(xPoints, yPoints, 3);

            g2.setColor(new Color(30, 20, 35));
            g2.fillOval(centerX - 28, centerY - 28, 56, 56);
            g2.setColor(ORO_ANTIGUO);
            g2.setStroke(new BasicStroke(4));
            g2.drawOval(centerX - 28, centerY - 28, 56, 56);
        }

        public String getPiezaSeleccionada() {
            String pieza = piezas[indiceSeleccionado];
            return pieza.equals("LOBO") ? "HOMBRE_LOBO" : pieza;
        }
    }

    private void girarRuleta() {
        if (girosRestantes <= 0) {
            lblMensaje.setText("Sin giros disponibles");
            return;
        }

        try {
            btnGirarRuleta.setEnabled(false);

            ruletaPanel.girar(() -> {
                String seleccionada = ruletaPanel.getPiezaSeleccionada();

                if (tienePiezaTipo(seleccionada)) {
                    piezaPermitida = seleccionada;
                    lblPiezaSeleccionada.setText("Pieza elegida: " + getNombreCompleto(seleccionada));
                    lblMensaje.setText("Selecciona tu " + getNombreCompleto(seleccionada) + " en el tablero");
                    girosRestantes = 0;
                } else {
                    girosRestantes--;
                    if (girosRestantes > 0) {
                        lblMensaje.setText(" No tienes esa pieza. Giros restantes: " + girosRestantes);
                        btnGirarRuleta.setEnabled(true);
                    } else {
                        lblMensaje.setText("Sin piezas disponibles - Pierdes turno");
                        Timer timer = new Timer(2500, ev -> {
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
                "¿Abandonar la batalla?\nTu oponente será declarado vencedor.",
                "Confirmar Retiro",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            String ganador = tableroLogico.getTurnoActual().equals("BLANCO")
                    ? tableroLogico.getJugador2()
                    : tableroLogico.getJugador1();

            String perdedor = tableroLogico.getTurnoActual().equals("BLANCO")
                    ? tableroLogico.getJugador1()
                    : tableroLogico.getJugador2();

            registrarPartidaParaAmbos(ganador, perdedor, true);
            
            tableroLogico.finalizarPartida(ganador, "RETIRO");

            JOptionPane.showMessageDialog(this,
                    "" + ganador + " ha ganado por retiro!\n💎 +3 puntos",
                    "Victoria",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();
            new MenuPrincipal().setVisible(true);
        }
    }

    private void manejarClick(int fila, int col) {
        try {
            if (piezaPermitida == null) {
                lblMensaje.setText("Primero gira la ruleta del destino");
                return;
            }

            Pieza piezaEnCasilla = tableroLogico.getPieza(fila, col);

            if (!esperandoDestino) {
                if (piezaEnCasilla == null) {
                    lblMensaje.setText("Casilla vacía");
                    return;
                }

                if (!piezaEnCasilla.getColor().equals(tableroLogico.getTurnoActual())) {
                    lblMensaje.setText("Esa pieza no es tuya");
                    return;
                }

                if (!piezaEnCasilla.getTipo().equals(piezaPermitida)) {
                    lblMensaje.setText(" Debes usar " + getNombreCompleto(piezaPermitida));
                    return;
                }

                filaSeleccionada = fila;
                colSeleccionada = col;
                esperandoDestino = true;
                lblMensaje.setText("Pieza seleccionada - Elige destino");
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
                            "La Muerte puede realizar acciones especiales:", 
                            "Acción de la Muerte",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null, opciones, opciones[0]);

                    if (opcion == 1) {
                        String resultado = tableroLogico.conjurarZombie(
                                filaSeleccionada, colSeleccionada, filaDestino, colDestino);
                        lblMensaje.setText(resultado);
                        finalizarTurno();
                        return;
                    }
                } else if (origen instanceof Lobo) {
                    int deltaF = Math.abs(filaDestino - filaSeleccionada);
                    int deltaC = Math.abs(colDestino - colSeleccionada);

                    if (deltaF == 2 || deltaC == 2) {
                        String resultado = tableroLogico.loboMover2Casillas(
                                filaSeleccionada, colSeleccionada, filaDestino, colDestino);
                        lblMensaje.setText(resultado);
                        finalizarTurno();
                        return;
                    }
                }

                boolean movido = tableroLogico.moverPieza(
                        filaSeleccionada, colSeleccionada, filaDestino, colDestino);

                if (movido) {
                    lblMensaje.setText("Movimiento realizado");
                    finalizarTurno();
                } else {
                    lblMensaje.setText("Movimiento inválido");
                }
            } else if (!destino.getColor().equals(tableroLogico.getTurnoActual())) {
                String[] opciones = obtenerOpcionesAtaque(origen, filaDestino, colDestino);

                int opcion = JOptionPane.showOptionDialog(this,
                        "Selecciona el tipo de ataque:", "⚔️ Atacar Enemigo",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null, opciones, opciones[0]);

                String resultado = ejecutarAtaque(opcion, origen, filaDestino, colDestino);
                lblMensaje.setText(resultado);
                finalizarTurno();
            } else {
                lblMensaje.setText("No puedes atacar aliados");
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

            registrarPartidaParaAmbos(ganador, perdedor, false);
            
            tableroLogico.finalizarPartida(ganador, "VICTORIA");

            JOptionPane.showMessageDialog(this,
                    "¡" + ganador + " ha ganado esta la noche!\n tiene +3 puntos",
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
        try {
            Cuenta usuario = Cuenta.getUsuarioActual();
            
            Cuenta cuentaGanadora = usuario.buscarCuenta(ganador);
            if (cuentaGanadora != null) {
                cuentaGanadora.registrarPartida(true);
            }

            Cuenta cuentaPerdedora = usuario.buscarCuenta(perdedor);
            if (cuentaPerdedora != null) {
                cuentaPerdedora.registrarPartida(false);
            }
        } catch (Exception e) {
            System.err.println("Error al registrar partida: " + e.getMessage());
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

    private int contarTodasPiezas(String color) {
        int cuenta = 0;
        for (int i = 0; i < TAMANO_TABLERO; i++) {
            for (int j = 0; j < TAMANO_TABLERO; j++) {
                Pieza p = tableroLogico.getPieza(i, j);
                if (p != null && p.getColor().equals(color)) {
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
        lblTurno.setText("Turno de: " + jugador + " (" + color + ")");
        lblPiezaSeleccionada.setText("Gira la ruleta (" + girosRestantes + " giros)");
        lblMensaje.setText("");
        
        int piezasBlanco = contarTodasPiezas("BLANCO");
        int piezasNegro = contarTodasPiezas("NEGRO");
        
        lblPiezasBlanco.setText("-" + tableroLogico.getJugador1() + ": " + piezasBlanco + " Piezas");
        lblPiezasNegro.setText("-" + tableroLogico.getJugador2() + ": " + piezasNegro + " Piezas");
        
        tableroGUI.repaint();
    }

    private String getNombreCompleto(String tipo) {
        switch (tipo) {
            case "HOMBRE_LOBO":
                return "Hombre Lobo";
            case "VAMPIRO":
                return "Vampiro";
            case "MUERTE":
                return "Muerte";
            case "ZOMBIE":
                return "Zombie";
            default:
                return tipo;
        }
    }

    private void mostrarError(String mensaje) {
        System.err.println(" " + mensaje);
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private class TableroPanel extends JPanel {
        private CasillaButton[][] casillas;

        public TableroPanel() {
            setLayout(new GridLayout(TAMANO_TABLERO, TAMANO_TABLERO, 4, 4));
            setBackground(FONDO_PRINCIPAL);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(SANGRE_OSCURA, 6),
                    BorderFactory.createEmptyBorder(18, 18, 18, 18)
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

    private class CasillaButton extends JButton {
        private int fila, col;
        private Color colorBase;
        private ImageIcon imagenActual = null;

        public CasillaButton(int fila, int col) {
            this.fila = fila;
            this.col = col;

            colorBase = ((fila + col) % 2 == 0)
                    ? new Color(55, 50, 60)    
                    : new Color(35, 30, 40);  

            setPreferredSize(new Dimension(110, 110));
            setBackground(colorBase);
            setFont(new Font("Arial", Font.BOLD, 11));
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

            g2.setColor(colorBase);
            g2.fillRect(0, 0, getWidth(), getHeight());

            Color overlay = obtenerColorOverlay();
            if (overlay != null) {
                g2.setColor(overlay);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }

            if (imagenActual != null) {
                int x = (getWidth() - imagenActual.getIconWidth()) / 2;
                int y = (getHeight() - imagenActual.getIconHeight()) / 2;
                imagenActual.paintIcon(this, g2, x, y);
            }

            Pieza pieza = tableroLogico.getPieza(fila, col);
            if (pieza != null) {
                g2.setFont(new Font("Arial", Font.BOLD, 12));
                String stats = "V️" + pieza.getVidas() + " E️" + pieza.getEscudo();
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(stats);

                g2.setColor(new Color(10, 5, 10, 220));
                g2.fillRoundRect((getWidth() - textWidth) / 2 - 5, getHeight() - 24, textWidth + 10, 20, 6, 6);

                Color colorTexto = pieza.getColor().equals("BLANCO")
                        ? new Color(180, 200, 255) : new Color(255, 150, 150);
                g2.setColor(colorTexto);
                g2.drawString(stats, (getWidth() - textWidth) / 2, getHeight() - 9);
            }

            Color bordeColor = obtenerColorBorde();
            int bordeTamaño = obtenerTamañoBorde();
            g2.setColor(bordeColor);
            g2.setStroke(new BasicStroke(bordeTamaño));
            g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        }

        private Color obtenerColorOverlay() {
            if (fila == filaSeleccionada && col == colSeleccionada) {
                return new Color(200, 170, 100, 160);
            }

            if (esperandoDestino && filaSeleccionada != -1) {
                Pieza seleccionada = tableroLogico.getPieza(filaSeleccionada, colSeleccionada);
                Pieza actual = tableroLogico.getPieza(fila, col);

                if (seleccionada instanceof Muerte && actual == null) {
                    return new Color(110, 50, 140, 130);
                }
                else if (actual == null && esMovimientoValido(seleccionada, fila, col)) {
                    return new Color(40, 120, 60, 130);
                }
                else if (actual != null && !actual.getColor().equals(tableroLogico.getTurnoActual())
                        && esAtaqueValido(seleccionada, fila, col)) {
                    return new Color(180, 30, 30, 150);
                }
            }

            return null;
        }

        private Color obtenerColorBorde() {
            if (fila == filaSeleccionada && col == colSeleccionada) {
                return ORO_ANTIGUO;
            }
            return new Color(60, 50, 70);
        }

        private int obtenerTamañoBorde() {
            if (fila == filaSeleccionada && col == colSeleccionada) {
                return 6;
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
                String ruta = "/imagenes/" + tipo + "_" + color + ".png";
                java.net.URL url = getClass().getResource(ruta);
                if (url != null) {
                    imagenActual = new ImageIcon(url);
                } else {
                    imagenActual = null;
                }
            } else {
                imagenActual = null;
            }
            repaint();
        }

        private boolean esMovimientoValido(Pieza pieza, int destinoF, int destinoC) {
            if (pieza == null) return false;

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
            if (pieza == null) return false;

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