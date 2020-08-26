package com.example.staffmanagement.ViewModel;

public interface CallBackFunc<T> {
    void success(T data);
    void error(String message);
}
