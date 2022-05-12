package com.example.androidproject;

public class RecordVO {
    String distance,step,time,height,speed,minHeight,maxHeight;

    public RecordVO(String distance, String step, String time, String height, String speed, String minHeight, String maxHeight) {
        this.distance = distance;
        this.step = step;
        this.time = time;
        this.height = height;
        this.speed = speed;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(String minHeight) {
        this.minHeight = minHeight;
    }

    public String getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(String maxHeight) {
        this.maxHeight = maxHeight;
    }
}
