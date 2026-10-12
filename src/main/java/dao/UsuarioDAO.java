package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import modelo.Usuarios;

public class UsuarioDAO {

    Conexion c = Conexion.getInstancia();

    public void crearUsuario(Usuarios usuario) {
        try (Connection con = c.conexion(); CallableStatement cs = con.prepareCall("{CALL registrar_usuario(?,?,?,?)}")) {
            cs.setString(1, usuario.getCorreo());
            cs.setString(2, usuario.getPassword());
            cs.setString(3, usuario.getRol().name());
            cs.registerOutParameter(4, Types.INTEGER);

            cs.executeUpdate();
            System.out.println("Usuario registrado correctamente! (id: " + cs.getInt(4) + ")");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
