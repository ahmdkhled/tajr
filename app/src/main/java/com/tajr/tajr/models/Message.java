package com.tajr.tajr.models;

public class Message {

    private String userName;
    private String UserId;
    private String message;
    private String type;
    private long time;
    private String imageUrl;
    private String messageKey;
    private String recordUrl;

    public Message() {
    }

    public Message(String userName, String userId, String message, String type, long time, String imageUrl, String messageKey, String recordUrl) {
        this.userName = userName;
        UserId = userId;
        this.message = message;
        this.type = type;
        this.time = time;
        this.imageUrl = imageUrl;
        this.messageKey = messageKey;
        this.recordUrl = recordUrl;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getRecordUrl() {
        return recordUrl;
    }

    public void setRecordUrl(String recordUrl) {
        this.recordUrl = recordUrl;
    }
}
