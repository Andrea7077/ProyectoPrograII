/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pantallas;
import Clases.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
/**
 *
 * @author andre
 */
public class PReportes extends JFrame{
 private JTabbedPane tabbedPane;
    private JButton btnRegresar;
    private Cuenta usuario;
    private static InterfazGuardado storageGlobal = Tablero.getStorageGlobal(); // ✅ Usar storage compartido

    public PReportes() {
        usuario = Cuenta.getUsuarioActual();
        
        if (usuario == null) {
            JOptionPane.showMessageDialog(null,
                "No hay usuario logueado",
                "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setTitle("Reportes - Vampire Wargame");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(false);

        FondoPanel fondo = new FondoPanel();
        fondo.setLayout(new BorderLayout(15, 15));
        fondo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(fondo);

        JLabel lblTitulo = new JLabel("📊 REPORTES", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Serif", Font.BOLD, 40));
        lblTitulo.setForeground(new Color(255, 70, 70));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        fondo.add(lblTitulo, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabbedPane.setBackground(new Color(30, 30, 45));
        tabbedPane.setForeground(Color.WHITE);

        tabbedPane.addTab("🏆 Ranking", crearPanelRanking());
        tabbedPane.addTab("📜 Mis Partidas", crearPanelLogs());

        fondo.add(tabbedPane, BorderLayout.CENTER);

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.setOpaque(false);

        btnRegresar = new JButton("⬅️ Regresar al Menú");
        btnRegresar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnRegresar.setBackground(new Color(139, 0, 0));
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.setPreferredSize(new Dimension(250, 50));
        btnRegresar.setFocusPainted(false);
        btnRegresar.setBorderPainted(false);
        btnRegresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegresar.addActionListener(e -> {
            dispose();
            new MenuPrincipal().setVisible(true);
        });

        panelBoton.add(btnRegresar);
        fondo.add(panelBoton, BorderLayout.SOUTH);
    }

    private JPanel crearPanelRanking() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(25, 25, 40));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        String[] columnas = {"#", "Usuario", "Puntos", "Jugadas", "Ganadas", "% Victoria"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabla = new JTable(modelo);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tabla.setRowHeight(35);
        tabla.setBackground(new Color(40, 40, 55));
        tabla.setForeground(Color.WHITE);
        tabla.setGridColor(new Color(80, 80, 100));
        tabla.setSelectionBackground(new Color(139, 0, 0));
        tabla.setSelectionForeground(Color.WHITE);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        tabla.getTableHeader().setBackground(new Color(139, 0, 0));
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getTableHeader().setPreferredSize(new Dimension(0, 40));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        tabla.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(200);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(100);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(100);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(120);

        ArrayList<Cuenta> ranking = usuario.obtenerRanking();
        for (int i = 0; i < ranking.size(); i++) {
            Cuenta c = ranking.get(i);
            String posicion = String.valueOf(i + 1);
            if (i == 0) posicion = "🥇";
            else if (i == 1) posicion = "🥈";
            else if (i == 2) posicion = "🥉";

            String username = c.getUsername();
            if (c.getUsername().equals(usuario.getUsername())) {
                username = "► " + username + " ◄";
            }

            modelo.addRow(new Object[]{
                posicion,
                username,
                c.getPuntos(),
                c.getPartidasJugadas(),
                c.getPartidasGanadas(),
                String.format("%.1f%%", c.getPorcentajeVictorias())
            });
        }

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(139, 0, 0), 3));
        panel.add(scroll, BorderLayout.CENTER);

        JPanel panelStats = new JPanel(new GridLayout(1, 3, 15, 0));
        panelStats.setOpaque(false);
        panelStats.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        int totalJugadores = ranking.size();
        int posicionActual = 0;
        for (int i = 0; i < ranking.size(); i++) {
            if (ranking.get(i).getUsername().equals(usuario.getUsername())) {
                posicionActual = i + 1;
                break;
            }
        }

        panelStats.add(crearStatLabel("Total Jugadores", String.valueOf(totalJugadores)));
        panelStats.add(crearStatLabel("Tu Posición", posicionActual + "°"));
        panelStats.add(crearStatLabel("Tus Puntos", String.valueOf(usuario.getPuntos())));

        panel.add(panelStats, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelLogs() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(25, 25, 40));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        String[] columnas = {"#", "Oponente", "Resultado", "Puntos", "Fecha"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabla = new JTable(modelo);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tabla.setRowHeight(35);
        tabla.setBackground(new Color(40, 40, 55));
        tabla.setForeground(Color.WHITE);
        tabla.setGridColor(new Color(80, 80, 100));
        tabla.setSelectionBackground(new Color(139, 0, 0));
        tabla.setSelectionForeground(Color.WHITE);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        tabla.getTableHeader().setBackground(new Color(139, 0, 0));
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getTableHeader().setPreferredSize(new Dimension(0, 40));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        tabla.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(200);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(250);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(100);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(150);

        // ✅ CORREGIDO: Obtener logs del storage global
        String[] logs = storageGlobal.obtenerLogs(usuario.getUsername());
        
        if (logs.length == 0) {
            modelo.addRow(new Object[]{"", "Sin partidas", "Juega tu primera batalla", "", ""});
        } else {
            for (int i = 0; i < logs.length; i++) {
                String log = logs[i];
                
                String oponente = extraerOponente(log);
                ResultadoPartida resultado = extraerResultado(log);
                String fecha = java.time.LocalDate.now().toString();
                
                modelo.addRow(new Object[]{
                    i + 1,
                    oponente,
                    resultado.texto,
                    resultado.puntos,
                    fecha
                });
            }
        }

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(139, 0, 0), 3));
        panel.add(scroll, BorderLayout.CENTER);

        // Stats inferiores
        JPanel panelStats = new JPanel(new GridLayout(1, 4, 15, 0));
        panelStats.setOpaque(false);
        panelStats.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        int totalPartidas = usuario.getPartidasJugadas();
        int victorias = usuario.getPartidasGanadas();
        int derrotas = totalPartidas - victorias;
        int puntosGanados = victorias * 3;

        panelStats.add(crearStatLabel("Total Partidas", String.valueOf(totalPartidas)));
        panelStats.add(crearStatLabel("Victorias", String.valueOf(victorias)));
        panelStats.add(crearStatLabel("Derrotas", String.valueOf(derrotas)));
        panelStats.add(crearStatLabel("Puntos Ganados", "+" + puntosGanados));

        panel.add(panelStats, BorderLayout.SOUTH);

        return panel;
    }

    private class ResultadoPartida {
        String texto;
        String puntos;
        
        ResultadoPartida(String texto, String puntos) {
            this.texto = texto;
            this.puntos = puntos;
        }
    }

    private String extraerOponente(String log) {
        try {
            String usuarioNombre = usuario.getUsername();
            
            // Formato: "ganador VENCIO A perdedor, FELICIDADES HAS GANADO 3 PUNTOS"
            if (log.contains("VENCIO A")) {
                String[] partes = log.split("VENCIO A");
                String ganador = partes[0].trim();
                String perdedor = partes[1].split(",")[0].trim();
                
                // Retornar el que NO es el usuario actual
                if (ganador.equalsIgnoreCase(usuarioNombre)) {
                    return perdedor;
                } else if (perdedor.equalsIgnoreCase(usuarioNombre)) {
                    return ganador;
                } else {
                    // Si ninguno coincide, retornar el primero que no sea el usuario
                    return ganador.equalsIgnoreCase(usuarioNombre) ? perdedor : ganador;
                }
            }
            // Formato: "perdedor SE HA RETIRADO, FELICIDADES ganador, HAS GANADO 3 PUNTOS"
            else if (log.contains("SE HA RETIRADO")) {
                String[] partes = log.split("SE HA RETIRADO");
                String retirado = partes[0].trim();
                
                if (log.contains("FELICIDADES")) {
                    String[] partesGanador = log.split("FELICIDADES");
                    if (partesGanador.length > 1) {
                        String ganador = partesGanador[1].split(",")[0].trim();
                        
                        // Retornar el que NO es el usuario actual
                        if (retirado.equalsIgnoreCase(usuarioNombre)) {
                            return ganador;
                        } else if (ganador.equalsIgnoreCase(usuarioNombre)) {
                            return retirado;
                        } else {
                            return retirado.equalsIgnoreCase(usuarioNombre) ? ganador : retirado;
                        }
                    }
                }
                
                // Si no hay FELICIDADES, el retirado es el oponente si no es el usuario
                return retirado.equalsIgnoreCase(usuarioNombre) ? "Desconocido" : retirado;
            }
        } catch (Exception e) {
            System.err.println("Error al extraer oponente: " + e.getMessage());
            e.printStackTrace();
        }
        return "Desconocido";
    }

    private ResultadoPartida extraerResultado(String log) {
        try {
            String usuarioNombre = usuario.getUsername();
            
            // VICTORIA - Usuario venció a alguien
            if (log.contains(usuarioNombre + " VENCIO A") || 
                log.contains(usuarioNombre + " VENCIO")) {
                return new ResultadoPartida("🏆 VICTORIA", "+3");
            }
            
            // DERROTA - Alguien venció al usuario
            if (log.contains("VENCIO A " + usuarioNombre)) {
                return new ResultadoPartida("💀 DERROTA", "0");
            }
            
            // RETIRO - Usuario se retiró
            if (log.contains(usuarioNombre + " SE HA RETIRADO")) {
                return new ResultadoPartida("🏳️ TE RETIRASTE", "0");
            }
            
            // VICTORIA POR RETIRO - Oponente se retiró
            if (log.contains("SE HA RETIRADO") && 
                log.contains("FELICIDADES " + usuarioNombre)) {
                return new ResultadoPartida("🏆 OPONENTE SE RETIRÓ", "+3");
            }
        } catch (Exception e) {
            System.err.println("Error al extraer resultado: " + e.getMessage());
        }
        
        return new ResultadoPartida("⚔️ Partida jugada", "0");
    }

    private JPanel crearStatLabel(String titulo, String valor) {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setOpaque(false);

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setForeground(new Color(255, 215, 0));

        JLabel lblValor = new JLabel(valor, SwingConstants.CENTER);
        lblValor.setFont(new Font("Arial", Font.BOLD, 22));
        lblValor.setForeground(Color.WHITE);

        panel.add(lblTitulo);
        panel.add(lblValor);

        return panel;
    }

    static class FondoPanel extends JPanel {
        private Image imagen;

        public FondoPanel() {
            try {
                imagen = new ImageIcon(getClass().getResource("/imagenes/sdfgn.jpg")).getImage();
            } catch (Exception e) {
                setBackground(new Color(20, 20, 30));
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (imagen != null) {
                g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
                g.setColor(new Color(0, 0, 0, 120));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }
}