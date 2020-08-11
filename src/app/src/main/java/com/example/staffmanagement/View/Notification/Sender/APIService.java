package com.example.staffmanagement.View.Notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAqtKH0_8:APA91bGsSdNMDe515Uw5D1SpZmToizAhdxh43Gm7VnNNk38vxo4GsFgsejP1-XK9a8eqhqRJuYA3Nw0HXJJBgjNlhs_klhYgq8DmdbFGSxgADU4LBGe_SOEaeHKRjcP10DnfMsakyY-7" // Your server key refer to video for finding your server key
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);

}
