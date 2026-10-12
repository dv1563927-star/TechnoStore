package utilidades;

import dao.Conexion;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Celular;
import modelo.Marca;

public class ReporteUtils {

    Conexion c = Conexion.getInstancia();

    public void reporteStockBajo() {
        System.out.println("""
                ==============================
                   CELULARES CON STOCK BAJO
                ==============================""");

        try (Connection con = c.conexion(); CallableStatement cs = con.prepareCall("{CALL reporte_stock_bajo()}")) {
            ResultSet rs = cs.executeQuery();
            List<Celular> lista = new ArrayList<>();

            while (rs.next()) {
                Marca marca = new Marca(0, rs.getString("marca"));
                lista.add(new Celular(rs.getInt("id"), marca, rs.getString("modelo"), "", Celular.gama.valueOf(rs.getString("gama")), 0, rs.getInt("stock")));
            }

            if (lista.isEmpty()) {
                System.out.println("No hay celulares con stock bajo");
                return;
            }

            //stream para mostrar solo los que tienen stock menor a 5
            lista.stream()
                    .filter(cel -> cel.getStock() < 5)
                    .forEach(cel -> System.out.println(
                    " " + cel.getMarca().getNombre() + " " + cel.getModelo()
                    + " | Stock: " + cel.getStock()
                    + " | Gama: " + cel.getGama()));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void reporteTop3Vendidos() {
        System.out.println("""
                ==============================
                     TOP 3 MÁS VENDIDOS
                ==============================""");

        try (Connection con = c.conexion(); CallableStatement cs = con.prepareCall("{CALL reporte_top3_vendidos()}")) {
            ResultSet rs = cs.executeQuery();
            List<String> lista = new ArrayList<>();

            while (rs.next()) {
                lista.add(rs.getString("marca") + " " + rs.getString("modelo")
                        + " | Unidades vendidas: " + rs.getInt("unidades_vendidas"));
            }

            if (lista.isEmpty()) {
                System.out.println("No hay ventas registradas");
                return;
            }

            //stream para mostrar el top 3 con su posicion
            int[] posicion = {1};
            lista.stream()
                    .limit(3)
                    .forEach(item -> System.out.println(" " + posicion[0]++ + ". " + item));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void reporteVentasMes() {
        System.out.println("""
                ==============================
                       VENTAS POR MES
                ==============================""");

        try (Connection con = c.conexion(); CallableStatement cs = con.prepareCall("{CALL reporte_ventas_por_mes()}")) {
            ResultSet rs = cs.executeQuery();
            List<String> lista = new ArrayList<>();

            while (rs.next()) {
                lista.add("Año: " + rs.getInt("año")
                        + " | Mes: " + rs.getInt("mes")
                        + " | Ventas: " + rs.getInt("numero_ventas")
                        + " | Total: $" + rs.getDouble("total"));
            }

            if (lista.isEmpty()) {
                System.out.println("No hay ventas registradas");
                return;
            }

            lista.stream().forEach(item -> System.out.println(" " + item));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
