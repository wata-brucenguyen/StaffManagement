package com.example.staffmanagement.Model.FirebaseDb.Notification;

import com.example.staffmanagement.Model.FirebaseDb.Notification.Sender.MyResponse;
import com.example.staffmanagement.Model.FirebaseDb.Notification.Sender.NotificationSender;
import com.example.staffmanagement.Model.FirebaseDb.Notification.Sender.NotificationSenderWithRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface NotificationApi {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAcfXPd5I:APA91bFkMdwN7h3wRqF_GajFMep6uIK4WYYhJKAli1asjI67oVRbVij-1mfmMrX4u0L9lYm6UyQ9faayWfJBteZHcic2gRN8GeY0spwyjOb56R-Pn_xWv9COSqqF1J_f11P4BPPRsKqU"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotificationWithRequest(@Body NotificationSenderWithRequest body);

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAcfXPd5I:APA91bFkMdwN7h3wRqF_GajFMep6uIK4WYYhJKAli1asjI67oVRbVij-1mfmMrX4u0L9lYm6UyQ9faayWfJBteZHcic2gRN8GeY0spwyjOb56R-Pn_xWv9COSqqF1J_f11P4BPPRsKqU"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotificationAdminToStaff(@Body NotificationSender body);

    @GET("tokens.json")
    Call<Object> getAllToken();

    @GET("tokens/role_id_{idRole}.json")
    Call<Object> getListTokenByRole(@Path("idRole") int idRole);

    @GET("tokens/role_id_{idRole}/uid_{idUser}.json")
    Call<Object> getListTokenOfUser(@Path("idRole") int idRole, @Path("idUser") int idUser);

    @POST("tokens/role_id_{idRole}/uid_{idUser}.json")
    Call<Object> post(@Path("idRole") int idRole, @Path("idUser") int idUser, @Body String token);

    @DELETE("tokens/role_id_{idRole}/uid_{idUser}/{key}.json")
    Call<String> delete(@Path("idRole") int idRole, @Path("idUser") int idUser,@Path("key") String key);
}
