package utilidades;

import dao.Conexion;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ArchivoUtils {

    Conexion c = Conexion.getInstancia();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    Path ruta = Path.of("reporte_ventas.dat");

    public void generarReporte() {
        try (Connection con = c.conexion();
             CallableStatement csVentas = con.prepareCall("{CALL listar_ventas()}");
             BufferedWriter escritor = Files.newBufferedWriter(ruta, StandardCharsets.UTF_8)) {

            ResultSet rsVentas = csVentas.executeQuery();

            escritor.write("========== REPORTE DE VENTAS TECHNOSTORE ==========");
            escritor.newLine();
            escritor.write("Generado: " + LocalDateTime.now().format(formatter));
            escritor.newLine();
            escritor.newLine();

            boolean hayVentas = false;

            while (rsVentas.next()) {
                hayVentas = true;
                int idVenta = rsVentas.getInt("id");
                String cliente = rsVentas.getString("cliente");
                String fecha = rsVentas.getTimestamp("fecha").toLocalDateTime().format(formatter);
                double total = rsVentas.getDouble("total");

                escritor.write("VENTA #" + idVenta);
                escritor.newLine();
                escritor.write("Cliente:  " + cliente);
                escritor.newLine();
                escritor.write("Fecha:    " + fecha);
                escritor.newLine();
                escritor.newLine();
                escritor.write("ARTICULOS:");
                escritor.newLine();

                //para cada venta busco su detalle
                try (CallableStatement csDetalle = con.prepareCall("{CALL listar_detalle_venta(?)}")) {
                    csDetalle.setInt(1, idVenta);
                    ResultSet rsDetalle = csDetalle.executeQuery();
                    while (rsDetalle.next()) {
                        escritor.write("  " + rsDetalle.getString("marca") + " " + rsDetalle.getString("modelo"));
                        escritor.newLine();
                        escritor.write("  Cantidad:  " + rsDetalle.getInt("cantidad"));
                        escritor.newLine();
                        escritor.write(String.format("  Subtotal:  $%,.0f", rsDetalle.getDouble("subtotal")));
                        escritor.newLine();
                        escritor.write("  ----------------------------------------");
                        escritor.newLine();
                    }
                }

                escritor.write(String.format("TOTAL: $%,.0f", total));
                escritor.newLine();
                escritor.write("==================================================");
                escritor.newLine();
                escritor.newLine();
            }

            if (!hayVentas) {
                escritor.write("No hay ventas registradas.");
                escritor.newLine();
            }

            System.out.println("Reporte generado correctamente!");
            System.out.println("Ubicacion: " + ruta.toAbsolutePath());

        } catch (SQLException | IOException e) {
            System.out.println("Error al generar el reporte: " + e.getMessage());
        }
    }

}
