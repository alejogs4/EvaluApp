package com.ags.agsmvmm.evaluacionesudem;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
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
 * Created by Alejandro Garcia on 20/03/2018.
 */

public class AddQuestion extends AppCompatActivity {

    private EditText txtQuestion;
    private EditText txtAnswer;
    private TextInputLayout textInputQuestion;
    private TextInputLayout textInputAnswer;

    private RadioGroup correctAnswer;
    private Button btnAdd;
    private Spinner spinnerTypeTest;

    private String type;

    private int interval = 1;
    private int questionsCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_question);
        connect();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(AddQuestion.this,R.array.type_array,android.R.layout.simple_dropdown_item_1line);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTypeTest.setAdapter(adapter);

        Bundle extras = getIntent().getExtras();
        final String id = extras.getString("id");


        // Eventos de los elementos de la interfaz
        btnAdd.setOnClickListener(view -> {
            questionsCount++;
            if ( isEqualQuestionCountToInterval() ) {
                setVisibilityOfTheElements(false,0);
                resetQuestionsCount();
            }
            addQuestion(id,type);
        });

        spinnerTypeTest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if( i != 0) {
                    setVisibilityOfTheElements(true,i);
                    setTypeQuestion( getType(i) );
                    setInterval( setQuestionsInterval(i) );
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /**
     * Agrega pregunta haciendo una llamada a la API
     * @param id
     * @param type
     */
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
                type.equals("3") ? true : getCorrect()
        );
        question.enqueue(new Callback<List<Questions>>() {
            @Override
            public void onResponse(Call<List<Questions>> call, Response<List<Questions>> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(AddQuestion.this,"Pregunta guardada",Toast.LENGTH_SHORT).show();
                    txtAnswer.setText("");
                    if(getCorrect()) correctAnswer.setVisibility(View.INVISIBLE);
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

    /**
     * Verifica si la opcion marcada es correcta
     * @return true si se marco la correcta false en caso contrario
     */
    private boolean getCorrect () {
        return correctAnswer.getCheckedRadioButtonId() == R.id.true_answer;
    }

    /**
     * Agrega respuesta a la pregunta
     * @param type
     * @return
     */
    private String getAnswer (String type) {
        if(type == "2"){
            return getCorrect() ? "VERDADERO" : "FALSO";
        }
        return txtAnswer.getText().toString();
    }

    /**
     * Agrega intervalo para mostrar o no los elementos
     * @param i
     * @return 4 si el tipo de pregunta es cuatro opciones, y 1 si es verdadero falso o completar
     */
    private int setQuestionsInterval (int i) {
        if( isOfFourOptions(i) ) return 4;
        return 1;
    }

    /**
     * Oculta o descubre los elementos
     * @param visibility
     */
    private void setVisibilityOfTheElements (boolean visibility,int type) {
        if( visibility && isOfFourOptions(type) ) {
            textInputAnswer.setVisibility(View.VISIBLE);
            textInputQuestion.setVisibility(View.VISIBLE);
            correctAnswer.setVisibility(View.VISIBLE);
            btnAdd.setVisibility(View.VISIBLE);
            spinnerTypeTest.setVisibility(View.INVISIBLE);
        }
        else if( visibility && isTrueOrFalse(type) ) {
            textInputQuestion.setVisibility(View.VISIBLE);
            correctAnswer.setVisibility(View.VISIBLE);
            btnAdd.setVisibility(View.VISIBLE);
            spinnerTypeTest.setVisibility(View.INVISIBLE);
        }
        else if (visibility && isToComplete(type)) {
            textInputAnswer.setVisibility(View.VISIBLE);
            textInputQuestion.setVisibility(View.VISIBLE);
            btnAdd.setVisibility(View.VISIBLE);
            spinnerTypeTest.setVisibility(View.INVISIBLE);
        }
        else {
            textInputAnswer.setVisibility(View.INVISIBLE);
            textInputQuestion.setVisibility(View.INVISIBLE);
            correctAnswer.setVisibility(View.INVISIBLE);
            btnAdd.setVisibility(View.INVISIBLE);
            spinnerTypeTest.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Consigue el tipo de pregunta
     * @param i
     * @return
     */
    private String getType (int i) {
        if(isOfFourOptions(i)) return "1";
        if(isTrueOrFalse(i)) return "2";
        return "3";
    }

    /**
     * Verifica si el tipo de la pregunta es de cuatro opciones
     * @param type
     * @return
     */
    private boolean isOfFourOptions (int type) { return type == 1; }

    /**
     * Verifica si el tipo de la pregunta es verdadero o falso
     * @param type
     * @return
     */
    private boolean isTrueOrFalse (int type) { return  type == 2; }

    /**
     * Verifica si el tipo de pregunta es de completar
     * @param type
     * @return
     */
    private boolean isToComplete (int type) { return  type == 3; }

    // Setters

    /**
     * Agrega el valor al intervalo de preguntas
     * @param interval
     */
    private void setInterval (int interval) { this.interval = interval; }

    /**
     * Agrega valor al tipo de pregunta
     * @param type
     */
    private void  setTypeQuestion (String type) { this.type = type; }

    /**
     * Resetea el contador de preguntas
     */
    private void resetQuestionsCount () { this.questionsCount = 0; }

    /**
     * Verifica si el numero de preguntas es igual al intervalo
     * @return
     */
    private boolean isEqualQuestionCountToInterval () { return questionsCount == interval; }

    /**
     * conecta los elementos de la interfaz con el codigo java
     */
    private void connect () {
        spinnerTypeTest = findViewById(R.id.spinner_type_test);
        txtQuestion = findViewById(R.id.edit_question);
        txtAnswer = findViewById(R.id.edit_answer);
        textInputQuestion = findViewById(R.id.text_input_question);
        textInputAnswer = findViewById(R.id.text_input_answer);
        correctAnswer = findViewById(R.id.type_answer);
        btnAdd = findViewById(R.id.btn_add_question);
    }
}
