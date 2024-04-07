package MySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class ConexionBDD {
    public static Connection conex;
    // Declaramos los datos de conexion a la bd
    private static final String driver="com.mysql.jdbc.Driver";
    private static final String user="root";
    private static final String pass="";
    private static final String url="jdbc:mysql://localhost:3306/cobranzascg";
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }
    // Funcion que va conectarse a mi bd de mysql
    public Connection conectar(){
      conex = null;
      try{
          conex = (Connection) DriverManager.getConnection(url, user, pass);
          if(conex!=null){
          }
      }
      catch(SQLException e)
      {
          JOptionPane.showMessageDialog(null,"Error" + e.toString());
      }
      return conex;
    }
}
