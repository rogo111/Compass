package com.friendlyphire.compass;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {Password.class}, version = 1)
public abstract class PasswordDB extends RoomDatabase {

    public abstract PasswordDao passwordDao();

    private static PasswordDB instance;

    public static PasswordDB getInstance(final Context context){
        if(instance==null) {
            synchronized (PasswordDB.class) {
                if (instance == null) {
                    SharedPreferences myPref = context.getSharedPreferences(context.getString(R.string.sharedPrefName),Context.MODE_PRIVATE);

                    if(myPref.getBoolean(context.getString(R.string.firstRun),true)){
                        myPref.edit().putBoolean(context.getString(R.string.firstRun),false).commit();
                        instance = Room.databaseBuilder(context.getApplicationContext(),
                                PasswordDB.class, "password_database").addCallback(sRoomDatabaseCallback)
                                .build();
                    }
                    else{
                        instance = Room.databaseBuilder(context.getApplicationContext(),
                                PasswordDB.class, "password_database")
                                .build();
                    }
                }
            }
        }
        return instance;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(instance).execute();
                }
            };
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final PasswordDao mDao;

        PopulateDbAsync(PasswordDB db) {
            mDao = db.passwordDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDao.deleteAll();
            Password word = new Password("Example hint: touch me to view password","Example password");
            mDao.insert(word);
            word = new Password("Example hint: hold me to delete me","Example password: hold me to delete me");
            mDao.insert(word);
            return null;
        }
    }
}
