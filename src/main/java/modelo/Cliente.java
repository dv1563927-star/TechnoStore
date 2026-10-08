package modelo;

public class Cliente {

    private int id;
    private Usuarios usuario;
    private String nombre;
    private String identificacion;
    private String telefono;

    public Cliente(int id, Usuarios usuario, String nombre, String identificacion, String telefono) {
        this.id = id;
        this.usuario = usuario;
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.telefono = telefono;
    }

    public int getId() {
        return id;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String toString() {
        return """
               ======= CLIENTE =======
                  Id:                                %s
                  Usuario:                        %s
                  Nombre:                       %s
                  Identificacion:                %s
                  Telefono:                      %s
               =====================
               """.formatted(id, usuario, nombre, identificacion, telefono );
    }

}
