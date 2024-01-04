package com.example.fixapp.model;

public class NotificationItem implements Comparable<NotificationItem> {
    private String content;
    private String dateTime;
    private String title;
    private String userID;

    public NotificationItem(){

    }

    public NotificationItem(String content, String dateTime, String title){
        this.content = content;
        this.dateTime = dateTime;
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getTitle() {
        return title;
    }

    public String getUserID() {
        return userID;
    }

    @Override
    public int compareTo(NotificationItem other) {
        if (this.getDateTime() == null || other.getDateTime() == null) {
            return 0;
        }
        return other.getDateTime().compareTo(this.getDateTime());
    }

}
