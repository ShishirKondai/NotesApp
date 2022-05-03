package com.example.androidnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    private EditText title_note, description_note;
    private long prevTime;
    private Note note;

    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String note_title, note_description;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        title_note = findViewById(R.id.note_title);
        description_note = findViewById(R.id.edit_description);

        Intent intent = getIntent();
        if (intent.hasExtra("Edit Note")) {
            note = (Note) intent.getSerializableExtra("Edit Note");
            if (note != null) {
                note_title = note.getTitle();
                note_description = note.getDescription();
                prevTime = intent.getLongExtra("Time", 0);

                title_note.setText(note_title);
                description_note.setText(note_description);
            }
            flag = false;
        }
        else {
            flag = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_icon) {
            onSaveClick();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onSaveClick() {
        String noteTitle = title_note.getText().toString();
        String noteText = description_note.getText().toString();

        if (!flag) {
            if (noteTitle.trim().isEmpty() == true) {
                titleMissing();
                return;
            }
            note.setTitle(noteTitle);
            note.setLastUpdate(prevTime);
            note.setDescription(noteText);
            Intent intent = new Intent();
            intent.putExtra("Edit Note", note);
            setResult(2, intent);
            finish();
        }
        else {
            if (noteTitle.trim().isEmpty()) {
                titleMissing();
                return;
            }
            Note note = new Note(noteTitle, noteText);
            Intent intent = new Intent();
            intent.putExtra("New Note", note);
            setResult(1, intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {

        String title = title_note.getText().toString();
        String description = description_note.getText().toString();

        if (flag && (title.trim().isEmpty()) == false) {
            askSave();
        }
        else if (flag && (description.trim().isEmpty()) == false) {
            askSave();
        }
        else if (!(note == null) && (title.equals(note.getTitle())) == false) {
            askSave();
        }
        else if (!(note == null) && (description.equals(note.getDescription())) == false) {
            askSave();
        }
        else {
            EditActivity.super.onBackPressed();
        }
    }

    public void askSave() {
        String noteTitle = title_note.getText().toString();
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage( "Your note is not saved! " + "\n" + "Save Note '" + noteTitle + "'?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onSaveClick();
                        finish();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditActivity.super.onBackPressed();
                        finish();
                    }
                })
                .show();
        //alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.background_dark);
    }

    public void titleMissing() {
        Toast.makeText(this, R.string.titleMissing, Toast.LENGTH_SHORT).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Cannot Save Without Title. Exit without saving ?");
        builder.setPositiveButton("YES", (dialogInterface, i) ->
                EditActivity.super.onBackPressed());
        builder.setNegativeButton("NO", (dialogInterface, i) -> {
            return;
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        //alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.background_dark);
    }

}