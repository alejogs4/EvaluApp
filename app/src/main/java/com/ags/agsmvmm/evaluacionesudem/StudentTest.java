package com.ags.agsmvmm.evaluacionesudem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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

public class StudentTest extends AppCompatActivity {

    private Button reviewNotesButton;
    private ListView listTests;
    private ProgressBar progressTests;
    private TextView welcomeTitle;

    private List<Tests> tests = new ArrayList<>();
    private ArrayList<String> testsArray = new ArrayList<>();
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_test);
        connect();
        loadTests();
        Bundle extras = getIntent().getExtras();

        String username = extras.getString("name");
        final String id = extras.getString("id");
        welcomeTitle.setText(welcomeTitle.getText().toString() + "  " + username);

        adapter = new ArrayAdapter(StudentTest.this,android.R.layout.simple_list_item_1,testsArray);
        listTests.setAdapter(adapter);

        reviewNotesButton.setOnClickListener(view -> {
            Intent i = new Intent(StudentTest.this,NotesActivity.class);
            i.putExtra("id",id);
            startActivity(i);
        });

        listTests.setOnItemClickListener((adapterView, view, i, l) -> {
            String idTest = testsArray.get(i).split(" ")[0];
            Intent in = new Intent(StudentTest.this,TestActivity.class);
            in.putExtra("id",idTest);
            in.putExtra("idStudent",id);
            in.putExtra("typeTest", tests.get(i).getTestType()+"");
            startActivity(in);
        });

    }

    private void loadTests () {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(TeacherLogin.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CallsService service = retrofit.create(CallsService.class);

        final Call<List<Tests>> test = service.getTests();

        test.enqueue(new Callback<List<Tests>>() {

            @Override
            public void onResponse(Call<List<Tests>> call, Response<List<Tests>> response) {
                if(response.code() == 200) {
                    tests = response.body();
                    for(Tests t : tests) {
                        testsArray.add(t.getId() + " " + t.getTitle());
                    }
                    adapter.notifyDataSetChanged();
                    progressTests.setVisibility(View.INVISIBLE);
                }
                else {
                    Toast.makeText(StudentTest.this,"No se encontraron pruebas",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Tests>> call, Throwable t) {
                Toast.makeText(StudentTest.this,"Error al conseguir",Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void connect () {
        welcomeTitle = findViewById(R.id.txt_name_welcome_student);
        reviewNotesButton = findViewById(R.id.btn_review_notes);
        listTests = findViewById(R.id.list_tests_student);
        progressTests = findViewById(R.id.pb_student);
    }
}
