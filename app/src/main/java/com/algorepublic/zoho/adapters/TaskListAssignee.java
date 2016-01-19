package com.algorepublic.zoho.adapters;

/**
 * Created by android on 1/19/16.
 */
public class TaskListAssignee {

    public void setUserID(int userID){
        this.userID = userID;
    }
    public int getUserID(){
        return userID;
    }
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    public String getFirstName(){
        return firstName;
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    public String getLastName(){
        return lastName;
    }


    int userID;
    String firstName;
    String lastName;
}
