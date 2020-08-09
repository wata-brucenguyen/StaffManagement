package com.example.staffmanagement.Model.Database.Entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


import com.example.staffmanagement.Model.Database.Ultils.ConstString;

import java.io.Serializable;
import java.util.Objects;

@Entity(tableName = ConstString.REQUEST_TABLE_NAME)
public class Request implements Serializable, Comparable, Cloneable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ConstString.REQUEST_COL_ID)
    private int id;

    @ColumnInfo(name = ConstString.REQUEST_COL_ID_USER)
    private int idUser;

    @ColumnInfo(name = ConstString.REQUEST_COL_ID_STATE)
    private int idState;

    @ColumnInfo(name = ConstString.REQUEST_COL_TITLE)
    private String title;

    @ColumnInfo(name = ConstString.REQUEST_COL_CONTENT)
    private String content;

    @ColumnInfo(name = ConstString.REQUEST_COL_DATETIME)
    private long dateTime;

    public Request(int id, int idUser, int idState, String title, String content, long dateTime) {
        this.id = id;
        this.idUser = idUser;
        this.idState = idState;
        this.title = title;
        this.content = content;
        this.dateTime = dateTime;
    }

    @Override
    public int compareTo(Object o) {
        if (this == o) return 1;
        if (o == null || getClass() != o.getClass()) return 0;
        Request request = (Request) o;
        if (id == request.id &&
                idUser == request.idUser &&
                idState == request.idState &&
                dateTime == request.dateTime &&
                title.equals(request.getTitle()) &&
                content.equals(request.getContent()))
            return 1;
        return 0;
    }

    @NonNull
    @Override
    protected Object clone() {
        Request clone;
        try {
            clone = (Request) super.clone();

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        return clone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdState() {
        return idState;
    }

    public void setIdState(int idState) {
        this.idState = idState;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }


}
