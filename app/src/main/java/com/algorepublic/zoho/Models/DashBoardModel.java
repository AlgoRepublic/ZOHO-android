package com.algorepublic.zoho.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by waqas on 3/12/16.
 */
public class DashBoardModel {
    private static DashBoardModel _obj = null;

    private DashBoardModel() {

    }

    public static DashBoardModel getInstance() {
        if (_obj == null) {
            _obj = new DashBoardModel();
        }
        return _obj;
    }

    public void setList(DashBoardModel obj) {
        _obj = obj;
    }

    @SerializedName("ResponseCode")
    public Integer responseCode;

    @SerializedName("exceptionObject")
    public Object exceptionObject;

    @SerializedName("responseObject")
    @Expose
    public ResponseObject responseObject = new ResponseObject();

    public class ResponseObject {

        @SerializedName("Tasks")
        public ArrayList<Tasks> tasks = new ArrayList<>();

        @SerializedName("TaskLists")
        public ArrayList<TaskLists> tasksList =new ArrayList<>();

        @SerializedName("Milestones")
        public ArrayList<Milestones> milestones = new ArrayList<>();
    }

    public class Tasks {
       public int Position;
    }

    public class TaskLists {

    }

    public class Milestones {

    }
}
