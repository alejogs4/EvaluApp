package POJOS;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Alejandro on 20/03/2018.
 */

public class Questions {
    @SerializedName("id")
    private Integer id;

    @SerializedName("question")
    private String question;

    @SerializedName("answer")
    private String answer;

    @SerializedName("is_correct")
    private Boolean isCorrect;

    @SerializedName("id_test")
    private Integer idTest;

    @SerializedName("title")
    private String title;

    @SerializedName("value_test")
    private Integer valueTest;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getQuestion() {
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }
    public String getAnswer() {
        return answer;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    public Boolean getIsCorrect() {
        return isCorrect;
    }
    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }
    public Integer getIdTest() {
        return idTest;
    }
    public void setIdTest(Integer idTest) {
        this.idTest = idTest;
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
}

