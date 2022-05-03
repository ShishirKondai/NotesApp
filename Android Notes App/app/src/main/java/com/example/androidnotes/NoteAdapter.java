package com.example.androidnotes;

import android.view.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<MyViewHolder>{

    private final MainActivity mainActivity;
    private final List<Note> noteList;

    public NoteAdapter(MainActivity mainActivity, List<Note> noteList) {
        this.mainActivity = mainActivity;
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_entry, parent, false);

        view.setOnClickListener(mainActivity);
        view.setOnLongClickListener(mainActivity);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int pos) {
        Note note = noteList.get(pos);
        String note_title;
        note_title = note.getTitle();
        if (note_title.length() > 80) {
            note_title = note_title.substring(0, 80) + "...";
        }
        holder.title.setText(note_title);

        String date_time = note.getLastTime().toString();
        Date d = note.getLastTime();
        String formatted_time = date_time.substring(0, date_time.length() - 9);
        holder.time.setText(getCurrentTime(d));

        String desc;
        desc = note.getDescription();
        if (desc.length() > 80){
            desc = desc.substring(0, 80) + "...";
        }
        holder.description.setText(desc);
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public String getCurrentTime(Date d) {
        DateFormat df = new SimpleDateFormat("E MMM d, h:mm a");
        return df.format(d);
    }
}

