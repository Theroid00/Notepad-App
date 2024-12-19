package com.example.ahhhhhhhhhhhhhhhhhh;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ahhhhhhhhhhhhhhhhhh.note.Note;
import com.example.ahhhhhhhhhhhhhhhhhh.NoteDatabase;
import com.example.ahhhhhhhhhhhhhhhhhh.note.NoteDao;

public class NoteDetailActivity extends AppCompatActivity {
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        // Hide the ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        TextView titleTextView = findViewById(R.id.text_view_note_title);
        TextView contentTextView = findViewById(R.id.text_view_note_content);
        Button deleteButton = findViewById(R.id.button_delete_note);
        Button backButton = findViewById(R.id.button_back);

        // Get the note details from the intent
        note = (Note) getIntent().getSerializableExtra("note");

        if (note != null) {
            titleTextView.setText(note.getTitle());
            contentTextView.setText(note.getContent());

            // Set dynamic text color
            titleTextView.setTextColor(Color.parseColor("#FF0000")); // Example: Red color
            contentTextView.setTextColor(Color.parseColor("#0000FF")); // Example: Blue color
        }

        deleteButton.setOnClickListener(v -> deleteNote());
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(NoteDetailActivity.this, SavedNotesActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void deleteNote() {
        if (note != null) {
            NoteDatabase db = NoteDatabase.getInstance(getApplicationContext());
            NoteDao noteDao = db.noteDao();

            new Thread(() -> {
                noteDao.delete(note);
                runOnUiThread(() -> {
                    Toast.makeText(NoteDetailActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(NoteDetailActivity.this, SavedNotesActivity.class);
                    startActivity(intent);
                    finish(); // Close the activity
                });
            }).start();
        }
    }
}