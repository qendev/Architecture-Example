package com.example.architectureexample;



import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    //create a constant for coderequest
    public static final int ADD_NOTE_REQUEST=1;
    public static final int EDIT_NOTE_REQUEST=2;

    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FloatingActionButton buttonAddNote=findViewById(R.id.button_add_note);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, AddEditNoteActivity.class);
                startActivityForResult(intent,ADD_NOTE_REQUEST);
            }
        });

        RecyclerView recyclerview=findViewById(R.id.recycler_view);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setHasFixedSize(true);


         final NoteAdapter adapter=new NoteAdapter();
        recyclerview.setAdapter(adapter);

           noteViewModel= ViewModelProviders.of(this).get(NoteViewModel.class);
           noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
               @Override
               public void onChanged(List<Note> notes) {
                   adapter.submitList(notes);

               }
           });

           //This convinience class will make the recyclerview swipperble.
           new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                   ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
               @Override
               //For drag and drop functionality but we will not use it in this app.
               public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                   return false;
               }

               @Override
               //For swipping functionality;we will use this for this app.
               public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                   noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                   Toast.makeText(MainActivity.this, "Note Deleted!!!", Toast.LENGTH_SHORT).show();


               }
           }).attachToRecyclerView(recyclerview);

           adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
               @Override
               public void onItemClick(Note note) {
                   //to open the activity where we edit the note
                   Intent intent=new Intent(MainActivity.this, AddEditNoteActivity.class);
                   //to send Title,Description,Priority to this Activity to put em in the EditText fiels so as to edit em.
                   intent.putExtra(AddEditNoteActivity.EXTRA_ID,note.getId());
                   intent.putExtra(AddEditNoteActivity.EXTRA_TITLE,note.getTitle());
                   intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION,note.getDescription());
                   intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY,note.getPriority());
                   startActivityForResult(intent,EDIT_NOTE_REQUEST);

               }
           });


           }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ADD_NOTE_REQUEST && resultCode==RESULT_OK){
            String title=data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description=data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int priority=data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY,1);

            Note note =new Note(title,description,priority);
            noteViewModel.insert(note);

            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();
        }else if(requestCode==EDIT_NOTE_REQUEST && resultCode==RESULT_OK){
            int id=data.getIntExtra(AddEditNoteActivity.EXTRA_ID,-1);

            if(id==-1){
                Toast.makeText(this, "Note Cant Be Updated!", Toast.LENGTH_SHORT).show();
                return;
            }

            String title=data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description=data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int priority=data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY,1);

            Note note=new Note(title,description,priority);
            note.setId(id);
            noteViewModel.update(note);

            Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();


        }else{
            Toast.makeText(this, "Note not saved!!!", Toast.LENGTH_SHORT).show();
        }
    }
    //to implement an options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }
    //When a menu is clicked.Android will call the  callback function below on the Activity class.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_all_notes:
                noteViewModel.deleteAllNote();
                Toast.makeText(this, "All Notes Deleted!!", Toast.LENGTH_SHORT).show();
                return true;
                default:
                    return super.onOptionsItemSelected(item);

        }

    }
}

