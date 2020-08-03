package com.example.staffmanagement.Model.Database.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.staffmanagement.Model.Database.DAL.ConstString;

import java.io.Serializable;

@Entity(tableName = ConstString.REQUEST_TABLE_NAME, foreignKeys = {
        @ForeignKey(
                entity = StateRequest.class,
                parentColumns = ConstString.STATE_REQUEST_COL_ID,
                childColumns = ConstString.REQUEST_COL_ID_STATE,
                onUpdate = ForeignKey.CASCADE),
        @ForeignKey(entity = User.class,
                parentColumns = ConstString.USER_COL_ID,
                childColumns = ConstString.REQUEST_COL_ID_USER,
                onUpdate = ForeignKey.CASCADE),
})
public class Request implements Serializable {

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
