package com.example.staffmanagement.View.Notification.CrudGroup;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIGroup {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAqtKH0_8:APA91bGsSdNMDe515Uw5D1SpZmToizAhdxh43Gm7VnNNk38vxo4GsFgsejP1-XK9a8eqhqRJuYA3Nw0HXJJBgjNlhs_klhYgq8DmdbFGSxgADU4LBGe_SOEaeHKRjcP10DnfMsakyY-7",
                    "project_id:733676557311"
            }
    )

    @POST("fcm/notification")
    Call<GroupKeyResponse> createGroup(@Body CreateGroup body);

    @POST("fcm/notification")
    Call<GroupKeyResponse> addOrRemove(@Body AddorRemove body);

}
