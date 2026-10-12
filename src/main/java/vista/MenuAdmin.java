package vista;

public class MenuAdmin {

    Validaciones v = new Validaciones();

    public void mostrarMenu() {
        System.out.println("""
                ==============================
                     TECHNOSTORE - ADMIN
                ==============================
                 1. Gestión de celulares
                 2. Gestión de marcas
                 3. Gestión de clientes
                 4. Ventas
                 5. Reportes
                 0. Cerrar sesión
                ==============================""");
    }

    public void mostrarMenuVentas() {
        System.out.println("""
                ==============================
                          VENTAS
                ==============================
                 1. Nueva venta
                 2. Ver todas las ventas
                 0. Volver
                ==============================""");
    }

    public void mostrarMenuReportes() {
        System.out.println("""
                ==============================
                         REPORTES
                ==============================
                 1. Celulares con stock bajo
                 2. Top 3 más vendidos
                 3. Ventas por mes
                 4. Generar reporte .txt
                 0. Volver
                ==============================""");
    }

    public int pedirOpcion() {
        return v.validarEntero("Seleccione una opción: ");
    }

}
