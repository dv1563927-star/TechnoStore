package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final Conexion INSTANCIA = new Conexion();
    
    private final String url = "jdbc:mysql:http//localhost:3306/technostore";
    private final String usuario = "root";
    private final String password = "1097911972";
    
    private Conexion(){
    }
    
    public static Conexion getInstancia() {
        return INSTANCIA;
    }
    
    public Connection conxeion() throws SQLException {
        return DriverManager.getConnection(url, usuario, password);
    }
}