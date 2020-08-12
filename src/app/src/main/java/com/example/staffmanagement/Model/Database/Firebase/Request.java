package com.example.staffmanagement.Model.Database.Firebase;

import java.io.Serializable;

public class Request implements Serializable {

    private String id;
    private String idUser;
    private String idState;
    private String title;
    private String content;
    private long dateTime;

    public Request() {
    }

    public Request(String id, String idUser, String idState, String title, String content, long dateTime) {
        this.id = id;
        this.idUser = idUser;
        this.idState = idState;
        this.title = title;
        this.content = content;
        this.dateTime = dateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdState() {
        return idState;
    }

    public void setIdState(String idState) {
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
