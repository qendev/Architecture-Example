package com.example.architectureexample;

//Here we are creating the first Entity and called it Note.

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note {
    //First declare member variables.

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    private String description;

    private int priority;


    //create constructor to recreate them in the database.
    public Note(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }
    //if a field is not in the constructor it will not be recreated by room hence have a setter method for that.
    public void setId(int id) {
        this.id = id;
    }

    //for Room to persist the values add getter methods for the fields.

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }
}
