package com.friendlyphire.compass;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface PasswordDao {
    @Query("SELECT * FROM password ORDER BY hint ASC")
    List<Password> getAll();

    @Query("SELECT * FROM password ORDER BY hint ASC")
    LiveData<List<Password>> getAllPasswords();

    @Query("SELECT * FROM password WHERE uid IN (:userIds)")
    List<Password> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM password WHERE hint LIKE :hint "
            + "LIMIT 1")
    Password findByHint(String hint);

    @Insert
    void insertAll(Password... passwords);

    @Insert
    void insert(Password password);

    @Delete
    void delete(Password password);

    @Query("DELETE FROM password")
    void deleteAll();
}
