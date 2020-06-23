package com.example.android.notepad.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.android.notepad.database.NotesDB;
import com.example.android.notepad.database.NotesDao;
import com.example.android.notepad.entity.Notes;
import com.example.android.notepad.R;

import java.util.Date;
import java.util.Objects;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class EditNoteActivity extends AppCompatActivity {
    private EditText inputNoteTitle;
    private EditText inputNote;

    private int note_id;
    private NotesDao dao;
    private Notes temp;
    public static final String NOTE_EXTRA_Key = "notes_id";
    public static final String NOTE_TITLE_Key = "title";
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // set theme
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
        int theme = sharedPreferences.getInt(MainActivity.THEME_Key, R.style.AppTheme);
        setTheme(theme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edite_note);

        Toolbar toolbar = findViewById(R.id.edit_note_activity_toolbar);
        setSupportActionBar(toolbar);

        inputNoteTitle = findViewById(R.id.input_note_title);
        inputNote = findViewById(R.id.input_note);
        dao = NotesDB.getInstance(this).notesDao();

        note_id = getIntent().getIntExtra(NOTE_EXTRA_Key, 0);
        temp = dao.getNoteById(note_id);
        if (note_id != 0) {
//            if (Objects.requireNonNull(getIntent().getExtras()).getInt(NOTE_EXTRA_Key, 0) != 0) {
            inputNoteTitle.setText(temp.getNoteTitle());
//            int id = Objects.requireNonNull(getIntent().getExtras()).getInt(NOTE_EXTRA_Key, 0);
            temp = dao.getNoteById(note_id);
            inputNote.setText(temp.getNoteText());
        } else {
            //新的note
            inputNoteTitle.setText(getIntent().getStringExtra(NOTE_TITLE_Key));
            inputNote.setFocusable(true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edite_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save_note)
            onSaveNote();
        return super.onOptionsItemSelected(item);
    }

    //与dao交互的部分
    private void onSaveNote() {
        String title = inputNoteTitle.getText().toString();
        String text = inputNote.getText().toString();
        if (!text.isEmpty()) {
            long date = new Date().getTime(); // get  system time
            // if  exist update els crete new
            if (temp == null) {
                // 以Notes的形式插入dao中
                temp = new Notes(title, text, date);
                dao.insertNote(temp); // create new note and inserted to database
            } else {
                temp.setNoteText(text);
                temp.setNoteDate(date);
                dao.updateNote(temp); // change text and date and update note on database
            }

            finish(); // return to the MainActivity
        }

    }
}
