package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import modelo.Cliente;
import modelo.Usuarios;

public class ClienteDAO {

    Conexion c = Conexion.getInstancia();

    public int crearCliente(Connection con, Cliente cliente) {
        //un cliente sin usuario asociado no se puede registrar: el procedimiento
        //necesita el usuario_fk, y sin esta comprobacion se producia un
        //NullPointerException al intentar leer el id del usuario
        if (cliente == null || cliente.getUsuario() == null) {
            System.out.println("No se pudo registrar el cliente: falta el usuario asociado");
            return -1;
        }

        try (CallableStatement cs = con.prepareCall("{CALL registrar_cliente(?,?,?,?,?)}")) {
            cs.setInt(1, cliente.getUsuario().getId());
            cs.setString(2, cliente.getNombre());
            cs.setString(3, cliente.getIdentificacion());
            cs.setString(4, cliente.getTelefono());
            cs.registerOutParameter(5, Types.INTEGER);

            cs.executeUpdate();
            int id = cs.getInt(5);
            System.out.println("Cliente registrado correctamente! (id: " + id + ")");
            return id;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }

    }

    public void actualizarCliente(Cliente cliente) {
        try (Connection con = c.conexion(); CallableStatement cs = con.prepareCall("{CALL actualizar_cliente(?,?,?,?)}")) {
            cs.setInt(1, cliente.getId());
            cs.setString(2, cliente.getNombre());
            cs.setString(3, cliente.getIdentificacion());
            cs.setString(4, cliente.getTelefono());
            cs.executeUpdate();
            System.out.println("Cliente actualizado correctamente!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void eliminarCliente(int id) {
        try (Connection con = c.conexion(); CallableStatement cs = con.prepareCall("{CALL eliminar_cliente(?)}")) {
            cs.setInt(1, id);
            cs.executeUpdate();
            System.out.println("Cliente eliminado correctamente");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Cliente> listarClientes() {
        List<Cliente> lista = new ArrayList<>();

        try (Connection con = c.conexion(); CallableStatement cs = con.prepareCall("{CALL listar_clientes()}")) {
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                //El procedure de listar los clientes devuelve el correo del usuario pero no todos los datos, entonces construyo un usuario solo con el correo pa mostrar
                Usuarios usuario = new Usuarios(0, rs.getString("correo"), "", Usuarios.Rol.CLIENTE);
                Cliente cliente = new Cliente(
                        rs.getInt("id"),
                        usuario,
                        rs.getString("nombre"),
                        rs.getString("identificacion"),
                        rs.getString("telefono")
                );
                lista.add(cliente);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return lista;
    }
    
    public Cliente buscarCliente(int id) {
        try(Connection con = c.conexion(); CallableStatement cs = con.prepareCall("{CALL buscar_cliente(?,?,?,?,?)}")) {
            cs.setInt(1, id);
            cs.registerOutParameter(2, Types.INTEGER); //el usuario_fk
            cs.registerOutParameter(3, Types.VARCHAR); //el nombre
            cs.registerOutParameter(4, Types.VARCHAR); //la identificacion
            cs.registerOutParameter(5, Types.VARCHAR); //el telefono
            
            cs.execute();
            
            //recupero el usuario completo con el id
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            Usuarios usuario = usuarioDAO.buscarPorId(cs.getInt(2));
            
            return new Cliente(id, usuario, cs.getString(3), cs.getString(4), cs.getString(5));
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    
}
