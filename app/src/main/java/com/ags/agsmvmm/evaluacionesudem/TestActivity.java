package com.ags.agsmvmm.evaluacionesudem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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


    private List<Questions> questions = new ArrayList<>();

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

        });
        btnFinish.setOnClickListener(view -> {

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

    /**
     * Guarda la nota obtenida por el usuario
     * @param id
     */
    private void saveNote(String id) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(TeacherLogin.API_URL).addConverterFactory(GsonConverterFactory.create())
                .build();
        CallsService service = retrofit.create(CallsService.class);
        final Call<List<Object>> getNotes = service.saveNotes(questions.get(0).getIdTest(),id,0);
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
            return;
        }
        String searchParameter = questions.get(0).getQuestion().split("-")[1];
        String type = questions.get(0).getQuestion().split("-")[0];

        List<String> answersToQuestions = new ArrayList<>();

        for(int i = 0;i < questions.size();i++) {
            if(questions.get(i).getQuestion().split("-")[1].equals(searchParameter)) {
                answersToQuestions.add(questions.get(i).getAnswer());
                questions.remove(i);
            }
        }
        setVisibility(type);
        txtQuestionTitle.setText(searchParameter);
        if(isFourOptions(type)) {
            for(int i = 0; i < answersToQuestions.size(); i++) {
                if(i == 0) answer1.setText(answersToQuestions.get(0));
                if(i == 1) answer2.setText(answersToQuestions.get(1));
                if(i == 2) answer3.setText(answersToQuestions.get(2));
                if(i == 3) answer4.setText(answersToQuestions.get(3));
            }
        }
        else if(isTrueOrFalse(type)) {
            for(int i = 0;i < answersToQuestions.size();i++) {
                if(i == 0) answer1.setText(answersToQuestions.get(0));
                if(i == 1) answer2.setText(answersToQuestions.get(1));
            }
        }
        else if(isToComplete(type)) {
            
        }

    }

    private void setVisibility(String type) {
        if(isFourOptions(type)) {
            answers.setVisibility(View.VISIBLE);
            answer3.setVisibility(View.VISIBLE);
            answer4.setVisibility(View.VISIBLE);
        }
        else if(isTrueOrFalse(type)) {
            answer3.setVisibility(View.INVISIBLE);
            answer4.setVisibility(View.INVISIBLE);
        }
        else if(isToComplete(type)) {

        }
    }

    private boolean isFourOptions(String type){ return type.equals("1"); }

    private boolean isTrueOrFalse(String type){ return type.equals("2"); }

    private boolean isToComplete(String type){ return type.equals("3"); }

    private void setQuestions(List<Questions> questions) { this.questions = questions; }

    private void connect () {
        txtQuestionTitle = findViewById(R.id.question_title);
        btnNext = findViewById(R.id.btn_next_question_test);
        btnFinish = findViewById(R.id.btn_finish_test);
        answer1 = findViewById(R.id.a_1);
        answer2 = findViewById(R.id.a_2);
        answer3 = findViewById(R.id.a_3);
        answer4 = findViewById(R.id.a_4);
        answers = findViewById(R.id.answer_group);
    }

}