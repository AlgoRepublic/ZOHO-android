package com.algorepublic.zoho.adapters;

/**
 * Created by android on 2/2/16.
 */
public class ProjectsList {


    public ProjectsList() {
    }

    public void setCompOrDeptID(String compOrDeptID){
        this.compOrDeptID = compOrDeptID;
    }
    public String getCompOrDeptID(){
        return compOrDeptID;
    }
    public void setCompOrDeptName(String compOrDeptName){
        this.compOrDeptName = compOrDeptName;
    }
    public String getCompOrDeptName(){
        return compOrDeptName;
    }
    public void setProjectID(String projectID){
        this.projectID = projectID;
    }
    public String getProjectID(){
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


    private String projectDesc;
    private String projectName;
    private String projectID;
    private String compOrDeptName;
    private String compOrDeptID;
    private String totalTasks;
    private String totalMilestones;
    private String totalUsers;

    public String getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(String totalTasks) {
        this.totalTasks = totalTasks;
    }

    public String getTotalMilestones() {
        return totalMilestones;
    }

    public void setTotalMilestones(String totalMilestones) {
        this.totalMilestones = totalMilestones;
    }

    public String getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(String totalUsers) {
        this.totalUsers = totalUsers;
    }
}
