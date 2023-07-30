package com.example.plantrckr;

public class History {

    private String hisId;
    private String userId;
    private String transName;
    private String transProvider;
    private String transFee;
    private String transDate;

    private long timestamp;

    public History() {
        // Default constructor required for Firebase
    }

    public History(String hisId, String userId, String transName, String transProvider, String transFee, String transDate, Long timestamp) {
        this.hisId = hisId;
        this.userId = userId;
        this.transName = transName;
        this.transProvider = transProvider;
        this.transFee = transFee;
        this.transDate = transDate;
        this.timestamp = timestamp;
    }



    public String getHisId() {
        return hisId;
    }

    public void setHisId(String hisId) {
        this.hisId = hisId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTransName() {
        return transName;
    }

    public void setTransName(String transName) {
        this.transName = transName;
    }

    public String getTransProvider() {
        return transProvider;
    }

    public void setTransProvider(String transProvider) {
        this.transProvider = transProvider;
    }

    public String getTransFee() {
        return transFee;
    }

    public void setTransFee(String transFee) {
        this.transFee = transFee;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public void setTimestamp(long timestamp) {this.timestamp = timestamp;}
    public long getTimestamp() {
        return  timestamp;
    }
}


