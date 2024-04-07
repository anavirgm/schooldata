package Frames;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import MySQL.ConexionBDD;

import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.Toolkit;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JMenu;
import java.awt.Font;

public class Index extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	
	

	
	public static void main(String[] args) {
		ConexionBDD conex = new ConexionBDD();
		conex.conectar();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Index frame = new Index();
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
	 */
	public Index() {
		setResizable(false);
		setTitle("SchoolData");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Index.class.getResource("/Img/icons8-montón-de-dinero-20.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 500);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(45, 88, 123));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("Registrarse");
		btnNewButton.setBounds(181, 332, 120, 37);
		contentPane.add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            Registro registroFrame = new Registro();
	            registroFrame.setLocationRelativeTo(null);
	            registroFrame.setVisible(true);
	            dispose();
	        }
	    });
		
		JButton btnNewButton_1 = new JButton("Iniciar sesion");
		btnNewButton_1.setBounds(403, 332, 120, 37);
		contentPane.add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            Login loginFrame = new Login();
	            loginFrame.setLocationRelativeTo(null);
	            loginFrame.setVisible(true);
	            dispose();
	        }
	    });
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 769, 37);
		contentPane.add(menuBar);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Inicio");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Index indexFrame = new Index();
				indexFrame.setLocationRelativeTo(null);
	            indexFrame.setVisible(true);
	            dispose();
			}
		});
		mntmNewMenuItem.setHorizontalAlignment(SwingConstants.CENTER);
		menuBar.add(mntmNewMenuItem);
		
		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Contáctenos");
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Email: schooldata@gmail.com"
						+ "\nNúmero telefónico: +58 414-6680-822"
						+ "\nUbicación: Av. Circunvalación 2, Maracaibo, Zulia");
			}
		});
		mntmNewMenuItem_2.setHorizontalAlignment(SwingConstants.CENTER);
		menuBar.add(mntmNewMenuItem_2);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("     Acerca de nosotros");
		mntmNewMenuItem_1.setHorizontalAlignment(SwingConstants.CENTER);
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String mess = "             ----------------N813P----------------"
						+ "\n               Samuel Báez - 30.442.573"
						+ "\n                      Ana Mota - 30.597.012"
						+ "\n           Samuel Rincon - 29.877.987"
						+ "\n             Jesús Santos - 29.749.940"
						+ "\n            Juan Urdaneta - 29.903.089"
						+ "\n         Pablo Velasquez - 28.391.153";
				
				Object[] options = {"Ir a WhatsApp"};

		        int choice = JOptionPane.showOptionDialog(null, mess, "Acerca de nosotros", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

		        if (choice == JOptionPane.YES_OPTION) {
		            String message = "¡Hola! Quisiera contactarte.";
		            String[] contactNumbers = {"584246052565"}; 

		            for (String number : contactNumbers) {
		                try {
		                    String encodedMessage = URLEncoder.encode(message, "UTF-8");
		                    String url = "https://api.whatsapp.com/send?phone=" + number + "&text=" + encodedMessage;
		                    java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
		                } catch (Exception ex) {
		                    ex.printStackTrace();
		                }
		            }
		        }
			}
		});
		menuBar.add(mntmNewMenuItem_1);
		
		JMenuItem mntmNewMenuItem_5 = new JMenuItem("Administrador");
		mntmNewMenuItem_5.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        // Crear el campo de contraseña
		        JPasswordField passwordField = new JPasswordField();

		        // Crear el cuadro de diálogo
		        JOptionPane pane = new JOptionPane(
		            passwordField,
		            JOptionPane.PLAIN_MESSAGE,
		            JOptionPane.OK_CANCEL_OPTION
		        );

		        // Personalizar el título del cuadro de diálogo
		        JDialog dialog = pane.createDialog(Index.this, "Ingrese la contraseña de administrador:");

		        // Enfocar automáticamente el campo de contraseña
		        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
		            @Override
		            public void windowOpened(java.awt.event.WindowEvent windowEvent) {
		                passwordField.requestFocusInWindow();
		            }
		        });

		        // Mostrar el cuadro de diálogo
		        dialog.setVisible(true);

		        // Obtener el resultado del cuadro de diálogo
		        Object selectedValue = pane.getValue();

		        // Si se presiona "Aceptar" en el cuadro de diálogo
		        if (selectedValue != null && ((Integer) selectedValue).intValue() == JOptionPane.OK_OPTION) {
		            // Obtener la contraseña ingresada
		            char[] passwordChars = passwordField.getPassword();
		            String password = new String(passwordChars);

		            // Verificar la contraseña ingresada
		            if ("$$$$".equals(password)) {
		                Admin admin = new Admin();
		                admin.setVisible(true);
		                admin.setLocationRelativeTo(null);
		                dispose(); // Cerrar la ventana actual si se autentica correctamente
		            } else {
		                JOptionPane.showMessageDialog(Index.this, "Contraseña incorrecta", "Error", JOptionPane.ERROR_MESSAGE);
		            }

		            // Limpia la contraseña en memoria
		            Arrays.fill(passwordChars, ' ');
		        }
		    }
		});

		menuBar.add(mntmNewMenuItem_5);
		
		JLabel lblNewLabel_2 = new JLabel("New label");
		lblNewLabel_2.setIcon(new ImageIcon(Index.class.getResource("/Img/sch (1).png")));
		lblNewLabel_2.setBounds(-18, 67, 702, 287);
		contentPane.add(lblNewLabel_2);
	}
}
