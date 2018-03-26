package com.ags.agsmvmm.evaluacionesudem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

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

public class AddTest extends AppCompatActivity {

    private Button addTestButton;
    private EditText editTitle;
    private EditText editValue;
    private RadioGroup groupType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_test);

        Bundle extras = getIntent().getExtras();
        final String id = extras.getString("id");

        connect();

        addTestButton.setOnClickListener(view -> {
            if ( thereAreEmptyInputs() ) {
                Toast.makeText(AddTest.this,"Tienes que llenar todos los campos",Toast.LENGTH_SHORT).show();
                return;
            }
            addTest(id);
        });

    }

    /**
     * Metodo que crea nuevo examen
     * @param id
     */
    private void addTest(String id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TeacherLogin.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CallsService service = retrofit.create(CallsService.class);
        final Call<List<Tests>> test = service.addTest(
                editTitle.getText().toString(),
                editValue.getText().toString(),
                id,
                getType()
        );
        test.enqueue(new Callback<List<Tests>>() {
            @Override
            public void onResponse(Call<List<Tests>> call, Response<List<Tests>> response) {
                if(response.code() == 201) {
                    finish();
                }
                else {
                    Toast.makeText(AddTest.this, R.string.not_create_test,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Tests>> call, Throwable t) {
                Toast.makeText(AddTest.this,R.string.not_create_test,Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Method that return
     * @return 1 si el examen es de verdadero o falso,2 si el examen es de 4 opciones
     */
    private String getType () {
        return  groupType.getCheckedRadioButtonId() == R.id.false_true ? "1" : "2";
    }

    /**
     * Verifica si hay
     * @return
     */
    private boolean thereAreEmptyInputs () {
        return editTitle.getText().toString().isEmpty() || editValue.getText().toString().isEmpty();
    }

    private void connect () {
        addTestButton = findViewById(R.id.btn_add_a);
        editTitle = findViewById(R.id.edit_title);
        editValue = findViewById(R.id.edit_value);
        groupType = findViewById(R.id.type_group);
    }
}
