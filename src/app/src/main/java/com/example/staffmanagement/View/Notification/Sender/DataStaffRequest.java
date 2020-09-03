package com.example.staffmanagement.View.Notification.Sender;

public class DataStaffRequest extends Data {
    public String type;
    public int idRequest;

    public DataStaffRequest(String title, String message, String type, int idRequest) {
        super(title, message);
        this.type = type;
        this.idRequest = idRequest;
    }

    public DataStaffRequest() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIdRequest() {
        return idRequest;
    }

    public void setIdRequest(int idRequest) {
        this.idRequest = idRequest;
    }
}
