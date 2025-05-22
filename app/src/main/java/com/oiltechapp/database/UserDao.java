package com.oiltechapp.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.oiltechapp.models.User;

@Dao
public interface UserDao {
    @Insert
    void insertUser(User user);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    User getUser(String username, String password);

    @Query("SELECT * FROM users WHERE username = :username")
    User getUserByUsername(String username);
}