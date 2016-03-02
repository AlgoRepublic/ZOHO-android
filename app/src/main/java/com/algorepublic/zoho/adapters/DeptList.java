package com.algorepublic.zoho.adapters;

import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.List;

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

    public void setType(int type){
        this.Type = type;
    }
    public int getType(){
        return Type;
    }


    public void setProjectsLists(ArrayList<ProjectsList> projectsLists){
        this.projectsLists.addAll(projectsLists);
    }
    public List<ProjectsList> getProjectList(){
        return projectsLists;
    }

    private String DeptName;
    private String DeptID;
    private int Type;
    private List<ProjectsList> projectsLists = new ArrayList<>();
}
