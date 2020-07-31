package com.example.staffmanagement.Model.Database.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.lang.reflect.ParameterizedType;

@Dao
public interface BaseDAO<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(T entity);

    @Update
    public void update(T entity);

    @Delete
    public void delete(T entity);


}
