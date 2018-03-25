package com.ags.agsmvmm.evaluacionesudem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import POJOS.Student;
import POJOS.Teacher;
import PetitionsInterfaces.CallsService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Alejandro Garcia Serna on 20/03/2018.
 */

public class StudenLogin extends AppCompatActivity {

    private TextView linkRegistry;
    private EditText userField;
    private EditText passwordField;
    private Button loginButton;
    private List<Student> students = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_student);
        connect();

        loginButton.setOnClickListener(view -> {
            if ( thereAreEmptyInput() ) {
                Toast.makeText(StudenLogin.this,"Todos los campos deben estar llenos",Toast.LENGTH_SHORT).show();
                return;
            }
            executeLogin();
        });

        linkRegistry.setOnClickListener(view -> {
            Intent i = new Intent(StudenLogin.this,StudentRegistry.class);
            startActivity(i);
        });
    }

    /**
     * Metodo que verifica si hay inputs vacios
     * retorna true si hay alguno vacio false en caso contrario
     * @return
     */
    private boolean thereAreEmptyInput ()  {
        return userField.getText().toString().isEmpty() || passwordField.getText().toString().isEmpty() ;
    }

    /**
     * Verifica si el usuario esta registrado en la base de datos de ser asi lanza una nueva actividad
     * y si no muestra un mensaje diciendo que hay algun dato incorrecto
     */
    private void executeLogin () {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(TeacherLogin.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CallsService service = retrofit.create(CallsService.class);
        final Call<List<Student>> teacher = service.loginStudent(
                userField.getText().toString(),
                passwordField.getText().toString()
        );

        teacher.enqueue(new Callback<List<Student>>() {

            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                if(response.code() == 200){
                    students = response.body();
                    Intent i = new Intent(StudenLogin.this,StudentTest.class);
                    i.putExtra("name",students.get(0).getUsername());
                    i.putExtra("id",students.get(0).getId());
                    startActivity(i);
                }
                else {
                    Toast.makeText(StudenLogin.this,"Usuario o contraseña incorrectas",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
                Toast.makeText(StudenLogin.this,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void connect () {
        linkRegistry = findViewById(R.id.registry_link);
        userField = findViewById(R.id.user_field_student);
        passwordField = findViewById(R.id.password_field_student);
        loginButton = findViewById(R.id.btn_login_student);
    }
}
