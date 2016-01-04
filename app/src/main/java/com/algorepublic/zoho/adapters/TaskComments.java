package com.algorepublic.zoho.adapters;

/**
 * Created by android on 1/1/16.
 */
public class TaskComments {

    public String getComment() {
        return comment;
    }
    public void setComment(String title) {
        this.comment = title;
    }
    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String author) {
        this.userName = author;
    }

    public String getDateTime() {
        return dateTime;
    }
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    private String comment ;
    private String userName;
    private String userImage;
    private String dateTime;
}
