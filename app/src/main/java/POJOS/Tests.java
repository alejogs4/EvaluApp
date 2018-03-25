package POJOS;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Alejandro on 19/03/2018.
 */
public class Tests {

    @SerializedName("id")
    private Integer id;

    @SerializedName("title")
    private String title;

    @SerializedName("value_test")
    private Integer valueTest;

    @SerializedName("id_teacher")
    private String idTeacher;

    @SerializedName("test_type")
    private String testType;

    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getValueTest() {
        return valueTest;
    }

    public void setValueTest(Integer valueTest) {
        this.valueTest = valueTest;
    }

    public String getIdTeacher() {
        return idTeacher;
    }

    public void setIdTeacher(String idTeacher) {
        this.idTeacher = idTeacher;
    }

    public String getTestType() {
        return testType;
    }
    public void setTestType(String testType) {
        this.testType = testType;
    }

}