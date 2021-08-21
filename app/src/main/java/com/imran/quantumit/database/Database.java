package com.imran.quantumit.database;

import androidx.room.RoomDatabase;


import com.imran.quantumit.model.User;


@androidx.room.Database(entities = {User.class},version = 1)
public abstract class Database extends RoomDatabase {

    public abstract MyDao getUserDao();
}
