package com.example.staffmanagement.Model.Repository.Base;

public interface ApiResponse<RequestType>  {
    void onSuccess(Success<RequestType> resource);
    void onLoading(Error<RequestType> resource);
    void onError(Loading<RequestType> resource , String message);
}
