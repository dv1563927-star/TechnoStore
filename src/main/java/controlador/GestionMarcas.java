package controlador;

import dao.MarcaDAO;
import modelo.Marca;
import vista.IngresoDatosMarca;

public class GestionMarcas {

    IngresoDatosMarca vista = new IngresoDatosMarca();
    MarcaDAO marcaDAO = new MarcaDAO();

    public void iniciar() {
        int opcion;
        do {
            vista.mostrarMenu();
            opcion = vista.pedirOpcion();
            switch (opcion) {
                case 1 -> registrarMarca();
                case 2 -> actualizarMarca();
                case 3 -> eliminarMarca();
                case 4 -> listarMarcas();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción invalida");
            }
        } while (opcion != 0);
    }

    private void registrarMarca() {
        String nombre = vista.pedirNombre();
        marcaDAO.crearMarca(new Marca(0, nombre));
    }

    private void actualizarMarca() {
        vista.mostrarMarcas(marcaDAO.obtenerMarcas());
        int id = vista.pedirId();
        String nombre = vista.pedirNombre();
        marcaDAO.actualizarMarca(new Marca(id, nombre));
    }

    private void eliminarMarca() {
        vista.mostrarMarcas(marcaDAO.obtenerMarcas());
        int id = vista.pedirId();
        marcaDAO.eliminarMarca(new Marca(id, ""));
    }

    private void listarMarcas() {
        vista.mostrarMarcas(marcaDAO.obtenerMarcas());
    }

}
