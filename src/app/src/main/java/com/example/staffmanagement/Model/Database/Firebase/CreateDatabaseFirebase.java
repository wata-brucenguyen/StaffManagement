package com.example.staffmanagement.Model.Database.Firebase;

import android.util.Log;

import com.example.staffmanagement.Model.LocalDb.Database.Ultils.ConstString;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateDatabaseFirebase {
    DatabaseReference reference;
    DatabaseReference user, role, stateRequest, userState, request;
    //lock, active
    String[] idUserState = {"-MEWGaijrPbNhfnKH3_Y", "-MEWGajMjgRHLiBj-k6T"};
    //admin, staff
    String[] idRole = {"-MEWGajNGfl9K50TdyA1", "-MEWGajMjgRHLiBj-k6T"};
    //waiting, accept, decline
    String[] idStateRequest = {"-MEWGajT3PYjz9bED7fY", "-MEWGajT3PYjz9bED7fZ", "-MEWGajT3PYjz9bED7f_"};
    //1 2 3
    String[] idRequest = {"-MEWGajQhZYBsL-I5HeD", "-MEWGajQhZYBsL-I5HeE", "-MEWGajRNHWDwkpRgBg5"};
    //vuong, hoang, triet, suu, teo, ti
    String[] idUser = {"-MEWGajUzo2CfQYMoEp0", "-MEWGajS7LLklhYAQd99", "-MEWGajS7LLklhYAQd9A", "-MEWGajUzo2CfQYMoEp1", "-MEWGajUzo2CfQYMoEp2", "-MEWGajUzo2CfQYMoEp3"};


    public void createDatabase() {
        reference = FirebaseDatabase.getInstance().getReference();

        user = reference.child("database").child(ConstString.USER_TABLE_NAME);
        role = reference.child("database").child(ConstString.ROLE_TABLE_NAME);
        stateRequest = reference.child("database").child(ConstString.STATE_REQUEST_TABLE_NAME);
        userState = reference.child("database").child(ConstString.USER_STATE_TABLE_NAME);
        request = reference.child("database").child(ConstString.REQUEST_TABLE_NAME);

        //role
        for (int i = 0; i < Data.getRoleList("0").size(); i++) {
            role.child(idRole[i]).setValue(Data.getRoleList(idRole[i]).get(i));
        }

        //request
        for (int i = 0; i < Data.getRequestList("0", "0", "0").size(); i++) {
            request.child(idRequest[i]).setValue(Data.getRequestList(idRequest[i], idUser[i+3], idStateRequest[i]).get(i));
        }

        //userState
        for (int i=0;i<Data.getUserStateList("0").size();i++){
            userState.child(idUserState[i]).setValue(Data.getUserStateList(idUserState[i]).get(i));
        }

        //stateRequest
        for (int i=0;i<Data.getStateList("0").size();i++){
            stateRequest.child(idStateRequest[i]).setValue(Data.getStateList(idStateRequest[i]).get(i));
        }

        //user
        for (int i=0;i<Data.getUserList("0","0","0").size();i++){
            if(i<3){
                user.child(idUser[i]).setValue(Data.getUserList(idUser[i],idRole[0],idUserState[0]).get(i));
            }else {
                user.child(idUser[i]).setValue(Data.getUserList(idUser[i],idRole[1],idUserState[0]).get(i));
            }
        }
    }
}
