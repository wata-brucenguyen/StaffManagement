package com.example.staffmanagement.Model.Repository.Base;

import java.util.List;

public class Error<T> extends Resource<T> {

    public Error(T data, String message) {
        super(data, message);
    }
}
