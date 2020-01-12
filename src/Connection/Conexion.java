/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;

import ModelClass.ListClass;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author avice
 */
public class Conexion extends ListClass{
    private String db = "system_ventas";
    private String user = "root";
    private String password = "";
    private String url = "jdbc:mysql://localhost/" + db + "?SslMode=none";
    private Connection conn = null;
    
    public Conexion() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(this.url, this.user, this.password);
            if (conn != null) {
                System.out.println("Conexion a la base de datos " + this.db +".... Listo ");
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error: " + e);
        } 
    }
    
    public Connection getConn() {
        return conn;
    }
}
