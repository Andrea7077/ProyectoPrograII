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
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(false);

        FondoPanel fondo = new FondoPanel();
        fondo.setLayout(new BorderLayout(15, 15));
        fondo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(fondo);

        // Título
        JLabel lblTitulo = new JLabel("📊 REPORTES", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Serif", Font.BOLD, 40));
        lblTitulo.setForeground(new Color(255, 70, 70));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        fondo.add(lblTitulo, BorderLayout.NORTH);

        // Tabs
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabbedPane.setBackground(new Color(30, 30, 45));
        tabbedPane.setForeground(Color.WHITE);

        tabbedPane.addTab("🏆 Ranking", crearPanelRanking());
        tabbedPane.addTab("📜 Mis Partidas", crearPanelLogs());

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

        // Tabla con diseño mejorado
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

        // Centrar todas las columnas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Ajustar anchos de columnas
        tabla.getColumnModel().getColumn(0).setPreferredWidth(50);   // #
        tabla.getColumnModel().getColumn(1).setPreferredWidth(200);  // Usuario
        tabla.getColumnModel().getColumn(2).setPreferredWidth(100);  // Puntos
        tabla.getColumnModel().getColumn(3).setPreferredWidth(100);  // Jugadas
        tabla.getColumnModel().getColumn(4).setPreferredWidth(100);  // Ganadas
        tabla.getColumnModel().getColumn(5).setPreferredWidth(120);  // % Victoria

        // Llenar datos
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

        // Stats inferiores
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

        // Tabla de partidas
        String[] columnas = {"#", "Oponente", "Resultado", "Fecha"};
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

        // Centrar columnas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Ajustar anchos
        tabla.getColumnModel().getColumn(0).setPreferredWidth(50);   // #
        tabla.getColumnModel().getColumn(1).setPreferredWidth(200);  // Oponente
        tabla.getColumnModel().getColumn(2).setPreferredWidth(300);  // Resultado
        tabla.getColumnModel().getColumn(3).setPreferredWidth(150);  // Fecha

        // Obtener logs
        String[] logs = storage.obtenerLogs(usuario.getUsername());
        
        if (logs.length == 0) {
            modelo.addRow(new Object[]{"", "No hay partidas", "Juega tu primera batalla", ""});
        } else {
            for (int i = 0; i < logs.length; i++) {
                String log = logs[i];
                String oponente = extraerOponente(log);
                String resultado = extraerResultado(log);
                String fecha = java.time.LocalDate.now().toString(); // Podrías mejorarlo guardando fecha real
                
                modelo.addRow(new Object[]{
                    i + 1,
                    oponente,
                    resultado,
                    fecha
                });
            }
        }

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(139, 0, 0), 3));
        panel.add(scroll, BorderLayout.CENTER);

        // Stats inferiores
        JPanel panelStats = new JPanel(new GridLayout(1, 3, 15, 0));
        panelStats.setOpaque(false);
        panelStats.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

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

        panelStats.add(crearStatLabel("Total Partidas", String.valueOf(totalPartidas)));
        panelStats.add(crearStatLabel("Victorias", String.valueOf(victorias)));
        panelStats.add(crearStatLabel("Derrotas", String.valueOf(derrotas)));

        panel.add(panelStats, BorderLayout.SOUTH);

        return panel;
    }

    private String extraerOponente(String log) {
        // Extraer el nombre del oponente del log
        if (log.contains("VENCIO A")) {
            String[] partes = log.split("VENCIO A");
            if (partes.length > 1) {
                String oponente = partes[1].split(",")[0].trim();
                return oponente.equals(usuario.getUsername()) ? partes[0].trim() : oponente;
            }
        } else if (log.contains("SE HA RETIRADO")) {
            String[] partes = log.split("SE HA RETIRADO");
            if (partes.length > 0) {
                String retirado = partes[0].trim();
                if (log.contains("FELICIDADES")) {
                    String[] partesGanador = log.split("FELICIDADES");
                    if (partesGanador.length > 1) {
                        String ganador = partesGanador[1].split(",")[0].trim();
                        return retirado.equals(usuario.getUsername()) ? ganador : retirado;
                    }
                }
            }
        }
        return "Desconocido";
    }

    private String extraerResultado(String log) {
        if (log.contains(usuario.getUsername() + " VENCIO")) {
            return "🏆 VICTORIA";
        } else if (log.contains("VENCIO A " + usuario.getUsername())) {
            return "💀 DERROTA";
        } else if (log.contains(usuario.getUsername() + " SE HA RETIRADO")) {
            return "🏳️ TE RETIRASTE";
        } else if (log.contains("SE HA RETIRADO") && log.contains("FELICIDADES " + usuario.getUsername())) {
            return "🏆 OPONENTE SE RETIRÓ";
        }
        return "⚔️ Partida jugada";
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