package modelo;

import modelo.GeneradorIds;

public class Usuarios {
    
    public enum Rol {
    ADMIN, CLIENTE
}
    
    private int id;
    private String correo;
    private String password;
    private Rol rol;

    public Usuarios(String correo, String password, Rol rol) {
        this.id = GeneradorIds.nuevaId();
        this.correo = correo;
        this.password = password;
        this.rol = rol;
    }

    public int getId() {
        return id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
    
    
}