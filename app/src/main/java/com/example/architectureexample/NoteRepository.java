package com.example.architectureexample;
//This repository is just but a simple java class.its not part of the  architecture component library.
//It provides another abstraction layer btwn the different data sources and the rest of the App.
//The repository is just a way to abstract all the datasource operations and provide a clean Api to the rest of the App.
//The view model just calls methods directly on the repository since its not aware where the data comes from or where it is fetched.


import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Update;

public class NoteRepository {
    //First create member variables
    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

//create a constructor where you assign the variables
    public NoteRepository(Application application){
        NoteDatabase database=NoteDatabase.getInstance(application);
        noteDao=database.noteDao();
        allNotes=noteDao.getAllNotes();
    }
    //Now create methods for all our different database opertaions.
    public void insert(Note note){
        new InsertNoteAsyncTask(noteDao).execute(note);

    }
    public void update(Note note){
        new UpdateNoteAsyncTask(noteDao).execute(note);

    }
    public void delete(Note note){
        new DeleteNoteAsyncTask(noteDao).execute(note);

    }
    public void deleteAllNotes(){
        new DeleteAllNotesNoteAsyncTask(noteDao).execute();

    }
    public LiveData<List<Note>> getAllNotes(){
        return allNotes;
    }
    //Since Room doest allow DB operations on the main thread bcz that could freeze the app.
    //We will execute the code on the background thread using Async task...
    //So we do the following as shown in the code below...
    private static class InsertNoteAsyncTask extends AsyncTask<Note,Void,Void> {
        private NoteDao noteDao;//we need this to make DB operations.

        private InsertNoteAsyncTask(NoteDao noteDao){ //we use this constructor since the class is static n we need to access the NoteDao
            this.noteDao=noteDao;

        }

        @Override
        protected Void doInBackground(Note... notes) { //This is the only manadatory method that we need to override in AsyncTask the rest are optional.
            noteDao.insert(notes[0]);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<Note,Void,Void> {
        private NoteDao noteDao;//we need this to make DB operations.

        private UpdateNoteAsyncTask(NoteDao noteDao){ //we use this constructor since the class is static n we need to access the NoteDao
            this.noteDao=noteDao;

        }

        @Override
        protected Void doInBackground(Note... notes) { //This is the only manadatory method that we need to override in AsyncTask the rest are optional.
            noteDao.update(notes[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<Note,Void,Void> {
        private NoteDao noteDao;//we need this to make DB operations.

        private DeleteNoteAsyncTask(NoteDao noteDao){ //we use this constructor since the class is static n we need to access the NoteDao
            this.noteDao=noteDao;

        }

        @Override
        protected Void doInBackground(Note... notes) { //This is the only manadatory method that we need to override in AsyncTask the rest are optional.
            noteDao.delete(notes[0]);
            return null;
        }
    }

    private static class DeleteAllNotesNoteAsyncTask extends AsyncTask<Void,Void,Void> {
        private NoteDao noteDao;//we need this to make DB operations.

        private DeleteAllNotesNoteAsyncTask(NoteDao noteDao){ //we use this constructor since the class is static n we need to access the NoteDao
            this.noteDao=noteDao;

        }

        @Override
        protected Void doInBackground(Void... voids) { //This is the only manadatory method that we need to override in AsyncTask the rest are optional.
            noteDao.deleteAllNotes();
            return null;
        }
    }




}
