package com.example.androidproject;

import java.util.ArrayList;

public class BoardItemData {
    private String title;
    private String content;
    private String img;
    private ArrayList<String> imgList;
    private String name;
    private String time;

    public BoardItemData(String title, String content, ArrayList<String> imgList, String name, String time) {
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
/*

    public BoardItemData(String title, String content, String img, String name,String time) {
        this.title = title;
        this.content = content;
        this.img = img;
        this.name = name;
        this.time = time;
    }
*/


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

   /* public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }*/

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
