/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pantallas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.ArrayList;
import Clases.*;

/**
 *
 * @author andre
 */
public class PCrearCuenta extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnCrear, btnCancelar, btnRegresar;
    private JCheckBox chkMostrar;
    private JLabel lblMensaje;

    public PCrearCuenta() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        setTitle("Crear Cuenta - Vampire Wargame");
        setSize(600, 400);
        setLocationRelativeTo(null);

        FondoPanel fondo = new FondoPanel();
        fondo.setLayout(new GridBagLayout());
        setContentPane(fondo);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10);

        JPanel panelCentro = new JPanel(new GridBagLayout());
        panelCentro.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;

        JLabel lblTitulo = new JLabel("CREAR NUEVA CUENTA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Serif", Font.BOLD, 22));
        lblTitulo.setForeground(Color.RED);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        panelCentro.add(lblTitulo, c);

        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUsuario.setForeground(Color.WHITE);
        c.gridy++;
        c.gridwidth = 1;
        c.gridx = 0;
        panelCentro.add(lblUsuario, c);

        txtUsername = new JTextField(15);
        txtUsername.setBackground(new Color(60, 60, 80));
        txtUsername.setForeground(Color.WHITE);
        txtUsername.setCaretColor(Color.WHITE);
        c.gridx = 1;
        panelCentro.add(txtUsername, c);

        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPassword.setForeground(Color.WHITE);
        c.gridy++;
        c.gridx = 0;
        panelCentro.add(lblPassword, c);

        txtPassword = new JPasswordField(15);
        txtPassword.setBackground(new Color(60, 60, 80));
        txtPassword.setForeground(Color.WHITE);
        txtPassword.setCaretColor(Color.WHITE);
        txtPassword.setEchoChar('*');
        c.gridx = 1;
        panelCentro.add(txtPassword, c);

        chkMostrar = new JCheckBox("Mostrar contraseña");
        chkMostrar.setForeground(Color.WHITE);
        chkMostrar.setOpaque(false);
        chkMostrar.addActionListener(e -> {
            txtPassword.setEchoChar(chkMostrar.isSelected() ? (char) 0 : '*');
        });
        c.gridy++;
        c.gridx = 1;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        panelCentro.add(chkMostrar, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;

        lblMensaje = new JLabel("", SwingConstants.CENTER);
        lblMensaje.setForeground(Color.RED);
        c.gridy++;
        c.gridx = 0;
        c.gridwidth = 2;
        panelCentro.add(lblMensaje, c);

        JPanel panelBotones = new JPanel();
        panelBotones.setOpaque(false);

        btnCrear = new JButton("Crear Cuenta");
        btnCrear.setBackground(new Color(139, 0, 0));
        btnCrear.setForeground(Color.WHITE);
        btnCrear.setFocusPainted(false);
        btnCrear.setBorderPainted(false);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(178, 34, 34));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorderPainted(false);

        btnRegresar = new JButton("Regresar");
        btnRegresar.setBackground(new Color(90, 0, 0));
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.setFocusPainted(false);
        btnRegresar.setBorderPainted(false);

        panelBotones.add(btnCrear);
        panelBotones.add(btnCancelar);
        panelBotones.add(btnRegresar);

        c.gridy++;
        c.gridwidth = 2;
        panelCentro.add(panelBotones, c);

        fondo.add(panelCentro, gbc);

        btnCrear.addActionListener(e -> crearCuenta());
        btnCancelar.addActionListener(e -> limpiarCampos());
        btnRegresar.addActionListener(e -> {
            dispose();
            new MenuDeInicio().setVisible(true);
        });

        txtPassword.addActionListener(e -> crearCuenta());
    }

    private void crearCuenta() {
        try {
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword());

            if (username.isEmpty()) {
                mostrarError("El nombre de usuario no puede estar vacío");
                return;
            }

            if (password.length() != 5) {
                mostrarError("La contraseña debe tener exactamente 5 caracteres especiales");
                return;
            }

            boolean todosEspeciales = true;
            for (char c : password.toCharArray()) {
                if (Character.isLetterOrDigit(c)) {
                    todosEspeciales = false;
                    break;
                }
            }

            if (!todosEspeciales) {
                mostrarError("️La contraseña debe tener 5 caracteres especiales");
                return;
            }

            Cuenta resultado = Cuenta.crearCuenta(username, password);

            if (resultado != null) {
                lblMensaje.setForeground(new Color(0, 255, 100));
                lblMensaje.setText("Cuenta creada exitosamente");

                Timer timer = new Timer(1000, ev -> {
                    dispose();
                    new MenuPrincipal().setVisible(true);
                });
                timer.setRepeats(false);
                timer.start();
            } else {
                boolean existe = false;
                for (Cuenta c : Cuenta.getTodasCuentas()) {
                    if (c.getUsername().equalsIgnoreCase(username) && c.isActivo()) {
                        existe = true;
                        break;
                    }
                }

                if (existe) {
                    mostrarError("Ese nombre de usuario ya existe, elige otro");
                } else {
                    mostrarError("La contraseña no cumple con las reglas (5 caracteres especiales)");
                }
            }
        } catch (Exception e) {
            mostrarError("Error inesperado: " + e.getMessage());
        }

    }

    private void limpiarCampos() {
        txtUsername.setText("");
        txtPassword.setText("");
        lblMensaje.setText("");
        chkMostrar.setSelected(false);
        txtPassword.setEchoChar('*');
    }

    private void mostrarError(String mensaje) {
        lblMensaje.setForeground(Color.RED);
        lblMensaje.setText(mensaje);
    }

    static class FondoPanel extends JPanel {

        private Image imagen;

        public FondoPanel() {
            try {
                imagen = new ImageIcon(getClass().getResource("/imagenes/FondoCrearPLayer.png")).getImage();
            } catch (Exception e) {
                setBackground(new Color(20, 20, 20));
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (imagen != null) {
                g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
            } else {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }
}
