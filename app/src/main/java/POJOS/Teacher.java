package POJOS;

/**
 * Created by Alejandro Garcia on 19/03/2018.
 */

public class Teacher {

    private String id;
    private String nombre;
    private String username;

    public Teacher(String id, String nombre, String username) {
        this.id = id;
        this.nombre = nombre;
        this.username = username;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
