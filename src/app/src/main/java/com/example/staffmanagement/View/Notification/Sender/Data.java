package com.example.staffmanagement.View.Notification.Sender;

public class Data {
    private String Title;
    public String Message;

    public Data(String title, String message) {
        Title = title;
        Message = message;
    }

    public Data() {
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
