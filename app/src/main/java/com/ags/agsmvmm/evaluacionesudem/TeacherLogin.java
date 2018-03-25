package com.ags.agsmvmm.evaluacionesudem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import POJOS.Teacher;
import PetitionsInterfaces.CallsService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Alejandro Garcia on 18/03/2018.
 */

public class TeacherLogin extends AppCompatActivity {

    private EditText userField;
    private EditText passwordField;
    private Button loginButton;
    private List<Teacher> teachers = new ArrayList<>();

    public final static String API_URL = "http://167.99.99.255:3001/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_login);

        connect();
        loginButton.setOnClickListener(view -> {
            if ( thereAreEmptyInputs() ){
                Toast.makeText(TeacherLogin.this,"Debes ingresar todos los datos",Toast.LENGTH_SHORT).show();
                return;
            }
            executeLogin();
        });
    }

    private boolean thereAreEmptyInputs () {
        return userField.getText().toString().isEmpty() || passwordField.getText().toString().isEmpty();
    }

    public void executeLogin () {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CallsService service = retrofit.create(CallsService.class);
        final Call<List<Teacher>> teacher = service.loginTeacher(
                userField.getText().toString(),
                passwordField.getText().toString()
        );

        teacher.enqueue(new Callback<List<Teacher>>() {

            @Override
            public void onResponse(Call<List<Teacher>> call, Response<List<Teacher>> response) {
                if(response.code() == 200){
                    teachers = response.body();
                    Intent i = new Intent(TeacherLogin.this,TestsTeacher.class);
                    i.putExtra("name",teachers.get(0).getNombre());
                    i.putExtra("id",teachers.get(0).getId());
                    startActivity(i);
                }
                else {
                    Toast.makeText(TeacherLogin.this,"Usuario o contraseña incorrectas",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Teacher>> call, Throwable t) {
                Toast.makeText(TeacherLogin.this,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void connect () {
        userField = findViewById(R.id.user_field);
        passwordField = findViewById(R.id.password_field);
        loginButton = findViewById(R.id.btn_login);
    }
}
