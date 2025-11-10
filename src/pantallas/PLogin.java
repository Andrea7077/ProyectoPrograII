
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pantallas;

import Clases.Cuenta;
import Clases.Jugador;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author andre
 */
public class PLogin extends JFrame {

 
    private JTextField tuser;
    private JPasswordField tcontra;
    private JLabel labelmensaje;
    private JButton btnLogin, btnRegresar;

    public PLogin() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        setTitle("Vampire Wargame - Log In");
        setSize(600, 400);
        setLocationRelativeTo(null);

        FondoPanel fondoPanel = new FondoPanel();
        fondoPanel.setLayout(new GridBagLayout());
        setContentPane(fondoPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel panelCentro = new JPanel(new GridBagLayout());
        panelCentro.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;

        JLabel lblTitulo = new JLabel("VAMPIRE WARGAME", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Serif", Font.BOLD, 24));
        lblTitulo.setForeground(Color.RED);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        panelCentro.add(lblTitulo, c);

        JLabel lblSubtitulo = new JLabel("Iniciar Sesion", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblSubtitulo.setForeground(Color.WHITE);
        c.gridy++;
        panelCentro.add(lblSubtitulo, c);

        JLabel lblUser = new JLabel("Usuario:");
        lblUser.setForeground(Color.WHITE);
        c.gridy++;
        c.gridwidth = 1;
        c.gridx = 0;
        panelCentro.add(lblUser, c);

        tuser = new JTextField(15);
        tuser.setBackground(new Color(60, 60, 80));
        tuser.setForeground(Color.WHITE);
        tuser.setCaretColor(Color.WHITE);
        c.gridx = 1;
        panelCentro.add(tuser, c);

        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setForeground(Color.WHITE);
        c.gridy++;
        c.gridx = 0;
        panelCentro.add(lblPass, c);

        tcontra = new JPasswordField(15);
        tcontra.setBackground(new Color(60, 60, 80));
        tcontra.setForeground(Color.WHITE);
        tcontra.setCaretColor(Color.WHITE);
        c.gridx = 1;
        panelCentro.add(tcontra, c);

        labelmensaje = new JLabel("", SwingConstants.CENTER);
        labelmensaje.setForeground(Color.RED);
        c.gridy++;
        c.gridx = 0;
        c.gridwidth = 2;
        panelCentro.add(labelmensaje, c);

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);

        btnLogin = new JButton("Entrar");
        btnLogin.setBackground(new Color(150, 0, 0));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);

        btnRegresar = new JButton("Regresar");
        btnRegresar.setBackground(new Color(90, 0, 0));
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.setFocusPainted(false);

        btnPanel.add(btnLogin);
        btnPanel.add(btnRegresar);
        c.gridy++;
        c.gridwidth = 2;
        panelCentro.add(btnPanel, c);

        fondoPanel.add(panelCentro, gbc);

        btnRegresar.addActionListener(e -> {
            dispose();
            new MenuDeInicio().setVisible(true);
        });

        btnLogin.addActionListener(e -> validarLogin());
        
        tcontra.addActionListener(e -> validarLogin());
    }

    private void validarLogin() {
        try {
            String username = tuser.getText().trim();
            String password = new String(tcontra.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                labelmensaje.setForeground(Color.YELLOW);
                labelmensaje.setText("Completa todos los campos");
                return;
            }

            if (Cuenta.iniciarSesion(username, password)) {
                labelmensaje.setForeground(Color.GREEN);
                labelmensaje.setText("Inicio de sesión exitoso");
                
                Timer timer = new Timer(1000, ev -> {
                    dispose();
                    new MenuPrincipal().setVisible(true);
                });
                timer.setRepeats(false);
                timer.start();
            } else {
                labelmensaje.setForeground(Color.RED);
                labelmensaje.setText("Usuario o contraseña incorrectos");
                tcontra.setText("");
            }
        } catch (Exception e) {
            labelmensaje.setForeground(Color.RED);
            labelmensaje.setText("rror: " + e.getMessage());
        }
    }

    static class FondoPanel extends JPanel {
        private Image imagen;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            try {
                imagen = new ImageIcon(getClass().getResource("/imagenes/FondoLogIn.png")).getImage();
                g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
            } catch (Exception e) {
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }
}