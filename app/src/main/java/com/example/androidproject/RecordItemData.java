package com.example.androidproject;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class RecordItemData {
    private String distance;
    private String time;
    private String height;
    private String step;
    private int posi;
    private String maxHeight;
    private String minHeight;
    private String avg;
    private String timestamp;
    private ArrayList<LatLng> userLocation;


    public ArrayList<LatLng> getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(ArrayList<LatLng> userLocation) {
        this.userLocation = userLocation;
    }


    public RecordItemData(String distance, String time, String height, String step, String maxHeight, String minHeight, String avg, String timestamp, ArrayList<LatLng> userLocation) {
        this.distance = distance;
        this.time = time;
        this.height = height;
        this.step = step;
        this.maxHeight = maxHeight;
        this.minHeight = minHeight;
        this.avg = avg;
        this.timestamp = timestamp;
        this.userLocation = userLocation;
    }

    public RecordItemData(String distance, String time, String height, String step, String maxHeight, String minHeight, String avg, String timestamp) {
        this.distance = distance;
        this.time = time;
        this.height = height;
        this.step = step;
        this.maxHeight = maxHeight;
        this.minHeight = minHeight;
        this.avg = avg;
        this.timestamp = timestamp;
    }

    public int getPosi() {
        return posi;
    }

    public void setPosi(int posi) {
        this.posi = posi;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
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

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(String maxHeight) {
        this.maxHeight = maxHeight;
    }

    public String getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(String minHeight) {
        this.minHeight = minHeight;
    }

    public String getAvg() {
        return avg;
    }

    public void setAvg(String avg) {
        this.avg = avg;
    }
}
