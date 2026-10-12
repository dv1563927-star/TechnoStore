package vista;

import java.util.List;
import modelo.Celular;
import modelo.DetalleVenta;
import modelo.Venta;

public class MenuCliente {

    Validaciones v = new Validaciones();

    public void mostrarMenu() {
        System.out.println("""
                ==============================
                  TECHNOSTORE - BIENVENIDO
                ==============================
                 1. Ver catálogo
                 2. Realizar compra
                 3. Ver mis compras
                 0. Cerrar sesión
                ==============================""");
    }

    public void mostrarCatalogo(List<Celular> celulares) {
        if (celulares.isEmpty()) {
            System.out.println("No hay celulares disponibles");
            return;
        }
        celulares.forEach(c -> System.out.println(c));
    }

    public void mostrarMisCompras(List<Venta> ventas) {
        if (ventas.isEmpty()) {
            System.out.println("No tienes compras registradas");
            return;
        }
        ventas.forEach(v -> System.out.println(v));
    }

    public void mostrarDetalleCompra(List<DetalleVenta> detalles) {
        if (detalles.isEmpty()) {
            System.out.println("No hay detalles para esta compra");
            return;
        }
        detalles.forEach(d -> System.out.println(d));
    }

    public int pedirOpcion() {
        return v.validarEntero("Seleccione una opción: ");
    }

    public int pedirCelularId() {
        return v.validarEntero("Ingrese el id del celular: ");
    }

    public int pedirCantidad() {
        return v.validarEnteroPositivo("Cantidad: ");
    }

}
