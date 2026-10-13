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

    // --- Login: solo se comprueba la credencial, no se aplican reglas de formato ---

    public String pedirCorreoLogin() {
        return v.validarTexto("Correo: ");
    }

    public String pedirPasswordLogin() {
        return v.validarTexto("Contraseña: ");
    }

    // --- Registro: aqui si se validan el formato del correo y la longitud ---

    public String pedirCorreoRegistro() {
        return v.validarCorreo("Correo: ");
    }

    public String pedirPasswordRegistro() {
        return v.validarPassword("Contraseña (mínimo 6 caracteres): ");
    }

    public String pedirNombre() {
        return v.validarTexto("Nombre: ");
    }

    public String pedirIdentificacion() {
        return v.validarTexto("Identificación: ");
    }

    public String pedirTelefono() {
        return v.validarTexto("Teléfono: ");
    }

}
