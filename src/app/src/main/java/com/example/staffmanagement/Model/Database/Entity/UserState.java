package com.example.staffmanagement.Model.Database.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity( tableName = "UserState")
public class UserState {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    private int Id;

    @ColumnInfo(name = "Name")
    private String Name;

    public UserState(int id, String name) {
        Id = id;
        Name = name;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
