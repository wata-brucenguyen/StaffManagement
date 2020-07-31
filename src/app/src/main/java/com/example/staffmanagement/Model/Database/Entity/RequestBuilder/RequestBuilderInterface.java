package com.example.staffmanagement.Model.Database.Entity.RequestBuilder;

import com.example.staffmanagement.Model.Database.Entity.Request;

public interface RequestBuilderInterface {
    RequestBuilderInterface buildId(int id);

    RequestBuilderInterface buildIdUser(int idUser);

    RequestBuilderInterface buildIdState(int idState);

    RequestBuilderInterface buildITitle(String title);

    RequestBuilderInterface buildContent(String content);

    RequestBuilderInterface buildDateTime(long dateTime);

    Request build();
}
