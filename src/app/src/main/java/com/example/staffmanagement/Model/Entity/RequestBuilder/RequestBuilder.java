package com.example.staffmanagement.Model.Entity.RequestBuilder;


import com.example.staffmanagement.Model.Entity.Request;
import com.example.staffmanagement.Model.Entity.StateRequest;
import com.example.staffmanagement.Model.Entity.UserBuilder.UserBuilderInterface;

public class RequestBuilder implements RequestBuilderInterface {
    private int id, idUser, idState;
    private String title, content,nameOfUser;
    private long dateTime;
    private StateRequest stateRequest;
    @Override
    public Request build(){
        return new Request(id,idUser,title,content,dateTime,stateRequest,nameOfUser);
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

    @Override
    public RequestBuilderInterface buildStateRequest(StateRequest stateRequest) {
        this.stateRequest = stateRequest;
        return this;
    }

    @Override
    public RequestBuilderInterface buildNameOfUser(String nameOfUser) {
        this.nameOfUser = nameOfUser;
        return this;
    }
}
