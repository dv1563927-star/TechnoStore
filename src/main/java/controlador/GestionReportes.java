package controlador;

import utilidades.ArchivoUtils;
import utilidades.ReporteUtils;
import vista.MenuAdmin;

public class GestionReportes {

    MenuAdmin menuAdmin = new MenuAdmin();
    ReporteUtils reporteUtils = new ReporteUtils();
    ArchivoUtils archivoUtils = new ArchivoUtils();

    public void iniciar() {
        int opcion;
        do {
            menuAdmin.mostrarMenuReportes();
            opcion = menuAdmin.pedirOpcion();
            switch (opcion) {
                case 1 -> reporteUtils.reporteStockBajo();
                case 2 -> reporteUtils.reporteTop3Vendidos();
                case 3 -> reporteUtils.reporteVentasMes();
                case 4 -> archivoUtils.generarReporte();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción invalida");
            }
        } while (opcion != 0);
    }

}
