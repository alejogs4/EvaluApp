package com.ags.agsmvmm.evaluacionesudem;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

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
 * Created by Alejandro Garcia on 19/03/2018.
 */

public class TestsTeacher  extends AppCompatActivity{

    private Button addTestButton;
    private ListView listTests;
    private ProgressBar progressTests;
    private TextView welcomeTitle;
    private TextView thereAreNotTests;

    private List<Tests> tests = new ArrayList<>();
    private ArrayList<String> testsArray = new ArrayList<>();
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        connect();

        adapter = new ArrayAdapter(TestsTeacher.this,android.R.layout.simple_list_item_1,testsArray);
        listTests.setAdapter(adapter);

        Bundle extras = getIntent().getExtras();
        String name = extras.getString("name");
        final String id = extras.getString("id");

        welcomeTitle.setText(getString(R.string.welcome_teacher) + name);

        addTestButton.setOnClickListener(view -> {
            Intent i = new Intent(TestsTeacher.this,AddTest.class);
            i.putExtra("id",id);
            startActivity(i);
        });
        loadTests();
        listTests.setOnItemClickListener((adapterView, view, i, l) -> {
            String idTest = testsArray.get(i).split(" ")[0];
            Intent in = new Intent(TestsTeacher.this,AddQuestion.class);
            in.putExtra("id",idTest);
            in.putExtra("idTest",tests.get(i).getTestType().toString());
            startActivity(in);
        });
        listTests.setOnItemLongClickListener((adapterView, view, i, l) -> {
            String idTest = testsArray.get(i).split(" ")[0];
            Intent in = new Intent(TestsTeacher.this,DeleteActivity.class);
            in.putExtra("id",idTest);
            startActivity(in);
            return false;
        });
    }

    protected void onRestart () {
        super.onRestart();
        testsArray.clear();
        progressTests.setVisibility(View.VISIBLE);
        loadTests();
    }

    private void loadTests () {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TeacherLogin.API_URL)
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
                    thereAreNotTests.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<List<Tests>> call, Throwable t) {
                Toast.makeText(TestsTeacher.this,"Error al conseguir",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void connect () {
        welcomeTitle = findViewById(R.id.txt_name_welcome);
        addTestButton = findViewById(R.id.btn_add_test);
        listTests = findViewById(R.id.list_tests);
        progressTests = findViewById(R.id.pb_tests);
        thereAreNotTests = findViewById(R.id.txt_no_tests);
    }

}
