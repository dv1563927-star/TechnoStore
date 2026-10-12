package controlador;

import vista.MenuAdmin;

public class GestionReportes {

    MenuAdmin menuAdmin = new MenuAdmin();

    public void iniciar() {
        int opcion;
        do {
            menuAdmin.mostrarMenuReportes();
            opcion = menuAdmin.pedirOpcion();
            switch (opcion) {
                case 1 -> stockBajo();
                case 2 -> top3Vendidos();
                case 3 -> ventasPorMes();
                case 4 -> generarTxt();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción invalida");
            }
        } while (opcion != 0);
    }

    private void stockBajo() {
        //se implementa cuando este listo ReporteUtils
        System.out.println("Reporte de stock bajo proximamente...");
    }

    private void top3Vendidos() {
        //se implementa cuando este listo ReporteUtils
        System.out.println("Top 3 vendidos proximamente...");
    }

    private void ventasPorMes() {
        //se implementa cuando este listo ReporteUtils
        System.out.println("Ventas por mes proximamente...");
    }

    private void generarTxt() {
        //se implementa cuando este listo ArchivoUtils
        System.out.println("Generando reporte .txt proximamente...");
    }

}
