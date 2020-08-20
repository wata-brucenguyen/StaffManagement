package com.example.staffmanagement.Model.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.staffmanagement.Model.Ultils.ConstString;


@Entity(tableName = ConstString.ROLE_TABLE_NAME)
public class Role {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ConstString.ROLE_COL_ID)
    private int Id;

    @ColumnInfo(name = ConstString.ROLE_COL_NAME)
    private String Name;

    public Role(int id, String name) {
        this.Id = id;
        this.Name = name;
    }

    public Role() {
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
