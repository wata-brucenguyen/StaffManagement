package com.example.staffmanagement.Admin.MainAdminActivity;

public class User {
    private String name;
    private String role;
    private int requestNumber;

    public User(String name, String role, int requestNumber) {
        this.name = name;
        this.role = role;
        this.requestNumber = requestNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getRequestNumber() {
        return requestNumber;
    }

    public void setRequestNumber(int requestNumber) {
        this.requestNumber = requestNumber;
    }
}
