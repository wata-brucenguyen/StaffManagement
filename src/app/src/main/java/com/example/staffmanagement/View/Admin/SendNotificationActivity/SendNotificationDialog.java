package com.example.staffmanagement.View.Admin.SendNotificationActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Notification.CrudGroup.APIGroup;
import com.example.staffmanagement.View.Notification.CrudGroup.AddorRemove;
import com.example.staffmanagement.View.Notification.CrudGroup.CreateGroup;
import com.example.staffmanagement.View.Notification.CrudGroup.GroupKeyResponse;
import com.example.staffmanagement.View.Notification.Sender.APIService;
import com.example.staffmanagement.View.Notification.Sender.Client;
import com.example.staffmanagement.View.Notification.Sender.Data;
import com.example.staffmanagement.View.Notification.Sender.MyResponse;
import com.example.staffmanagement.View.Notification.Sender.NotificationSender;
import com.example.staffmanagement.View.Staff.RequestManagement.RequestActivity.StaffRequestActivity;
import com.example.staffmanagement.View.Ultils.CheckNetwork;
import com.example.staffmanagement.View.Ultils.GeneralFunc;
import com.example.staffmanagement.View.Ultils.NetworkState;
import com.example.staffmanagement.ViewModel.Admin.UserListViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendNotificationDialog extends DialogFragment {
    private SendNotificationInterface mInterface;
    private EditText editText_Title, editText_Content;
    private Button btnSendNotification, btnCancel;
    private UserListViewModel mViewModel;

    private String notification_key_name = "GroupSend";
    private String[] registration_ids = new String[]{};
    private APIService apiService;
    private APIGroup apiGroup;

    public SendNotificationDialog(SendNotificationInterface Interface, UserListViewModel ViewModel) {
        this.mInterface = Interface;
        this.mViewModel = ViewModel;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_admin_send_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapping(view);
        eventRegister();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    private void mapping(View view) {
        editText_Title = view.findViewById(R.id.editText_Title);
        editText_Content = view.findViewById(R.id.editText_Content);
        btnSendNotification = view.findViewById(R.id.buttonSendNotification);
        btnCancel = view.findViewById(R.id.buttonCancel);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        apiGroup = Client.getClient("https://fcm.googleapis.com/").create(APIGroup.class);
    }

    private void eventRegister() {
        btnSendNotification.setOnClickListener(view -> {
            //createGroup();
            if (CheckNetwork.checkInternetConnection(getActivity())) {
                if(TextUtils.isEmpty(editText_Title.getText().toString()) && TextUtils.isEmpty(editText_Content.getText().toString())){
                    mInterface.showMessage("Please fill in the title or the content");
                    return;
                }
                sendMessageToOneUser();
                mInterface.showMessage("Sent");
                getDialog().dismiss();
                mInterface.onCancelDialog();
            }
        });
        btnCancel.setOnClickListener(view -> {
            getDialog().dismiss();
            mInterface.onCancelDialog();
        });
    }

    public void sendMessageToOneUser() {
        final List<String> listUserToken = new ArrayList<>();
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("tokens");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    for (int i = 0; i < mViewModel.getUserCheckList().size(); i++) {
                        if (d.getKey().equals(String.valueOf(mViewModel.getUserCheckList().get(i).getId()))) {
                            for (DataSnapshot data : d.getChildren()) {
                                String userToken = data.getValue(String.class);
                                listUserToken.add(userToken);
                            }
                        }
                    }
                }
                for (String s : listUserToken)
                    sendNotifications(s, editText_Title.getText().toString().trim(), editText_Content.getText().toString().trim());

                ref.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void sendNotifications(String usertoken, String title, String message) {
        Data data = new Data(title, message);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200)
                    Log.d("Key", " " + response.body().success);
                if (response.code() == 400)
                    Log.d("Key", " Toang ");
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }


    public void createGroup() {
        final CreateGroup body = new CreateGroup("create", notification_key_name, registration_ids);
        apiGroup.createGroup(body).enqueue(new Callback<GroupKeyResponse>() {
            @Override
            public void onResponse(Call<GroupKeyResponse> call, Response<GroupKeyResponse> response) {
                if (response.code() == 200) {
                    Log.d("Key", " " + response.body().notification_key);
                    String notification_key = response.body().notification_key;
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("notification_key");
                    myRef.child("Group_1").setValue(notification_key);
                }
                if (response.code() == 400) {
                    Log.d("Key", "400");
                    FirebaseDatabase.getInstance().getReference()
                            .child("notification_key")
                            .child("Group_1")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String notification_key = snapshot.getValue(String.class);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }

            }

            @Override
            public void onFailure(Call<GroupKeyResponse> call, Throwable t) {

            }
        });

    }

    public void addOrRemove(String operation, String notification_key, String idUser) {
        String[] regisId = {idUser};
        final AddorRemove body = new AddorRemove(operation, notification_key_name, regisId, notification_key);
        apiGroup.addOrRemove(body).enqueue(new Callback<GroupKeyResponse>() {
            @Override
            public void onResponse(Call<GroupKeyResponse> call, Response<GroupKeyResponse> response) {

                // Toast.makeText(SendNotifActivity.this,"Add or remove successfully",Toast.LENGTH_SHORT).show();
                if (response.code() == 400) {
                    Log.d("Loi", " " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<GroupKeyResponse> call, Throwable t) {

            }
        });
    }

}
