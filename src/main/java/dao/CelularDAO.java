package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Types;
import modelo.Celular;

public class CelularDAO {

    Conexion c = Conexion.getInstancia();

    public void crearCelular(Celular celular) {

        try (Connection con = c.conexion(); CallableStatement cs = con.prepareCall("{CALL registrar_celular(?, ?, ?, ?, ?, ?, ?)}")) {
            cs.setInt(1, celular.getMarca().getId());
            cs.setString(2, celular.getModelo());
            cs.setString(3, celular.getSistemaOperativo());
            cs.setString(4, celular.getGama().name());
            cs.setDouble(5, celular.getPrecio());
            cs.setInt(6, celular.getStock());

            cs.registerOutParameter(7, Types.INTEGER);

            cs.executeUpdate();
            System.out.println("Celular registrado correctamente!: (id" + cs.getInt(7) + ")");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void actualizarCelular(Celular celular) {

        try (Connection con = c.conexion(); CallableStatement cs = con.prepareCall("{CALL actualizar_celular(?,?,?,?,?,?,?)}")) {
            cs.setInt(1, celular.getId());
            cs.setInt(2, celular.getMarca().getId());
            cs.setString(3, celular.getModelo());
            cs.setString(4, celular.getSistemaOperativo());
            cs.setString(5, celular.getGama().name());
            cs.setDouble(6, celular.getPrecio());
            cs.setInt(7, celular.getStock());
            cs.executeUpdate();
            
            System.out.println("Celular actualizado correctamente");
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void eliminarCelular(Celular celular) {
        try (Connection con = c.conexion(); CallableStatement cs = con.prepareCall({CALL eliminar_celular(?)})) {
        
    }
    }

}
