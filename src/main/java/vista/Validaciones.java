package vista;

import java.util.Scanner;

public class Validaciones {

    Scanner x = new Scanner(System.in);

    public String validarTexto(String mensaje) {
        String texto = "";
        boolean validacion;
        do {
            validacion = true;
            System.out.println(mensaje);
            texto = x.nextLine();
            if (texto.isBlank()) {
                System.out.println("Error, el campo no puede estar vacio");
                validacion = false;
            }
        } while (!validacion);
        return texto;
    }

    public int validarEntero(String mensaje) {
        System.out.println(mensaje);
        while (!x.hasNextInt()) {
            System.out.println("Error, se espera un valor entero");
            x.next();
        }
        int valor = x.nextInt();
        x.nextLine(); //limpio el buffer
        return valor;
    }

    public int validarEnteroPositivo(String mensaje) {
        int valor;
        do {
            valor = validarEntero(mensaje);
            if (valor <= 0) {
                System.out.println("Error, el valor debe ser mayor a 0");
            }
        } while (valor <= 0);
        return valor;
    }

    public double validarDecimal(String mensaje) {
        System.out.println(mensaje);
        while (!x.hasNextDouble()) {
            System.out.println("Error, se espera un valor decimal");
            x.next();
        }
        double valor = x.nextDouble();
        x.nextLine(); //limpio el buffer
        return valor;
    }

    public double validarDecimalPositivo(String mensaje) {
        double valor;
        do {
            valor = validarDecimal(mensaje);
            if (valor <= 0) {
                System.out.println("Error, el valor debe ser mayor a 0");
            }
        } while (valor <= 0);
        return valor;
    }

    public String validarCorreo(String mensaje) {
        String correo = "";
        boolean validacion;
        do {
            validacion = true;
            System.out.println(mensaje);
            correo = x.nextLine();
            if (!correo.contains("@") || !correo.contains(".") || correo.isBlank()) {
                System.out.println("Error, formato de correo invalido");
                validacion = false;
            }
        } while (!validacion);
        return correo;
    }

    public String validarPassword(String mensaje) {
        String password = "";
        boolean validacion;
        do {
            validacion = true;
            System.out.println(mensaje);
            password = x.nextLine();
            if (password.length() < 6) {
                System.out.println("Error, la contraseña debe tener al menos 6 caracteres");
                validacion = false;
            }
        } while (!validacion);
        return password;
    }

}
