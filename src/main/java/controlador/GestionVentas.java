package controlador;

import dao.CelularDAO;
import dao.ClienteDAO;
import dao.VentaDAO;
import modelo.Cliente;
import vista.MenuAdmin;
import vista.MenuCliente;

public class GestionVentas {

    VentaDAO ventaDAO = new VentaDAO();
    CelularDAO celularDAO = new CelularDAO();
    ClienteDAO clienteDAO = new ClienteDAO();
    MenuAdmin menuAdmin = new MenuAdmin();

    public void iniciarAdmin() {
        int opcion;
        do {
            menuAdmin.mostrarMenuVentas();
            opcion = menuAdmin.pedirOpcion();
            switch (opcion) {
                case 1 -> seleccionarClienteYVender();
                case 2 -> verTodasLasVentas();
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

    private void seleccionarClienteYVender() {
        MenuCliente menuCliente = new MenuCliente();

        //muestro los clientes disponibles para que el admin elija
        System.out.println("=== CLIENTES DISPONIBLES ===");
        clienteDAO.listarClientes().forEach(c -> System.out.println(" " + c.getId() + ". " + c.getNombre()));

        int clienteId = menuAdmin.pedirOpcion();
        Cliente cliente = clienteDAO.buscarCliente(clienteId);
        if (cliente == null) {
            System.out.println("El cliente no existe");
            return;
        }
        nuevaVenta(cliente, menuCliente);
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
