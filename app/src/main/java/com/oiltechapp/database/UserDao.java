package com.oiltechapp.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.oiltechapp.models.User;
import androidx.annotation.NonNull;

@Dao
public interface UserDao {
    @Insert
    void insertUser(@NonNull User user);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    User getUserByUsername(@NonNull String username);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    User getUser(@NonNull String username, @NonNull String password);
}