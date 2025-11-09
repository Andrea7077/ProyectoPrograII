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

        // Título
        JLabel lblTitulo = new JLabel("VAMPIRE WARGAME", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Serif", Font.BOLD, 48));
        lblTitulo.setForeground(new Color(255, 50, 50));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelCentro.add(lblTitulo, gbc);

        // Mensaje de bienvenida
        Cuenta usuario = Cuenta.getUsuarioActual();
        if (usuario != null) {
            JLabel lblBienvenida = new JLabel("Bienvenido, " + usuario.getUsername() + " :)", SwingConstants.CENTER);
            lblBienvenida.setFont(new Font("Segoe UI", Font.ITALIC, 18));
            lblBienvenida.setForeground(new Color(255, 215, 0));
            gbc.gridy++;
            panelCentro.add(lblBienvenida, gbc);
        }

        Dimension btnSize = new Dimension(250, 50);
        Font btnFont = new Font("Segoe UI", Font.BOLD, 18);

        // Botón Jugar
        btnJugar = crearBoton("Jugar", new Color(139, 0, 0), btnSize, btnFont);
        gbc.gridy++;
        panelCentro.add(btnJugar, gbc);

        // Botón Mi Perfil
        btnPerfil = crearBoton("Mi Perfil", new Color(178, 34, 34), btnSize, btnFont);
        gbc.gridy++;
        panelCentro.add(btnPerfil, gbc);

        // Botón Reportes
        btnReportes = crearBoton("Reportes", new Color(100, 30, 30), btnSize, btnFont);
        gbc.gridy++;
        panelCentro.add(btnReportes, gbc);

        // Botón Cerrar Sesión
        btnSalir = crearBoton("Cerrar Sesión", new Color(90, 0, 0), btnSize, btnFont);
        gbc.gridy++;
        panelCentro.add(btnSalir, gbc);

        fondo.add(panelCentro, gbcMain);

        // ═══════════════════════════════════════
        // ACCIONES DE LOS BOTONES
        // ═══════════════════════════════════════

        btnJugar.addActionListener(e -> {
            dispose();
            new PTablero().setVisible(true);
        });

        btnPerfil.addActionListener(e -> {
            dispose();
            new PMiPerfil().setVisible(true);
        });

        btnReportes.addActionListener(e -> {
            dispose();
            new PReportes().setVisible(true);
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
        btn.setPreferredSize(size);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(font);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto hover
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            Color originalColor = bgColor;
            
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor.brighter());
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(originalColor);
            }
        });
        
        return btn;
    }

    static class FondoPanel extends JPanel {
        private Image imagen;
        
        public FondoPanel() {
            try {
                imagen = new ImageIcon(getClass().getResource("/imagenes/FondoMenu2.jpg")).getImage();
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