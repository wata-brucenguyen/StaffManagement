package com.example.staffmanagement.Model.FirebaseDb.Notification;

import com.example.staffmanagement.View.Notification.Sender.MyResponse;
import com.example.staffmanagement.View.Notification.Sender.NotificationSender;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NotificationApi {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAw2qhgXs:APA91bEmBtbSQ_XFMt4wqr8qSmqzGNH2iduyFESw_O2bklSD-6nw03OIaAE9cmLi76PYSEXFHHUEmJQ0NduGdkVJiOo-PT4vyP68nEbQhmOALDfc-y3oFnLiC2fGHy1h18KrpO7So4AV" // Your server key refer to video for finding your server key
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body NotificationSender body);

    @GET("tokens_1.json")
    Call<Object> getAllToken();

    @GET("tokens_1/role_id_{idRole}.json")
    Call<Object> getListTokenByRole(@Path("idRole") int idRole);

    @GET("tokens_1/role_id_{idRole}/uid_{idUser}.json")
    Call<Object> getListTokenOfUser(@Path("idRole") int idRole, @Path("idUser") int idUser);

    @POST("tokens_1/role_id_{idRole}/uid_{idUser}.json")
    Call<Object> post(@Path("idRole") int idRole, @Path("idUser") int idUser, @Body String token);

    @DELETE("tokens_1/role_id_{idRole}/uid_{idUser}/{key}.json")
    Call<String> delete(@Path("idRole") int idRole, @Path("idUser") int idUser,@Path("key") String key);
}
