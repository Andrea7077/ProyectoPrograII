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
public class MenuPrincipal extends JFrame {

  public JButton btnJugar, btnPerfil, btnSalir, btnReportes;

    public MenuPrincipal() {
         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        
        setTitle("Vampire Wargame - Menu Principal");
        setSize(1024, 768);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        FondoPanel fondo = new FondoPanel();
        fondo.setLayout(new GridBagLayout());
        setContentPane(fondo);

        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.insets = new Insets(100, 100, 100, 100);
        gbcMain.anchor = GridBagConstraints.CENTER;

        JPanel panelCentro = new JPanel(new GridBagLayout());
        panelCentro.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("VAMPIRE WARGAME", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Serif", Font.BOLD, 48));
        lblTitulo.setForeground(new Color(255, 50, 50));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelCentro.add(lblTitulo, gbc);

        Dimension btnSize = new Dimension(250, 50);
        Font btnFont = new Font("Segoe UI", Font.BOLD, 18);

        btnJugar = crearBoton("Jugar", new Color(139, 0, 0), btnSize, btnFont);
        gbc.gridy++;
        panelCentro.add(btnJugar, gbc);

        btnPerfil = crearBoton("Mi Perfil", new Color(178, 34, 34), btnSize, btnFont);
        gbc.gridy++;
        panelCentro.add(btnPerfil, gbc);

        btnReportes = crearBoton("Reportes", new Color(178, 34, 34), btnSize, btnFont);
        gbc.gridy++;
        panelCentro.add(btnReportes, gbc);

        btnSalir = crearBoton("Cerrar Sesión", new Color(90, 0, 0), btnSize, btnFont);
        gbc.gridy++;
        panelCentro.add(btnSalir, gbc);

        fondo.add(panelCentro, gbcMain);

        // 🔹 Acciones
        btnJugar.addActionListener(e -> {
            dispose();
            new PTablero().setVisible(true);
        });

        btnPerfil.addActionListener(e -> {
            Cuenta usuario = Cuenta.getUsuarioActual();
            if (usuario != null) {
                JOptionPane.showMessageDialog(this,
                        "Usuario: " + usuario.getUsername(),
                        "Mi Perfil", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "No hay usuario logueado.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnReportes.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Aquí irían los reportes (ranking y logs).",
                    "Reportes", JOptionPane.INFORMATION_MESSAGE);
        });

        btnSalir.addActionListener(e -> {
            int resp = JOptionPane.showConfirmDialog(this,
                    "¿Deseas cerrar sesión?",
                    "Cerrar Sesión",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (resp == JOptionPane.YES_OPTION) {
                Cuenta.cerrarSesion();
                dispose();
                new MenuDeInicio().setVisible(true);
            }
        });
    }

    private JButton crearBoton(String texto, Color bgColor, Dimension size, Font font) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(200,60));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(font);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        return btn;
    }

    static class FondoPanel extends JPanel {
        private Image imagen;
        public FondoPanel() {
            try {
                imagen = new ImageIcon(getClass().getResource("/imagenes/sdfgn.jpg")).getImage();
            } catch (Exception e) {
                setBackground(Color.BLACK);
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
