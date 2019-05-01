package com.friendlyphire.compass;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class PasswordViewModel extends AndroidViewModel {

    private PasswordRepository repository;

    public LiveData<List<Password>> getPasswords() {
        return passwords;
    }

    public void insert(Password password){
        repository.insert(password);
    }

    public void delete(Password password){ repository.delete(password);}

    private LiveData<List<Password>> passwords;

    public PasswordViewModel(Application application){
        super(application);
        repository = new PasswordRepository(application);
        passwords = repository.getPasswords();
    }
}