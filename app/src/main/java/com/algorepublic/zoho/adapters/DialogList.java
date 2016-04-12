package com.algorepublic.zoho.adapters;

import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by android on 4/12/16.
 */
public class DialogList extends ArrayList<Parcelable> implements Comparable<DialogList>{

    public void setID(int taskID){
        this.ID = taskID;
    }
    public int getID(){
        return ID;
    }

    public void setName(String taskListName){
        this.Name = taskListName;
    }
    public String getName(){
        return Name;
    }



    @Override
    public int compareTo(DialogList object) {
        return (this.getName().compareTo(object.getName()));
    }

    int ID;
    String Name;
}
