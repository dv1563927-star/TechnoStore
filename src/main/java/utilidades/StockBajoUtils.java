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
import modelo.Celular.gama;

public class StockBajoUtils {

    Conexion c = Conexion.getInstancia();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    Path ruta = Path.of("reporte_stock.txt");

    public void generarReporteStockBajo() {
        try (Connection con = c.conexion();
             CallableStatement csStock = con.prepareCall("{CALL reporte_stock_txt()}");
             BufferedWriter escritor = Files.newBufferedWriter(ruta, StandardCharsets.UTF_8)) {

            ResultSet rsStock = csStock.executeQuery();

            escritor.write("========== REPORTE STOCK BAJO ==========");
            escritor.newLine();
            escritor.write("Generado: " + LocalDateTime.now().format(formatter));
            escritor.newLine();
            escritor.newLine();

            boolean stockBajo = false;

            while (rsStock.next()) {
                stockBajo = true;
                int id = rsStock.getInt("id");
                int Stock = rsStock.getInt("stock");
                String nombre = rsStock.getString("marca");
                String modelo = rsStock.getString("modelo");
                
                escritor.write("ID #" + id);
                escritor.newLine();
                escritor.write("Nombre celular:  " + nombre );
                escritor.newLine();
                escritor.write("Modelo: " + modelo);
                escritor.newLine();
                escritor.write("Stock:  " + Stock );
                escritor.newLine();

                escritor.write("==================================================");
                escritor.newLine();
                escritor.newLine();
            }

            if (!stockBajo) {
                escritor.write("No hay celulares con stock bajo");
                escritor.newLine();
            }

            System.out.println("Reporte generado correctamente!");
            System.out.println("Ubicacion: " + ruta.toAbsolutePath());

        } catch (SQLException | IOException e) {
            System.out.println("Error al generar el reporte: " + e.getMessage());
        }
    }

}
