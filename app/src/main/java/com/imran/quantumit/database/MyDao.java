package com.imran.quantumit.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.imran.quantumit.model.User;

@Dao
public interface MyDao {
    @Query("SELECT * FROM User where email= :mail and password= :password")
    User getUser(String mail, String password);

    @Insert
    void insert(User user);
}
