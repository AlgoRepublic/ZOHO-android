package com.algorepublic.zoho.adapters;

import java.util.ArrayList;

/**
 * Created by android on 12/29/15.
 */
public class HeaderTasksList {


    public void setHeader(String headerList){
        this.header = headerList;
    }
    public String getHeader(){
        return header;
    }
    public void setHeaderID(long headerid){
        this.headerID = headerid;
    }
    public long getHeaderID(){
        return headerID;
    }
    public void setChildList(ArrayList<TasksList> tasksList){
        this.childList.addAll(tasksList);
    }
    public ArrayList<TasksList> getChildList(){
        return childList;
    }

    public static String header;
    public static long headerID;
    public static ArrayList<TasksList> childList = new ArrayList<>();
}
