package com.example.staffmanagement.Model.Database.Entity;

import java.io.Serializable;

public class Request implements Serializable {
    private int id,idUser,idState;
    private String title,content,dateTime;

    public Request(int id, int idUser, int idState, String title, String content,String dateTime) {
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
