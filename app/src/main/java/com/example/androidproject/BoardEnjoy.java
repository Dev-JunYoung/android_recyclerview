package com.example.androidproject;

import java.util.ArrayList;

public class BoardEnjoy {

    String title;
    String content;
    ArrayList photos;
    String Time;
    String name;

    public BoardEnjoy(String title, String content, ArrayList photos, String time, String name) {
        this.title = title;
        this.content = content;
        this.photos = photos;
        Time = time;
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList photos) {
        this.photos = photos;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
