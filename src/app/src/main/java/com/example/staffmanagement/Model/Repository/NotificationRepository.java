package com.example.staffmanagement.Model.Repository;

import android.util.Log;

import com.example.staffmanagement.Model.Entity.User;
import com.example.staffmanagement.Model.FirebaseDb.Base.ApiResponse;
import com.example.staffmanagement.Model.FirebaseDb.Base.Resource;
import com.example.staffmanagement.Model.FirebaseDb.Notification.KeyToken;
import com.example.staffmanagement.Model.FirebaseDb.Notification.NotificationService;

import java.util.List;

public class NotificationRepository {
    private NotificationService mService;

    public NotificationRepository() {
        mService = new NotificationService();
    }

    public void saveToken(User user,String token){
        mService.getListTokenOfUser(user.getRole().getId(), user.getId(), new ApiResponse<List<KeyToken>>() {
            @Override
            public void onSuccess(Resource<List<KeyToken>> suc) {
                Log.i("TEST_TOKEN",suc.getData().size()+" 123 ");
                int flag = 0;
                for(int i=0;i<suc.getData().size();i++){
                    if(suc.getData().get(i).token.equals(token)){
                        flag = 1;
                        break;
                    }
                }

                if(flag == 0){
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

    public void deleteToken(User user,String token){
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
}
