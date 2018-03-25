package POJOS;

/**
 * Created by Alejandro Garcia on 20/03/2018.
 */

public class Student {

    private String id;
    private String username;
    private String pass;

    public Student(String id, String username, String pass) {
        this.id = id;
        this.username = username;
        this.pass = pass;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
