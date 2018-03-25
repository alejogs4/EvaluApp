package com.ags.agsmvmm.evaluacionesudem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button teacherLoginButton;
    private Button studentLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connect();
        teacherLoginButton.setOnClickListener(view -> {
            openNewActivity(TeacherLogin.class);
        });
        studentLoginButton.setOnClickListener(view -> {
            openNewActivity(StudenLogin.class);
        });
    }

    /**
     * Abre una nueva actividad pasandole como parametro la actividad a abrir
     * @param c
     */
    private void openNewActivity (Class c) {
        Intent i = new Intent(MainActivity.this,c);
        startActivity(i);
    }

    private void connect () {
        teacherLoginButton = findViewById(R.id.btn_teacher_login);
        studentLoginButton = findViewById(R.id.btn_student_login);
    }

}
