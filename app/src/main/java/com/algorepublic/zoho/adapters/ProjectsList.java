package com.algorepublic.zoho.adapters;

import java.util.ArrayList;

/**
 * Created by android on 2/2/16.
 */
public class ProjectsList implements Comparable<ProjectsList>{


    public void setCompOrDeptID(int compOrDeptID){
        this.compOrDeptID = compOrDeptID;
    }
    public int getCompOrDeptID(){
        return compOrDeptID;
    }
    public void setCompOrDeptName(String taskName){
        this.compOrDeptName = compOrDeptName;
    }
    public String getCompOrDeptName(){
        return compOrDeptName;
    }
    public void setProjectID(int projectID){
        this.projectID = projectID;
    }
    public int getProjectID(){
        return projectID;
    }
    public void setProjectName(String projectName){
        this.projectName = projectName;
    }
    public String getProjectName(){
        return projectName;
    }
    public void setProjectDesc(String projectDesc){
        this.projectDesc = projectDesc;
    }
    public String getProjectDesc(){
        return projectDesc;
    }
    public void setCharToAscii(long charToAscii){
        this.charToAscii = charToAscii;
    }
    public long getCharToAscii(){
        return charToAscii;
    }
    @Override
    public int compareTo(ProjectsList object) {
        return (this.getProjectName().compareTo(object.getProjectName()));
    }

    long charToAscii;
    String projectDesc;
    String projectName;
    int projectID;
    String compOrDeptName;
    int compOrDeptID;

}
