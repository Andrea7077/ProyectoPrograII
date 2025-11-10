/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pantallas;
import javax.swing.*;
import java.awt.*;
import Clases.Cuenta;
/**
 *
 * @author andre
 */

public class VentanaCambiarPassword extends JDialog {

    public VentanaCambiarPassword(JFrame parent, Cuenta usuario) {
        super(parent, "Cambiar Contraseña", true);
        setSize(420, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        setUndecorated(true);

        JPanel fondo = new JPanel();
        fondo.setBackground(new Color(30, 30, 45));
        fondo.setBorder(BorderFactory.createLineBorder(new Color(139, 0, 0), 3));
        fondo.setLayout(new GridBagLayout());
        add(fondo);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel lblTitulo = new JLabel("🔒 Cambiar Contraseña", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Serif", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(255, 80, 80));
        gbc.gridwidth = 2;
        fondo.add(lblTitulo, gbc);

        gbc.gridy++;
        fondo.add(crearCampo("Contraseña actual:", "actual"), gbc);

        gbc.gridy++;
        fondo.add(crearCampo("Nueva contraseña (5 especiales):", "nueva"), gbc);

        gbc.gridy++;
        fondo.add(crearCampo("Confirmar nueva:", "confirmar"), gbc);

        gbc.gridy++;
        gbc.gridwidth = 2;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        panelBotones.setOpaque(false);
        JButton btnAceptar = new JButton("✅ Cambiar");
        JButton btnCancelar = new JButton("❌ Cancelar");

        estilizarBoton(btnAceptar, new Color(100, 0, 0));
        estilizarBoton(btnCancelar, new Color(60, 60, 70));

        panelBotones.add(btnAceptar);
        panelBotones.add(btnCancelar);
        fondo.add(panelBotones, gbc);

        JPasswordField txtActual = (JPasswordField) campos.get("actual");
        JPasswordField txtNueva = (JPasswordField) campos.get("nueva");
        JPasswordField txtConfirmar = (JPasswordField) campos.get("confirmar");

        btnCancelar.addActionListener(e -> dispose());

        btnAceptar.addActionListener(e -> {
            String actual = new String(txtActual.getPassword());
            String nueva = new String(txtNueva.getPassword());
            String confirmar = new String(txtConfirmar.getPassword());

            if (!nueva.equals(confirmar)) {
                JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (usuario.cambiarPassword(actual, nueva)) {
                JOptionPane.showMessageDialog(this, "Contraseña cambiada correctamente ✅");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error: Contraseña actual incorrecta o nueva inválida\n(Recuerda: 5 caracteres especiales)",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private java.util.Map<String, JPasswordField> campos = new java.util.HashMap<>();

    private JPanel crearCampo(String texto, String clave) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        JLabel label = new JLabel(texto);
        label.setForeground(Color.WHITE);
        JPasswordField campo = new JPasswordField();
        campo.setBackground(new Color(60, 60, 80));
        campo.setForeground(Color.WHITE);
        campo.setCaretColor(Color.WHITE);
        panel.add(label, BorderLayout.NORTH);
        panel.add(campo, BorderLayout.CENTER);
        campos.put(clave, campo);
        return panel;
    }

    private void estilizarBoton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
    }
}