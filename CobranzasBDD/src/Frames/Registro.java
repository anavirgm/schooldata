package Frames;

import MySQL.ConexionBDD;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

public class Registro extends JFrame {

    private JPanel contentPane;
    private JTextField inputNombre;
    private JTextField inputCedula;
    private JTextField inputTelefono;
    private JTextField inputDireccion;
    private JTextField inputNacimiento;
    private JLabel lblNewLabel_1;
    private JTextField txtEmail;
    private JPasswordField passwordField;
    private JLabel lblNewLabel_2;
    private JComboBox comboBox_1;

    /**
     * Launch the application.
     */
    
    
    public static void main(String[] args) {
 
        ConexionBDD conex = new ConexionBDD();
        conex.conectar();
        
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Registro frame = new Registro();
                    frame.setVisible(true);
                    frame.setLocationRelativeTo(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    // solo se ingresen números en los campos de texto
    private void validarSoloNumeros(JTextField textField) {
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume();
                }
            }
        });
    }

 // mover el foco al presionar Enter
    private void asignarListener(Component origen, Component destino) {
        origen.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (origen == txtEmail) { 
                        
                        String email = txtEmail.getText();
                        if (!validarEmail(email)) {
                            JOptionPane.showMessageDialog(null, "Por favor, ingresa un correo electrónico válido.");
                            return;
                        }
                    }
                    destino.requestFocus();
                }
            }
        });
    }

    // validar el formato del correo electrónico
    private boolean validarEmail(String email) {

        String regex = "^(.+)@(.+)$";
        return email.matches(regex);
    }



    /**
     * Create the frame.
     */
    public Registro() {
        setResizable(false);
        setTitle("SchoolData");
        setIconImage(Toolkit.getDefaultToolkit().getImage(Registro.class.getResource("/Img/icons8-montón-de-dinero-20.png")));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 630, 700);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        inputNombre = new JTextField();
        inputNombre.setForeground(Color.BLACK);
        inputNombre.setBounds(92, 185, 186, 35);
        contentPane.add(inputNombre);
        inputNombre.setColumns(10);
        
        inputCedula = new JTextField();
        inputCedula.setForeground(Color.BLACK);
        inputCedula.setBounds(325, 185, 186, 35);
        contentPane.add(inputCedula);
        inputCedula.setColumns(10);
        
        inputTelefono = new JTextField();
        inputTelefono.setForeground(Color.BLACK);
        inputTelefono.setBounds(325, 269, 186, 35);
        contentPane.add(inputTelefono);
        inputTelefono.setColumns(10);
        
        inputDireccion = new JTextField();
        inputDireccion.setForeground(Color.BLACK);
        inputDireccion.setColumns(10);
        inputDireccion.setBounds(92, 353, 186, 35);
        contentPane.add(inputDireccion);
        
        inputNacimiento = new JTextField();
        inputNacimiento.setForeground(Color.BLACK);
        inputNacimiento.setBounds(325, 353, 186, 35);
        contentPane.add(inputNacimiento);
        inputNacimiento.setColumns(10);
        
        JButton btnNewButton = new JButton("Registrar");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
       
                String nombre = inputNombre.getText();
                String cedula = inputCedula.getText();
                String telefono = inputTelefono.getText();
                String direccion = inputDireccion.getText();
                char[] contraseña = passwordField.getPassword();
                String nacimiento = inputNacimiento.getText();
                String email = txtEmail.getText();
                String pass = new String(contraseña);
                String genero = comboBox_1.getSelectedItem() != null ? comboBox_1.getSelectedItem().toString() : "";

                try (Connection conexion = ConexionBDD.getConnection()) {
                   
                    String consultaEstudiante = "INSERT INTO estudiantes (nombre, cedula, telefono, direccion, contraseña, fechaNacimiento, email, genero) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement pstmtEstudiante = conexion.prepareStatement(consultaEstudiante, PreparedStatement.RETURN_GENERATED_KEYS)) {
                        pstmtEstudiante.setString(1, nombre);
                        pstmtEstudiante.setString(2, cedula);
                        pstmtEstudiante.setString(3, telefono);
                        pstmtEstudiante.setString(4, direccion);
                        pstmtEstudiante.setString(5, pass);
                        pstmtEstudiante.setString(6, nacimiento);
                        pstmtEstudiante.setString(7, email);
                        pstmtEstudiante.setString(8, genero);

                        int rowsAffected = pstmtEstudiante.executeUpdate();

                        if (rowsAffected > 0) {
                            try (ResultSet generatedKeys = pstmtEstudiante.getGeneratedKeys()) {
                                if (generatedKeys.next()) {
                                    int idEstudiante = generatedKeys.getInt(1);

                                    JOptionPane.showMessageDialog(null, "El estudiante se guardo correctamente");

                                    Login lg = new Login();
                                    lg.setVisible(true);
                                    lg.setLocationRelativeTo(null);
                                    dispose();
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Error al guardar el estudiante en la base de datos.");
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Error al ejecutar la consulta para guardar el estudiante: " + ex.getMessage());
                    }

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos: " + ex.getMessage());
                }
            }
        });

        
        btnNewButton.setBounds(243, 518, 128, 35);
        contentPane.add(btnNewButton);
        
        lblNewLabel_1 = new JLabel("¿Ya tienes cuenta? Inicia sesion");
        lblNewLabel_1.setForeground(Color.BLACK);
        lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_1.setBounds(180, 564, 252, 29);
        contentPane.add(lblNewLabel_1);
        
        txtEmail = new JTextField();
        txtEmail.setForeground(Color.BLACK);
        txtEmail.setColumns(10);
        txtEmail.setBounds(92, 269, 186, 35);
        contentPane.add(txtEmail);
        
        comboBox_1 = new JComboBox();
        comboBox_1.setForeground(Color.BLACK);
        comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"", "Masculino", "Femenino"}));
        comboBox_1.setBounds(92, 437, 186, 35);
        contentPane.add(comboBox_1);
        
        passwordField = new JPasswordField();
        passwordField.setToolTipText("");
        passwordField.setBounds(325, 437, 186, 35);
        contentPane.add(passwordField);
        
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBounds(0, 0, 614, 29);
        contentPane.add(menuBar);
        
        JMenuItem mntmInicio = new JMenuItem("Inicio");
        mntmInicio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Index indexFrame = new Index();
                indexFrame.setLocationRelativeTo(null);
                indexFrame.setVisible(true);
                dispose();
            }
        });
        menuBar.add(mntmInicio);
        
        lblNewLabel_2 = new JLabel("Contraseña");
        lblNewLabel_2.setBounds(325, 412, 68, 14);
        contentPane.add(lblNewLabel_2);
        
        JLabel lblNombre = new JLabel("Nombre Completo");
        lblNombre.setBounds(92, 160, 186, 14);
        contentPane.add(lblNombre);
        
        JLabel lblNewLabel_3 = new JLabel("Cédula");
        lblNewLabel_3.setBounds(325, 160, 46, 14);
        contentPane.add(lblNewLabel_3);
        
        JLabel lblEmail = new JLabel("Email");
        lblEmail.setBounds(92, 244, 68, 14);
        contentPane.add(lblEmail);
        
        JLabel lblTelfono = new JLabel("Teléfono");
        lblTelfono.setBounds(325, 244, 68, 14);
        contentPane.add(lblTelfono);
        
        JLabel lblDireccin = new JLabel("Dirección");
        lblDireccin.setBounds(92, 328, 68, 14);
        contentPane.add(lblDireccin);
        
        JLabel lblFechaDeNacimiento = new JLabel("Fecha de Nacimiento (yyyy-mm-dd)");
        lblFechaDeNacimiento.setBounds(325, 328, 200, 14);
        contentPane.add(lblFechaDeNacimiento);
        
        JLabel lblNewLabel_2_1 = new JLabel("Género");
        lblNewLabel_2_1.setBounds(92, 412, 68, 14);
        contentPane.add(lblNewLabel_2_1);
        
        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setIcon(new ImageIcon(Registro.class.getResource("/Img/registrate.png")));
        lblNewLabel.setBounds(0, 11, 614, 663);
        contentPane.add(lblNewLabel);
        lblNewLabel_1.addMouseListener((MouseListener) new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Login loginFrame = new Login();
                loginFrame.setLocationRelativeTo(null);
                loginFrame.setVisible(true);
                dispose();
            }
        });
        
        validarSoloNumeros(inputCedula);
        asignarListener(inputNombre, inputCedula);
        asignarListener(inputCedula, txtEmail);
        asignarListener(txtEmail, inputTelefono);
        validarSoloNumeros(inputTelefono);
        asignarListener(inputTelefono, inputDireccion);
        asignarListener(inputDireccion, inputNacimiento);
        asignarListener(inputNacimiento, comboBox_1);
        asignarListener(comboBox_1, passwordField);
        asignarListener(passwordField, btnNewButton);
    }
}
