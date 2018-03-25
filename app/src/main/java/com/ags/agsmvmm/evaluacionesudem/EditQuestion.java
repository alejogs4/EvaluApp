package com.ags.agsmvmm.evaluacionesudem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import POJOS.Questions;
import PetitionsInterfaces.CallsService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Alejandro Garcia on 22/03/2018.
 */

public class EditQuestion extends AppCompatActivity {

    private EditText editQuestion;
    private EditText editAnswer;
    private RadioGroup correctAnswer;
    private RadioButton trueOption;
    private RadioButton falseOption;
    private Button btnEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_question);
        connect();

        Bundle extras = getIntent().getExtras();

        final int id = extras.getInt("id");

        editQuestion.setText(extras.getString("question"));

        editAnswer.setText(extras.getString("answer"));

        falseOption.setChecked(!extras.getBoolean("isCorrect"));

        trueOption.setChecked(extras.getBoolean("isCorrect"));

        btnEdit.setOnClickListener(view -> {

            if ( thereAreEmptyInputs() ) {
                Toast.makeText(EditQuestion.this,"Debes llenar todos los campos",Toast.LENGTH_SHORT).show();
                return;
            }
            executeEditQuestion(id);
        });
    }

    private boolean thereAreEmptyInputs () {
        return editQuestion.getText().toString().isEmpty() || editAnswer.getText().toString().isEmpty();
    }

    private void executeEditQuestion(int id) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(TeacherLogin.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CallsService service = retrofit.create(CallsService.class);

        Call<List<Questions>> petition = service.editQuestion(
                editQuestion.getText().toString(),
                editAnswer.getText().toString(),
                getOption(),
                id
        );

        petition.enqueue(new Callback<List<Questions>>() {

            @Override
            public void onResponse(Call<List<Questions>> call, Response<List<Questions>> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(EditQuestion.this,"Pregunta editada correctamente",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else  {
                    Toast.makeText(EditQuestion.this,response.message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Questions>> call, Throwable t) {
                Toast.makeText(EditQuestion.this,"No se pudo editar la pregunta",Toast.LENGTH_SHORT).show();
            }

        });
    }

    private boolean getOption() {
        return  correctAnswer.getCheckedRadioButtonId() == R.id.true_answer_e;
    }

    private void connect () {
        editQuestion = findViewById(R.id.edit_question_e);
        editAnswer = findViewById(R.id.edit_answer_e);
        correctAnswer = findViewById(R.id.type_answer_e);
        btnEdit = findViewById(R.id.btn_add_question_e);
        trueOption = findViewById(R.id.true_answer_e);
        falseOption = findViewById(R.id.false_answer_e);
    }
}
