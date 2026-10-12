package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Types;
import modelo.Celular;
import modelo.Marca;

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
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void eliminarCelular(int id) {
        try (Connection con = c.conexion(); CallableStatement cs = con.prepareCall("{CALL eliminar_celular(?)}")) {
            cs.setInt(1, id);
            cs.executeUpdate();
            System.out.println("Celular eliminado correctamente!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Celular> listarCelulares() {
        List<Celular> lista = new ArrayList<>();

        try (Connection con = c.conexion(); CallableStatement cs = con.prepareCall("{CALL listar_celulares()}")) {
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                Marca marca = new Marca(rs.getInt("marca_id"), rs.getString("marca"));
                Celular celular = new Celular(
                        rs.getInt("id"),
                        marca,
                        rs.getString("modelo"),
                        rs.getString("sistema_operativo"),
                        Celular.gama.valueOf(rs.getString("gama")),
                        rs.getDouble("precio"),
                        rs.getInt("stock")
                );
                lista.add(celular);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return lista;
    }

    public Celular buscarCelular(int id) {
        try (Connection con = c.conexion(); CallableStatement cs = con.prepareCall("{CALL buscar_celular(?,?,?,?,?,?,?)}")) {
            cs.setInt(1, id);
            cs.registerOutParameter(2, Types.INTEGER); //este es marca_fk
            cs.registerOutParameter(3, Types.VARCHAR); //este es modelo
            cs.registerOutParameter(4, Types.VARCHAR); //este es el os
            cs.registerOutParameter(5, Types.VARCHAR); //este es gama
            cs.registerOutParameter(6, Types.DECIMAL); //este es el precio
            cs.registerOutParameter(7, Types.INTEGER); //este es el stock

            cs.execute();

            //con lo de la marca_fk q puse arribita recupero la marca completa
            MarcaDAO marcaDAO = new MarcaDAO();
            Marca marca = marcaDAO.buscar(cs.getInt(2));

            return new Celular(
                    id, marca, cs.getString(3), cs.getString(4), Celular.gama.valueOf(cs.getString(5)), cs.getDouble(6), cs.getInt(7)
            );
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

}
