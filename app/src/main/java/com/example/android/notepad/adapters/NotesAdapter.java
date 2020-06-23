package com.example.android.notepad.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.notepad.R;
import com.example.android.notepad.activity.EditNoteActivity;
import com.example.android.notepad.entity.Notes;
import com.example.android.notepad.utils.DateUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.mikepenz.iconics.Iconics.getApplicationContext;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteHolder> {
    private Context context;
    private ArrayList<Notes> notes;
    //    private NoteEventListener listener;
    private boolean multiCheckMode = false;
    public static final String NOTE_EXTRA_Key = "notes_id";

    public NotesAdapter(Context context, ArrayList<Notes> notes) {
        this.context = context;
        this.notes = notes;
    }


    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false);
        return new NoteHolder(v);
    }

    @Override
    public void onBindViewHolder(@NotNull final NoteHolder holder, final int position) {
        final Notes note = getNote(position);
        if (note != null) {
            holder.noteTitle.setText(note.getNoteTitle());
            holder.noteDate.setText(DateUtil.StringToDate(note.getNoteDate()));
            // init note click event
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    listener.onNoteClick(note);

                    //TODO
                    Toast.makeText(getApplicationContext(), note.toString(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    //Calling startActivity() from outside of an Activity
                    // context requires the FLAG_ACTIVITY_NEW_TASK flag. Is this really what you want?
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(NOTE_EXTRA_Key, note.getId());
                    getApplicationContext().startActivity(intent.setClass(getApplicationContext(), EditNoteActivity.class));
                }
            });

            // init note long click
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
//                    listener.onNoteLongClick(note);
                    return false;
                }
            });

            // check checkBox if note selected
            if (multiCheckMode) {
                holder.checkBox.setVisibility(View.VISIBLE); // show checkBox if multiMode on
                holder.checkBox.setChecked(note.isChecked());
            } else holder.checkBox.setVisibility(View.GONE); // hide checkBox if multiMode off


        }
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    private Notes getNote(int position) {
        return notes.get(position);
    }


    /**
     * get All checked notes
     *
     * @return Array
     */
    public List<Notes> getCheckedNotes() {
        List<Notes> checkedNotes = new ArrayList<>();
        for (Notes n : this.notes) {
            if (n.isChecked())
                checkedNotes.add(n);
        }

        return checkedNotes;
    }


    static class NoteHolder extends RecyclerView.ViewHolder {
        TextView noteTitle, noteDate;
        CheckBox checkBox;

        NoteHolder(View itemView) {
            super(itemView);
            noteDate = itemView.findViewById(R.id.note_date);
            noteTitle = itemView.findViewById(R.id.note_title);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }


    public void setMultiCheckMode(boolean multiCheckMode) {
        this.multiCheckMode = multiCheckMode;
        if (!multiCheckMode)
            for (Notes note : this.notes) {
                note.setChecked(false);
            }
        notifyDataSetChanged();
    }
}
