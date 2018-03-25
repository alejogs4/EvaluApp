package com.ags.agsmvmm.evaluacionesudem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import POJOS.Notes;
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

public class NotesActivity extends AppCompatActivity {

    private ListView listNotes;
    private ArrayList<String> notesArray = new ArrayList<>();
    private ArrayAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_activity);

        Bundle extras = getIntent().getExtras();
        String id = extras.getString("id");
        listNotes = findViewById(R.id.list_notes);

        adapter = new ArrayAdapter(NotesActivity.this,android.R.layout.simple_list_item_1,notesArray);
        listNotes.setAdapter(adapter);
        load(id);
    }

    /**
     * Carga las notas del estudiante y las muestra en el listView
     * @param id
     */
    private void load(String id) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(TeacherLogin.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CallsService service = retrofit.create(CallsService.class);
        final Call<List<Notes>> notes = service.getNotesByUser(id);
        notes.enqueue(new Callback<List<Notes>>() {
            @Override
            public void onResponse(Call<List<Notes>> call, Response<List<Notes>> response) {
                if(response.code() == 200) {
                    for(Notes note : response.body()) {
                        notesArray.add(note.getUsername() + " " + note.getValueTest());
                    }
                    adapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(NotesActivity.this,"No se consiguio data",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Notes>> call, Throwable t) {
                Toast.makeText(NotesActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
