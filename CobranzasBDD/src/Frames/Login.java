package Frames;
import MySQL.ConexionBDD;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.SwingConstants;
import javax.swing.JTextField;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Login extends JFrame {
    private JPanel contentPane;
    private JTextField txtEmail;
    private JLabel lblNewLabel_1;
    private JPasswordField passwordField;
    private JMenuBar menuBar;
    private JMenuItem mntmNewMenuItem;
    private JMenuItem mntmNewMenuItem_1;
    private JLabel lblNewLabel_2;
    private JLabel lblNewLabel_3;
    private JLabel lblNewLabel_4;

    /**
     * Launch the application.
     */
    
    public static void main(String[] args) {
        ConexionBDD conexion = new ConexionBDD();
        conexion.conectar();
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Login frame = new Login();
                    frame.setVisible(true);
                    frame.setLocationRelativeTo(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    /**
     * Create the frame.
     * @return 
     */
    
    public Login() {
        setResizable(false);
        setTitle("SchoolData");
        setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/Img/icons8-montón-de-dinero-20.png")));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 450);
        
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
        mntmNewMenuItem = new JMenuItem("Inicio");
        mntmNewMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Index indexFrame = new Index();
                indexFrame.setLocationRelativeTo(null);
                indexFrame.setVisible(true);
                dispose();
            }
        });
        menuBar.add(mntmNewMenuItem);
        
        mntmNewMenuItem_1 = new JMenuItem("");
        menuBar.add(mntmNewMenuItem_1);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(45, 88, 123));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        txtEmail = new JTextField();
        txtEmail.setForeground(Color.BLACK);
        txtEmail.setBounds(116, 123, 203, 30);
        contentPane.add(txtEmail);
        txtEmail.setColumns(10);
        txtEmail.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                	validarEmail();
                }
            }
        });
        
        JButton btnNewButton = new JButton("Entrar");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                validarCampos();
            }
        });
            
        btnNewButton.setBounds(162, 252, 111, 36);
        contentPane.add(btnNewButton);
        
        lblNewLabel_1 = new JLabel("¿Aún no tienes cuenta? Registrate");
        lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_1.setBounds(116, 299, 203, 30);
        contentPane.add(lblNewLabel_1);
        lblNewLabel_1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Registro registroFrame = new Registro();
                registroFrame.setLocationRelativeTo(null);
                registroFrame.setVisible(true);
                dispose();
            }
        });
        
        passwordField = new JPasswordField();
        passwordField.setToolTipText("");
        passwordField.setBounds(116, 201, 203, 30);
        contentPane.add(passwordField);
        
        lblNewLabel_2 = new JLabel("Contraseña");
        lblNewLabel_2.setBounds(116, 176, 82, 14);
        contentPane.add(lblNewLabel_2);
        
        lblNewLabel_3 = new JLabel("Email");
        lblNewLabel_3.setBounds(116, 98, 63, 14);
        contentPane.add(lblNewLabel_3);
        
        lblNewLabel_4 = new JLabel("");
        lblNewLabel_4.setIcon(new ImageIcon(Login.class.getResource("/Img/Inicio de Sesión.png")));
        lblNewLabel_4.setBounds(0, 0, 434, 383);
        contentPane.add(lblNewLabel_4);
        
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnNewButton.doClick();
                }
            }
        });
    }
    
    private void validarEmail() {
    	String email = txtEmail.getText();
    	
    	try (Connection conexion = ConexionBDD.getConnection()) {
            Statement stmt = conexion.createStatement();
            String consulta = "SELECT * FROM estudiantes WHERE email = '" + email + "'";
            ResultSet rs = stmt.executeQuery(consulta);

            if (rs.next()) {
            	passwordField.requestFocus();
            } else {
                JOptionPane.showMessageDialog(null, "Email incorrecto. Intenta nuevamente.");
            }
        } catch (SQLException ex) {
            System.out.println("Error al conectar con la base de datos");
        }
    }
    
    
    private void validarCampos() {
        String email = txtEmail.getText();
        char[] contraseña = passwordField.getPassword();
        String pass = new String(contraseña);

        try (Connection conexion = ConexionBDD.getConnection()) {
            Statement stmt = conexion.createStatement();
            String consulta = "SELECT * FROM estudiantes WHERE email = '" + email + "' AND contraseña = '" + pass + "'";
            ResultSet rs = stmt.executeQuery(consulta);

            if (rs.next()) {
                String nombreEstudiante = rs.getString("nombre");

                Page pg = new Page(nombreEstudiante);
                pg.setVisible(true);
                pg.setLocationRelativeTo(null);
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Contraseña incorrecta. Intenta nuevamente.");
            }
        } catch (SQLException ex) {
            System.out.println("Error al conectar con la base de datos");
        }
    }
}
