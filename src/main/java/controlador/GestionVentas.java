package controlador;

import dao.CelularDAO;
import dao.VentaDAO;
import modelo.Cliente;
import vista.MenuAdmin;
import vista.MenuCliente;

public class GestionVentas {

    VentaDAO ventaDAO = new VentaDAO();
    CelularDAO celularDAO = new CelularDAO();
    MenuAdmin menuAdmin = new MenuAdmin();

    public void iniciarAdmin() {
        int opcion;
        do {
            menuAdmin.mostrarMenuVentas();
            opcion = menuAdmin.pedirOpcion();
            switch (opcion) {
                case 1 -> verTodasLasVentas();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción invalida");
            }
        } while (opcion != 0);
    }

    public void iniciarCliente(Cliente cliente) {
        MenuCliente menuCliente = new MenuCliente();
        int opcion;
        do {
            menuCliente.mostrarMenu();
            opcion = menuCliente.pedirOpcion();
            switch (opcion) {
                case 1 -> menuCliente.mostrarCatalogo(celularDAO.listarCelulares());
                case 2 -> nuevaVenta(cliente, menuCliente);
                //solo las ventas de este cliente, no las de toda la tienda
                case 3 -> menuCliente.mostrarMisCompras(ventaDAO.listarVentasPorCliente(cliente.getId()));
                case 0 -> System.out.println("Sesión cerrada");
                default -> System.out.println("Opción invalida");
            }
        } while (opcion != 0);
    }

    private void nuevaVenta(Cliente cliente, MenuCliente menuCliente) {
        menuCliente.mostrarCatalogo(celularDAO.listarCelulares());

        int ventaId = ventaDAO.crearVenta(cliente.getId());
        if (ventaId == -1) {
            System.out.println("Error al crear la venta");
            return;
        }

        boolean continuar;
        boolean fallo = false;
        do {
            int celularId = menuCliente.pedirCelularId();
            int cantidad = menuCliente.pedirCantidad();
            if (!ventaDAO.agregarItem(ventaId, celularId, cantidad)) {
                fallo = true;
            }
            //Enter termina la compra; antes la linea vacia se rechazaba y el
            //usuario quedaba atrapado en el bucle
            continuar = menuCliente.preguntarAgregarOtro();
        } while (continuar);

        if (fallo) {
            System.out.println("Algunos celulares no se pudieron agregar a la venta");
        } else {
            System.out.println("Venta registrada correctamente!");
        }
        menuCliente.mostrarDetalleCompra(ventaDAO.listarDetalleVenta(ventaId));
    }

    private void verTodasLasVentas() {
        new MenuCliente().mostrarMisCompras(ventaDAO.listarVentas());
    }

}
