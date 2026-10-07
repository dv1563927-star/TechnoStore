package modelo;

import java.time.LocalDateTime;
import modelo.GeneradorIds;

public class Venta {
    
    private int id;
    private Cliente cliente;
    private LocalDateTime fecha;
    private double total;

    public Venta(Cliente cliente, LocalDateTime fecha, double total) {
        this.id = GeneradorIds.nuevaId();
        this.cliente = cliente;
        this.fecha = fecha;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
    
    public String toString() {
        return """
               ======= VENTA =======
                Id:                               %s
                Cliente:                        %s
                Fecha:                          %s
                Total:                           %s
               ====================
               """.formatted(id, cliente, fecha, total);
    }
    
}
