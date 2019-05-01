package com.sreekanth.dev.ilahianz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class NotesActivity extends AppCompatActivity {

    static ArrayList<String> Notes = new ArrayList<>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        listView = findViewById(R.id.NotesList);
        Notes.add("Example 1");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Notes);

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                Intent intent = new Intent(getApplicationContext(),NoteEditor.class);
//                intent.putExtra("noteId",position);
//                startActivity(intent);
            }
        });

    }
}
