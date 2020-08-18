package com.example.staffmanagement.MVVM.Model.Repository.Base;

public interface ApiResponse<RequestType>  {
    void onSuccess(Resource<RequestType> success);
    void onLoading(Resource<RequestType> loading);
    void onError(Resource<RequestType> error);
}
