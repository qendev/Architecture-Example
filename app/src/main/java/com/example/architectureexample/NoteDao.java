package com.example.architectureexample;
//This is our Dao where we will define all the DB operations that we will make on the Entity.
//Dao has to be an Interface or an abstract classes for we dont have to provide the method body.
//Just create a method and anotate it and Room will provide the necessary code for us.
//Its good practice to create one Dao per entity.

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface NoteDao {
    //Create all the methods for operations on the Note Entity.
    //For methods,just create the Method name,Return type and arguments
    //Just need to anotate with the corresponding DB operation.

    @Insert
     void insert(Note note);

     @Update
     void update(Note note);

     @Delete
     void delete(Note note);

     //Room does not have Convinient anotations for all posible DB operations.


    //This operation craetes an option for deleting all notes at once...
    //So use...
     @Query("DELETE FROM note_table") //Room does not have a Convinient anotation for it.
     void deleteAllNotes();

     //This option returns all the notes so as to return them in the RecyclerView
    //So use...
     @Query("SELECT * FROM note_table ORDER BY priority DESC")
     LiveData<List<Note>> getAllNotes(); //Room does not have a convinient anotation for it.
    // *means all columns.
    //<LiveData> returns live data.
}
