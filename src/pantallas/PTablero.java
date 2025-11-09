/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pantallas;

import Clases.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.Timer;
import java.util.HashMap;
import javax.swing.border.Border;
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
    
    // Cache de imágenes para mejor performance
    private HashMap<String, ImageIcon> cacheImagenes = new HashMap<>();
    
    // Variables de control del juego
    private String piezaPermitida = null;
    private int filaSeleccionada = -1;
    private int colSeleccionada = -1;
    private boolean esperandoDestino = false;

    public PTablero() {
        super("🧛 Vampire Wargame");
        
        Cuenta usuario = Cuenta.getUsuarioActual();
        String jugador1 = (usuario != null) ? usuario.getUsername() : "Jugador1";
        String jugador2 = "Jugador2";
        
        tableroLogico = new Tablero(jugador1, jugador2);
        
        // Pre-cargar imágenes
        precargarImagenes();
        
        inicializarComponentes();
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setResizable(false);
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
                        Image img = icono.getImage().getScaledInstance(65, 65, Image.SCALE_SMOOTH);
                        cacheImagenes.put(key, new ImageIcon(img));
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ No se encontró imagen: " + ruta);
                }
            }
        }
        
        if (cacheImagenes.isEmpty()) {
            System.out.println("ℹ️ Usando emojis (no se encontraron imágenes)");
        } else {
            System.out.println("✅ Cargadas " + cacheImagenes.size() + " imágenes");
        }
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(20, 20, 30));

        add(crearPanelSuperior(), BorderLayout.NORTH);
        
        tableroGUI = new TableroPanel();
        add(tableroGUI, BorderLayout.CENTER);
        
        add(crearPanelDerecho(), BorderLayout.EAST);

        actualizarInterfaz();
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
        panel.setBackground(new Color(40, 40, 50));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lblTurno = new JLabel("Turno: " + tableroLogico.getTurnoJugadorActual(), SwingConstants.CENTER);
        lblTurno.setFont(new Font("Arial", Font.BOLD, 20));
        lblTurno.setForeground(Color.WHITE);

        lblPiezaSeleccionada = new JLabel("🎰 Gira la ruleta para comenzar", SwingConstants.CENTER);
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
        panel.setPreferredSize(new Dimension(300, 0));

        // Título Ruleta
        JLabel lblRuleta = new JLabel("🎰 RULETA MÁGICA", SwingConstants.CENTER);
        lblRuleta.setForeground(new Color(255, 215, 0));
        lblRuleta.setFont(new Font("Arial", Font.BOLD, 18));
        lblRuleta.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Panel de Ruleta
        ruletaPanel = new RuletaPanel();
        ruletaPanel.setMaximumSize(new Dimension(250, 250));
        ruletaPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Botón Girar
        btnGirarRuleta = new JButton("⚡ GIRAR RULETA");
        btnGirarRuleta.setFont(new Font("Arial", Font.BOLD, 15));
        btnGirarRuleta.setBackground(new Color(150, 0, 0));
        btnGirarRuleta.setForeground(Color.WHITE);
        btnGirarRuleta.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnGirarRuleta.setMaximumSize(new Dimension(230, 50));
        btnGirarRuleta.setFocusPainted(false);
        btnGirarRuleta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGirarRuleta.addActionListener(e -> girarRuleta());

        // Botón Retirarse
        btnRetirarse = new JButton("🏳️ RETIRARSE");
        btnRetirarse.setFont(new Font("Arial", Font.BOLD, 13));
        btnRetirarse.setBackground(new Color(100, 0, 0));
        btnRetirarse.setForeground(Color.WHITE);
        btnRetirarse.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRetirarse.setMaximumSize(new Dimension(230, 40));
        btnRetirarse.setFocusPainted(false);
        btnRetirarse.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRetirarse.addActionListener(e -> retirarse());

        // Separador
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(260, 2));
        sep.setForeground(Color.GRAY);

        // Log
        JLabel lblLog = new JLabel("📜 Historial de Juego", SwingConstants.CENTER);
        lblLog.setForeground(Color.WHITE);
        lblLog.setFont(new Font("Arial", Font.BOLD, 14));
        lblLog.setAlignmentX(Component.CENTER_ALIGNMENT);

        areaLog = new JTextArea(12, 20);
        areaLog.setEditable(false);
        areaLog.setBackground(new Color(20, 20, 30));
        areaLog.setForeground(new Color(200, 200, 200));
        areaLog.setFont(new Font("Monospaced", Font.PLAIN, 10));
        areaLog.setLineWrap(true);
        areaLog.setWrapStyleWord(true);
        JScrollPane scrollLog = new JScrollPane(areaLog);
        scrollLog.setMaximumSize(new Dimension(270, 200));

        // Agregar todo
        panel.add(lblRuleta);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(ruletaPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(btnGirarRuleta);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnRetirarse);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(sep);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(lblLog);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(scrollLog);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    // ========================================
    // RULETA ANIMADA CIRCULAR
    // ========================================
    private class RuletaPanel extends JPanel {
        private double angulo = 0;
        private Timer timer;
        private boolean girando = false;
        private int velocidad = 25;
        private String[] piezas = {"VAMPIRO", "LOBO", "MUERTE"};
        private Color[] colores = {
            new Color(200, 0, 0),     // Rojo vampiro
            new Color(150, 100, 0),   // Marrón lobo
            new Color(120, 0, 180)    // Púrpura muerte
        };

        public RuletaPanel() {
            setPreferredSize(new Dimension(250, 250));
            setOpaque(false);
            
            timer = new Timer(25, e -> {
                angulo += velocidad;
                if (angulo >= 360) angulo -= 360;
                repaint();
            });
        }

        public void girar(Runnable onComplete) {
            if (girando) return;
            
            girando = true;
            velocidad = 25;
            timer.start();

            // Desacelerar gradualmente
            Timer desaceleracion = new Timer(80, null);
            desaceleracion.addActionListener(e -> {
                velocidad = Math.max(1, velocidad - 1);
                if (velocidad <= 1) {
                    desaceleracion.stop();
                    
                    Timer detener = new Timer(2000, ev -> {
                        timer.stop();
                        girando = false;
                        onComplete.run();
                    });
                    detener.setRepeats(false);
                    detener.start();
                }
            });
            desaceleracion.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            int radius = Math.min(getWidth(), getHeight()) / 2 - 25;

            // Sombra
            g2.setColor(new Color(0, 0, 0, 80));
            g2.fillOval(centerX - radius + 5, centerY - radius + 5, radius * 2, radius * 2);

            // Fondo oscuro
            g2.setColor(new Color(30, 30, 30));
            g2.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);

            // Secciones coloreadas
            double seccionAngulo = 360.0 / piezas.length;
            for (int i = 0; i < piezas.length; i++) {
                g2.setColor(colores[i]);
                double startAngle = angulo + (i * seccionAngulo);
                g2.fill(new Arc2D.Double(
                    centerX - radius, centerY - radius,
                    radius * 2, radius * 2,
                    startAngle, seccionAngulo,
                    Arc2D.PIE
                ));
                
                // Borde de sección
                g2.setColor(new Color(0, 0, 0, 100));
                g2.setStroke(new BasicStroke(2));
                g2.draw(new Arc2D.Double(
                    centerX - radius, centerY - radius,
                    radius * 2, radius * 2,
                    startAngle, seccionAngulo,
                    Arc2D.PIE
                ));
            }

            // Nombres con sombra
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            for (int i = 0; i < piezas.length; i++) {
                double angleRad = Math.toRadians(angulo + (i * seccionAngulo) + (seccionAngulo / 2));
                int textX = centerX + (int) (radius * 0.6 * Math.cos(angleRad));
                int textY = centerY + (int) (radius * 0.6 * Math.sin(angleRad));
                
                String texto = piezas[i];
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(texto);
                
                // Sombra del texto
                g2.setColor(Color.BLACK);
                g2.drawString(texto, textX - textWidth / 2 + 2, textY + fm.getHeight() / 4 + 2);
                
                // Texto
                g2.setColor(Color.WHITE);
                g2.drawString(texto, textX - textWidth / 2, textY + fm.getHeight() / 4);
            }

            // Puntero (flecha amarilla)
            g2.setColor(new Color(255, 215, 0));
            int[] xPoints = {centerX, centerX - 15, centerX + 15};
            int[] yPoints = {centerY - radius - 15, centerY - radius, centerY - radius};
            g2.fillPolygon(xPoints, yPoints, 3);
            
            // Borde del puntero
            g2.setColor(new Color(200, 150, 0));
            g2.setStroke(new BasicStroke(2));
            g2.drawPolygon(xPoints, yPoints, 3);

            // Borde exterior dorado
            g2.setColor(new Color(255, 215, 0));
            g2.setStroke(new BasicStroke(4));
            g2.drawOval(centerX - radius, centerY - radius, radius * 2, radius * 2);

            // Centro decorativo
            g2.setColor(new Color(255, 215, 0));
            g2.fillOval(centerX - 15, centerY - 15, 30, 30);
            g2.setColor(new Color(30, 30, 30));
            g2.fillOval(centerX - 10, centerY - 10, 20, 20);
        }

        public String getPiezaSeleccionada() {
            double normalizado = angulo % 360;
            double seccionAngulo = 360.0 / piezas.length;
            int indice = (int) ((360 - normalizado) / seccionAngulo) % piezas.length;
            return piezas[indice];
        }
    }

    private void girarRuleta() {
        try {
            btnGirarRuleta.setEnabled(false);
            agregarLog("🎲 Girando ruleta...");
            
            ruletaPanel.girar(() -> {
                String seleccionada = ruletaPanel.getPiezaSeleccionada();
                
                String tipoPieza = null;
                switch (seleccionada) {
                    case "VAMPIRO": tipoPieza = "VAMPIRO"; break;
                    case "LOBO": tipoPieza = "HOMBRE_LOBO"; break;
                    case "MUERTE": tipoPieza = "MUERTE"; break;
                }
                
                if (tienePiezaTipo(tipoPieza)) {
                    piezaPermitida = tipoPieza;
                    lblPiezaSeleccionada.setText("✨ Seleccionado: " + getNombreCompleto(tipoPieza));
                    lblMensaje.setText("👆 Selecciona tu " + getNombreCompleto(tipoPieza));
                    agregarLog("✅ " + getNombreCompleto(tipoPieza) + " disponible");
                } else {
                    lblMensaje.setText("❌ No tienes esa pieza - Pierdes turno");
                    agregarLog("❌ Sin " + getNombreCompleto(tipoPieza) + " - Turno perdido");
                    Timer timer = new Timer(2500, e -> {
                        btnGirarRuleta.setEnabled(true);
                        cambiarTurno();
                    });
                    timer.setRepeats(false);
                    timer.start();
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
            
            tableroLogico.finalizarPartida(ganador, "RETIRO");
            
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
                // Seleccionar pieza
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
                // Realizar acción
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
                // Movimiento
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
                // Ataque
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
            tableroLogico.finalizarPartida(ganador, "VICTORIA");
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

    private void cambiarTurno() {
        tableroLogico.cambiarTurno();
        actualizarInterfaz();
    }

    private void actualizarInterfaz() {
        String jugador = tableroLogico.getTurnoJugadorActual();
        String color = tableroLogico.getTurnoActual();
        lblTurno.setText("🎮 Turno: " + jugador + " (" + color + ")");
        lblPiezaSeleccionada.setText("🎰 Gira la ruleta");
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
            case "HOMBRE_LOBO": return "Hombre Lobo 🐺";
            case "VAMPIRO": return "Vampiro 🧛";
            case "MUERTE": return "Muerte 💀";
            case "ZOMBIE": return "Zombie 🧟";
            default: return tipo;
        }
    }

    private void mostrarError(String mensaje) {
        System.err.println("❌ " + mensaje);
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // ========================================
    // TABLERO CON IMÁGENES Y HIGHLIGHTS
    // ========================================
    private class TableroPanel extends JPanel {
        private CasillaButton[][] casillas;

        public TableroPanel() {
            setLayout(new GridLayout(TAMANO_TABLERO, TAMANO_TABLERO, 3, 3));
            setBackground(new Color(40, 40, 50));
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 215, 0), 4),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
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
    // CASILLA CON IMÁGENES Y HIGHLIGHTS
    // ========================================
    private class CasillaButton extends JButton {
        private int fila, col;
        private Color colorBase;

        public CasillaButton(int fila, int col) {
            this.fila = fila;
            this.col = col;
            
            colorBase = ((fila + col) % 2 == 0) ? 
                new Color(240, 217, 181) : new Color(181, 136, 99);
            
            setPreferredSize(new Dimension(90, 90));
            setBackground(colorBase);
            setFont(new Font("Arial", Font.BOLD, 11));
            setFocusPainted(false);
            setBorderPainted(true);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setHorizontalTextPosition(SwingConstants.CENTER);
            setVerticalTextPosition(SwingConstants.BOTTOM);
            
            addActionListener(e -> manejarClick(fila, col));
        }

        public void actualizar() {
            Pieza pieza = tableroLogico.getPieza(fila, col);
            
            // Restaurar color base
            Color colorActual = colorBase;
            Border bordeActual = BorderFactory.createLineBorder(Color.DARK_GRAY, 1);
            
            // HIGHLIGHT: Pieza seleccionada
            if (fila == filaSeleccionada && col == colSeleccionada) {
                colorActual = new Color(255, 255, 0, 180); // Amarillo brillante
                bordeActual = BorderFactory.createLineBorder(Color.YELLOW, 4);
            } 
            // HIGHLIGHT: Mostrar casillas válidas
            else if (esperandoDestino && filaSeleccionada != -1) {
                Pieza seleccionada = tableroLogico.getPieza(filaSeleccionada, colSeleccionada);
                
                if (pieza == null && esMovimientoValido(seleccionada, fila, col)) {
                    // 🟢 VERDE: Casilla de movimiento válido
                    colorActual = new Color(0, 255, 0, 120);
                    bordeActual = BorderFactory.createLineBorder(new Color(0, 200, 0), 3);
                } 
                else if (pieza != null && !pieza.getColor().equals(tableroLogico.getTurnoActual()) &&
                           esAtaqueValido(seleccionada, fila, col)) {
                    // 🔴 ROJO: Casilla de ataque válido
                    colorActual = new Color(255, 0, 0, 120);
                    bordeActual = BorderFactory.createLineBorder(new Color(200, 0, 0), 3);
                }
            }
            
            setBackground(colorActual);
            setBorder(bordeActual);
            
            // Mostrar pieza (imagen o emoji)
            if (pieza != null) {
                String key = pieza.getTipo().toLowerCase() + "_" + pieza.getColor().toLowerCase();
                ImageIcon icono = cacheImagenes.get(key);
                
                if (icono != null) {
                    // Usar imagen
                    setIcon(icono);
                    setText("");
                } else {
                    // Fallback a emoji
                    setIcon(null);
                    String emoji = getEmojiPieza(pieza);
                    setText("<html><center><font size='7'>" + emoji + "</font></center></html>");
                }
                
                // Color del texto según equipo
                setForeground(pieza.getColor().equals("BLANCO") ? 
                    new Color(200, 0, 0) : new Color(0, 0, 200));
            } else {
                setIcon(null);
                setText("");
            }
        }

        private boolean esMovimientoValido(Pieza pieza, int destinoF, int destinoC) {
            if (pieza == null) return false;
            
            int deltaF = Math.abs(destinoF - filaSeleccionada);
            int deltaC = Math.abs(destinoC - colSeleccionada);
            
            // Movimiento básico (1 casilla adyacente)
            if (deltaF <= 1 && deltaC <= 1 && !(deltaF == 0 && deltaC == 0)) {
                return true;
            }
            
            // Lobo puede moverse 2 casillas
            if (pieza instanceof Lobo) {
                if ((deltaF == 2 && deltaC == 0) || 
                    (deltaF == 0 && deltaC == 2) || 
                    (deltaF == 2 && deltaC == 2)) {
                    // Verificar que el camino esté libre
                    return verificarCaminoLibre(filaSeleccionada, colSeleccionada, destinoF, destinoC);
                }
            }
            
            return false;
        }

        private boolean esAtaqueValido(Pieza pieza, int destinoF, int destinoC) {
            if (pieza == null) return false;
            
            int deltaF = Math.abs(destinoF - filaSeleccionada);
            int deltaC = Math.abs(destinoC - colSeleccionada);
            
            // Ataque normal (adyacente)
            if (deltaF <= 1 && deltaC <= 1 && !(deltaF == 0 && deltaC == 0)) {
                return true;
            }
            
            // Muerte puede lanzar lanza a 2 casillas
            if (pieza instanceof Muerte) {
                if ((deltaF == 2 && deltaC == 0) || 
                    (deltaF == 0 && deltaC == 2) || 
                    (deltaF == 2 && deltaC == 2)) {
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