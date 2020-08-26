package com.example.staffmanagement.Model.Entity.RequestBuilder;


import com.example.staffmanagement.Model.Entity.Request;

public class RequestBuilder implements RequestBuilderInterface {
    private int id, idUser, idState;
    private String title, content,nameOfUser;
    private long dateTime;

    @Override
    public Request build(){
        return new Request(id,idUser,idState,title,content,dateTime);
    }

    @Override
    public RequestBuilderInterface buildId(int id) {
        this.id = id;
        return this;
    }

    @Override
    public RequestBuilderInterface buildIdUser(int idUser) {
        this.idUser = idUser;
        return this;
    }

    @Override
    public RequestBuilderInterface buildIdState(int idState) {
        this.idState = idState;
        return this;
    }

    @Override
    public RequestBuilderInterface buildITitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public RequestBuilderInterface buildContent(String content) {
        this.content = content;
        return this;
    }

    @Override
    public RequestBuilderInterface buildDateTime(long dateTime) {
        this.dateTime = dateTime;
        return this;
    }
}
