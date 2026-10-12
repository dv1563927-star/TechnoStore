package vista;

import java.util.List;
import modelo.Celular;
import modelo.Marca;

public class IngresoDatosCelular {

    Validaciones v = new Validaciones();

    public void mostrarMenu() {
        System.out.println("""
                ==============================
                   GESTIÓN DE CELULARES
                ==============================
                 1. Registrar celular
                 2. Actualizar celular
                 3. Eliminar celular
                 4. Listar celulares
                 0. Volver
                ==============================""");
    }

    public void mostrarMarcas(List<Marca> marcas) {
        System.out.println("=== MARCAS DISPONIBLES ===");
        marcas.forEach(m -> System.out.println(" " + m.getId() + ". " + m.getNombre()));
    }

    public void mostrarGama() {
        System.out.println("""
                === GAMA ===
                 1. ALTA
                 2. MEDIA
                 3. BAJA""");
    }

    public void mostrarCelulares(List<Celular> celulares) {
        if (celulares.isEmpty()) {
            System.out.println("No hay celulares registrados");
            return;
        }
        celulares.forEach(c -> System.out.println(c));
    }

    public int pedirOpcion() {
        return v.validarEntero("Seleccione una opción: ");
    }

    public int pedirId() {
        return v.validarEntero("Ingrese el id del celular: ");
    }

    public int pedirMarcaId() {
        return v.validarEntero("Seleccione el id de la marca: ");
    }

    public String pedirModelo() {
        return v.validarTexto("Modelo: ");
    }

    public String pedirSistemaOperativo() {
        return v.validarTexto("Sistema operativo: ");
    }

    public int pedirGama() {
        return v.validarEntero("Seleccione la gama (1.ALTA 2.MEDIA 3.BAJA): ");
    }

    public double pedirPrecio() {
        return v.validarDecimalPositivo("Precio: ");
    }

    public int pedirStock() {
        return v.validarEnteroPositivo("Stock inicial: ");
    }

}
