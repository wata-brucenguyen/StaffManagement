package com.example.staffmanagement.Model.Entity.RequestBuilder;

import com.example.staffmanagement.Model.Entity.Request;
import com.example.staffmanagement.Model.Entity.StateRequest;
import com.example.staffmanagement.Model.Entity.UserBuilder.UserBuilderInterface;

public interface RequestBuilderInterface {
    RequestBuilderInterface buildId(int id);

    RequestBuilderInterface buildIdUser(int idUser);

    RequestBuilderInterface buildITitle(String title);

    RequestBuilderInterface buildContent(String content);

    RequestBuilderInterface buildDateTime(long dateTime);

    RequestBuilderInterface buildStateRequest(StateRequest stateRequest);

    RequestBuilderInterface buildNameOfUser(String nameOfUser);
    Request build();
}
