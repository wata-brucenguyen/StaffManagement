package com.example.staffmanagement.View.Admin.Home;

public class Weather {
    String id;

    String linkImage;
    String name;

    public Weather() {
    }

    public Weather(String id, String linkImage, String name) {
        this.id = id;
        this.linkImage = linkImage;
        this.name = name;
    }

    public String getLinkImage() {
        return linkImage;
    }

    public void setLinkImage(String linkImage) {
        this.linkImage = linkImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
