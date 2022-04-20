package com.example.androidproject;

public class User {

/*
    private static User user=new User();
    private User(){}
    public static User getInstance(){
        return user;
    }*/

    private String id;
    private String pass;
    private String name;
    private String age;

    public User(String id, String pass, String name, String age) {
        this.id = id;
        this.pass = pass;
        this.name = name;
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
