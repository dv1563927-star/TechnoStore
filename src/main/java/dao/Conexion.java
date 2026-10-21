package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final Conexion INSTANCIA = new Conexion();
    
    private final String url = "jdbc:mysql://localhost:3306/technostore";
    private final String usuario = "campus2023";
    private final String password = "campus2023";
    
    private Conexion(){
    }
    
    public static Conexion getInstancia() {
        return INSTANCIA;
    }
    
    public Connection conexion() throws SQLException {
        return DriverManager.getConnection(url, usuario, password);
    }
}