package com.example.staffmanagement.View.Notification.CrudGroup;

import com.example.staffmanagement.View.Notification.CrudGroup.AddorRemove;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIGroup {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAw2qhgXs:APA91bEmBtbSQ_XFMt4wqr8qSmqzGNH2iduyFESw_O2bklSD-6nw03OIaAE9cmLi76PYSEXFHHUEmJQ0NduGdkVJiOo-PT4vyP68nEbQhmOALDfc-y3oFnLiC2fGHy1h18KrpO7So4AV",
                    "project_id:839307592059"
            }
    )

    @POST("fcm/notification")
    Call<GroupKeyResponse> createGroup(@Body CreateGroup body);

    @POST("fcm/notification")
    Call<GroupKeyResponse> addOrRemove(@Body AddorRemove body);

}
