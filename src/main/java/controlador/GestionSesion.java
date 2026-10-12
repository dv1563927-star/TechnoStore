package controlador;

import dao.ClienteDAO;
import dao.UsuarioDAO;
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
        String correo = menuLogin.pedirCorreo();
        String password = menuLogin.pedirPassword();

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
                    .filter(c -> c.getUsuario().getCorreo().equals(correo))
                    .findFirst()
                    .orElse(null);
            new GestionCliente(cliente).iniciar();
        }
    }

    private void registrarse() {
        String correo = menuLogin.pedirCorreo();
        String password = menuLogin.pedirPassword();

        //verifico que el correo no este ya registrado
        if (usuarioDAO.buscarPorCorreo(correo) != null) {
            System.out.println("Ese correo ya está registrado");
            return;
        }

        String nombre = menuLogin.pedirNombre();
        String identificacion = menuLogin.pedirIdentificacion();
        String telefono = menuLogin.pedirTelefono();

        //el rol siempre es CLIENTE, nunca se le pregunta al usuario
        Usuarios usuario = new Usuarios(0, correo, password, Usuarios.Rol.CLIENTE);
        usuarioDAO.crearUsuario(usuario);

        //busco el usuario recien creado para obtener su id generado
        Usuarios usuarioCreado = usuarioDAO.buscarPorCorreo(correo);
        clienteDAO.crearCliente(new Cliente(0, usuarioCreado, nombre, identificacion, telefono));
        System.out.println("Registro exitoso! Ya puede iniciar sesion");
    }

}
