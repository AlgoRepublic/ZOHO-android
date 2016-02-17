package com.algorepublic.zoho.adapters;

/**
 * Created by android on 2/15/16.
 */
public class DeptList {



    public void setDeptID(String compOrDeptID){
        this.DeptID = compOrDeptID;
    }
    public String getDeptID(){
        return DeptID;
    }
    public void setDeptName(String compOrDeptName){
        this.DeptName = compOrDeptName;
    }
    public String getDeptName(){
        return DeptName;
    }

    private String DeptName;
    private String DeptID;
}
