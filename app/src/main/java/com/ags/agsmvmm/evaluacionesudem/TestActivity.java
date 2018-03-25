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
    private List<Questions> currentQuestions = new ArrayList<>();

    private String type;

    private float score;
    private int goodAnswer;
    private  int init;
    private float numQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        score  = 0;
        goodAnswer = 0;
        init = 0;
        numQuestions = 0;
        connect();
        Bundle extras = getIntent().getExtras();
        final String id = extras.getString("id");
        final String idStudent = extras.getString("idStudent");
        type = extras.getString("typeTest");
        loadQuestions(id);
        btnNext.setOnClickListener(view -> {
            numQuestions = questions.size() == 0 ? numQuestions : numQuestions++;
            verifyGoodAnswer();
            setQuestion(type);
        });
        btnFinish.setOnClickListener(view -> {
            Toast.makeText(TestActivity.this, R.string.test_finish,Toast.LENGTH_SHORT).show();
            float tas;

            tas = 5 / (numQuestions / 4);

            score =  tas * goodAnswer;
            if(type.equals("1")) score = numQuestions;
            saveNote(idStudent);
            finish();
        });
    }

    private void verifyGoodAnswer() {
        switch (answers.getCheckedRadioButtonId()) {
            case R.id.a_1 :
                goodAnswer = getQuestion(answer1.getText().toString()) ? goodAnswer + 1 : goodAnswer;
                break;
            case R.id.a_2 :
                goodAnswer = getQuestion(answer2.getText().toString()) ? goodAnswer + 1 : goodAnswer;
                break;
            case R.id.a_3 :
                goodAnswer = getQuestion(answer3.getText().toString()) ? goodAnswer + 1 : goodAnswer;
                break;
            default :
                goodAnswer = getQuestion(answer4.getText().toString()) ? goodAnswer + 1 : goodAnswer;
                break;
        }
    }

    private boolean getQuestion (String answer) {
        for(int i = 0; i < currentQuestions.size();i++ ) {
            if(currentQuestions.get(i).getAnswer().equals(answer)) {
                return currentQuestions.get(i).getIsCorrect();
            }
        }
        return false;
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
                    questions = response.body();
                    setQuestion(type);
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
        final Call<List<Object>> getNotes = service.saveNotes(questions.get(0).getIdTest(),id,score);
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
     * @param type
     */
    private void setQuestion(String type) {
        numQuestions++;
        int pass = type.equals("1") ? 1 : 4;
        if(questions.size() == 0 || init + pass > questions.size()) {
            Toast.makeText(TestActivity.this, R.string.no_more_questions,Toast.LENGTH_SHORT).show();
            return;
        }
        currentQuestions.clear();

        for(int j = init ; j < init + pass; j++) {
            currentQuestions.add(questions.get(j));
        }
        init = init + pass;
        txtQuestionTitle.setText(currentQuestions.get(0).getQuestion());

        if(pass == 1) {
            answer1.setText("VERDADERO");
            answer2.setText("FALSO");
            answer3.setVisibility(View.INVISIBLE);
            answer4.setVisibility(View.INVISIBLE);
        }
        else {
            for (int i = 0; i < currentQuestions.size(); i++) {
                if( i == 0) answer1.setText(currentQuestions.get(i).getAnswer());

                if( i == 1) answer2.setText(currentQuestions.get(i).getAnswer());

                if( i == 2) answer3.setText(currentQuestions.get(i).getAnswer());

                if( i == 3) answer4.setText(currentQuestions.get(i).getAnswer());
            }
        }
    }

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