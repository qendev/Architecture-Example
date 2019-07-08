package com.example.architectureexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class AddEditNoteActivity extends AppCompatActivity {

    //create constant keys to be used by intent EXTRAS so as to use the Use start activity method.
    public static final String EXTRA_ID=
            "com.example.architectureexample.EXTRA_ID";
    public static final String EXTRA_TITLE=
            "com.example.architectureexample.EXTRA_TITLE";

    public static final String EXTRA_DESCRIPTION=
            "com.example.architectureexample.EXTRA_DESCRIPTION";


    public static final String EXTRA_PRIORITY=
            "com.example.architectureexample.EXTRA_PRIORITY";





    //First declare the member variables
    private EditText editTextTitle;
    private EditText editTextDescription;
    private NumberPicker numberPickerPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        //assign views by id
        editTextTitle=findViewById(R.id.edit_text_title);
        editTextDescription=findViewById(R.id.edit_text_description);
        numberPickerPriority=findViewById(R.id.number_picker_priority);


        //give the numberPicker a min and max value
        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(10);

        //to get close on the top left corner
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent=getIntent();

        if(intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Note");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            numberPickerPriority.setValue(intent.getIntExtra(EXTRA_PRIORITY,1));
        }else {
            setTitle("Add Note");
        }
    }

    private void saveNote(){
        //first get inputs fro the editTexts and the number picker.
        String title=editTextTitle.getText().toString();
        String description=editTextDescription.getText().toString();
        int priority=numberPickerPriority.getValue();

        //check if the editTexts are empty and if they are empty we dont want to receive inputs.

        if(title.trim().isEmpty()||description.trim().isEmpty()){
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
            return;
        }
        //To accept input and a note.And to insert the note into the db.
        Intent data=new Intent();
        data.putExtra(EXTRA_TITLE,title);
        data.putExtra(EXTRA_DESCRIPTION,description);
        data.putExtra(EXTRA_PRIORITY,priority);

        int id=getIntent().getIntExtra(EXTRA_ID,-1);
        if(id!=-1){
            data.putExtra(EXTRA_ID,id);
        }


        //to indicate whether the input was successful or not.
        setResult(RESULT_OK,data);
        finish();

    }
    //To confirm the input when you click the save menu icon
    //First get the icon there.

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu,menu);
        return true;
    }
    //To handle clicks on the menu icon.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.save_note:
                saveNote();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }

    }
}
