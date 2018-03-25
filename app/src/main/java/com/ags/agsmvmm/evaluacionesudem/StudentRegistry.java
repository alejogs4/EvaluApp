package com.ags.agsmvmm.evaluacionesudem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import POJOS.Student;
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

public class StudentRegistry extends AppCompatActivity {

    private EditText id;
    private EditText user;
    private EditText pass;
    private Button registryButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_registry);
        connect();

        registryButton.setOnClickListener(view -> {
            if(id.getText().toString().isEmpty() || user.getText().toString().isEmpty() || pass.getText().toString().isEmpty()) {
                Toast.makeText(StudentRegistry.this,"Llena todos los campos",Toast.LENGTH_SHORT).show();
                return;
            }
            registry();
        });
    }

    /**
     * Ejecuta el registro del usuario
     */
    private void registry() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TeacherLogin.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CallsService service = retrofit.create(CallsService.class);

        final Call<List<Student>> student = service.registryStudent(
                id.getText().toString(),
                user.getText().toString(),
                pass.getText().toString()
        );

        student.enqueue(new Callback<List<Student>>() {

            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                if(response.code() == 201) {
                    Toast.makeText(StudentRegistry.this,"Registro exitoso",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(StudentRegistry.this,"Registro infructuoso",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
                Toast.makeText(StudentRegistry.this,"Registro infructuoso",Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void connect () {
        id = findViewById(R.id.id_field_registry);
        user = findViewById(R.id.user_field_registry);
        pass = findViewById(R.id.password_field_registry);
        registryButton = findViewById(R.id.btn_registry);
    }
}
