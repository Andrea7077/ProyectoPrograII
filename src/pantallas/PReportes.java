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
    private InterfazGuardado storage;

    public PReportes() {
        usuario = Cuenta.getUsuarioActual();
        storage = new Guardado();
        
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
        setSize(900, 650);
        setLocationRelativeTo(null);
        setResizable(false);

        FondoPanel fondo = new FondoPanel();
        fondo.setLayout(new BorderLayout(15, 15));
        fondo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(fondo);

        // Título
        JLabel lblTitulo = new JLabel("📊 REPORTES", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Serif", Font.BOLD, 38));
        lblTitulo.setForeground(new Color(255, 50, 50));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        fondo.add(lblTitulo, BorderLayout.NORTH);

        // Tabs
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tabbedPane.setBackground(new Color(30, 30, 45));
        tabbedPane.setForeground(Color.WHITE);

        tabbedPane.addTab("🏆 Ranking de Jugadores", crearPanelRanking());
        tabbedPane.addTab("📜 Mis Últimos Partidos", crearPanelLogs());

        fondo.add(tabbedPane, BorderLayout.CENTER);

        // Botón Regresar
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

        // Descripción
        JLabel lblDescripcion = new JLabel(
            "<html><center>🏆 Ranking de todos los jugadores del sistema<br>" +
            "Ordenados por puntos de mayor a menor</center></html>",
            SwingConstants.CENTER
        );
        lblDescripcion.setFont(new Font("Arial", Font.PLAIN, 14));
        lblDescripcion.setForeground(new Color(200, 200, 200));
        lblDescripcion.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        panel.add(lblDescripcion, BorderLayout.NORTH);

        // Tabla
        String[] columnas = {"Posición", "👤 Usuario", "⭐ Puntos", "🎮 Jugadas", "🏆 Ganadas", "📊 Victorias"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabla = new JTable(modelo);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabla.setRowHeight(30);
        tabla.setBackground(new Color(40, 40, 55));
        tabla.setForeground(Color.WHITE);
        tabla.setGridColor(new Color(80, 80, 100));
        tabla.setSelectionBackground(new Color(139, 0, 0));
        tabla.setSelectionForeground(Color.WHITE);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabla.getTableHeader().setBackground(new Color(139, 0, 0));
        tabla.getTableHeader().setForeground(Color.WHITE);

        // Centrar contenido
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Llenar datos
        ArrayList<Cuenta> ranking = usuario.obtenerRanking();
        for (int i = 0; i < ranking.size(); i++) {
            Cuenta c = ranking.get(i);
            String posicion = (i + 1) + "°";
            if (i == 0) posicion = "🥇";
            else if (i == 1) posicion = "🥈";
            else if (i == 2) posicion = "🥉";

            String username = c.getUsername();
            if (c.getUsername().equals(usuario.getUsername())) {
                username = "➤ " + username + " (TÚ)";
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
        scroll.setBorder(BorderFactory.createLineBorder(new Color(139, 0, 0), 2));
        panel.add(scroll, BorderLayout.CENTER);

        // Estadísticas totales
        JPanel panelStats = new JPanel(new GridLayout(1, 3, 15, 0));
        panelStats.setOpaque(false);
        panelStats.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        int totalJugadores = ranking.size();
        int posicionActual = 0;
        for (int i = 0; i < ranking.size(); i++) {
            if (ranking.get(i).getUsername().equals(usuario.getUsername())) {
                posicionActual = i + 1;
                break;
            }
        }

        panelStats.add(crearStatLabel("👥 Total Jugadores", String.valueOf(totalJugadores)));
        panelStats.add(crearStatLabel("📍 Tu Posición", posicionActual + "°"));
        panelStats.add(crearStatLabel("⭐ Tus Puntos", String.valueOf(usuario.getPuntos())));

        panel.add(panelStats, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelLogs() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(25, 25, 40));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Descripción
        JLabel lblDescripcion = new JLabel(
            "<html><center>📜 Historial de tus partidas<br>" +
            "Del más reciente al más antiguo</center></html>",
            SwingConstants.CENTER
        );
        lblDescripcion.setFont(new Font("Arial", Font.PLAIN, 14));
        lblDescripcion.setForeground(new Color(200, 200, 200));
        lblDescripcion.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        panel.add(lblDescripcion, BorderLayout.NORTH);

        // Área de texto para logs
        JTextArea areaLogs = new JTextArea();
        areaLogs.setEditable(false);
        areaLogs.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaLogs.setBackground(new Color(20, 20, 30));
        areaLogs.setForeground(new Color(220, 220, 220));
        areaLogs.setLineWrap(true);
        areaLogs.setWrapStyleWord(true);
        areaLogs.setMargin(new Insets(10, 10, 10, 10));

        // Obtener logs del usuario
        String[] logs = storage.obtenerLogs(usuario.getUsername());
        
        if (logs.length == 0) {
            areaLogs.setText("📭 Aún no has jugado ninguna partida.\n\n" +
                           "¡Ve al menú principal y comienza tu primera batalla!");
            areaLogs.setForeground(new Color(150, 150, 150));
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("═══════════════════════════════════════════════════════════\n");
            sb.append("               📜 HISTORIAL DE PARTIDAS                    \n");
            sb.append("═══════════════════════════════════════════════════════════\n\n");
            
            for (int i = 0; i < logs.length; i++) {
                sb.append("🎮 PARTIDA #").append(i + 1).append("\n");
                sb.append("───────────────────────────────────────────────────────\n");
                sb.append(formatearLog(logs[i])).append("\n\n");
            }
            
            areaLogs.setText(sb.toString());
        }

        JScrollPane scroll = new JScrollPane(areaLogs);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(139, 0, 0), 2));
        panel.add(scroll, BorderLayout.CENTER);

        // Estadísticas de logs
        JPanel panelStats = new JPanel(new GridLayout(1, 3, 15, 0));
        panelStats.setOpaque(false);
        panelStats.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        int totalPartidas = logs.length;
        int victorias = 0;
        int derrotas = 0;

        for (String log : logs) {
            if (log.contains(usuario.getUsername()) && 
                (log.contains("VENCIO") || log.contains("ganado"))) {
                victorias++;
            } else if (log.contains(usuario.getUsername())) {
                derrotas++;
            }
        }

        panelStats.add(crearStatLabel("🎮 Total Partidas", String.valueOf(totalPartidas)));
        panelStats.add(crearStatLabel("🏆 Victorias", String.valueOf(victorias)));
        panelStats.add(crearStatLabel("💀 Derrotas", String.valueOf(derrotas)));

        panel.add(panelStats, BorderLayout.SOUTH);

        return panel;
    }

    private String formatearLog(String log) {
        // Agregar íconos y formato al log
        if (log.contains("VENCIO")) {
            log = "⚔️ " + log;
        } else if (log.contains("RETIRADO")) {
            log = "🏳️ " + log;
        }
        
        log = log.replace("FELICIDADES", "🎉 FELICIDADES");
        log = log.replace("ganado", "✅ ganado");
        log = log.replace("puntos", "⭐ puntos");
        
        return log;
    }

    private JPanel crearStatLabel(String titulo, String valor) {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setOpaque(false);

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 13));
        lblTitulo.setForeground(new Color(255, 215, 0));

        JLabel lblValor = new JLabel(valor, SwingConstants.CENTER);
        lblValor.setFont(new Font("Arial", Font.BOLD, 20));
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
                // Oscurecer la imagen para mejor legibilidad
                g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
                g.setColor(new Color(0, 0, 0, 100));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }
}