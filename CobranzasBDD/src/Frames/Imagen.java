package Frames;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import MySQL.ConexionBDD;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.itextpdf.text.log.Logger;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Imagen extends JFrame {
	String filename = null;
	byte[] utiles_img = null;
	byte[] picture = null;
	private JTextField txtNombre;
	String s;
	
	Connection conn;
	PreparedStatement st;

	
	public static void main(String[] args) {
		ConexionBDD conex = new ConexionBDD();
		conex.conectar();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Imagen frame = new Imagen();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	
	public Imagen() {
		setMinimumSize(new Dimension(465, 630));
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(Imagen.class.getResource("/Img/icons8-montón-de-dinero-20.png")));
		setTitle("SchoolData");
		getContentPane().setBackground(new Color(0, 128, 192));
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(45, 88, 123));
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 545, 28);
		panel.add(menuBar);
		
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
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Útiles");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Utiles utiles = new Utiles();
            	utiles.setVisible(true);
            	utiles.setLocationRelativeTo(null);
            	dispose();
			}
		});
		menuBar.add(mntmNewMenuItem_1);
		
		JLabel lbl_img = new JLabel("");
		lbl_img.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		lbl_img.setBounds(99, 120, 235, 231);
		panel.add(lbl_img);
		
		JButton btn_img = new JButton("Elegir Imagen");
		btn_img.addActionListener(new ActionListener() {
		     public void actionPerformed(ActionEvent e) {
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

		btn_img.setBounds(99, 430, 235, 40);
		panel.add(btn_img);
		
		JButton btnSubir = new JButton("Subir Datos");
		btnSubir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nombre1 = txtNombre.getText();
				try {
					Class.forName("com.mysql.jdbc.Driver");
					conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cobranzascg", "root", "");
					st = conn.prepareStatement("INSERT INTO utiles_imagen(imagen, util_nombre) values (?, ?)");
					FileInputStream is = new FileInputStream(new File(s));
					st.setBlob(1, is);
					st.setString(2, nombre1);
					st.executeUpdate();
					JOptionPane.showMessageDialog(null, "Se ingresaron los datos");
					
					lbl_img.setIcon(null);
					txtNombre.setText(null);
					
				} catch (ClassNotFoundException e1) {
					//
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnSubir.setBounds(99, 490, 235, 40);
		panel.add(btnSubir);
		
		JLabel lblNewLabel = new JLabel("Nombre");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel.setBounds(99, 380, 69, 28);
		panel.add(lblNewLabel);
		
		txtNombre = new JTextField();
		txtNombre.setBounds(162, 380, 172, 30);
		panel.add(txtNombre);
		txtNombre.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon(Imagen.class.getResource("/Img/imgna.png")));
		lblNewLabel_1.setBounds(0, 28, 449, 530);
		panel.add(lblNewLabel_1);
	}
}
