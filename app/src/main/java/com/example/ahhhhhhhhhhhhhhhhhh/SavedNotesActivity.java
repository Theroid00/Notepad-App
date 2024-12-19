package com.example.ahhhhhhhhhhhhhhhhhh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ahhhhhhhhhhhhhhhhhh.note.Note;
import com.example.ahhhhhhhhhhhhhhhhhh.note.NoteAdapter;
import com.example.ahhhhhhhhhhhhhhhhhh.NoteDatabase;
import com.example.ahhhhhhhhhhhhhhhhhh.note.NoteDao;

import java.io.Serializable;
import java.util.List;

public class SavedNotesActivity extends AppCompatActivity {
    private NoteAdapter noteAdapter;
    private TextView noNotesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);

        // Hide the ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_view_notes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(
                note -> {
                    // Delete the note on long click
                    NoteDatabase db = NoteDatabase.getInstance(getApplicationContext());
                    NoteDao noteDao = db.noteDao();

                    new Thread(() -> {
                        noteDao.delete(note);
                        runOnUiThread(() -> {
                            noteAdapter.removeNote(note);
                            Toast.makeText(SavedNotesActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
                            checkNotesVisibility();
                        });
                    }).start();
                },
                note -> {
                    // Handle note click
                    Intent intent = new Intent(SavedNotesActivity.this, NoteDetailActivity.class);
                    intent.putExtra("note", (Serializable) note);
                    startActivity(intent);
                }
        );
        recyclerView.setAdapter(noteAdapter);

        noNotesTextView = findViewById(R.id.text_view_no_notes);

        loadNotes();

        Button backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(SavedNotesActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes(); // Refresh the list when returning to this activity
    }

    private void loadNotes() {
        NoteDatabase db = NoteDatabase.getInstance(getApplicationContext());
        NoteDao noteDao = db.noteDao();

        noteDao.getAllNotes().observe(this, notes -> {
            noteAdapter.setNotes(notes);
            checkNotesVisibility();
        });
    }

    private void checkNotesVisibility() {
        if (noteAdapter.getItemCount() == 0) {
            noNotesTextView.setVisibility(View.VISIBLE);
        } else {
            noNotesTextView.setVisibility(View.GONE);
        }
    }
}