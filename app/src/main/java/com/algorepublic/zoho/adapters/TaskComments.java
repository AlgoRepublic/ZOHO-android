package com.algorepublic.zoho.adapters;

/**
 * Created by android on 1/1/16.
 */
public class TaskComments {

    public String getCommentID() {
        return commentID;
    }
    public void setCommentID(String title) {
        this.commentID = title;
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String title) {
        this.comment = title;
    }
    public String getUserImagePath() {
        return userImagePath;
    }

    public void setUserImagePath(String userImage) {
        this.userImagePath = userImage;
    }
    public Integer getUserImageID() {
        return userImageID;
    }

    public void setUserImageID(Integer userImage) {
        this.userImageID = userImage;
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

    private String commentID ;
    private String comment ;
    private String userName;
    private String userImagePath;
    private Integer userImageID;
    private String dateTime;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    private Integer userId;
}
