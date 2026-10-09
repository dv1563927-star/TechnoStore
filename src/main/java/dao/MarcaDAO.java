package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Types;
import modelo.Marca;

public class MarcaDAO {

    Conexion c = Conexion.getInstancia();

    public void crearMarca(Marca marca) {
        try (Connection con = c.conxeion(); CallableStatement cs = con.prepareCall("{CALL registrar_marcas(?, ?)}")) {

            cs.setString(1, marca.getNombre());
            cs.registerOutParameter(2, Types.INTEGER);
            cs.executeUpdate();
            System.out.println("Marca creada correctamente! (id: " + cs.getInt(2) + ")");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        

    }
