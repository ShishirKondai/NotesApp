package com.example.androidnotes;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonWriter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.*;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {

    private final List<Note> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private Note note;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private NoteAdapter nAdapter;
    private int position;
    int isNewNote = 1, isOldNote = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readJSON();
        setTitle(getResources().getString(R.string.app_name) + " (" + list.size() + ")");

        recyclerView = findViewById(R.id.recycler);

        nAdapter = new NoteAdapter(this, list);
        recyclerView.setAdapter(nAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                this::handleResult);
    }

    @Override
    protected void onPause() {
        writeJSON();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.about_icon) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }
        else if (item.getItemId() == R.id.add_icon) {
            Intent intent = new Intent(this, EditActivity.class);
            activityResultLauncher.launch(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void readJSON() {
        try {
            InputStream inputStream = getApplicationContext().openFileInput(getString(R.string.fileName));
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONArray jsonArray = new JSONArray(sb.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String noteTitle = jsonObject.getString("noteTitle");
                String noteText = jsonObject.getString("noteText");
                long lastDateUpdate = jsonObject.getLong("lastNoteUpdateDate");
                Note note = new Note(noteTitle, noteText);
                note.setLastUpdate(lastDateUpdate);
                list.add(note);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void writeJSON() {
        try {
            FileOutputStream fileOutputStream = getApplicationContext().openFileOutput(getResources().getString(R.string.fileName), Context.MODE_PRIVATE);
            JsonWriter jsonWriter = new JsonWriter(new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8));

            jsonWriter.setIndent("  ");
            jsonWriter.beginArray();
            for (Note note : list) {
                jsonWriter.beginObject();
                jsonWriter.name("noteTitle").value(note.getTitle());
                jsonWriter.name("noteText").value(note.getDescription());
                jsonWriter.name("lastNoteUpdateDate").value(note.getLastTime().getTime());
                jsonWriter.endObject();
            }
            jsonWriter.endArray();
            jsonWriter.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

        position = recyclerView.getChildAdapterPosition(view);
        note = list.get(position);
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("Edit Note", note);
        activityResultLauncher.launch(intent);
    }

    @Override
    public boolean onLongClick (View view) {
        position = recyclerView.getChildAdapterPosition(view);
        note = list.get(position);
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage("Delete Note '" + note.getTitle() + "'?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        list.remove(position);
                        Collections.sort(list);
                        nAdapter.notifyDataSetChanged();

                        setTitle(getResources().getString(R.string.app_name) + " (" + list.size() + ")");
                        return;
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                })
                .show();
        return true;
    }

    public void handleResult(ActivityResult result) {
        if (result.getResultCode() == isNewNote) {
            Intent data = result.getData();
            if (data != null) {
                note = (Note) data.getSerializableExtra("New Note");
                if (note != null) {
                    list.add(note);
                    Collections.sort(list);
                    nAdapter.notifyDataSetChanged();
                    String app_name = getResources().getString(R.string.app_name);
                    setTitle(app_name + " (" + list.size() + ")");
                }
            }
        }

        if (result.getResultCode() == isOldNote) {
            Intent data = result.getData();

            if (data != null) {
                note = (Note) data.getSerializableExtra("Edit Note");
                if (note == null) {
                    Toast.makeText(this, R.string.toastMessage, Toast.LENGTH_LONG).show();
                }
                else {
                    list.get(position).setTitle(note.getTitle());
                    list.get(position).setLastUpdate(System.currentTimeMillis());
                    list.get(position).setDescription(note.getDescription());
                    Collections.sort(list);
                    nAdapter.notifyDataSetChanged();
                }
            }
        }

    }
}