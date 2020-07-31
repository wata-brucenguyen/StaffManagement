package com.example.staffmanagement.Model.Database.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.staffmanagement.Model.Database.DAL.ConstString;
import com.example.staffmanagement.Model.Database.DAO.BaseDAO;

@Entity(tableName = ConstString.ROLE_TABLE_NAME)
public class Role {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ConstString.ROLE_COL_ID)
    private int id;

    @ColumnInfo(name = ConstString.ROLE_COL_NAME)
    private String name;

    public Role(int id, String name) {
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
