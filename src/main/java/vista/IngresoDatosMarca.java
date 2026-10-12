package vista;

import java.util.List;
import modelo.Marca;

public class IngresoDatosMarca {

    Validaciones v = new Validaciones();

    public void mostrarMenu() {
        System.out.println("""
                ==============================
                     GESTIÓN DE MARCAS
                ==============================
                 1. Registrar marca
                 2. Actualizar marca
                 3. Eliminar marca
                 4. Listar marcas
                 0. Volver
                ==============================""");
    }

    public void mostrarMarcas(List<Marca> marcas) {
        if (marcas.isEmpty()) {
            System.out.println("No hay marcas registradas");
            return;
        }
        marcas.forEach(m -> System.out.println(m));
    }

    public int pedirOpcion() {
        return v.validarEntero("Seleccione una opción: ");
    }

    public int pedirId() {
        return v.validarEntero("Ingrese el id de la marca: ");
    }

    public String pedirNombre() {
        return v.validarTexto("Nombre de la marca: ");
    }

}
