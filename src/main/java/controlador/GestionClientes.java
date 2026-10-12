package controlador;

import dao.ClienteDAO;
import modelo.Cliente;
import vista.IngresoDatosCliente;

public class GestionClientes {

    IngresoDatosCliente vista = new IngresoDatosCliente();
    ClienteDAO clienteDAO = new ClienteDAO();

    public void iniciar() {
        int opcion;
        do {
            vista.mostrarMenu();
            opcion = vista.pedirOpcion();
            switch (opcion) {
                case 1 -> actualizarCliente();
                case 2 -> eliminarCliente();
                case 3 -> listarClientes();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción invalida");
            }
        } while (opcion != 0);
    }

    private void actualizarCliente() {
        vista.mostrarClientes(clienteDAO.listarClientes());
        int id = vista.pedirId();
        Cliente cliente = clienteDAO.buscarCliente(id);
        if (cliente == null) {
            System.out.println("El cliente no existe");
            return;
        }
        String nombre = vista.pedirNombre();
        String identificacion = vista.pedirIdentificacion();
        String telefono = vista.pedirTelefono();
        clienteDAO.actualizarCliente(new Cliente(id, cliente.getUsuario(), nombre, identificacion, telefono));
    }

    private void eliminarCliente() {
        vista.mostrarClientes(clienteDAO.listarClientes());
        int id = vista.pedirId();
        clienteDAO.eliminarCliente(id);
    }

    private void listarClientes() {
        vista.mostrarClientes(clienteDAO.listarClientes());
    }

}
