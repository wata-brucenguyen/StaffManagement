package com.example.staffmanagement.Model.Repository.Base;

public interface ApiResponse<T>  {
    void onSuccess(T data);
    void onLoading(T data);
    void onError(T data , String message);
}
