package controlador;

import dao.ClienteDAO;
import dao.Conexion;
import dao.UsuarioDAO;
import java.sql.Connection;
import java.sql.SQLException;
import modelo.Cliente;
import modelo.Usuarios;
import vista.MenuLogin;

public class GestionSesion {

    MenuLogin menuLogin = new MenuLogin();
    UsuarioDAO usuarioDAO = new UsuarioDAO();
    ClienteDAO clienteDAO = new ClienteDAO();

    public void iniciar() {
        int opcion;
        do {
            menuLogin.mostrarMenu();
            opcion = menuLogin.pedirOpcion();
            switch (opcion) {
                case 1 -> iniciarSesion();
                case 2 -> registrarse();
                case 0 -> System.out.println("Hasta luego!");
                default -> System.out.println("Opción invalida");
            }
        } while (opcion != 0);
    }

    private void iniciarSesion() {
        //en el login no se aplican las reglas de formato del registro:
        //solo se comprueba que las credenciales coincidan
        String correo = menuLogin.pedirCorreoLogin();
        String password = menuLogin.pedirPasswordLogin();

        Usuarios usuario = usuarioDAO.buscarPorCorreo(correo);
        if (usuario == null || !usuario.getPassword().equals(password)) {
            System.out.println("Credenciales incorrectas, intente de nuevo");
            return;
        }

        //segun el rol muestro el menu correspondiente
        if (usuario.getRol() == Usuarios.Rol.ADMIN) {
            new GestionAdmin(usuario).iniciar();
        } else {
            //busco el cliente asociado a este usuario para pasarlo a gestion ventas
            Cliente cliente = clienteDAO.listarClientes().stream()
                    .filter(c -> c.getUsuario() != null && correo.equalsIgnoreCase(c.getUsuario().getCorreo()))
                    .findFirst()
                    .orElse(null);
            if (cliente == null) {
                //un usuario CLIENTE sin ficha de cliente no puede comprar;
                //antes esto llegaba a GestionVentas y terminaba en NullPointerException
                System.out.println("Su usuario no tiene ficha de cliente asociada. Contacte al administrador.");
                return;
            }
            new GestionCliente(cliente).iniciar();
        }
    }

    private void registrarse() {
        String correo = menuLogin.pedirCorreoRegistro();
        String password = menuLogin.pedirPasswordRegistro();

        //verifico que el correo no este ya registrado
        if (usuarioDAO.buscarPorCorreo(correo) != null) {
            System.out.println("Ese correo ya está registrado");
            return;
        }

        String nombre = menuLogin.pedirNombre();
        String identificacion = menuLogin.pedirIdentificacion();

        if (clienteDAO.listarClientes().stream().anyMatch(c -> identificacion.equals(c.getIdentificacion()))) {
            System.out.println("Ya existe un cliente con esa identificación");
            return;
        }

        String telefono = menuLogin.pedirTelefono();

        try (Connection con = Conexion.getInstancia().conexion()) {
            con.setAutoCommit(false);

            //el rol siempre es CLIENTE, nunca se le pregunta al usuario
            int idUsuario = usuarioDAO.crearUsuario(con, new Usuarios(0, correo, password, Usuarios.Rol.CLIENTE));

            //uso el id que devolvio el procedimiento, sin volver a consultar la base
            int idCliente = -1;
            if (idUsuario > 0) {
                Usuarios usuarioCreado = new Usuarios(idUsuario, correo, password, Usuarios.Rol.CLIENTE);
                idCliente = clienteDAO.crearCliente(con, new Cliente(0, usuarioCreado, nombre, identificacion, telefono));
            }

            if (idUsuario > 0 && idCliente > 0) {
                con.commit();
                System.out.println("Registro exitoso! Ya puede iniciar sesion");
            } else {
                con.rollback();
                System.out.println("No se pudo completar el registro. Verifique los datos e intente de nuevo.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
