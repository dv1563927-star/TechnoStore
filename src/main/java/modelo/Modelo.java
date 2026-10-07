package modelo;

import modelo.GeneradorIds;

public class Modelo {
    
    private int id;
    private Marca marca;
    private String nombre;

    public Modelo(Marca marca, String nombre) {
        this.id = GeneradorIds.nuevaId();
        this.marca = marca;
        this.nombre = nombre;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
     public String toString() {
        return """
               ======= MODELO =======
                Id:                                 %s
                Marca:                           %s
                Nombre:                        %s
               ====================
               """.formatted(id, marca, nombre);
     }
    
}
