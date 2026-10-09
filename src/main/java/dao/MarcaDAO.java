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
        try (Connection con = c.conexion(); CallableStatement cs = con.prepareCall("{CALL registrar_marcas(?, ?)}")) {

            cs.setString(1, marca.getNombre());
            cs.registerOutParameter(2, Types.INTEGER);
            cs.executeUpdate();
            System.out.println("Marca creada correctamente! (id: " + cs.getInt(2) + ")");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void actualizarMarca(Marca marca) {

        try (Connection con = c.conexion(); CallableStatement cs = con.prepareCall("{CALL actualizar_marca(?, ?)")) {
            cs.setInt(1, marca.getId());
            cs.setString(2, marca.getNombre());
            cs.executeUpdate();
            System.out.println("Marca actualizada correctamente!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void eliminarMarca(Marca marca) {
        try (Connection con = c.conexion(); CallableStatement cs = con.prepareCall("{CALL eliminar_marca(?)}")) {
            cs.setInt(1, marca.getId());
            cs.executeUpdate();
            System.out.println("Marca eliminada correctamente!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Marca> obtenerMarcas() {
        List<Marca> listaMarcas = new ArrayList<>();

        try (Connection con = c.conexion(); CallableStatement cs = con.prepareCall("{CALL listar_marcas()")) {
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                listaMarcas.add(new Marca(rs.getInt("id"), rs.getString("nombre")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return listaMarcas;
    }
    
    public Marca buscar(int id) {
        try (Connection con = c.conexion(); CallableStatement cs = con.prepareCall("{listar_marcas()}")) {
            ResultSet rs = cs.executeQuery();
            while (rs.next()){
                if(rs.getInt("id") == id) {
                    return new Marca(rs.getInt("id"), rs.getString("nombre"));
                }
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
