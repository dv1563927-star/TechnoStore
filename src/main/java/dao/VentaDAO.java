package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import modelo.Celular;
import modelo.Cliente;
import modelo.DetalleVenta;
import modelo.Marca;
import modelo.Usuarios;
import modelo.Venta;

public class VentaDAO {

    Conexion c = Conexion.getInstancia();

    public int crearVenta(int clienteId) {
        try (Connection con = c.conexion(); CallableStatement cs = con.prepareCall("{CALL crear_venta(?,?)}")) {
            cs.setInt(1, clienteId);
            cs.registerOutParameter(2, Types.INTEGER); //el id de la venta recien creada

            cs.executeUpdate();
            int idVenta = cs.getInt(2);
            System.out.println("Venta creada correctamente! (id: " + idVenta + ")");
            return idVenta;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    public boolean agregarItem(int ventaId, int celularId, int cantidad) {
        try (Connection con = c.conexion(); CallableStatement cs = con.prepareCall("{CALL agregar_item_venta(?,?,?)}")) {
            cs.setInt(1, ventaId);
            cs.setInt(2, celularId);
            cs.setInt(3, cantidad);

            cs.executeUpdate();
            System.out.println("Item agregado correctamente!");
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Todas las ventas de la tienda. Es el listado del administrador.
     */
    public List<Venta> listarVentas() {
        List<Venta> lista = new ArrayList<>();

        try (Connection con = c.conexion(); CallableStatement cs = con.prepareCall("{CALL listar_ventas()}")) {
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                lista.add(mapearVenta(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return lista;
    }

    /**
     * Solo las ventas del cliente indicado. Es el listado de "Ver mis compras".
     */
    public List<Venta> listarVentasPorCliente(int clienteId) {
        List<Venta> lista = new ArrayList<>();

        try (Connection con = c.conexion(); CallableStatement cs = con.prepareCall("{CALL listar_ventas_por_cliente(?)}")) {
            cs.setInt(1, clienteId);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                lista.add(mapearVenta(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return lista;
    }

    public List<DetalleVenta> listarDetalleVenta(int ventaId) {
        List<DetalleVenta> lista = new ArrayList<>();

        try (Connection con = c.conexion(); CallableStatement cs = con.prepareCall("{CALL listar_detalle_venta(?)}")) {
            cs.setInt(1, ventaId);

            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                //listar_detalle_venta devuelve modelo y marca del celular, construyo uno parcial
                Marca marca = new Marca(0, rs.getString("marca"));
                Celular celular = new Celular(0, marca, rs.getString("modelo"), "", Celular.gama.MEDIA, 0, 0);
                DetalleVenta detalle = new DetalleVenta(
                        rs.getInt("id"),
                        null, //la venta no la necesitamos adentro del detalle para mostrarlo
                        celular,
                        rs.getInt("cantidad"),
                        rs.getDouble("subtotal")
                );
                lista.add(detalle);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return lista;
    }

    /**
     * Los procedimientos de listado devuelven el nombre del cliente pero no
     * todos sus datos, asi que construyo un cliente parcial solo para mostrar.
     */
    private Venta mapearVenta(ResultSet rs) throws SQLException {
        Usuarios usuario = new Usuarios(0, "", "", Usuarios.Rol.CLIENTE);
        Cliente cliente = new Cliente(0, usuario, rs.getString("cliente"), "", "");
        return new Venta(
                rs.getInt("id"),
                cliente,
                rs.getTimestamp("fecha").toLocalDateTime(),
                rs.getDouble("total")
        );
    }

}
