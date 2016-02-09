package com.algorepublic.zoho;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import com.algorepublic.zoho.FragmentsTasks.TaskEditTitleFragment;
import com.algorepublic.zoho.adapters.AdapterTaskMenu;
import com.algorepublic.zoho.adapters.TasksList;
import com.algorepublic.zoho.fragments.TasksListFragment;
import com.algorepublic.zoho.utils.BaseClass;
import com.algorepublic.zoho.utils.Constants;
import com.algorepublic.zoho.utils.GenericHttpClient;
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
    int position;
    BaseClass baseClass;
    public static GridView gridViewTaskMenu;
    public static ACProgressFlower dialog;
    public static TasksList tasksObj;
    public static ArrayList<File> filesList;
    public static ArrayList<Integer> assigneeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        gridViewTaskMenu = (GridView) findViewById(R.id.gridview_taskmenu);
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
        }else{
            position=-1;
        }
        setTaskValuesTinyDB();
        aq =new AQuery(this);
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
        aq.id(R.id.btn_title).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aq.id(R.id.title_text).enabled(true);
                aq.id(R.id.title_text).getEditText().requestFocus();
                aq.id(R.id.title_text).setSelection(aq.id(R.id.title_text).getText().length());
            }
        });
        aq.id(R.id.title_bar).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aq.id(R.id.title_text).enabled(false);
            }
        });
        gridViewTaskMenu.setAdapter(new AdapterTaskMenu(this));
        if(savedInstanceState==null){
            callFragmentWithReplace(R.id.edittask_container, TaskEditTitleFragment.newInstance(position), "TaskTitle");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
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
                try {
                    GetJsonObject();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
        if(position > -1) {
            baseClass.db.putString("TaskName", TasksListFragment.generalList.get(position).getTaskName());
            baseClass.db.putString("StartDate", TasksListFragment.generalList.get(position).getStartDate());
            baseClass.db.putString("EndDate", TasksListFragment.generalList.get(position).getEndDate());
            baseClass.db.putInt("Priority", TasksListFragment.generalList.get(position).getPriority());
        }else
        {
            baseClass.db.putString("TaskName", "");
            baseClass.db.putString("StartDate", "");
            baseClass.db.putString("EndDate", "");
            baseClass.db.putInt("Priority", 0);
        }
    }
}
