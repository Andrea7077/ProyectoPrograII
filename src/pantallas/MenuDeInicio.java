/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pantallas;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author andre
 */
public class MenuDeInicio extends JFrame {

    public MenuDeInicio() {
        setTitle("Vampire Wargame - Menú de Inicio");
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
        lblTitulo.setFont(new Font("Serif", Font.BOLD, 48)); // Tamaño de fuente aumentado
        lblTitulo.setForeground(new Color(255, 50, 50)); // Rojo brillante
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        panelCentro.add(lblTitulo, gbc);
        
       
        Dimension btnSize = new Dimension(250, 50); // Tamaño consistente y más grande
        Font btnFont = new Font("Segoe UI", Font.BOLD, 18);
        
        JButton btnLogin = crearBoton("Log In", new Color(139, 0, 0), btnSize, btnFont);
        gbc.gridy++;
        panelCentro.add(btnLogin, gbc);
        
        JButton btnCrear = crearBoton("Crear Player", new Color(178, 34, 34), btnSize, btnFont);
        gbc.gridy++;
        panelCentro.add(btnCrear, gbc);
        
        JButton btnSalir = crearBoton("Salir", new Color(90, 0, 0), btnSize, btnFont);
        gbc.gridy++;
        panelCentro.add(btnSalir, gbc);
        
        
        fondo.add(panelCentro, gbcMain);

        btnLogin.addActionListener(e -> {
             dispose(); 
             new PLogin().setVisible(true);
        });
        
        btnCrear.addActionListener(e -> {
             dispose(); 
             new PCrearCuenta().setVisible(true);
        });
        
        btnSalir.addActionListener(e -> {
            int resp = JOptionPane.showConfirmDialog(this,
                    "¿Seguro que deseas salir?",
                    "Salir del juego",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (resp == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }
    
    /**
     * Helper method para crear botones con estilo consistente.
     */
    private JButton crearBoton(String texto, Color bgColor, Dimension size, Font font) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(size);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(font);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        return btn;
    }

    
    /**
     * Panel personalizado para dibujar una imagen como fondo.
     */
    static class FondoPanel extends JPanel {
        private Image imagen;

        public FondoPanel() {
            try {
             
                imagen = new ImageIcon(getClass().getResource("/imagenes/sdfgn.jpg")).getImage();
            } catch (Exception e) {
                System.err.println("ERROR: No se pudo cargar la imagen de fondo: /imagenes/FondoMenu.png");
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
