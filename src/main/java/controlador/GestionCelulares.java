package controlador;

import dao.CelularDAO;
import dao.MarcaDAO;
import java.util.List;
import modelo.Celular;
import modelo.Marca;
import vista.IngresoDatosCelular;

public class GestionCelulares {

    IngresoDatosCelular vista = new IngresoDatosCelular();
    CelularDAO celularDAO = new CelularDAO();
    MarcaDAO marcaDAO = new MarcaDAO();

    public void iniciar() {
        int opcion;
        do {
            vista.mostrarMenu();
            opcion = vista.pedirOpcion();
            switch (opcion) {
                case 1 -> registrarCelular();
                case 2 -> actualizarCelular();
                case 3 -> eliminarCelular();
                case 4 -> listarCelulares();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción invalida");
            }
        } while (opcion != 0);
    }

    private void registrarCelular() {
        List<Marca> marcas = marcaDAO.obtenerMarcas();
        if (marcas.isEmpty()) {
            System.out.println("No hay marcas registradas, registre una marca primero");
            return;
        }
        vista.mostrarMarcas(marcas);
        int marcaId = vista.pedirMarcaId();
        Marca marca = marcaDAO.buscar(marcaId);
        if (marca == null) {
            System.out.println("La marca no existe");
            return;
        }
        String modelo = vista.pedirModelo();
        String so = vista.pedirSistemaOperativo();
        vista.mostrarGama();
        Celular.gama gama = switch (vista.pedirGama()) {
            case 1 -> Celular.gama.ALTA;
            case 2 -> Celular.gama.MEDIA;
            default -> Celular.gama.BAJA;
        };
        double precio = vista.pedirPrecio();
        int stock = vista.pedirStock();

        celularDAO.crearCelular(new Celular(0, marca, modelo, so, gama, precio, stock));
    }

    private void actualizarCelular() {
        vista.mostrarCelulares(celularDAO.listarCelulares());
        int id = vista.pedirId();
        Celular celular = celularDAO.buscarCelular(id);
        if (celular == null) {
            System.out.println("El celular no existe");
            return;
        }
        List<Marca> marcas = marcaDAO.obtenerMarcas();
        vista.mostrarMarcas(marcas);
        int marcaId = vista.pedirMarcaId();
        Marca marca = marcaDAO.buscar(marcaId);
        if (marca == null) {
            System.out.println("La marca no existe");
            return;
        }
        String modelo = vista.pedirModelo();
        String so = vista.pedirSistemaOperativo();
        vista.mostrarGama();
        Celular.gama gama = switch (vista.pedirGama()) {
            case 1 -> Celular.gama.ALTA;
            case 2 -> Celular.gama.MEDIA;
            default -> Celular.gama.BAJA;
        };
        double precio = vista.pedirPrecio();
        int stock = vista.pedirStock();

        celularDAO.actualizarCelular(new Celular(id, marca, modelo, so, gama, precio, stock));
    }

    private void eliminarCelular() {
        vista.mostrarCelulares(celularDAO.listarCelulares());
        int id = vista.pedirId();
        celularDAO.eliminarCelular(id);
    }

    private void listarCelulares() {
        vista.mostrarCelulares(celularDAO.listarCelulares());
    }

}
