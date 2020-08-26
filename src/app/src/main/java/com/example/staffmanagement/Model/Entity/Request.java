package com.example.staffmanagement.Model.Entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.staffmanagement.Model.Ultils.ConstString;

import java.io.Serializable;

@Entity(tableName = ConstString.REQUEST_TABLE_NAME)
public class Request implements Serializable, Comparable, Cloneable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ConstString.REQUEST_COL_ID)
    private int Id;

    @ColumnInfo(name = ConstString.REQUEST_COL_ID_USER)
    private int IdUser;

    @ColumnInfo(name = ConstString.REQUEST_COL_ID_STATE)
    private int IdState;

    @ColumnInfo(name = ConstString.REQUEST_COL_TITLE)
    private String Title;

    @ColumnInfo(name = ConstString.REQUEST_COL_CONTENT)
    private String Content;

    @ColumnInfo(name = ConstString.REQUEST_COL_DATETIME)
    private long DateTime;

//    @ColumnInfo(name = "NameOfUser")
//    private String NameOfUser;

    public Request(int id, int idUser, int idState, String title, String content, long dateTime) {
        this.Id = id;
        this.IdUser = idUser;
        this.IdState = idState;
        this.Title = title;
        this.Content = content;
        this.DateTime = dateTime;
        //this.NameOfUser = nameOfUser;
    }

    public Request() {
    }

    @Override
    public int compareTo(Object o) {
        if (this == o) return 1;
        if (o == null || getClass() != o.getClass()) return 0;
        Request request = (Request) o;
        if (Id == request.getId() &&
                IdUser == request.getIdUser() &&
                IdState == request.getIdState() &&
                DateTime == request.getDateTime() &&
                Title.equals(request.getTitle()) &&
                Content.equals(request.getContent()))
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
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public int getIdUser() {
        return IdUser;
    }

    public void setIdUser(int idUser) {
        this.IdUser = idUser;
    }

    public int getIdState() {
        return IdState;
    }

    public void setIdState(int idState) {
        this.IdState = idState;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        this.Content = content;
    }

    public long getDateTime() {
        return DateTime;
    }

    public void setDateTime(long dateTime) {
        this.DateTime = dateTime;
    }

}
