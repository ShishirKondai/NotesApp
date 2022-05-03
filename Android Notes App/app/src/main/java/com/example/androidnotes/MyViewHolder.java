package com.example.androidnotes;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder{
    TextView title;
    TextView time;
    TextView description;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.note_title);
        time = itemView.findViewById(R.id.note_time);
        description = itemView.findViewById(R.id.note_description);
    }
}

