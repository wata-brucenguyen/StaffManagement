package com.example.staffmanagement.Model.FirebaseDb.Notification.Sender;

public class DataStaffRequest {
    public String Title;
    public String Message;
    public String type;
    public int idRequest;

    public DataStaffRequest(String title, String message, String type, int idRequest) {
        Title = title;
        Message = message;
        this.type = type;
        this.idRequest = idRequest;
    }

    public DataStaffRequest() {
    }

}
