package Frames;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import MySQL.ConexionBDD;

import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.Color;
import javax.swing.ImageIcon;

public class Admin extends JFrame {

	private JPanel contentPane;
	private JTextField txtNombre;
	private JTextField txtEmail;
	private JTextField txtFecha;
	private JTextField txtCedula;
	private JTextField txtTlf;
	private JTextField txtDirec;
	private JTextField textField_6;
	private JTable table;
	private List<Student> studentsList = new ArrayList<>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		ConexionBDD conex = new ConexionBDD();
		conex.conectar();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Admin frame = new Admin();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	public class Student {
	    private int id;
	    private String nombre;
	    private String cedula;
	    private String telefono;
	    private String email;
	    private String fechaNacimiento;
	    private String direccion;
	    private String genero;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getNombre() {
			return nombre;
		}
		public void setNombre(String nombre) {
			this.nombre = nombre;
		}
		public String getCedula() {
			return cedula;
		}
		public void setCedula(String cedula) {
			this.cedula = cedula;
		}
		public String getTelefono() {
			return telefono;
		}
		public void setTelefono(String telefono) {
			this.telefono = telefono;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getFechaNacimiento() {
			return fechaNacimiento;
		}
		public void setFechaNacimiento(String fechaNacimiento) {
			this.fechaNacimiento = fechaNacimiento;
		}
		public String getDireccion() {
			return direccion;
		}
		public void setDireccion(String direccion) {
			this.direccion = direccion;
		}
		public String getGenero() {
			return genero;
		}
		public void setGenero(String genero) {
			this.genero = genero;
		}

	    // Constructor, getters, and setters
	}
	
	private void cargarDatosEstudiantes() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        studentsList.clear();

        List<Student> studentsList = new ArrayList<>();

        try (Connection conexion = ConexionBDD.getConnection()) {
            Statement stmt = conexion.createStatement();

            String query = "SELECT id, nombre, cedula, email, telefono, direccion, fechaNacimiento, genero FROM estudiantes";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
            	int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String cedula = rs.getString("cedula");
                String email = rs.getString("email");
                String telefono = rs.getString("telefono");
                String direccion = rs.getString("direccion");
                String fechaNacimiento = rs.getString("fechaNacimiento");
                String genero = rs.getString("genero");

                Student student = new Student();
                student.setId(id);
                student.setNombre(nombre);
                student.setCedula(cedula);
                student.setEmail(email);
                student.setTelefono(telefono);
                student.setDireccion(direccion);
                student.setFechaNacimiento(fechaNacimiento);
                student.setGenero(genero);

                studentsList.add(student);
            }

            rs.close();
            stmt.close();
            conexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Agregar los estudiantes a la JTable
        for (Student student : studentsList) {
	        Object[] rowData = {
	            student.getId(),
	            student.getNombre(),
	            student.getCedula(),
	            student.getTelefono(),
	            student.getEmail(),
	            student.getFechaNacimiento(),
	            student.getDireccion(),
	            student.getGenero()
	        };
	        model.addRow(rowData);
	    }
    }
	
	
	private void initialize() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 630, 770);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(45, 88, 123));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNombre = new JLabel("Nombre");
		lblNombre.setBounds(330, 105, 68, 14);
		contentPane.add(lblNombre);
		
		txtNombre = new JTextField();
		txtNombre.setForeground(Color.BLACK);
		txtNombre.setColumns(10);
		txtNombre.setBounds(330, 130, 186, 35);
		contentPane.add(txtNombre);
		
		JLabel lblEmail = new JLabel("Email");
		lblEmail.setBounds(100, 264, 68, 14);
		contentPane.add(lblEmail);
		
		txtEmail = new JTextField();
		txtEmail.setForeground(Color.BLACK);
		txtEmail.setColumns(10);
		txtEmail.setBounds(100, 289, 186, 35);
		contentPane.add(txtEmail);
		
		JLabel lblDireccin = new JLabel("Dirección");
		lblDireccin.setBounds(330, 264, 68, 14);
		contentPane.add(lblDireccin);
		
		txtFecha = new JTextField();
		txtFecha.setForeground(Color.BLACK);
		txtFecha.setColumns(10);
		txtFecha.setBounds(100, 370, 186, 35);
		contentPane.add(txtFecha);
		
		JLabel lblNewLabel_2_1 = new JLabel("Género");
		lblNewLabel_2_1.setBounds(330, 344, 68, 14);
		contentPane.add(lblNewLabel_2_1);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"", "Masculino", "Femenino"}));
		comboBox_1.setForeground(Color.BLACK);
		comboBox_1.setBounds(330, 370, 186, 35);
		contentPane.add(comboBox_1);
		
		JLabel lblNewLabel_3 = new JLabel("Cédula");
		lblNewLabel_3.setBounds(100, 185, 46, 14);
		contentPane.add(lblNewLabel_3);
		
		txtCedula = new JTextField();
		txtCedula.setForeground(Color.BLACK);
		txtCedula.setDropMode(DropMode.INSERT);
		txtCedula.setColumns(10);
		txtCedula.setBounds(100, 210, 186, 35);
		contentPane.add(txtCedula);
		
		JLabel lblTelfono = new JLabel("Teléfono");
		lblTelfono.setBounds(330, 185, 68, 14);
		contentPane.add(lblTelfono);
		
		txtTlf = new JTextField();
		txtTlf.setForeground(Color.BLACK);
		txtTlf.setColumns(10);
		txtTlf.setBounds(330, 210, 186, 35);
		contentPane.add(txtTlf);
		
		JLabel lblFechaDeNacimiento = new JLabel("Fecha de Nacimiento");
		lblFechaDeNacimiento.setBounds(99, 344, 187, 14);
		contentPane.add(lblFechaDeNacimiento);
		
		txtDirec = new JTextField();
		txtDirec.setForeground(Color.BLACK);
		txtDirec.setColumns(10);
		txtDirec.setBounds(330, 289, 186, 35);
		contentPane.add(txtDirec);
		
		textField_6 = new JTextField();
		textField_6.setEditable(false);
		textField_6.setForeground(Color.BLACK);
		textField_6.setColumns(10);
		textField_6.setBounds(100, 130, 186, 35);
		contentPane.add(textField_6);
		
		JLabel lblId = new JLabel("ID");
		lblId.setBounds(100, 105, 68, 14);
		contentPane.add(lblId);
		
		JButton btnNewButton = new JButton("Actualizar");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int selectedRow = table.getSelectedRow();
	            if (selectedRow != -1) {
	                DefaultTableModel model = (DefaultTableModel) table.getModel();
	                String id = textField_6.getText();
	                String nombre = txtNombre.getText();
	                String cedula = txtCedula.getText();
	                String telefono = txtTlf.getText();
	                String email = txtEmail.getText();
	                String fechaNacimiento = txtFecha.getText();
	                String direccion = txtDirec.getText();
	                String genero = (String) comboBox_1.getSelectedItem();
	                
	                model.setValueAt(id, selectedRow, 0);
	                model.setValueAt(nombre, selectedRow, 1);
	                model.setValueAt(cedula, selectedRow, 2);
	                model.setValueAt(telefono, selectedRow, 3);
	                model.setValueAt(email, selectedRow, 4);
	                model.setValueAt(fechaNacimiento, selectedRow, 5);
	                model.setValueAt(direccion, selectedRow, 6);
	                model.setValueAt(genero, selectedRow, 7);
	                
	                Statement stmt = null;
	                try (Connection conexion = ConexionBDD.getConnection()) {
	                	stmt = conexion.createStatement();

	                    // Consulta para actualizar los datos en la base de datos
	                    String updateQuery = "UPDATE estudiantes SET nombre = '" + nombre + "', cedula = '" + cedula + "', "
	                            + "telefono = '" + telefono + "', email = '" + email + "', fechaNacimiento = '" + fechaNacimiento + "', "
	                            + "direccion = '" + direccion + "', genero = '" + genero + "' WHERE id = " + id;
	                    stmt.executeUpdate(updateQuery);

	                    stmt.close();
	                    conexion.close();
	                } catch (SQLException ex) {
	                    ex.printStackTrace();
	                }
	            }
			}
		});
		btnNewButton.setBounds(99, 440, 107, 35);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Limpiar");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtNombre.setText(null);
			    txtEmail.setText(null);
			    txtFecha.setText(null);
			    txtCedula.setText(null);
			    txtTlf.setText(null);
			    txtDirec.setText(null);
			    textField_6.setText(null);
			    comboBox_1.setSelectedIndex(0);
	
			}
		});
		btnNewButton_1.setBounds(249, 440, 119, 35);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Eliminar");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int selectedRow = table.getSelectedRow();
				if (selectedRow != -1) {
				    DefaultTableModel model = (DefaultTableModel) table.getModel();
				    String idString = model.getValueAt(selectedRow, 0).toString(); // Obtener el valor como String
				    int id = Integer.parseInt(idString); 
				    
				    try (Connection conexion = ConexionBDD.getConnection()) {
		                Statement stmt = conexion.createStatement();

		                // Consulta para eliminar el estudiante de la base de datos
		                String deleteQuery = "DELETE FROM estudiantes WHERE id = " + id;
		                stmt.executeUpdate(deleteQuery);

		                stmt.close();
		                conexion.close();

		                model.removeRow(selectedRow);

		                txtNombre.setText(null);
					    txtEmail.setText(null);
					    txtFecha.setText(null);
					    txtCedula.setText(null);
					    txtTlf.setText(null);
					    txtDirec.setText(null);
					    textField_6.setText(null);
					    comboBox_1.setSelectedIndex(0);
					    
		            } catch (SQLException ex) {
		                ex.printStackTrace();
		            }
		        }
			}
		});
		btnNewButton_2.setBounds(406, 440, 107, 35);
		contentPane.add(btnNewButton_2);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(97, 500, 416, 150);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
	        public void valueChanged(ListSelectionEvent event) {
	        	
	        	if (!event.getValueIsAdjusting() && table.getSelectedRow() != -1) {
	        		
	                int selectedRow = table.getSelectedRow();
	                textField_6.setText(getValueFromCell(selectedRow, 0));
	                txtNombre.setText(getValueFromCell(selectedRow, 1));
	                txtCedula.setText(getValueFromCell(selectedRow, 2));
	                txtTlf.setText(getValueFromCell(selectedRow, 3));
	                txtEmail.setText(getValueFromCell(selectedRow, 4));
	                txtFecha.setText(getValueFromCell(selectedRow, 5));
	                txtDirec.setText(getValueFromCell(selectedRow, 6));
	                comboBox_1.setSelectedItem(getValueFromCell(selectedRow, 7));
	            }
	        }
	        
	        private String getValueFromCell(int row, int column) {
	            Object value = table.getValueAt(row, column);
	            return value != null ? value.toString() : "";
	        }
	    });
			
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"ID", "Nombre", "C\u00E9dula", "Teléfono", "Email", "Fecha Nacimiento", "Dirección", "G\u00E9nero"
			}
		));
		scrollPane.setViewportView(table);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 688, 28);
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
		menuBar.add(mntmNewMenuItem);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Utiles");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Utiles utiles = new Utiles();
				utiles.setVisible(true);
				utiles.setLocationRelativeTo(null);
				dispose();
			}
		});
		menuBar.add(mntmNewMenuItem_1);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(Admin.class.getResource("/Img/users (1).png")));
		lblNewLabel.setBounds(0, 39, 614, 720);
		contentPane.add(lblNewLabel);
	}
	
	public Admin() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Admin.class.getResource("/Img/icons8-montón-de-dinero-20.png")));
		setTitle("SchoolData");
		initialize();
		cargarDatosEstudiantes();
	}
}
