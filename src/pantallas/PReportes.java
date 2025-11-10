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
 private static final Color FONDO_PRINCIPAL = new Color(18, 8, 15);
    private static final Color SANGRE_OSCURA = new Color(120, 0, 20);
    private static final Color SANGRE_BRILLANTE = new Color(180, 20, 40);
    private static final Color MORADO_OSCURO = new Color(60, 20, 70);
    private static final Color GRIS_PIEDRA = new Color(40, 35, 45);
    private static final Color ORO_ANTIGUO = new Color(200, 170, 100);
    private static final Color VERDE_VICTORIOSO = new Color(40, 150, 80);
    
    private JTabbedPane tabbedPane;
    private JButton btnRegresar;
    private Cuenta usuario;
    private static InterfazGuardado storageGlobal = Tablero.getStorageGlobal();

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
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setResizable(false);

        FondoPanel fondo = new FondoPanel();
        fondo.setLayout(new BorderLayout(15, 15));
        fondo.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        setContentPane(fondo);

        JLabel lblTitulo = new JLabel("REPORTES DE BATALLA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Cinzel", Font.BOLD, 42));
        lblTitulo.setForeground(ORO_ANTIGUO);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        fondo.add(lblTitulo, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 17));
        tabbedPane.setBackground(GRIS_PIEDRA);
        tabbedPane.setForeground(new Color(255, 240, 220));
        tabbedPane.setBorder(BorderFactory.createLineBorder(SANGRE_OSCURA, 3));

        tabbedPane.addTab("RANKING", crearPanelRanking());
        tabbedPane.addTab("MIS BATALLAS", crearPanelLogs());

        fondo.add(tabbedPane, BorderLayout.CENTER);

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.setOpaque(false);

        btnRegresar = new JButton("Regresar al Menu");
        btnRegresar.setFont(new Font("Arial", Font.BOLD, 17));
        btnRegresar.setBackground(SANGRE_OSCURA);
        btnRegresar.setForeground(new Color(255, 240, 220));
        btnRegresar.setPreferredSize(new Dimension(280, 55));
        btnRegresar.setFocusPainted(false);
        btnRegresar.setBorderPainted(false);
        btnRegresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegresar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ORO_ANTIGUO, 2),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        btnRegresar.addActionListener(e -> {
            dispose();
            new MenuPrincipal().setVisible(true);
        });

        panelBoton.add(btnRegresar);
        fondo.add(panelBoton, BorderLayout.SOUTH);
    }

    private JPanel crearPanelRanking() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(new Color(25, 20, 30));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columnas = {"", "Jugador", "Puntos", "Batallas", "Victorias", "% Victoria"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabla = new JTable(modelo);
        tabla.setFont(new Font("Arial", Font.PLAIN, 16));
        tabla.setRowHeight(42);
        tabla.setBackground(new Color(35, 30, 40));
        tabla.setForeground(new Color(255, 245, 230));
        tabla.setGridColor(new Color(80, 60, 90));
        tabla.setSelectionBackground(SANGRE_OSCURA);
        tabla.setSelectionForeground(Color.WHITE);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        tabla.getTableHeader().setBackground(MORADO_OSCURO);
        tabla.getTableHeader().setForeground(ORO_ANTIGUO);
        tabla.getTableHeader().setPreferredSize(new Dimension(0, 45));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        centerRenderer.setBackground(new Color(35, 30, 40));
        centerRenderer.setForeground(new Color(255, 245, 230));
        
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        tabla.getColumnModel().getColumn(0).setPreferredWidth(70);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(220);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(110);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(110);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(130);

        ArrayList<Cuenta> ranking = usuario.obtenerRanking();
        for (int i = 0; i < ranking.size(); i++) {
            Cuenta c = ranking.get(i);
            String posicion = String.valueOf(i + 1);
            if (i == 0) posicion = "1";
            else if (i == 1) posicion = "2";
            else if (i == 2) posicion = "3";

            String username = c.getUsername();
            if (c.getUsername().equals(usuario.getUsername())) {
                username = "" + username + "️";
            }

            modelo.addRow(new Object[]{
                posicion,
                username,
                c.getPuntos() + " pts",
                c.getPartidasJugadas(),
                c.getPartidasGanadas(),
                String.format("%.1f%%", c.getPorcentajeVictorias())
            });
        }

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(SANGRE_OSCURA, 4));
        scroll.getViewport().setBackground(new Color(35, 30, 40));
        panel.add(scroll, BorderLayout.CENTER);

        JPanel panelStats = new JPanel(new GridLayout(1, 3, 20, 0));
        panelStats.setOpaque(false);
        panelStats.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        int totalJugadores = ranking.size();
        int posicionActual = 0;
        for (int i = 0; i < ranking.size(); i++) {
            if (ranking.get(i).getUsername().equals(usuario.getUsername())) {
                posicionActual = i + 1;
                break;
            }
        }

        panelStats.add(crearStatLabel("Total Guerreros", String.valueOf(totalJugadores)));
        panelStats.add(crearStatLabel("Tu Posicion", posicionActual + "°"));
        panelStats.add(crearStatLabel("Tus Puntos", String.valueOf(usuario.getPuntos())));

        panel.add(panelStats, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelLogs() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(new Color(25, 20, 30));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columnas = {"#", "Adversario", "Resultado", "Puntos", "Fecha"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabla = new JTable(modelo);
        tabla.setFont(new Font("Arial", Font.PLAIN, 16));
        tabla.setRowHeight(42);
        tabla.setBackground(new Color(35, 30, 40));
        tabla.setForeground(new Color(255, 245, 230));
        tabla.setGridColor(new Color(80, 60, 90));
        tabla.setSelectionBackground(SANGRE_OSCURA);
        tabla.setSelectionForeground(Color.WHITE);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        tabla.getTableHeader().setBackground(MORADO_OSCURO);
        tabla.getTableHeader().setForeground(ORO_ANTIGUO);
        tabla.getTableHeader().setPreferredSize(new Dimension(0, 45));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        centerRenderer.setBackground(new Color(35, 30, 40));
        centerRenderer.setForeground(new Color(255, 245, 230));
        
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        tabla.getColumnModel().getColumn(0).setPreferredWidth(60);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(200);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(280);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(100);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(160);

        String[] logs = storageGlobal.obtenerLogs(usuario.getUsername());
        
        if (logs.length == 0) {
            modelo.addRow(new Object[]{"", "Sin registros", "️¡Juega tu primera batalla!", "", ""});
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
        scroll.setBorder(BorderFactory.createLineBorder(SANGRE_OSCURA, 4));
        scroll.getViewport().setBackground(new Color(35, 30, 40));
        panel.add(scroll, BorderLayout.CENTER);

        JPanel panelStats = new JPanel(new GridLayout(1, 4, 18, 0));
        panelStats.setOpaque(false);
        panelStats.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        int totalPartidas = usuario.getPartidasJugadas();
        int victorias = usuario.getPartidasGanadas();
        int derrotas = totalPartidas - victorias;
        int puntosGanados = victorias * 3;

        panelStats.add(crearStatLabel("Batallas", String.valueOf(totalPartidas)));
        panelStats.add(crearStatLabel("Victorias", String.valueOf(victorias)));
        panelStats.add(crearStatLabel("Derrotas", String.valueOf(derrotas)));
        panelStats.add(crearStatLabel("Puntos Total", "+" + puntosGanados));

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
            
            if (log.contains("VENCIO A")) {
                String[] partes = log.split("VENCIO A");
                String ganador = partes[0].trim();
                String perdedor = partes[1].split(",")[0].trim();
                
                if (ganador.equalsIgnoreCase(usuarioNombre)) {
                    return perdedor;
                } else if (perdedor.equalsIgnoreCase(usuarioNombre)) {
                    return ganador;
                } else {
                    return ganador.equalsIgnoreCase(usuarioNombre) ? perdedor : ganador;
                }
            }
            else if (log.contains("SE HA RETIRADO")) {
                String[] partes = log.split("SE HA RETIRADO");
                String retirado = partes[0].trim();
                
                if (log.contains("FELICIDADES")) {
                    String[] partesGanador = log.split("FELICIDADES");
                    if (partesGanador.length > 1) {
                        String ganador = partesGanador[1].split(",")[0].trim();
                        
                        if (retirado.equalsIgnoreCase(usuarioNombre)) {
                            return ganador;
                        } else if (ganador.equalsIgnoreCase(usuarioNombre)) {
                            return retirado;
                        } else {
                            return retirado.equalsIgnoreCase(usuarioNombre) ? ganador : retirado;
                        }
                    }
                }
                
                return retirado.equalsIgnoreCase(usuarioNombre) ? "Desconocido" : retirado;
            }
        } catch (Exception e) {
            System.err.println("Error al extraer oponente: " + e.getMessage());
        }
        return "Desconocido";
    }

    private ResultadoPartida extraerResultado(String log) {
        try {
            String usuarioNombre = usuario.getUsername();
            
            if (log.contains(usuarioNombre + " VENCIO A") || 
                log.contains(usuarioNombre + " VENCIO")) {
                return new ResultadoPartida("VICTORIA tiene", "+3");
            }
            
            if (log.contains("VENCIO A " + usuarioNombre)) {
                return new ResultadoPartida("DERROTA", "0");
            }
            
            if (log.contains(usuarioNombre + " SE HA RETIRADO")) {
                return new ResultadoPartida("RETIRO", "0");
            }
            
            if (log.contains("SE HA RETIRADO") && 
                log.contains("FELICIDADES " + usuarioNombre)) {
                return new ResultadoPartida("VICTORIA POR RETIRO", "+3");
            }
        } catch (Exception e) {
            System.err.println("Error al extraer resultado: " + e.getMessage());
        }
        
        return new ResultadoPartida("Batalla registrada", "0");
    }

    private JPanel crearStatLabel(String titulo, String valor) {
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 5));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SANGRE_OSCURA, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(new Color(40, 30, 45));

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setForeground(ORO_ANTIGUO);

        JLabel lblValor = new JLabel(valor, SwingConstants.CENTER);
        lblValor.setFont(new Font("Arial", Font.BOLD, 26));
        lblValor.setForeground(new Color(255, 240, 220));

        panel.add(lblTitulo);
        panel.add(lblValor);

        return panel;
    }

    static class FondoPanel extends JPanel {
        private Image imagen;

        public FondoPanel() {
            try {
                imagen = new ImageIcon(getClass().getResource("/imagenes/reportesfondo.jpg")).getImage();
            } catch (Exception e) {
                setBackground(FONDO_PRINCIPAL);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (imagen != null) {
                g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
                // Overlay oscuro para mejor contraste
                g.setColor(new Color(10, 5, 10, 180));
                g.fillRect(0, 0, getWidth(), getHeight());
            } else {
                g.setColor(FONDO_PRINCIPAL);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }
}