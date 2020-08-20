package com.example.staffmanagement.Model.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.staffmanagement.Model.Ultils.ConstString;

import java.io.Serializable;

@Entity(tableName = ConstString.USER_STATE_TABLE_NAME)
public class UserState implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ConstString.USER_STATE_COL_ID)
    private int Id;

    @ColumnInfo(name = ConstString.USER_STATE_COL_NAME)
    private String Name;

    public UserState(int id, String name) {
        this.Id = id;
        this.Name = name;
    }

    public UserState() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }
}
