package com.manacher.filesharing.models;

public class DataRequestNotificationSender {

    private DataRequestNotificationData data;
    private String to;

    public DataRequestNotificationSender(DataRequestNotificationData data, String to) {
        this.setData(data);
        this.setTo(to);
    }

    public DataRequestNotificationSender() {
    }

    public DataRequestNotificationData getData() {
        return data;
    }

    public void setData(DataRequestNotificationData data) {
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
