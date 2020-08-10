package com.example.staffmanagement.Model.Database.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.staffmanagement.Model.Database.Ultils.ConstString;

@Entity( tableName = ConstString.USER_STATE_TABLE_NAME)
public class UserState {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ConstString.USER_STATE_COL_ID)
    private int id;

    @ColumnInfo(name = ConstString.USER_STATE_COL_NAME)
    private String name;

    public UserState(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
