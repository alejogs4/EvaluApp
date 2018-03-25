package POJOS;

import com.google.gson.annotations.SerializedName;

/**
 * Created by USUARIO on 20/03/2018.
 */

public class Notes {

    @SerializedName("id_test")
    private Integer idTest;

    @SerializedName("id_student")
    private String idStudent;

    @SerializedName("value_test")
    private Integer valueTest;

    @SerializedName("username")
    private String username;

    public Integer getIdTest() {
        return idTest;
    }

    public void setIdTest(Integer idTest) {
        this.idTest = idTest;
    }

    public String getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(String idStudent) {
        this.idStudent = idStudent;
    }

    public Integer getValueTest() {
        return valueTest;
    }

    public void setValueTest(Integer valueTest) {
        this.valueTest = valueTest;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
