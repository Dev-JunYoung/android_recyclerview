package com.example.androidproject;

public class MountainVO {
    //산이름
    private String mntName;
    //별칭
    private String mntSubName;
    //해발고도
    private String mntHeight;
    //소재지
    private String mntLocation;
    //등산코스
    private String mntCourse;
    //선정이유
    private String mntPickReason;
    //상세설명
    private String mntDetail;
    //개요
    private String mntOverview;
    //오시는 길(
    private String mntTransport;
    //숙소정보
    private String mntTourInfo;


    public MountainVO(String mntName, String mntSubName, String mntHeight, String mntLocation, String mntCourse, String mntPickReason, String mntDetail, String mntOverview, String mntTransport, String mntTourInfo) {
        this.mntName = mntName;
        this.mntSubName = mntSubName;
        this.mntHeight = mntHeight;
        this.mntLocation = mntLocation;
        this.mntCourse = mntCourse;
        this.mntPickReason = mntPickReason;
        this.mntDetail = mntDetail;
        this.mntOverview = mntOverview;
        this.mntTransport = mntTransport;
        this.mntTourInfo = mntTourInfo;
    }

    public String getMntSubName() {
        return mntSubName;
    }

    public void setMntSubName(String mntSubName) {
        this.mntSubName = mntSubName;
    }

    public String getMntName() {
        return mntName;
    }

    public void setMntName(String mntName) {
        this.mntName = mntName;
    }

    public String getMntHeight() {
        return mntHeight;
    }

    public void setMntHeight(String mntHeight) {
        this.mntHeight = mntHeight;
    }

    public String getMntLocation() {
        return mntLocation;
    }

    public void setMntLocation(String mntLocation) {
        this.mntLocation = mntLocation;
    }

    public String getMntCourse() {
        return mntCourse;
    }

    public void setMntCourse(String mntCourse) {
        this.mntCourse = mntCourse;
    }

    public String getMntPickReason() {
        return mntPickReason;
    }

    public void setMntPickReason(String mntPickReason) {
        this.mntPickReason = mntPickReason;
    }

    public String getMntDetail() {
        return mntDetail;
    }

    public void setMntDetail(String mntDetail) {
        this.mntDetail = mntDetail;
    }

    public String getMntOverview() {
        return mntOverview;
    }

    public void setMntOverview(String mntOverview) {
        this.mntOverview = mntOverview;
    }

    public String getMntTransport() {
        return mntTransport;
    }

    public void setMntTransport(String mntTransport) {
        this.mntTransport = mntTransport;
    }

    public String getMntTourInfo() {
        return mntTourInfo;
    }

    public void setMntTourInfo(String mntTourInfo) {
        this.mntTourInfo = mntTourInfo;
    }
}
