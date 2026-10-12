package controlador;

import modelo.Cliente;

public class GestionCliente {

    Cliente clienteActivo;
    GestionVentas gestionVentas = new GestionVentas();

    public GestionCliente(Cliente clienteActivo) {
        this.clienteActivo = clienteActivo;
    }

    public void iniciar() {
        gestionVentas.iniciarCliente(clienteActivo);
    }

}
