package com.example.staffmanagement.MVVM.Model.LocalDb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

@Dao
public interface BaseDAO<T>{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(T entity);

    @Update
    void update(T entity);

    @Delete
    void delete(T entity);
}
