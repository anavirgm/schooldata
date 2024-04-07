package Frames;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JLabel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import MySQL.ConexionBDD;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.border.LineBorder;
import java.awt.SystemColor;

public class Page extends JFrame {

	private static int numeroFactura = 1;
	private int doc = 1; 

			
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JMenuBar menuBar;
	private JLabel lblNewLabel_1;
	private JTextField txtCodigo;
	private JTextField txtPrecio;
	private JTable table;
	private static String nombreEstudiante;
	private int idEstudiante;
	
	private JComboBox cCantidad;
	private Utiles selectedutil;
	private JLabel iva;
	private JLabel subt;
	private JLabel total;
	private static Page instance;
	private JLabel lblStock;
	private JLabel lblNewLabel_2;
	
	private String nombreUtiles;
	private int cantidadUtiles;
	
	private List<Utiles> utilesList;
	private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private JComboBox<Utiles> cNombre;
	private int stockSeleccionado;
	private JTextField txtBuscar;
	private JTable table_1;
    private String contrasenaEstudiante;

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		ConexionBDD conex = new ConexionBDD();
		conex.conectar();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

                    
					Page frame = new Page(nombreEstudiante);
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	public Page(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;

		inicialize();
	    mostrarNombreEstudiante(); 
    }
	
	public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }
	
	private class Utiles{
		int codigo;
		String nombre;
		int stock;
		double precio;
		double importe;
		
		public Utiles(int codigo, String nombre, int stock, double precio, double importe) {
            this.codigo = codigo;
			this.nombre = nombre;
			this.stock = stock;
            this.precio = precio;
            this.importe = importe;
            
		}
		
		@Override
        public String toString() {
            return nombre;
        }
        
	}
	
	public void setCodigo(String codigo) {
        txtCodigo.setText(codigo);
    }

    public void setNombre(String nombre) {
        cNombre.setSelectedItem(findUtilesByName(nombre));
    }

    void setCantidad(String string) {
        if (cCantidad != null) {
            cCantidad.setSelectedItem(String.valueOf(string));
        } else {
            System.out.println("Error: cCantidad es nulo");
        }
    }
    public void setPrecio(String precio) {
        txtPrecio.setText(precio);
    }
	
	 
	 private String obtenerFechaHoraActual() {
	        LocalDateTime now = LocalDateTime.now();
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	        return now.format(formatter);
	    }
	 
	// buscar Utiles por nombre
			private Utiles findUtilesByName(String nombre) {
			    for (Utiles util : utilesList) {
			        if (util.nombre.equals(nombre)) {
			            return util;
			        }
			    }
			    return null;
			}
			
			
			public void setNombreEstudiante(String nombreEstudiante) {
		        this.nombreEstudiante = nombreEstudiante;
		    }		
			
			
	 private void cargarDatosUtiles() {
		    try (Connection conexion = ConexionBDD.getConnection()) {
		        String query = "SELECT codigo, nombre, stock, precio FROM utiles";
		        PreparedStatement statement = conexion.prepareStatement(query);
		        ResultSet resultSet = statement.executeQuery();

		        utilesList = new ArrayList<>();
		        DefaultComboBoxModel<Utiles> model = new DefaultComboBoxModel<>();

		        while (resultSet.next()) {
		            int codigo = resultSet.getInt("codigo");
		            String nombre = resultSet.getString("nombre");
		            int stock = resultSet.getInt("stock");
		            double precio = resultSet.getDouble("precio");

		            Utiles util = new Utiles(codigo, nombre, stock, precio, 0.0);
		            utilesList.add(util);
		            model.addElement(util);
		        }

		        cNombre.setModel(new DefaultComboBoxModel<>(utilesList.toArray(new Utiles[0])));

		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		}


	 private void buscarUtiles(String textoBusqueda) {
		    DefaultComboBoxModel<Utiles> model = new DefaultComboBoxModel<>();

		    try (Connection conexion = ConexionBDD.getConnection()) {
		        String query = "SELECT codigo, nombre, stock, precio FROM utiles WHERE LOWER(nombre) LIKE ? COLLATE utf8mb4_general_ci";
		        PreparedStatement statement = conexion.prepareStatement(query);
		        statement.setString(1, "%" + textoBusqueda.toLowerCase() + "%");
		        ResultSet resultSet = statement.executeQuery();

		        while (resultSet.next()) {
		            int codigo = resultSet.getInt("codigo");
		            String nombre = resultSet.getString("nombre");
		            int stock = resultSet.getInt("stock");
		            double precio = resultSet.getDouble("precio");

		            Utiles util = new Utiles(codigo, nombre, stock, precio, 0.0);
		            model.addElement(util);
		        }

		        if (model.getSize() > 0) {
		            cNombre.setModel(model);
		            cNombre.setSelectedIndex(0);
		            Utiles utilSeleccionado = (Utiles) cNombre.getSelectedItem();
		            llenarCampos(utilSeleccionado);
		        } else {
		            JOptionPane.showMessageDialog(this, "No se encontraron resultados para la búsqueda.", "Sin resultados",
		                    JOptionPane.INFORMATION_MESSAGE);
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		}



	 private void llenarCampos(Utiles util) {
		    txtCodigo.setText(String.valueOf(util.codigo));
		    cNombre.setSelectedItem(util);
		    cCantidad.setSelectedIndex(0); 
		    txtPrecio.setText(String.valueOf(util.precio));
		}
	 

		// actualizar los totales
	 private void actualizarTotales(DefaultTableModel model) {
		    double subtotal = 0;

		    for (int row = 0; row < model.getRowCount(); row++) {
		        // Verificar si el valor no es nulo antes de convertirlo a double
		        Object importeObject = model.getValueAt(row, 4);
		        if (importeObject != null) {
		            double importe = (double) importeObject;
		            subtotal += importe;
		        }
		    }

		    double ivaCalculado = subtotal * 0.16;
		    double totalCalculado = subtotal + ivaCalculado;

		    subt.setText(String.valueOf(subtotal));
		    iva.setText(String.valueOf(ivaCalculado));
		    total.setText(String.valueOf(totalCalculado));
		}

	 
	 private int obtenerIdEstudiante() {
	        int idEstudiante = -1;

	        try (Connection conexion = ConexionBDD.getConnection()) {
	            // Cambia la consulta para buscar el id utilizando el nombre del estudiante
	            String query = "SELECT id FROM estudiantes WHERE nombre = ?";
	            PreparedStatement statement = conexion.prepareStatement(query);
	            statement.setString(1, nombreEstudiante);

	            ResultSet resultSet = statement.executeQuery();

	            if (resultSet.next()) {
	                idEstudiante = resultSet.getInt("id");
	            }

	            statement.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return idEstudiante;
	    }


	 
	 
	 
	 
	 
	 private void generarFacturaPDF(String cedulaEstudiante) {
		    double subtotal = Double.parseDouble(subt.getText());
		    double ivaCalculado = Double.parseDouble(iva.getText());
		    double totalCalculado = Double.parseDouble(total.getText());
		    DefaultTableModel model = (DefaultTableModel) table.getModel();
		    String fechaHora = obtenerFechaHoraActual();

		    int idCompra = obtenerUltimoIdCompra();
		    
		    try {
		        String nombreArchivo = "factura_" + idCompra + ".pdf"; // Nombre de archivo con número de factura
		        Document document = new Document();
		        PdfWriter.getInstance(document, new FileOutputStream(nombreArchivo));
		        document.open();

		        // Configurar el formato del texto centrado
		        Paragraph title = new Paragraph("SCHOOLDATA", FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLD, BaseColor.BLACK));
		        title.setAlignment(Element.ALIGN_CENTER);
		        document.add(title);
		        
		        Paragraph address = new Paragraph("CALLE 76 ENTRE AV 12 Y 13\nMARACAIBO ZULIA ZONA POSTAL 4001", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD));
	            address.setAlignment(Element.ALIGN_CENTER);
	            document.add(address);
	            
	            document.add(new Paragraph("\n"));
	            
	            Paragraph datosEstudiante = new Paragraph(
	                    "Documento: 000" + idCompra + "\n" +
	                    "Estudiante: " + nombreEstudiante + "\n" +
	                    "Cédula: " + cedulaEstudiante + "\n", // Agregar la cédula del estudiante
	                    FontFactory.getFont(FontFactory.HELVETICA, 12)
	                );
	                datosEstudiante.setAlignment(Element.ALIGN_LEFT);
	                document.add(datosEstudiante);

	            Paragraph fact = new Paragraph("FACTURA", FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLD, BaseColor.BLACK));
		        fact.setAlignment(Element.ALIGN_CENTER);
		        document.add(fact);
		        
		        
		        document.add(new Paragraph("\n"));

		        
		        Paragraph fechaHoraParagraph = new Paragraph("FECHA Y HORA: " + fechaHora, FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, BaseColor.BLACK));
		        fechaHoraParagraph.setAlignment(Element.ALIGN_CENTER);
		        document.add(fechaHoraParagraph);

		        document.add(new Paragraph("\n"));
		        
		        document.add(new Paragraph("DETALLES DE LA COMPRA:", FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, BaseColor.BLACK)));
		        document.add(new Paragraph("\n"));
		        
		        PdfPTable detallesCompraTable = new PdfPTable(5);
		        detallesCompraTable.setWidthPercentage(100);
		        detallesCompraTable.setWidths(new int[]{2, 3, 2, 2, 2});

		        PdfPCell cellCodigo = new PdfPCell(new Phrase("Código"));
		        cellCodigo.setHorizontalAlignment(Element.ALIGN_CENTER);
		        detallesCompraTable.addCell(cellCodigo);

		        PdfPCell cellNombre = new PdfPCell(new Phrase("Nombre"));
		        cellNombre.setHorizontalAlignment(Element.ALIGN_CENTER);
		        detallesCompraTable.addCell(cellNombre);

		        PdfPCell cellCantidad = new PdfPCell(new Phrase("Cantidad"));
		        cellCantidad.setHorizontalAlignment(Element.ALIGN_CENTER);
		        detallesCompraTable.addCell(cellCantidad);

		        PdfPCell cellPrecioUnitario = new PdfPCell(new Phrase("Precio Unitario"));
		        cellPrecioUnitario.setHorizontalAlignment(Element.ALIGN_CENTER);
		        detallesCompraTable.addCell(cellPrecioUnitario);

		        PdfPCell cellImporte = new PdfPCell(new Phrase("Importe"));
		        cellImporte.setHorizontalAlignment(Element.ALIGN_CENTER);
		        detallesCompraTable.addCell(cellImporte);

		        // Iterar sobre las filas de la tabla y agregar los detalles de la compra al PDF
		        for (int i = 0; i < model.getRowCount(); i++) {
		            detallesCompraTable.addCell(model.getValueAt(i, 0).toString());
		            detallesCompraTable.addCell(model.getValueAt(i, 1).toString());
		            detallesCompraTable.addCell(model.getValueAt(i, 2).toString());
		            detallesCompraTable.addCell(model.getValueAt(i, 3).toString());
		            detallesCompraTable.addCell(model.getValueAt(i, 4).toString());
		        }

		        document.add(detallesCompraTable);

		        document.add(new Paragraph("\n"));

		        Paragraph subtotalParagraph = new Paragraph("SUBTOTAL: $" + subtotal, FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, BaseColor.BLACK));
		        subtotalParagraph.setAlignment(Element.ALIGN_RIGHT);
		        document.add(subtotalParagraph);

		        Paragraph ivaParagraph = new Paragraph("IVA: $" + ivaCalculado, FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, BaseColor.BLACK));
		        ivaParagraph.setAlignment(Element.ALIGN_RIGHT);
		        document.add(ivaParagraph);

		        Paragraph totalParagraph = new Paragraph("TOTAL: $" + totalCalculado, FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLD, BaseColor.BLACK));
		        totalParagraph.setAlignment(Element.ALIGN_RIGHT);
		        document.add(totalParagraph);

		        document.add(new Paragraph("\n"));
		        
		        Paragraph thk = new Paragraph("GRACIAS POR SU COMPRA \nVUELVA PRONTO", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD));
	            thk.setAlignment(Element.ALIGN_CENTER);
	            document.add(thk);
		        document.close();
		        
		        numeroFactura++;

		        if (Desktop.isDesktopSupported()) {
		            File pdfFile = new File(nombreArchivo);
		            if (pdfFile.exists()) {
		                Desktop.getDesktop().open(pdfFile);
		            } else {
		                JOptionPane.showMessageDialog(null, "No se pudo abrir el archivo PDF.");
		            }
		        }
		    } catch (DocumentException | IOException e) {
		        e.printStackTrace();
		        JOptionPane.showMessageDialog(null, "Error al generar la factura en PDF.");
		    }
		}




	/**
	 * Create the frame.
	 */
	public void inicialize() {
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(Registro.class.getResource("/Img/icons8-montón-de-dinero-20.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 792, 580);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
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
		
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Cambiar contraseña");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                mostrarDialogoCambioContrasena();
			}
		});
		menuBar.add(mntmNewMenuItem_1);
		
		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Imagen - Util Escolar");
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Img_utiles page = new Img_utiles();
            	page.setVisible(true);
            	page.setLocationRelativeTo(null);
            	dispose();
			}
		});
		menuBar.add(mntmNewMenuItem_2);
		
		
		
		contentPane = new JPanel();
		contentPane.setBackground(new Color(45, 88, 123));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnDte_1 = new JButton("");
		btnDte_1.setIcon(new ImageIcon(Page.class.getResource("/Img/icons8-escoba-30.png")));
		btnDte_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtCodigo.setText(null);
		        cNombre.setSelectedIndex(-1);
		        cCantidad.setSelectedIndex(-1);
		        txtPrecio.setText(null);
		        txtBuscar.setText(null);
		        
		        cargarDatosUtiles();
			}
		});
		
		JLabel lblNewLabel_3_1 = new JLabel("");
		lblNewLabel_3_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Img_utiles im = new Img_utiles();
				im.setLocationRelativeTo(null);
				im.setVisible(true);
				dispose();
			}
		});
		lblNewLabel_3_1.setIcon(new ImageIcon(Page.class.getResource("/Img/icons8-avanzar-30.png")));
		lblNewLabel_3_1.setBounds(710, 455, 30, 30);
		contentPane.add(lblNewLabel_3_1);
		btnDte_1.setBounds(218, 320, 45, 40);
		contentPane.add(btnDte_1);
		
		lblStock = new JLabel("");
		lblStock.setForeground(Color.RED);
		lblStock.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblStock.setHorizontalAlignment(SwingConstants.CENTER);
		lblStock.setBounds(370, 220, 40, 20);
		contentPane.add(lblStock);
		
		JButton button = new JButton("");
		button.setIcon(new ImageIcon(Page.class.getResource("/Img/lupa.png")));
		button.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        String textoBusqueda = txtBuscar.getText().trim().toLowerCase();
		        buscarUtiles(textoBusqueda);
		    }
		});

		// Reiniciar el campo cNombre y volver a cargar todos los productos
		cNombre = new JComboBox();
		cargarDatosUtiles();


		
		button.setBounds(595, 380, 45, 30); // 380
		contentPane.add(button);

		lblNewLabel_1 = new JLabel("Código");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_1.setBounds(95, 120, 57, 14);
		contentPane.add(lblNewLabel_1);

		txtCodigo = new JTextField();
		txtCodigo.setEditable(false);
		txtCodigo.setForeground(Color.BLACK);
		txtCodigo.setColumns(10);
		txtCodigo.setBounds(152, 113, 210, 30);
		contentPane.add(txtCodigo);
		
		cCantidad = new JComboBox();
		/*
		cCantidad.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	
		        Utiles utilSeleccionado = (Utiles) cNombre.getSelectedItem();
		        if (utilSeleccionado != null) {
		            int cantidad = Integer.parseInt(cCantidad.getSelectedItem().toString());
		            double subtotal = cantidad * utilSeleccionado.precio;
		            double iv = subtotal * 0.16;
		            double totalCalculado = subtotal + iv;

				    subt.setText(String.valueOf(subtotal));
				    iva.setText(String.valueOf(iv));
		            total.setText(String.valueOf(totalCalculado));
		            
		            
		        } 
		    }
		});
		*/

		cCantidad.setEditable(true);
		cCantidad.setBounds(152, 215, 210, 30);
		contentPane.add(cCantidad);
		
		txtPrecio = new JTextField();
		txtPrecio.setEditable(false);
		txtPrecio.setForeground(Color.BLACK);
		txtPrecio.setColumns(10);
		txtPrecio.setBounds(152, 265, 210, 30);
		contentPane.add(txtPrecio);
		
		JLabel lblNewLabel_1_1 = new JLabel("Nombre");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_1_1.setBounds(95, 170, 57, 14);
		contentPane.add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Cantidad");
		lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_1_1_1.setBounds(95, 225, 57, 14);
		contentPane.add(lblNewLabel_1_1_1);
		
		JLabel lblNewLabel_1_1_2 = new JLabel("Precio");
		lblNewLabel_1_1_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_1_1_2.setBounds(95, 275, 57, 14);
		contentPane.add(lblNewLabel_1_1_2);
		
		
		JButton btnDte = new JButton("");
		btnDte.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        // Obten la fila seleccionada
		        int selectedRow = table.getSelectedRow();

		        if (selectedRow != -1) {
		        	
		            DefaultTableModel model = (DefaultTableModel) table.getModel();
		            model.removeRow(selectedRow);

		            
		            actualizarTotales(model); 
		        
			        txtCodigo.setText(null);
			        cNombre.setSelectedIndex(-1);
			        cCantidad.setSelectedIndex(-1);
			        txtPrecio.setText(null);
		        } else {
		            JOptionPane.showMessageDialog(null, "Selecciona una fila para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
		        }
		    }
		});

		btnDte.setIcon(new ImageIcon(Page.class.getResource("/Img/eliminar.png")));
		btnDte.setBounds(280, 320, 45, 40);
		contentPane.add(btnDte);
		
		this.cNombre = new JComboBox();
		cNombre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Utiles selectedUtil = (Utiles) cNombre.getSelectedItem();

		        if (selectedUtil != null) {
		            txtCodigo.setText(String.valueOf(selectedUtil.codigo));
		            txtPrecio.setText(String.valueOf(selectedUtil.precio));
		            lblStock.setText(String.valueOf(selectedUtil.stock +"uds"));
		            
		            cCantidad.removeAllItems();
		            for (int i = 1; i <= selectedUtil.stock; i++) {
		                cCantidad.addItem(String.valueOf(i));
		            }

		        } else {
		        	lblStock.setText(null);
		        	cCantidad.setSelectedIndex(-1);
		        }
		        
			}
		});
		cargarDatosUtiles();
		cNombre.setBounds(152, 160, 210, 30);
		contentPane.add(cNombre);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(419, 111, 226, 188);
		contentPane.add(scrollPane);
		

		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        int selectedRow = table.getSelectedRow();

		        if (selectedRow != -1) {
		            txtCodigo.setText(table.getValueAt(selectedRow, 0).toString());
		            
		            String nombre = table.getValueAt(selectedRow, 1).toString();
		            Utiles selectedUtil = findUtilesByName(nombre);
		            cNombre.setSelectedItem(selectedUtil);
		            
		            cCantidad.setSelectedItem(table.getValueAt(selectedRow, 2).toString());
		            txtPrecio.setText(table.getValueAt(selectedRow, 3).toString());
		        }
		    }
		});

		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Codigo", "Producto", "Cantidad", "Precio", "Importe"
			}
		));
		
		scrollPane.setViewportView(table);
		
		
		
		JButton btnUpd = new JButton("");
		btnUpd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Utiles utilSeleccionado = (Utiles) cNombre.getSelectedItem();
		        int cantidad = Integer.parseInt(cCantidad.getSelectedItem().toString());
		        double importe = cantidad * utilSeleccionado.precio;
		        
		        int selectedRow = table.getSelectedRow();

		        
		        DefaultTableModel model = (DefaultTableModel) table.getModel();
		        model.setValueAt(utilSeleccionado.codigo, selectedRow, 0);
		        model.setValueAt(utilSeleccionado.nombre, selectedRow, 1); 
		        model.setValueAt(cantidad, selectedRow, 2); 
		        model.setValueAt(utilSeleccionado.precio, selectedRow, 3); 
		        model.setValueAt(importe, selectedRow, 4);

		        actualizarTotales(model);
		        
		        txtCodigo.setText(null);
		        cNombre.setSelectedIndex(-1);
		        cCantidad.setSelectedIndex(-1);
		        txtPrecio.setText(null);
			}
		});
		btnUpd.setIcon(new ImageIcon(Page.class.getResource("/Img/actualizar.png")));
		btnUpd.setBounds(155, 320, 45, 40);
		contentPane.add(btnUpd);
		
		
		
		
		JButton btnAdd = new JButton("");
		btnAdd.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        Utiles utilSeleccionado = (Utiles) cNombre.getSelectedItem();
		        int cantidad = Integer.parseInt(cCantidad.getSelectedItem().toString());

		        // Verificar si hay suficiente stock
		        if (cantidad > utilSeleccionado.stock) {
		            JOptionPane.showMessageDialog(Page.this, "No hay suficiente stock disponible", "Error", JOptionPane.ERROR_MESSAGE);
		            return;
		        }

		        // Calcular importe y actualizar stock
		        double importe = cantidad * utilSeleccionado.precio;
		        utilSeleccionado.stock -= cantidad;

		        // Agregar fila a la tabla
		        DefaultTableModel model = (DefaultTableModel) table.getModel();
		        model.addRow(new Object[]{utilSeleccionado.codigo, utilSeleccionado.nombre, cantidad, utilSeleccionado.precio, importe});

		        // Actualizar subtotal, iva y total
		        actualizarTotales(model);
		        

		        txtCodigo.setText(null);
		        cNombre.setSelectedIndex(-1);
		        cCantidad.setSelectedIndex(-1);
		        txtPrecio.setText(null);
		    }
		});


		btnAdd.setIcon(new ImageIcon(Page.class.getResource("/Img/icons8-añadir-30.png")));
		btnAdd.setBounds(95, 320, 45, 40);
		contentPane.add(btnAdd);
		
		
		JButton btnNewButton_1 = new JButton("Comprar");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				guardarCompraEnBaseDeDatos(nombreEstudiante);
			}
		});
		btnNewButton_1.setIcon(new ImageIcon(Page.class.getResource("/Img/comprar.png")));
		btnNewButton_1.setBounds(320, 455, 108, 30);
		contentPane.add(btnNewButton_1);
		

		total = new JLabel("");
		total.setHorizontalAlignment(SwingConstants.RIGHT);
		total.setFont(new Font("Tahoma", Font.BOLD, 12));
		total.setBounds(595, 351, 46, 14);
		contentPane.add(total);
		

		iva = new JLabel("");
		iva.setHorizontalAlignment(SwingConstants.RIGHT);
		iva.setFont(new Font("Tahoma", Font.PLAIN, 12));
		iva.setBounds(595, 325, 46, 14);
		contentPane.add(iva);
		
		JLabel lblNewLabel_4 = new JLabel("IVA");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_4.setBounds(550, 325, 28, 14);
		contentPane.add(lblNewLabel_4);
		
		JLabel lblNewLabel_5 = new JLabel("TOTAL");
		lblNewLabel_5.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_5.setBounds(550, 351, 46, 14);
		contentPane.add(lblNewLabel_5);
		
		JLabel lblNewLabel_6 = new JLabel("Subtotal");
		lblNewLabel_6.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_6.setBounds(420, 325, 57, 14);
		contentPane.add(lblNewLabel_6);
		
		lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_2.setForeground(new Color(255, 255, 255));
		lblNewLabel_2.setBounds(77, 60, 178, 30);
		contentPane.add(lblNewLabel_2);
		
		subt = new JLabel("");
		subt.setFont(new Font("Tahoma", Font.PLAIN, 12));
		subt.setBounds(480, 325, 38, 14);
		contentPane.add(subt);
		

		txtBuscar = new JTextField();
		txtBuscar.setBounds(95, 380, 480, 30); //170
		contentPane.add(txtBuscar);
		txtBuscar.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setIcon(new ImageIcon(Page.class.getResource("/Img/productos.png")));
		lblNewLabel.setBounds(0, 0, 698, 480);
		contentPane.add(lblNewLabel);
	}
	
	
	private void mostrarNombreEstudiante() {
		
		lblNewLabel_2.setText("¡Bienvenido " + nombreEstudiante + "!");
		
    }
	
	private void guardarCompraEnBaseDeDatos(String nombreEstudiante) {
	    double totalCompra = Double.parseDouble(total.getText());
	    int idEstudiante = obtenerIdEstudiante();
	    String cedulaEstudiante = obtenerCedulaEstudiante(nombreEstudiante);

	    if (idEstudiante != -1) {
	        // Confirmación
	        int opcion = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea realizar la compra?", "Confirmar Compra", JOptionPane.YES_NO_OPTION);
	        if (opcion == JOptionPane.YES_OPTION) {
	            try (Connection conexion = ConexionBDD.getConnection()) {
	                String insertCompraQuery = "INSERT INTO ventas (id_estudiante, monto_total, fecha_pago) VALUES (?, ?, ?)";
	                try (PreparedStatement insertCompraStatement = conexion.prepareStatement(insertCompraQuery, Statement.RETURN_GENERATED_KEYS)) {
	                    insertCompraStatement.setInt(1, idEstudiante);
	                    insertCompraStatement.setDouble(2, totalCompra);
	                    insertCompraStatement.setString(3, obtenerFechaHoraActual());

	                    int affectedRows = insertCompraStatement.executeUpdate();

	                    if (affectedRows > 0) {
	                        ResultSet generatedKeys = insertCompraStatement.getGeneratedKeys();
	                        int idCompra = -1;
	                        if (generatedKeys.next()) {
	                            idCompra = generatedKeys.getInt(1);
	                        }

	                        DefaultTableModel model = (DefaultTableModel) table.getModel();
	                        for (int i = 0; i < model.getRowCount(); i++) {
	                            int codigoUtil = Integer.parseInt(model.getValueAt(i, 0).toString());
	                            int cantidad = Integer.parseInt(model.getValueAt(i, 2).toString());
	                            double precio = Double.parseDouble(model.getValueAt(i, 3).toString());

	                            // Calcular el importe
	                            double importe = cantidad * precio;

	                            // Insertar en la tabla utiles_comprados
	                            String insertUtilesCompradosQuery = "INSERT INTO utiles_comprados (codigo_util, cantidad) VALUES (?, ?)";
	                            try (PreparedStatement insertUtilesCompradosStatement = conexion.prepareStatement(insertUtilesCompradosQuery)) {
	                                insertUtilesCompradosStatement.setInt(1, codigoUtil);
	                                insertUtilesCompradosStatement.setInt(2, cantidad);

	                                insertUtilesCompradosStatement.executeUpdate();
	                            }
	                        }

	                        JOptionPane.showMessageDialog(null, "Compra guardada correctamente en la base de datos.");
	                        generarFacturaPDF(cedulaEstudiante);
	                    } else {
	                        JOptionPane.showMessageDialog(null, "No se pudo guardar la compra.", "Error", JOptionPane.ERROR_MESSAGE);
	                    }
	                }
	            } catch (SQLException e) {
	                e.printStackTrace();
	                JOptionPane.showMessageDialog(null, "Error al guardar la compra en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
	            }
	        }
	    } else {
	        JOptionPane.showMessageDialog(null, "El estudiante no existe en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}


	
	private List<String> obtenerNombresUtilDesdeTabla() {
	    List<String> nombresUtil = new ArrayList<>();

	    // Itera sobre todas las filas de la tabla
	    for (int i = 0; i < table.getRowCount(); i++) {
	        // Obtiene el nombre del útil de la columna 1 (indexada en 0) de la fila actual
	        String nombreUtil = table.getValueAt(i, 1).toString();
	        // Agrega el nombre del útil a la lista
	        nombresUtil.add(nombreUtil);
	    }

	    return nombresUtil;
	}

	
	private String obtenerCedulaEstudiante(String nombreEstudiante) {
	    String cedula = null;

	    try (Connection conexion = ConexionBDD.getConnection()) {
	        String query = "SELECT cedula FROM estudiantes WHERE nombre = ?";
	        PreparedStatement statement = conexion.prepareStatement(query);
	        statement.setString(1, nombreEstudiante);
	        ResultSet resultSet = statement.executeQuery();

	        if (resultSet.next()) {
	            cedula = resultSet.getString("cedula");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return cedula;
	}

	
	private int obtenerUltimoIdCompra() {
	    int ultimoIdCompra = 0;

	    try (Connection conexion = ConexionBDD.getConnection()) {
	        String query = "SELECT MAX(id) AS ultimo_id FROM ventas";
	        PreparedStatement statement = conexion.prepareStatement(query);
	        ResultSet resultSet = statement.executeQuery();

	        if (resultSet.next()) {
	            ultimoIdCompra = resultSet.getInt("ultimo_id");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(null, "Error al obtener el último ID de compra desde la base de datos.");
	    }

	    return ultimoIdCompra;
	}


	private String obtenerContrasenaEstudiante(String nombreEstudiante) {
        String contrasena = null;

        try (Connection conexion = ConexionBDD.getConnection()) {
            String query = "SELECT contraseña FROM estudiantes WHERE nombre = ?";
            PreparedStatement statement = conexion.prepareStatement(query);
            statement.setString(1, nombreEstudiante);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                contrasena = resultSet.getString("contraseña");
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contrasena;
    }
	
	private void actualizarContrasenaEstudiante(String nuevaContrasena) {
        try (Connection conexion = ConexionBDD.getConnection()) {
            String query = "UPDATE estudiantes SET contraseña = ? WHERE nombre = ?";
            PreparedStatement statement = conexion.prepareStatement(query);
            statement.setString(1, nuevaContrasena);
            statement.setString(2, nombreEstudiante);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar la contraseña en la base de datos.");
        }
    }

	
	private void mostrarDialogoCambioContrasena() {
	    JPasswordField currentPassword = new JPasswordField(20);
	    JPasswordField newPassword = new JPasswordField(20);
	    JPasswordField confirmPassword = new JPasswordField(20);

	    while (true) {
	        Object[] message = {
	            "Contraseña actual:", currentPassword,
	            "Nueva contraseña:", newPassword,
	            "Confirmar nueva contraseña:", confirmPassword
	        };

	        int option = JOptionPane.showConfirmDialog(
	            this,
	            message,
	            "Cambiar Contraseña",
	            JOptionPane.OK_CANCEL_OPTION
	        );

	        if (option == JOptionPane.OK_OPTION) {
	            char[] currentPass = currentPassword.getPassword();
	            char[] newPass = newPassword.getPassword();
	            char[] confirmPass = confirmPassword.getPassword();

	            String currentPassStr = new String(currentPass);
	            String newPassStr = new String(newPass);
	            String confirmPassStr = new String(confirmPass);

	            String storedPassword = obtenerContrasenaEstudiante(nombreEstudiante);

	            if (!currentPassStr.equals(storedPassword)) {
	                JOptionPane.showMessageDialog(this, "Contraseña actual incorrecta.");
	            } else if (!newPassStr.equals(confirmPassStr)) {
	                JOptionPane.showMessageDialog(this, "Las contraseñas nuevas no coinciden.");
	            } else {
	                actualizarContrasenaEstudiante(newPassStr);
	                JOptionPane.showMessageDialog(this, "Contraseña cambiada correctamente.");
	                break;
	            }
	        } else {
	            break;
	        }
	    }
	}

	
	public Page() {
		setTitle("SchoolData");
		
        contrasenaEstudiante = obtenerContrasenaEstudiante(nombreEstudiante);

		inicialize();
	}
}