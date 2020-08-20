package com.example.staffmanagement.MVVM.Model.FirebaseDb.Base;

public class Error<T> extends Resource<T> {

    public Error(T data, String message) {
        super(data, message);
    }
}
