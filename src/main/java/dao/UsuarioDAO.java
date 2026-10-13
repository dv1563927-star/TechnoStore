package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import modelo.Usuarios;

public class UsuarioDAO {

    Conexion c = Conexion.getInstancia();

    /**
     * Registra el usuario y devuelve el id generado por la base de datos.
     * Devuelve -1 si el registro fue rechazado (por ejemplo, correo con
     * formato invalido), de modo que quien llama pueda detectar el fallo en
     * lugar de continuar con un usuario inexistente.
     */
    public int crearUsuario(Connection con, Usuarios usuario) {
        try (CallableStatement cs = con.prepareCall("{CALL registrar_usuario(?,?,?,?)}")) {
            cs.setString(1, usuario.getCorreo());
            cs.setString(2, usuario.getPassword());
            cs.setString(3, usuario.getRol().name());
            cs.registerOutParameter(4, Types.INTEGER);

            cs.executeUpdate();
            int id = cs.getInt(4);
            System.out.println("Usuario registrado correctamente! (id: " + id + ")");
            return id;
        } catch (SQLException e) {
            System.out.println("No se pudo registrar el usuario: " + e.getMessage());
            return -1;
        }
    }

    public Usuarios buscarPorCorreo(String correo) {
        try (Connection con = c.conexion(); CallableStatement cs = con.prepareCall("{CALL buscar_usuario_por_correo(?)}")) {
            cs.setString(1, correo);

            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                return new Usuarios(
                        rs.getInt("id"),
                        rs.getString("correo"),
                        rs.getString("password"),
                        Usuarios.Rol.valueOf(rs.getString("rol"))
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Usuarios buscarPorId(int id) {
        try (Connection con = c.conexion(); CallableStatement cs = con.prepareCall("{CALL buscar_usuario_por_id(?)}")) {
            cs.setInt(1, id);

            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                return new Usuarios(
                        rs.getInt("id"),
                        rs.getString("correo"),
                        rs.getString("password"),
                        Usuarios.Rol.valueOf(rs.getString("rol"))
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
