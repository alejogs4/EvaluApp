package com.ags.agsmvmm.evaluacionesudem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

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
 * Created by USUARIO on 20/03/2018.
 */

public class AddQuestion extends AppCompatActivity {

    private EditText txtQuestion;
    private EditText txtAnswer;
    private RadioGroup correctAnswer;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_question);
        connect();
        Bundle extras = getIntent().getExtras();
        final String id = extras.getString("id");
        final String testType = extras.getString("idTest");

        txtAnswer.setVisibility(testType.equals("1") ? View.INVISIBLE : View.VISIBLE );

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addQuestion(id,testType);
            }
        });
    }

    private void addQuestion (String id,String type) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TeacherLogin.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CallsService service = retrofit.create(CallsService.class);
        final Call<List<Questions>> question = service.addQuestion(
                txtQuestion.getText().toString(),
                getAnswer(type),
                id,
                getCorrect()
        );
        question.enqueue(new Callback<List<Questions>>() {
            @Override
            public void onResponse(Call<List<Questions>> call, Response<List<Questions>> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(AddQuestion.this,"Pregunta guardada",Toast.LENGTH_SHORT).show();
                    txtAnswer.setText("");
                }
                else {
                    Toast.makeText(AddQuestion.this,"La pregunta no pudo ser guardada",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Questions>> call, Throwable t) {
                Toast.makeText(AddQuestion.this,"La pregunta no pudo ser guardada",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean getCorrect() {
        return correctAnswer.getCheckedRadioButtonId() == R.id.true_answer;
    }

    private String getAnswer(String type) {
        if(type == "1"){
            return getCorrect() ? "VERDADERO" : "FALSO";
        }
        return txtAnswer.getText().toString();
    }

    private void connect () {
        txtQuestion = findViewById(R.id.edit_question);
        txtAnswer = findViewById(R.id.edit_answer);
        correctAnswer = findViewById(R.id.type_answer);
        btnAdd = findViewById(R.id.btn_add_question);
    }
}
