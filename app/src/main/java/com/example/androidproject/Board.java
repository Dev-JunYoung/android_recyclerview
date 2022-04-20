package com.example.androidproject;

import java.util.ArrayList;

public class Board {


    String title;
    String content;
    ArrayList<Photo> photos;
    String Time;
    String name;

    public Board(String title, String content, ArrayList<Photo> photos, String time, String name) {
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

    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<Photo> photos) {
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
