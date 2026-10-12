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
                case 3 -> menuCliente.mostrarMisCompras(ventaDAO.listarVentas());
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

        String continuar;
        do {
            int celularId = menuCliente.pedirCelularId();
            int cantidad = menuCliente.pedirCantidad();
            ventaDAO.agregarItem(ventaId, celularId, cantidad);
            System.out.println("Desea agregar otro celular? (s para continuar, cualquier tecla para terminar): ");
            continuar = new vista.Validaciones().validarTexto("");
        } while (continuar.equalsIgnoreCase("s"));

        System.out.println("Venta registrada correctamente!");
        menuCliente.mostrarDetalleCompra(ventaDAO.listarDetalleVenta(ventaId));
    }

    private void verTodasLasVentas() {
        new MenuCliente().mostrarMisCompras(ventaDAO.listarVentas());
    }

}
