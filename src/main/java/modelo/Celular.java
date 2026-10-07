package modelo;

import modelo.GeneradorIds;

public class Celular {
    
    public enum gama {
        ALTA, MEDIA, BAJA
    }
    
    private int id;
    private Modelo modelo;
    private String sistemaOperativo;
    private gama gama;
    private double precio;
    private int stock;

    public Celular(int id, Modelo modelo, String sistemaOperativo, gama gama, double precio, int stock) {
        this.id = GeneradorIds.nuevaId();
        this.modelo = modelo;
        this.sistemaOperativo = sistemaOperativo;
        this.gama = gama;
        this.precio = precio;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    public String getSistemaOperativo() {
        return sistemaOperativo;
    }

    public void setSistemaOperativo(String sistemaOperativo) {
        this.sistemaOperativo = sistemaOperativo;
    }

    public gama getGama() {
        return gama;
    }

    public void setGama(gama gama) {
        this.gama = gama;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
    
    
    public String toString() {
        return """
               =======CELULAR =======
                Id                                %s
                Modelo                       %s
                Sistema Operativo        %s
                Gamma:                       %s
                Precio:                         %s
                Stock:                          %s
               ====================
               """.formatted(id, modelo, sistemaOperativo, gama, precio, stock);
    }
    
}
