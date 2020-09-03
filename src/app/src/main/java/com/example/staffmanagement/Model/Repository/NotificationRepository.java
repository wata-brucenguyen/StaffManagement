package com.example.staffmanagement.Model.Repository;

import android.util.Log;

import com.example.staffmanagement.Model.Entity.User;
import com.example.staffmanagement.Model.FirebaseDb.Base.ApiResponse;
import com.example.staffmanagement.Model.FirebaseDb.Base.CallBackFunc;
import com.example.staffmanagement.Model.FirebaseDb.Base.Resource;
import com.example.staffmanagement.Model.FirebaseDb.Notification.KeyToken;
import com.example.staffmanagement.Model.FirebaseDb.Notification.NotificationService;
import com.example.staffmanagement.View.Notification.Sender.Data;
import com.example.staffmanagement.View.Notification.Sender.DataStaffRequest;
import com.example.staffmanagement.View.Notification.Sender.NotificationSender;
import com.example.staffmanagement.View.Notification.Sender.NotificationSenderStaffToAdmin;

import java.util.List;

public class NotificationRepository {
    private NotificationService mService;

    public NotificationRepository() {
        mService = new NotificationService();
    }

    public void saveToken(User user, String token) {
        mService.getListTokenOfUser(user.getRole().getId(), user.getId(), new ApiResponse<List<KeyToken>>() {
            @Override
            public void onSuccess(Resource<List<KeyToken>> suc) {
                Log.i("TEST_TOKEN", suc.getData().size() + " 123 ");
                int flag = 0;
                for (int i = 0; i < suc.getData().size(); i++) {
                    if (suc.getData().get(i).token.equals(token)) {
                        flag = 1;
                        break;
                    }
                }

                if (flag == 0) {
                    mService.post(user, token, new ApiResponse<String>() {
                        @Override
                        public void onSuccess(Resource<String> success) {

                        }

                        @Override
                        public void onLoading(Resource<String> loading) {

                        }

                        @Override
                        public void onError(Resource<String> error) {

                        }
                    });
                }
            }

            @Override
            public void onLoading(Resource<List<KeyToken>> loading) {

            }

            @Override
            public void onError(Resource<List<KeyToken>> err) {

            }
        });
    }

    public void deleteToken(User user, String token) {
        mService.delete(user, token, new ApiResponse<String>() {
            @Override
            public void onSuccess(Resource<String> success) {

            }

            @Override
            public void onLoading(Resource<String> loading) {

            }

            @Override
            public void onError(Resource<String> error) {

            }
        });
    }

    public void sendNotificationForAllAdmin(DataStaffRequest data, CallBackFunc<Boolean> callBackFunc) {
        mService.getListTokenByRole(1, new ApiResponse<List<String>>() {
            @Override
            public void onSuccess(Resource<List<String>> success) {

                for (int i = 0; i < success.getData().size(); i++) {
                    NotificationSenderStaffToAdmin sender = new NotificationSenderStaffToAdmin(data, success.getData().get(i));
                    mService.sendStaffToAdmin(sender);
                }
                callBackFunc.onSuccess(true);
            }

            @Override
            public void onLoading(Resource<List<String>> loading) {

            }

            @Override
            public void onError(Resource<List<String>> error) {

            }
        });
    }

    public void sendNotification(Data data, List<User> userList,CallBackFunc<Boolean> callBackFunc){
        for (int i = 0; i < userList.size(); i++){
            Log.i("TEST_",userList.get(i).getFullName());
            mService.getListTokenOfUser(2, userList.get(i).getId(), new ApiResponse<List<KeyToken>>() {
                @Override
                public void onSuccess(Resource<List<KeyToken>> success) {
                    for(int j = 0;j<success.getData().size();j++){
                        Log.i("TEST_",success.getData().get(j).token);
                        NotificationSender sender = new NotificationSender(data,success.getData().get(j).token);
                        mService.sendAdminToStaff(sender);
                    }
                }

                @Override
                public void onLoading(Resource<List<KeyToken>> loading) {

                }

                @Override
                public void onError(Resource<List<KeyToken>> error) {

                }
            });
        }


    }
}
