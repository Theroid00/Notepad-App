package com.example.ahhhhhhhhhhhhhhhhhh;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ahhhhhhhhhhhhhhhhhh.note.Note;
import com.example.ahhhhhhhhhhhhhhhhhh.NoteDatabase;
import com.example.ahhhhhhhhhhhhhhhhhh.note.NoteDao;

public class NewNoteActivity extends AppCompatActivity {
    private EditText titleEditText, contentEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        // Hide the ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        titleEditText = findViewById(R.id.edit_text_title);
        contentEditText = findViewById(R.id.edit_text_content);
        Button saveButton = findViewById(R.id.button_save);
        Button backButton = findViewById(R.id.button_back);

        saveButton.setOnClickListener(v -> saveNote());
        backButton.setOnClickListener(v -> finish());
    }

    private void saveNote() {
        String title = titleEditText.getText().toString().trim();
        String content = contentEditText.getText().toString().trim();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(NewNoteActivity.this, "Title and Content cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        NoteDatabase db = NoteDatabase.getInstance(getApplicationContext());
        NoteDao noteDao = db.noteDao();

        new Thread(() -> {
            noteDao.insert(new Note(title, content));
            runOnUiThread(() -> {
                Toast.makeText(NewNoteActivity.this, "Note saved!", Toast.LENGTH_SHORT).show();
                finish(); // Return to the main page
            });
        }).start();
    }
}