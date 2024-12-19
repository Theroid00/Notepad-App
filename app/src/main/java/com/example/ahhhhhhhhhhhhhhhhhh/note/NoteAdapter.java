package com.example.ahhhhhhhhhhhhhhhhhh.note;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ahhhhhhhhhhhhhhhhhh.NoteDetailActivity;
import com.example.ahhhhhhhhhhhhhhhhhh.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private List<Note> notes = new ArrayList<>();
    private final OnNoteLongClickListener onNoteLongClickListener;
    private final OnNoteClickListener onNoteClickListener;

    public interface OnNoteLongClickListener {
        void onNoteLongClick(Note note);
    }

    public interface OnNoteClickListener {
        void onNoteClick(Note note);
    }

    public NoteAdapter(OnNoteLongClickListener onNoteLongClickListener, OnNoteClickListener onNoteClickListener) {
        this.onNoteLongClickListener = onNoteLongClickListener;
        this.onNoteClickListener = onNoteClickListener;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    public void removeNote(Note note) {
        notes.remove(note);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.titleTextView.setText(note.getTitle());

        holder.itemView.setOnLongClickListener(v -> {
            onNoteLongClickListener.onNoteLongClick(note);
            return true;
        });

        holder.itemView.setOnClickListener(v -> {
            onNoteClickListener.onNoteClick(note);
            Intent intent = new Intent(holder.itemView.getContext(), NoteDetailActivity.class);
            intent.putExtra("note", (Serializable) note);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.text_view_title);
        }
    }
}