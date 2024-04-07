package Frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.SwingConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Img_utiles extends JFrame {

    private JTextField name;
    private JLabel img1;
    private Connection conn;

    public class func {
    	public ResultSet find(String s) {
    	    ResultSet rs = null;
    	    try {
    	        Class.forName("com.mysql.jdbc.Driver");
    	        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cobranzascg", "root", "");
    	        PreparedStatement st = conn.prepareStatement("SELECT * FROM utiles WHERE nombre LIKE ?");
    	        st.setString(1, "%" + s + "%");
    	        rs = st.executeQuery();
    	    } catch (ClassNotFoundException | SQLException e) {
    	        e.printStackTrace();
    	    }
    	    return rs;
    	}

    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Img_utiles frame = new Img_utiles();
                    frame.setVisible(true);
                    frame.setLocationRelativeTo(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Img_utiles() {
        setMinimumSize(new Dimension(465, 620));
        setResizable(false);
        setIconImage(Toolkit.getDefaultToolkit().getImage(Img_utiles.class.getResource("/Img/icons8-montón-de-dinero-20.png")));
        setTitle("SchoolData");
        getContentPane().setBackground(new Color(0, 128, 192));

        JPanel panel = new JPanel();
        panel.setBackground(new Color(45, 88, 123));
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(null);
        
        JLabel lblNewLabel_3 = new JLabel("");
        lblNewLabel_3.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		Page pg = new Page();
        		pg.setLocationRelativeTo(null);
        		pg.setVisible(true);
        		dispose();
        	}
        });
        lblNewLabel_3.setIcon(new ImageIcon(Img_utiles.class.getResource("/Img/icons8-retroceder-30.png")));
        lblNewLabel_3.setBounds(53, 530, 30, 30);
        panel.add(lblNewLabel_3);

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

        JMenuItem mntmNewMenuItem_1 = new JMenuItem("Productos");
        mntmNewMenuItem_1.setHorizontalAlignment(SwingConstants.CENTER);
        mntmNewMenuItem_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Page page = new Page();
                page.setVisible(true);
                page.setLocationRelativeTo(null);
                dispose();
            }
        });
        menuBar.add(mntmNewMenuItem_1);

        img1 = new JLabel("");
        img1.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
        img1.setBounds(99, 120, 235, 231);
        panel.add(img1);

        JButton btnSubir = new JButton("Mostrar Imagen");
        btnSubir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                func f = new func();
                ResultSet rs = f.find(name.getText());
                try {
                    if (rs != null && rs.next()) {
                        byte[] img = rs.getBytes("imagen");
                        ImageIcon image = new ImageIcon(img);
                        Image im = image.getImage();
                        Image myimg = im.getScaledInstance(img1.getWidth(), img1.getHeight(), Image.SCALE_SMOOTH);
                        ImageIcon newImage = new ImageIcon(myimg);
                        img1.setIcon(newImage);
                    } else {
                        JOptionPane.showMessageDialog(null, "No se encontró ningún util con ese nombre.", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } finally {
                    try {
                        if (rs != null) rs.close();
                        if (conn != null) conn.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        btnSubir.setBounds(99, 450, 235, 40);
        panel.add(btnSubir);

        JLabel lblNewLabel = new JLabel("Introducir Util:");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblNewLabel.setBounds(99, 370, 158, 28);
        panel.add(lblNewLabel);

        name = new JTextField();
        name.setBounds(99, 400, 235, 30);
        panel.add(name);
        name.setColumns(10);
        
        JLabel lblNewLabel_1 = new JLabel("");
        lblNewLabel_1.setIcon(new ImageIcon(Img_utiles.class.getResource("/Img/buscar.png")));
        lblNewLabel_1.setBounds(0, 28, 450, 530);
        panel.add(lblNewLabel_1);
    }
}
