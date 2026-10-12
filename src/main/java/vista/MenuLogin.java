package vista;

public class MenuLogin {

    Validaciones v = new Validaciones();

    public void mostrarMenu() {
        System.out.println("""
                ==============================
                        TECHNOSTORE
                ==============================
                 1. Iniciar sesión
                 2. Registrarse
                 0. Salir
                ==============================""");
    }

    public int pedirOpcion() {
        return v.validarEntero("Seleccione una opción: ");
    }

    public String pedirCorreo() {
        return v.validarTexto("Correo: ");
    }

    public String pedirPassword() {
        return v.validarTexto("Contraseña: ");
    }

}
