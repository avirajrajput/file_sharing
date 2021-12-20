package com.manacher.filesharing.models;

public class DataRequestNotificationData {

    private String roomId;
    private String name;
    private String phoneNumber;
    private String dpUrl;
    private String userId;

    private String callStatus;

    public DataRequestNotificationData() {
    }

    public DataRequestNotificationData(String roomId, String name, String phoneNumber, String dpUrl, String userId, String callStatus) {
        this.roomId = roomId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.dpUrl = dpUrl;
        this.userId = userId;
        this.callStatus = callStatus;
    }

    public String getCallStatus() {
        return callStatus;
    }

    public void setCallStatus(String callStatus) {
        this.callStatus = callStatus;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDpUrl() {
        return dpUrl;
    }

    public void setDpUrl(String dpUrl) {
        this.dpUrl = dpUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
