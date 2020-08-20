package com.example.staffmanagement.Model.FirebaseDb.Base;

public interface ApiResponse<RequestType>  {
    void onSuccess(Resource<RequestType> success);
    void onLoading(Resource<RequestType> loading);
    void onError(Resource<RequestType> error);
}