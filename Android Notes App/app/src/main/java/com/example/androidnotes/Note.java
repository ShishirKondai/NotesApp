package com.example.androidnotes;

import android.util.JsonWriter;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.*;

public class Note implements Serializable, Comparable<Note> {

    private String title, desc;
    private long time;
    private Date prevUpdated;

    public Note(String title, String text){
        this.title = title;
        this.desc = text;
        this.prevUpdated = new Date();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLastUpdate(long lastUpdate) {
        this.prevUpdated = new Date(lastUpdate);
    }

    public void setDescription(String text) {
        this.desc = text;
    }

    public String getTitle() {
        return title;
    }

    public long getTime() {
        return time;
    }

    public Date getLastTime() {
        return prevUpdated;
    }

    public String getDescription() {
        return desc;
    }

    @Override
    public int compareTo(Note note) {
        int pos = 1; int neg = -1; int noValue = 0;
        if (prevUpdated.before(note.prevUpdated)) {
            return pos;
        }
        else if (prevUpdated.after(note.prevUpdated)) {
            return neg;
        }
        return noValue;
    }

    @NonNull
    public String toString() {
        String result = "";
        try {
            StringWriter sw = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(sw);
            jsonWriter.setIndent("  ");
            jsonWriter.beginObject();
            jsonWriter.name("title").value(getTitle());
            jsonWriter.name("time").value(getTime());
            jsonWriter.name("description").value(getDescription());
            jsonWriter.endObject();
            jsonWriter.close();
            return sw.toString();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }
}

