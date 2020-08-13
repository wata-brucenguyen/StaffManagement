package com.example.staffmanagement.View.Notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAw2qhgXs:APA91bEmBtbSQ_XFMt4wqr8qSmqzGNH2iduyFESw_O2bklSD-6nw03OIaAE9cmLi76PYSEXFHHUEmJQ0NduGdkVJiOo-PT4vyP68nEbQhmOALDfc-y3oFnLiC2fGHy1h18KrpO7So4AV" // Your server key refer to video for finding your server key
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body NotificationSender body);

}
