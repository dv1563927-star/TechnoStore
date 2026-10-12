package controlador;

import modelo.Usuarios;
import vista.MenuAdmin;

public class GestionAdmin {

    Usuarios usuarioActivo;
    MenuAdmin menuAdmin = new MenuAdmin();
    GestionCelulares gestionCelulares = new GestionCelulares();
    GestionMarcas gestionMarcas = new GestionMarcas();
    GestionClientes gestionClientes = new GestionClientes();
    GestionVentas gestionVentas = new GestionVentas();
    GestionReportes gestionReportes = new GestionReportes();

    public GestionAdmin(Usuarios usuarioActivo) {
        this.usuarioActivo = usuarioActivo;
    }

    public void iniciar() {
        int opcion;
        do {
            menuAdmin.mostrarMenu();
            opcion = menuAdmin.pedirOpcion();
            switch (opcion) {
                case 1 -> gestionCelulares.iniciar();
                case 2 -> gestionMarcas.iniciar();
                case 3 -> gestionClientes.iniciar();
                case 4 -> gestionVentas.iniciarAdmin();
                case 5 -> gestionReportes.iniciar();
                case 0 -> System.out.println("Sesión cerrada");
                default -> System.out.println("Opción invalida");
            }
        } while (opcion != 0);
    }

}
