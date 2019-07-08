package com.example.architectureexample;
//This class is for Room DB itself that connects all the different parts.
//And creates an actual instance of the DB.

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities={Note.class},version = 1)
public abstract class NoteDatabase extends RoomDatabase {
    //create a variable that thats turns this class into a singleton(wont create multiple instances of this database)but instead will use this instance everywhere in the databse.
    private static NoteDatabase instance;
    //Now create a method that will use to access the Dao
    public abstract NoteDao noteDao();
    //Now create a the database which is a singleton.
    //synchronized means that only one thread at a time can be able to acces this method.
    public static synchronized NoteDatabase getInstance(Context context){
        //Since we only want to instantiate this databse if we do not already have an instance.so do the following.
        if(instance==null) {
            instance= Room.databaseBuilder(context.getApplicationContext(),
                    NoteDatabase.class,"note_databse")
                    .fallbackToDestructiveMigration()//helps the app not to crush due to an illegal State exception because the version has been incremented.
                    .addCallback(roomCallback)//This is to attach the callbackto the databse.
                    .build();//helps return the instance of this database.


        }
        return instance;

    }
    //Create notes that will be displayed in the RecyclerView.
    //Inorder to get into onCreate method, do the following...
    private static RoomDatabase.Callback roomCallback=new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db); //onCreate method is called the first time the Database is created.
        }
    };
    private static class PopulateDBAsyncTask extends AsyncTask<Void,Void,Void>{
        private NoteDao noteDao;

        private PopulateDBAsyncTask(NoteDatabase db){
            noteDao=db.noteDao();
            //To execute the AsyncTask in onCreate...
            new PopulateDBAsyncTask(instance).execute();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            //Insert some notes.
            noteDao.insert(new Note("Title 1","Description 1",1));
            noteDao.insert(new Note("Title 2","Description 2",2));
            noteDao.insert(new Note("Title 3","Description 3",3));
            return null;
        }
    }

}
