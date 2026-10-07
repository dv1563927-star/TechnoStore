package modelo;

import modelo.GeneradorIds;

public class DetalleVenta {
    
    private int id;
    private Venta venta;
    private Celular celular;
    private int cantidad;
    private double subtotal;

    public DetalleVenta(Venta venta, Celular celular, int cantidad, double subtotal) {
        this.id = GeneradorIds.nuevaId();
        this.venta = venta;
        this.celular = celular;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    public int getId() {
        return id;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Celular getCelular() {
        return celular;
    }

    public void setCelular(Celular celular) {
        this.celular = celular;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
    
    public String toString() {
        return """
               ======= DETALLE DE VENTA =======
                            Id:                                 %s
                            Venta:                            %s
                            Celular:                          %s
                            Cantidad:                       %s
                            Subtotal:                        %s
               ============================
               """.formatted(id, venta, celular, cantidad, subtotal);
     }
    
}
