package com.example.androidproject;

import java.util.ArrayList;

public class BoardEnjoyItemData {
    private String title;
    private String content;
    private ArrayList<String> imgList;
    private String name;
    private String time;

    public BoardEnjoyItemData(String title, String content, ArrayList<String> imgList, String name, String time) {
        this.title = title;
        this.content = content;
        this.imgList = imgList;
        this.name = name;
        this.time = time;
    }

    public ArrayList<String> getImgList() {
        return imgList;
    }

    public void setImgList(ArrayList<String> imgList) {
        this.imgList = imgList;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
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


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
