package com.ags.agsmvmm.evaluacionesudem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
 * Created by Alejandro Garcia Serna on 20/03/2018.
 */

public class DeleteActivity extends AppCompatActivity {

    private ListView listQuestions;
    private List<Questions> questions = new ArrayList<>();
    private ArrayList<String> questionsArray = new ArrayList<>();
    private ArrayAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_activity);

        listQuestions = findViewById(R.id.delete_list);

        Bundle extras = getIntent().getExtras();
        int id = Integer.parseInt(extras.getString("id"));

        adapter = new ArrayAdapter(DeleteActivity.this,android.R.layout.simple_list_item_1,questionsArray);
        listQuestions.setAdapter(adapter);
        loadQuestions(id);

        /**Abre la actividad de editar pregunta si se da click en la pregunta**/
        listQuestions.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent in = new Intent(DeleteActivity.this,EditQuestion.class);
            in.putExtra("question",questions.get(i).getQuestion());
            in.putExtra("answer",questions.get(i).getAnswer());
            in.putExtra("isCorrect",questions.get(i).getIsCorrect());
            in.putExtra("id",questions.get(i).getId());
            startActivity(in);
        });

        /** Elimina pregunta si el usuario mantiene por un largo tiempo el item presionado**/
        listQuestions.setOnItemLongClickListener((adapterView, view, i, l) -> {
            int id1 = Integer.parseInt(questionsArray.get(i).split(" ")[0]);
            deleteQuestion(id1);
            return false;
        });

    }

    /**
     * Borra pregunta recibiendo el id de la pregunta
     * @param id
     */
    private void deleteQuestion(int id) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(TeacherLogin.API_URL).addConverterFactory(GsonConverterFactory.create())
                .build();
        CallsService service = retrofit.create(CallsService.class);

        final Call<List<Questions>> question = service.deleteQuestion(id);

        question.enqueue(new Callback<List<Questions>>() {

            @Override
            public void onResponse(Call<List<Questions>> call, Response<List<Questions>> response) {
                if(response.isSuccessful()) { finish(); }
            }

            @Override
            public void onFailure(Call<List<Questions>> call, Throwable t) {

            }
        });
    }

    private void loadQuestions (int id) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(TeacherLogin.API_URL).addConverterFactory(GsonConverterFactory.create())
                .build();
        CallsService service = retrofit.create(CallsService.class);

        final Call<List<Questions>> question = service.getQuestionsByTest(id);

        question.enqueue(new Callback<List<Questions>>() {

            @Override
            public void onResponse(Call<List<Questions>> call, Response<List<Questions>> response) {
                if(response.code() == 200) {
                    questions = response.body();
                    for(Questions question : questions) {
                        questionsArray.add(question.getId() + " " +question.getQuestion() + " " + question.getAnswer());
                    }
                    adapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(DeleteActivity.this,"No se encontraron preguntas",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Questions>> call, Throwable t) {
                Toast.makeText(DeleteActivity.this,"Error al conseguir",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
