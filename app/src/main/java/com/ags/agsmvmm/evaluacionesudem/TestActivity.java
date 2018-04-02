package com.ags.agsmvmm.evaluacionesudem;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import POJOS.Questions;
import POJOS.Tests;
import PetitionsInterfaces.CallsService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Alejandro Garcia on 20/03/2018.
 */

public class TestActivity extends AppCompatActivity {

    private TextView txtQuestionTitle;
    private Button btnNext;
    private Button btnFinish;

    private RadioGroup answers;
    private RadioButton answer1;
    private RadioButton answer2;
    private RadioButton answer3;
    private RadioButton answer4;

    private TextInputLayout inputToComplete;
    private EditText txtToComplete;

    private List<Questions> questions = new ArrayList<>();

    private double score;
    private int goodQuestions = 0;
    private int numberOfQuestions;

    private String answer = "";

    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);

        connect();
        Bundle extras = getIntent().getExtras();
        final String id = extras.getString("id");
        final String idStudent = extras.getString("idStudent");
        type = extras.getString("typeTest");
        loadQuestions(id);
        btnNext.setOnClickListener(view -> {
            if(this.questions.size() != 0) setScoreByQuestion();
            setQuestion();
        });
        btnFinish.setOnClickListener(view -> {
            saveNote(idStudent,id);
            finish();
        });
    }


    /**
     * Metodo que carga cada una de las preguntas del examen
     * @param id
     */
    private void loadQuestions (String id) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(TeacherLogin.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CallsService service = retrofit.create(CallsService.class);
        final Call<List<Questions>> question = service.getQuestionsByTest(Integer.parseInt(id));

        question.enqueue(new Callback<List<Questions>>() {
            @Override
            public void onResponse(Call<List<Questions>> call, Response<List<Questions>> response) {
                if(response.code() == 200) {
                    setQuestions(response.body());
                    setQuestion();
                }
                else {
                    Toast.makeText(TestActivity.this, R.string.questions_not_found,Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Questions>> call, Throwable t) {
                Toast.makeText(TestActivity.this, R.string.error_to_get,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setScoreByQuestion() {
        if(isFourOptions(this.type)) {
            verifyFourOptions();
        }
        else if(isTrueOrFalse(this.type)) {
            verifyTrueOrFalse();
        }
        else if(isToComplete(this.type)) {
            if(txtToComplete.getText().toString().equals(answer)) updateGoodQuestions();
        }
    }

    /**
     * Guarda la nota obtenida por el usuario
     * @param id
     */
    private void saveNote(String id,String idTest) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(TeacherLogin.API_URL).addConverterFactory(GsonConverterFactory.create())
                .build();
        CallsService service = retrofit.create(CallsService.class);
        double number = Double.parseDouble(numberOfQuestions + "");
        score = (5.0 / number) * goodQuestions;
        final Call<List<Object>> getNotes = service.saveNotes(Integer.parseInt(idTest),id,score);
        getNotes.enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(TestActivity.this, R.string.save_note,Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(TestActivity.this,response.message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Object>> call, Throwable t) {
                Toast.makeText(TestActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     *  Filtra las preguntas para asi irlas mostrando en la pantalla
     *
     */
    private void setQuestion() {
        if(questions.size() == 0) {
            Toast.makeText(TestActivity.this,"No hay mas preguntas",Toast.LENGTH_SHORT).show();
            btnFinish.setVisibility(View.VISIBLE);
            return;
        }
        updateNumberOfQuestions();
        String searchParameter = questions.get(0).getQuestion().split("-")[1];
        String type = questions.get(0).getQuestion().split("-")[0];
        setType(type);

        if(isTrueOrFalse(type)) setAnswer(questions.get(0).getAnswer());

        List<String> answersToQuestions = fillAnswers(searchParameter);
        setVisibility(type.trim());
        txtQuestionTitle.setText(searchParameter);
        setViewsContent(type,answersToQuestions);
    }

    /**
     * Agrega contenido a las opciones de respuesta
     * @param type
     * @param answersToQuestions
     */
    private void setViewsContent(String type,List<String> answersToQuestions) {
        if(isFourOptions(type)) {
            for(int i = 0; i < answersToQuestions.size(); i++) {
                if(i == 0) answer1.setText(answersToQuestions.get(0));
                if(i == 1) answer2.setText(answersToQuestions.get(1));
                if(i == 2) answer3.setText(answersToQuestions.get(2));
                if(i == 3) answer4.setText(answersToQuestions.get(3));
            }
        }
        else if(isTrueOrFalse(type)) {
            answer1.setText("VERDADERO");
            answer2.setText("FALSO");
        }
        else if(isToComplete(type)) {
            txtToComplete.setHint(R.string.complete_sentence);
        }
    }

    /**
     * Cambia la visibilidad de los elementos de la interfaz dependiendo del tipo de pregunta
     * @param type
     */
    private void setVisibility(String type) {
        if(isFourOptions(type)) {
            answers.setVisibility(View.VISIBLE);
            answer1.setVisibility(View.VISIBLE);
            answer2.setVisibility(View.VISIBLE);
            answer3.setVisibility(View.VISIBLE);
            answer4.setVisibility(View.VISIBLE);
            inputToComplete.setVisibility(View.INVISIBLE);
        }
        else if(isTrueOrFalse(type)) {
            answer1.setVisibility(View.VISIBLE);
            answer2.setVisibility(View.VISIBLE);
            answer3.setVisibility(View.INVISIBLE);
            answer4.setVisibility(View.INVISIBLE);
            inputToComplete.setVisibility(View.INVISIBLE);
        }
        else if(isToComplete(type)) {
            inputToComplete.setVisibility(View.VISIBLE);
            txtToComplete.setVisibility(View.VISIBLE);
            answers.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Llena la lista de respuestas
     * @param searchParameter
     * @return
     */
    private List<String> fillAnswers(String searchParameter) {
        List<String> answers = new ArrayList<>();
        List<Questions> newQuestions = new ArrayList<>();
        for(int i = 0;i < questions.size();i++) {
            if(this.questions.get(i).getQuestion().split("-")[1].equals(searchParameter)) {
                answers.add(this.questions.get(i).getAnswer());
                if(questions.get(i).getIsCorrect()) setAnswer(questions.get(i).getAnswer());
            }
            else {
                newQuestions.add(questions.get(i));
            }
        }
        setQuestions(newQuestions);
        return answers;
    }

    /**
     * Verifica si la opcion seleccionada es la respuesta correcta si la pregunta es de 4 opciones
     */
    private void verifyFourOptions() {
        switch (answers.getCheckedRadioButtonId()) {
            case R.id.a_1 :
                if(answer1.getText().toString().equals(answer)) updateGoodQuestions();
                break;
            case R.id.a_2 :
                if(answer2.getText().toString().equals(answer)) updateGoodQuestions();
                break;
            case R.id.a_3 :
                if(answer3.getText().toString().equals(answer)) updateGoodQuestions();
                break;
            case R.id.a_4 :
                if(answer4.getText().toString().equals(answer)) updateGoodQuestions();
                break;
        }
    }

    /**
     * Verifica si la opcion seleccionada es la correcta si la pregunta es de verdadero o falso
     */
    private void verifyTrueOrFalse() {
        switch (answers.getCheckedRadioButtonId()) {
            case R.id.a_1 :
                if(answer1.getText().toString().equals(answer)) updateGoodQuestions();
                break;
            case R.id.a_2 :
                if(answer2.getText().toString().equals(answer)) updateGoodQuestions();
                break;
        }
    }

    /**
     * Verifica si el tipo de pregunta es de cuatro opciones
     * @param type
     * @return
     */
    private boolean isFourOptions(String type){ return type.equals("1"); }

    /**
     * Verifica si el tipo de pregunta es verdadero o falso
     * @param type
     * @return
     */
    private boolean isTrueOrFalse(String type){ return type.equals("2"); }

    /**
     * Verifica si el tipo de pregunta es de completar
     * @param type
     * @return
     */
    private boolean isToComplete(String type){ return type.equals("3"); }

    /**
     * Agrega valor a la lista de preguntas
     * @param questions
     */
    private void setQuestions(List<Questions> questions) { this.questions = questions; }

    /**
     * Actualiza el numero de preguntas
     */
    private void updateNumberOfQuestions() { this.numberOfQuestions++; }

    private void updateGoodQuestions() { this.goodQuestions++; }

    /**
     * Agrega valor a la respuesta de la pregunta
     * @param answer
     */
    private void setAnswer(String answer) { this.answer = answer; }

    /**
     * Agrega valor al tipo de la pregunta
     * @param type
     */
    private void setType(String type) { this.type = type; }


    /**
     * Enlaza las vista de la interfaz con las clases de Java
     */
    private void connect () {
        txtQuestionTitle = findViewById(R.id.question_title);
        btnNext = findViewById(R.id.btn_next_question_test);
        btnFinish = findViewById(R.id.btn_finish_test);
        inputToComplete = findViewById(R.id.input_complete);
        txtToComplete = findViewById(R.id.txt_complete);
        answer1 = findViewById(R.id.a_1);
        answer2 = findViewById(R.id.a_2);
        answer3 = findViewById(R.id.a_3);
        answer4 = findViewById(R.id.a_4);
        answers = findViewById(R.id.answer_group);
    }

}