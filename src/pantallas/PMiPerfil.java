/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pantallas;

import Clases.Cuenta;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author andre
 */
public class PMiPerfil extends JFrame {
 private Cuenta usuario;
    private JLabel lblUsername, lblPuntos, lblFecha, lblPartidas, lblGanadas, lblPorcentaje;
    private JButton btnCambiarPassword, btnEliminarCuenta, btnRegresar;

    public PMiPerfil() {
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
        setTitle("Mi Perfil - Vampire Wargame");
        setSize(800, 650);
        setLocationRelativeTo(null);
        setResizable(false);

        FondoPanel fondo = new FondoPanel();
        fondo.setLayout(new GridBagLayout());
        setContentPane(fondo);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel panelCentral = crearPanelCentral();
        fondo.add(panelCentral, gbc);

        cargarDatos();
    }

    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 15, 10, 15);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("👤 MI PERFIL", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Serif", Font.BOLD, 38));
        lblTitulo.setForeground(new Color(255, 70, 70));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        panel.add(lblTitulo, c);

        JSeparator sep1 = new JSeparator();
        sep1.setPreferredSize(new Dimension(480, 2));
        sep1.setForeground(new Color(139, 0, 0));
        c.gridy++;
        panel.add(sep1, c);

        JPanel infoPanel = new JPanel(new GridLayout(6, 2, 12, 12));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        infoPanel.add(crearLabel("Usuario:"));
        lblUsername = crearLabelValor("");
        infoPanel.add(lblUsername);

        infoPanel.add(crearLabel("Puntos:"));
        lblPuntos = crearLabelValor("");
        infoPanel.add(lblPuntos);

        infoPanel.add(crearLabel("Miembro desde:"));
        lblFecha = crearLabelValor("");
        infoPanel.add(lblFecha);

        infoPanel.add(crearLabel("Partidas jugadas:"));
        lblPartidas = crearLabelValor("");
        infoPanel.add(lblPartidas);

        infoPanel.add(crearLabel("Partidas ganadas:"));
        lblGanadas = crearLabelValor("");
        infoPanel.add(lblGanadas);

        infoPanel.add(crearLabel("Tasa de victoria:"));
        lblPorcentaje = crearLabelValor("");
        infoPanel.add(lblPorcentaje);

        c.gridy++;
        c.gridwidth = 2;
        panel.add(infoPanel, c);

        JSeparator sep2 = new JSeparator();
        sep2.setPreferredSize(new Dimension(480, 2));
        sep2.setForeground(new Color(139, 0, 0));
        c.gridy++;
        panel.add(sep2, c);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnPanel.setOpaque(false);

        btnCambiarPassword = crearBoton("Cambiar Contraseña", new Color(139, 0, 0));
        btnCambiarPassword.addActionListener(e -> new VentanaCambiarPassword(this, usuario).setVisible(true));

        btnEliminarCuenta = crearBoton("Eliminar Cuenta", new Color(100, 0, 0));
        btnEliminarCuenta.addActionListener(e -> eliminarCuenta());

        btnRegresar = crearBoton("Regresar al Menú", new Color(70, 70, 90));
        btnRegresar.addActionListener(e -> {
            dispose();
            new MenuPrincipal().setVisible(true);
        });

        btnPanel.add(btnCambiarPassword);
        btnPanel.add(btnEliminarCuenta);
        btnPanel.add(btnRegresar);

        c.gridy++;
        panel.add(btnPanel, c);

        return panel;
    }

    private JLabel crearLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbl.setForeground(new Color(255, 220, 150));
        return lbl;
    }

    private JLabel crearLabelValor(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lbl.setForeground(Color.WHITE);
        return lbl;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(230, 45));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return btn;
    }

    private void cargarDatos() {
        lblUsername.setText(usuario.getUsername());
        lblPuntos.setText(String.valueOf(usuario.getPuntos()));
        lblFecha.setText(usuario.getFechaCreacion());
        lblPartidas.setText(String.valueOf(usuario.getPartidasJugadas()));
        lblGanadas.setText(String.valueOf(usuario.getPartidasGanadas()));
        lblPorcentaje.setText(String.format("%.1f%%", usuario.getPorcentajeVictorias()));
    }

    private void eliminarCuenta() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel lblAdvertencia = new JLabel("Cuidado, ¡No puedes restaurar tu cuenta una vez sea eliminada!");
        lblAdvertencia.setFont(new Font("Arial", Font.BOLD, 14));
        lblAdvertencia.setForeground(Color.RED);
        lblAdvertencia.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel lblInstruccion = new JLabel("Ingresa tu contraseña para confirmar:");
        lblInstruccion.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JPasswordField txtPassword = new JPasswordField(15);
        
        panel.add(lblAdvertencia);
        panel.add(lblInstruccion);
        panel.add(txtPassword);

        int opcion = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Eliminar Cuenta",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (opcion == JOptionPane.OK_OPTION) {
            String password = new String(txtPassword.getPassword());
            
            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Debes ingresar tu contraseña",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (Cuenta.eliminarCuentaActual(password)) {
                JOptionPane.showMessageDialog(this,
                        "Cuenta eliminada exitosamente\nSerás redirigido al menú de inicio",
                        "Cuenta Eliminada",
                        JOptionPane.INFORMATION_MESSAGE);
                
                dispose();
                new MenuDeInicio().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Contraseña incorrecta\nNo se pudo eliminar la cuenta",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    static class FondoPanel extends JPanel {

        private Image imagen;

        public FondoPanel() {
            try {
                imagen = new ImageIcon(getClass().getResource("/imagenes/fondomiperfil.png")).getImage();
            } catch (Exception e) {
                setBackground(new Color(20, 20, 30));
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (imagen != null) {
                g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}