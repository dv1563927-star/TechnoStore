package vista;

import java.util.List;
import modelo.Cliente;

public class IngresoDatosCliente {

    Validaciones v = new Validaciones();

    public void mostrarMenu() {
        System.out.println("""
                ==============================
                   GESTIÓN DE CLIENTES
                ==============================
                 1. Registrar cliente
                 2. Actualizar cliente
                 3. Eliminar cliente
                 4. Listar clientes
                 0. Volver
                ==============================""");
    }

    public void mostrarClientes(List<Cliente> clientes) {
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados");
            return;
        }
        clientes.forEach(c -> System.out.println(c));
    }

    public int pedirOpcion() {
        return v.validarEntero("Seleccione una opción: ");
    }

    public int pedirId() {
        return v.validarEntero("Ingrese el id del cliente: ");
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

    public String pedirCorreo() {
        return v.validarCorreo("Correo: ");
    }

    public String pedirPassword() {
        return v.validarPassword("Contraseña: ");
    }

}
