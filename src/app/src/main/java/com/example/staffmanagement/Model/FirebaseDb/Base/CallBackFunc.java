package com.example.staffmanagement.Model.FirebaseDb.Base;

public interface CallBackFunc<T> {
    void onSuccess(T data);
    void onError(String message);
}
