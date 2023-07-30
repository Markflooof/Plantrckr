package com.example.plantrckr;

import com.google.firebase.database.DataSnapshot;

public class Transaction {

    private String subId;
    private String userId;
    private String subName;
    private String subProvider;
    private String subDate;
    private  String subFee;
    private String subSched;
    private String newSched;

    public Transaction() {
        // Default constructor required for Firebase
    }

    public Transaction(String subId, String userId,  String subName, String subProvider, String subDate, String subFee, String subSched,  String newSched) {
        this.subId = subId;
        this.userId = userId;
        this.subName = subName;
        this.subProvider = subProvider;
        this.subDate = subDate;
        this.subFee = subFee;
        this.subSched = subSched;
        this.newSched = newSched;
    }

    public String getSubId() {
        return subId;
    }

    public String getUserId() {
        return userId;
    }

    public String getSubName() {
        return subName;
    }

    public String getSubProvider() {
        return subProvider;
    }

    public String getSubDate() {
        return subDate;
    }

    public String getSubFee() { return subFee; }

    public String getSubSched() {
        return subSched;
    }

    public String getNewSched() {
        return newSched;
    }


    public void setSubId(String subId) {
        this.subId = subId;
    }

    public void setUserId(String userId) {
        this.subId = userId;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public void setSubProvider(String subProvider) {
        this.subProvider = subProvider;
    }

    public void setSubDate(String subDate) {
        this.subDate = subDate;
    }

    public void setSubFee(String subFee) {
        this.subFee = subFee;
    }

    public void setSubSched(String subSched) {
        this.subSched = subSched;
    }

    public void setNewSched(String newSched) {
        this.newSched = newSched;
    }

}

