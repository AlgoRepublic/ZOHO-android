package com.algorepublic.zoho;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.algorepublic.zoho.FragmentsTasks.TaskAssignFragment;
import com.algorepublic.zoho.FragmentsTasks.TaskAttachmentFragment;
import com.algorepublic.zoho.FragmentsTasks.TaskEditTitleFragment;
import com.algorepublic.zoho.FragmentsTasks.TaskPriorityFragment;
import com.algorepublic.zoho.FragmentsTasks.TaskScheduleFragment;
import com.algorepublic.zoho.adapters.TasksList;
import com.algorepublic.zoho.fragments.TasksListFragment;
import com.algorepublic.zoho.utils.BaseClass;
import com.algorepublic.zoho.utils.Constants;
import com.algorepublic.zoho.utils.GenericHttpClient;
import com.algorepublic.zoho.utils.TinyDB;
import com.androidquery.AQuery;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;


public class ActivityTask extends BaseActivity{
    AQuery aq;
    RadioGroup radioGroup1,radioGroup2;
    int position;
    BaseClass baseClass;
    public static ACProgressFlower dialog;
    RadioGroup.OnCheckedChangeListener changeListener1,changeListener2;
    public  static TasksList tasksObj;
    public static ArrayList<File> filesList;
    public static ArrayList<Integer> assigneeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        filesList = new ArrayList<>();
        assigneeList = new ArrayList<>();
        baseClass = ((BaseClass) getApplicationContext());
        dialog = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .fadeColor(Color.DKGRAY).build();
        Bundle extras = getIntent().getExtras();
        tasksObj= new TasksList();
        if (extras != null) {
            position = Integer.parseInt(extras.get("pos").toString());
            setTaskValuesTinyDB();
        }

        aq =new AQuery(this);
        radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
        radioGroup2 = (RadioGroup) findViewById(R.id.radioGroup2);
        aq.id(R.id.back_arrow).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityTask.this.finish();
            }
        });
        aq.id(R.id.done).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTry().execute();
            }
        });
       changeListener1 = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioGroup1(checkedId);
                UpdateRadioGroup2();
            }
        };
       changeListener2 = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                UpdateRadioGroup1();
                RadioGroup2(checkedId);
            }
        };
        radioGroup1.setOnCheckedChangeListener(changeListener1);
        radioGroup2.setOnCheckedChangeListener(changeListener2);
        aq.id(R.id.edit_radioButton).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFragmentWithReplace(R.id.edittask_container, TaskEditTitleFragment.newInstance(position), "TaskTitle");
            }
        });
        aq.id(R.id.category_radioButton).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFragmentWithReplace(R.id.edittask_container, TaskAttachmentFragment.newInstance(), "TaskAttachment");
            }
        });
        aq.id(R.id.image_radioButton).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFragmentWithReplace(R.id.edittask_container, TaskAttachmentFragment.newInstance(), "TaskAttachment");
            }
        });
        aq.id(R.id.schedule_radioButton).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFragmentWithReplace(R.id.edittask_container, TaskScheduleFragment.newInstance(), "TaskSchedule");
            }
        });
        aq.id(R.id.employees_radioButton).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFragmentWithReplace(R.id.edittask_container, TaskAssignFragment.newInstance(), "TaskAssign");
            }
        });
        aq.id(R.id.priority_radioButton).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFragmentWithReplace(R.id.edittask_container, TaskPriorityFragment.newInstance(), "TasksPriority");
            }
        });
        if(savedInstanceState==null){
            callFragmentWithReplace(R.id.edittask_container, TaskEditTitleFragment.newInstance(position), "TaskTitle");
        }
    }
    public void RadioGroup1(int checkedId) {
        switch (radioGroup1.indexOfChild(findViewById(checkedId))) {
            case 0:
                aq.id(R.id.edit_radioButton).textColor(getResources().getColor(R.color.colorAccent));
                aq.id(R.id.category_radioButton).textColor(getResources().getColor(android.R.color.white));
                aq.id(R.id.image_radioButton).textColor(getResources().getColor(android.R.color.white));
                break;
            case 1:
                aq.id(R.id.category_radioButton).textColor(getResources().getColor(R.color.colorAccent));
                aq.id(R.id.edit_radioButton).textColor(getResources().getColor(android.R.color.white));
                aq.id(R.id.image_radioButton).textColor(getResources().getColor(android.R.color.white));
                break;
            case 2:
                aq.id(R.id.image_radioButton).textColor(getResources().getColor(R.color.colorAccent));
                aq.id(R.id.edit_radioButton).textColor(getResources().getColor(android.R.color.white));
                aq.id(R.id.category_radioButton).textColor(getResources().getColor(android.R.color.white));
                break;
        }
    }

    public void RadioGroup2(int checkedId) {
        switch (radioGroup2.indexOfChild(findViewById(checkedId))) {
            case 0:
                aq.id(R.id.employees_radioButton).textColor(getResources().getColor(R.color.colorAccent));
                aq.id(R.id.schedule_radioButton).textColor(getResources().getColor(android.R.color.white));
                aq.id(R.id.priority_radioButton).textColor(getResources().getColor(android.R.color.white));
                break;
            case 1:
                aq.id(R.id.schedule_radioButton).textColor(getResources().getColor(R.color.colorAccent));
                aq.id(R.id.employees_radioButton).textColor(getResources().getColor(android.R.color.white));
                aq.id(R.id.priority_radioButton).textColor(getResources().getColor(android.R.color.white));
                break;
            case 2:
                aq.id(R.id.priority_radioButton).textColor(getResources().getColor(R.color.colorAccent));
                aq.id(R.id.employees_radioButton).textColor(getResources().getColor(android.R.color.white));
                aq.id(R.id.schedule_radioButton).textColor(getResources().getColor(android.R.color.white));
                break;
        }
    }
    public void UpdateRadioGroup1()
    {
        radioGroup1.setOnCheckedChangeListener(null);
        radioGroup1.clearCheck();
        radioGroup1.setOnCheckedChangeListener(changeListener1);
        aq.id(R.id.edit_radioButton).textColor(getResources().getColor(android.R.color.white));
        aq.id(R.id.category_radioButton).textColor(getResources().getColor(android.R.color.white));
        aq.id(R.id.image_radioButton).textColor(getResources().getColor(android.R.color.white));
    }
    public void UpdateRadioGroup2()
    {
        radioGroup2.setOnCheckedChangeListener(null);
        radioGroup2.clearCheck();
        radioGroup2.setOnCheckedChangeListener(changeListener2);
        aq.id(R.id.employees_radioButton).textColor(getResources().getColor(android.R.color.white));
        aq.id(R.id.schedule_radioButton).textColor(getResources().getColor(android.R.color.white));
        aq.id(R.id.priority_radioButton).textColor(getResources().getColor(android.R.color.white));
    }
    public class AsyncTry extends AsyncTask<Void, Void, String> {
        GenericHttpClient httpClient;
        String response= null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {

                httpClient = new GenericHttpClient();
                response = httpClient.postAddTask(Constants.CreateTask_API
                        , assigneeList,filesList);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }
        @Override
        protected void onPostExecute(String result){
            dialog.dismiss();
            PopulateModel(result);
        }
    }
    private void PopulateModel (String json) {
        Log.e("Json","/"+json);
        JSONObject jsonObj;
//        try {
//            jsonObj = new JSONObject(json.toString());
//            Gson gson = new Gson();
//            RegisterUserModel obj ;
//            obj = gson.fromJson(jsonObj.toString(),
//                    RegisterUserModel.class);
//            RegisterUserModel.getInstance().setList(obj);
//            Log.e("status","/"+RegisterUserModel.getInstance().status+RegisterUserModel.getInstance().message);

//        }catch (Exception e){}
    }
    public String  GetJsonObject() throws JSONException {

        JSONObject object = new JSONObject();
        if(getIntent().getExtras()!=null) {
            object.put("ID", tasksObj.getTaskID());
        }
        BaseClass.db.putInt("CreateBy", 1);
        BaseClass.db.putInt("UpdateBy", 1);
        BaseClass.db.putInt("OwnerID", 1);
        BaseClass.db.putInt("Priority", 1);
        BaseClass.db.putString("Title", baseClass.db.getString("TaskName"));
        BaseClass.db.putInt("ProjectID", 4);
        Log.e("task",object.toString());
        return object.toString();
    }
    public void setTaskValuesTinyDB(){
     //   baseClass.db.putString("TaskListName",TasksListFragment.generalList.get(position).getTaskListName());
        baseClass.db.putString("TaskName",TasksListFragment.generalList.get(position).getTaskName());
        baseClass.db.putString("StartDate",TasksListFragment.generalList.get(position).getStartDate());
        baseClass.db.putString("EndDate",TasksListFragment.generalList.get(position).getEndDate());
        baseClass.db.putInt("Priority", TasksListFragment.generalList.get(position).getPriority());
    }
}
