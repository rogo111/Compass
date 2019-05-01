package com.friendlyphire.compass;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class PasswordRepository {

    private PasswordDao dao;
    private LiveData<List<Password>> passwords;

    PasswordRepository(Application application){
        PasswordDB db = PasswordDB.getInstance(application);
        dao = db.passwordDao();
        passwords = dao.getAllPasswords();
    }

    LiveData<List<Password>> getPasswords(){
        return passwords;
    }

    public void insert(Password password){
        new insertAsyncTask(dao).execute(password);
    }

    public void delete(Password password){ new deleteAsyncTask(dao).execute(password);}
    private static class insertAsyncTask extends AsyncTask<Password, Void, Void> {

        private PasswordDao mAsyncTaskDao;

        insertAsyncTask(PasswordDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Password... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
    private static class deleteAsyncTask extends AsyncTask<Password, Void, Void> {

        private PasswordDao mAsyncTaskDao;

        deleteAsyncTask(PasswordDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Password... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }
}
