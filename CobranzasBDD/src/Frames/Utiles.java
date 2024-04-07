package Frames;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import java.awt.ScrollPane;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.itextpdf.text.log.Level;

import MySQL.ConexionBDD;

import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;

import java.awt.SystemColor;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Dimension;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javax.swing.SwingConstants;

public class Utiles extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField nombre;
	private JTextField txtPrecio;
	private JTextField stock;
	private JTable table;
	String s;
	private JLabel lbl_img;
	private DefaultTableModel tableModel;
	protected String path2;


	public static void main(String[] args) {
		ConexionBDD conex = new ConexionBDD();
		conex.conectar();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Utiles frame = new Utiles();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private byte[] convertImageToBytes(String imagePath) {
	    byte[] imgBytes = null;
	    try {
	        File imgFile = new File(imagePath);
	        imgBytes = new byte[(int) imgFile.length()];
	        InputStream inputStream = new FileInputStream(imgFile);
	        inputStream.read(imgBytes);
	        inputStream.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return imgBytes;
	}

	
	public Utiles() {
		setTitle("SchoolData");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Utiles.class.getResource("/Img/icons8-montón-de-dinero-20.png")));
		setResizable(false);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 846, 680);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(45, 88, 123));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		nombre = new JTextField();
		nombre.setBounds(219, 139, 180, 30);
		contentPane.add(nombre);
		nombre.setColumns(10);
		
		txtPrecio = new JTextField();
		txtPrecio.setBounds(219, 280, 180, 30);
		contentPane.add(txtPrecio);
		txtPrecio.setColumns(10);
		
		stock = new JTextField();
		stock.setBounds(219, 210, 180, 30);
		contentPane.add(stock);
		stock.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Nombre del producto");
		lblNewLabel.setBounds(219, 116, 241, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Cantidad");
		lblNewLabel_1.setBounds(219, 185, 127, 14);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Precio");
		lblNewLabel_2.setBounds(219, 255, 127, 14);
		contentPane.add(lblNewLabel_2);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(219, 390, 400, 120);
		contentPane.add(scrollPane);
		

		

		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
		        int selectedRow = table.getSelectedRow();

		        if (selectedRow != -1) {
		            // Obtener la información de la fila seleccionada
		            String nombreProducto = table.getValueAt(selectedRow, 1).toString();
		            int cantidad = (int) table.getValueAt(selectedRow, 2);
		            double precio = (double) table.getValueAt(selectedRow, 3);

		            // Mostrar la información en los JTextField
		            nombre.setText(nombreProducto);
		            stock.setText(String.valueOf(cantidad));
		            txtPrecio.setText(String.valueOf(precio));

		            try (Connection conexion = ConexionBDD.getConnection()) {
		                // Obtener la imagen de la base de datos
		                String query = "SELECT imagen FROM utiles WHERE nombre=?";
		                PreparedStatement preparedStatement = conexion.prepareStatement(query);
		                preparedStatement.setString(1, nombreProducto);
		                ResultSet resultSet = preparedStatement.executeQuery();

		                if (resultSet.next()) {
		                    // Convertir los bytes de la imagen en un ImageIcon
		                    byte[] imageData = resultSet.getBytes("imagen");
		                    if (imageData != null) {
		                        ImageIcon imageIcon = new ImageIcon(imageData);
		                        // Escalar la imagen al tamaño del JLabel
		                        Image image = imageIcon.getImage().getScaledInstance(lbl_img.getWidth(), lbl_img.getHeight(), Image.SCALE_SMOOTH);
		                        lbl_img.setIcon(new ImageIcon(image));
		                    } else {
		                        lbl_img.setIcon(null);
		                    }
		                } else {
		                    lbl_img.setIcon(null);
		                }
		            } catch (SQLException ex) {
		                ex.printStackTrace();
		                JOptionPane.showMessageDialog(null, "Error al cargar la imagen desde la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
		            }
		        }
		    }
		});
		
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Codigo", "Utiles", "Stock", "Precio"
			}
		));
		
		tableModel = (DefaultTableModel) table.getModel();

        // Llenar la tabla con datos de la base de datos al iniciar la aplicación
        try (Connection conexion = ConexionBDD.getConnection()) {
            String query = "SELECT * FROM utiles";
            PreparedStatement preparedStatement = conexion.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
            	int codigo = resultSet.getInt("codigo");
                String nombreProducto = resultSet.getString("nombre");
                int cantidad = resultSet.getInt("stock");
                double precio = resultSet.getDouble("precio");                

                // Agregar datos al modelo de la JTable
                Object[] rowData = {codigo, nombreProducto, cantidad, precio };
                tableModel.addRow(rowData);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar datos desde la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
        }
		
		scrollPane.setViewportView(table);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 975, 25);
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
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Usuarios");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Admin admin = new Admin();
            	admin.setVisible(true);
            	admin.setLocationRelativeTo(null);
            	dispose();
			}
		});
		menuBar.add(mntmNewMenuItem_1);
		
		
		
		JButton btnNewButton_1 = new JButton("Limpiar");
		 btnNewButton_1.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                limpiar();
	            }
	        });
		btnNewButton_1.setBounds(358, 340, 120, 30);
		contentPane.add(btnNewButton_1);
		
		
		
		JButton btnNewButton_2 = new JButton("Agregar");
		btnNewButton_2.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	String nombreProducto = nombre.getText();
		        int cantidad = Integer.parseInt(stock.getText());
		        double precio = Double.parseDouble(txtPrecio.getText());

		        try (Connection conexion = ConexionBDD.getConnection()) {
		            // Verificar si el producto ya existe
		            String queryExistencia = "SELECT COUNT(*) FROM utiles WHERE nombre = ?";
		            PreparedStatement existenciaStatement = conexion.prepareStatement(queryExistencia);
		            existenciaStatement.setString(1, nombreProducto);
		            ResultSet existenciaResult = existenciaStatement.executeQuery();
		            existenciaResult.next();
		            int cantidadExistente = existenciaResult.getInt(1);
		            if (cantidadExistente > 0) {
		                JOptionPane.showMessageDialog(null, "El producto ya existe en la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
		                return;
		            }

		            // Convertir la imagen en un array de bytes
		            byte[] imagenBytes = convertImageToBytes(s);

		            String query = "INSERT INTO utiles (nombre, stock, precio, imagen) VALUES (?, ?, ?, ?)";
		            PreparedStatement preparedStatement = conexion.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
		            preparedStatement.setString(1, nombreProducto);
		            preparedStatement.setInt(2, cantidad);
		            preparedStatement.setDouble(3, precio);
		            preparedStatement.setBytes(4, imagenBytes);

		            preparedStatement.executeUpdate();

		            // Obtener el id
		            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
		            if (generatedKeys.next()) {
		                int codigoGenerado = generatedKeys.getInt(1);

		                // Agregar datos al JTable
		                Object[] rowData = { codigoGenerado, nombreProducto, cantidad, precio};
		                tableModel.addRow(rowData);

		                limpiar();
		            }
		        } catch (SQLException ex) {
		            ex.printStackTrace();
		            JOptionPane.showMessageDialog(null, "Error al agregar datos a la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
		        }
		    }
		});

		btnNewButton_2.setBounds(219, 340, 120, 30);
		contentPane.add(btnNewButton_2);
		
		lbl_img = new JLabel("Elegir imagen");
		lbl_img.setToolTipText("");
		lbl_img.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_img.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser chooser = new JFileChooser("C:\\Users\\58414\\3D Objects\\Eclipse\\cobranzas_cg\\Utiles");
		        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.IMAGE", "jpg", "png", "gif");
		        chooser.addChoosableFileFilter(filter);
		        int result = chooser.showSaveDialog(null);
		        if (result == JFileChooser.APPROVE_OPTION) {
		            File selectedFile = chooser.getSelectedFile();
		            String path = selectedFile.getAbsolutePath();
		            lbl_img.setIcon(ResizeImage(path));
		            s = path;
		        }
		    }
		    
		    public ImageIcon ResizeImage (String imgpath) {
		        ImageIcon MyImage= new ImageIcon(imgpath);
		        Image imge = MyImage.getImage();
		        Image newImage = imge.getScaledInstance(lbl_img.getWidth(), lbl_img.getHeight(), Image.SCALE_SMOOTH);
		        ImageIcon image = new ImageIcon(newImage);
		        return image;
		    
			
			}
		});
		lbl_img.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		lbl_img.setBounds(450, 139, 170, 170);
		contentPane.add(lbl_img);
		
		JButton btnNewButton = new JButton("Eliminar útil");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int codigo = (int) table.getValueAt(selectedRow, 0);

                    try (Connection conexion = ConexionBDD.getConnection()) {
                        // Eliminar el registro de la tabla 'utiles' en la base de datos
                        String deleteQuery = "DELETE FROM utiles WHERE codigo=?";
                        PreparedStatement deleteStatement = conexion.prepareStatement(deleteQuery);
                        deleteStatement.setInt(1, codigo);
                        deleteStatement.executeUpdate();

                        // Eliminar la fila de la JTable
                        tableModel.removeRow(selectedRow);
                        
                        limpiar();
                        
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error al eliminar registro de la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione una fila para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
			}
		});
		btnNewButton.setBounds(358, 530, 120, 30);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_3 = new JButton("Modificar");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Obtener la fila seleccionada
           	 int selectedRow = table.getSelectedRow();

                if (selectedRow != -1) {
                    // Obtener los datos modificados de los JTextField
                    String nombreProducto = nombre.getText();
                    int cantidad = Integer.parseInt(stock.getText());
                    double precio = Double.parseDouble(txtPrecio.getText());

                    // Actualizar la fila seleccionada en la JTable
                    tableModel.setValueAt(nombreProducto, selectedRow, 1);
                    tableModel.setValueAt(cantidad, selectedRow, 2);
                    tableModel.setValueAt(precio, selectedRow, 3);

                    // Convertir la imagen en un array de bytes
                    byte[] imagenBytes = null;
                    if (s != null && !s.isEmpty()) {
                        imagenBytes = convertImageToBytes(s);
                    }

                    // Actualizar los datos en la base de datos
                    try (Connection conexion = ConexionBDD.getConnection()) {
                        int codigo = (int) tableModel.getValueAt(selectedRow, 0);

                        String query;
                        PreparedStatement preparedStatement;
                        if (imagenBytes != null) {
                            query = "UPDATE utiles SET nombre=?, stock=?, precio=?, imagen=? WHERE codigo=?";
                            preparedStatement = conexion.prepareStatement(query);
                            preparedStatement.setBytes(4, imagenBytes); // El índice del parámetro debe ser 4
                            preparedStatement.setInt(5, codigo); // Este índice se mantiene igual si hay una nueva imagen
                        } else {
                            query = "UPDATE utiles SET nombre=?, stock=?, precio=? WHERE codigo=?";
                            preparedStatement = conexion.prepareStatement(query);
                            preparedStatement.setInt(4, codigo); // Este índice se mantiene igual si no hay una nueva imagen
                        }
                        preparedStatement.setString(1, nombreProducto);
                        preparedStatement.setInt(2, cantidad);
                        preparedStatement.setDouble(3, precio);

                        preparedStatement.executeUpdate();

                        limpiar();

                        JOptionPane.showMessageDialog(null, "Datos actualizados correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error al actualizar datos en la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Selecciona una fila para modificar", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
			}
		});
		btnNewButton_3.setBounds(498, 340, 120, 30);
		contentPane.add(btnNewButton_3);
		
		JLabel lblNewLabel_3 = new JLabel("");
		lblNewLabel_3.setIcon(new ImageIcon(Utiles.class.getResource("/Img/pub (1).png")));
		lblNewLabel_3.setBounds(138, 31, 564, 610);
		contentPane.add(lblNewLabel_3);
	}
	
	public void limpiar() {
		nombre.setText(null);
		stock.setText(null);
		txtPrecio.setText(null);
		lbl_img.setText("Elegir imagen");
		lbl_img.setIcon(null);
	}
}
