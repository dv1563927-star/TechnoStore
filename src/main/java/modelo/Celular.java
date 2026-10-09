package modelo;

public class Celular {
    
    public enum gama {
        ALTA, MEDIA, BAJA
    }
    
    private int id;
    private Marca marca;
    private String modelo;
    private String sistemaOperativo;
    private gama gama;
    private double precio;
    private int stock;

    public Celular(int id, Marca marca, String modelo, String sistemaOperativo, gama gama, double precio, int stock) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.sistemaOperativo = sistemaOperativo;
        this.gama = gama;
        this.precio = precio;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
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
                Id:                               %s
                Marca:                          %s
                Modelo:                         %s
                Sistema Operativo:      %s
                Gama:                           %s
                Precio:                         %s
                Stock:                          %s
               ====================
               """.formatted(id, marca, modelo, sistemaOperativo, gama, precio, stock);
    }
    
}
