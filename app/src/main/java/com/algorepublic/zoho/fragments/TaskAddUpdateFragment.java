package com.algorepublic.zoho.fragments;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.algorepublic.zoho.FragmentsTasks.TaskEditTitleFragment;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterTaskMenu;
import com.algorepublic.zoho.adapters.AttachmentList;
import com.algorepublic.zoho.adapters.DocumentsList;
import com.algorepublic.zoho.adapters.TasksList;
import com.algorepublic.zoho.utils.BaseClass;
import com.algorepublic.zoho.utils.Constants;
import com.algorepublic.zoho.utils.GenericHttpClient;
import com.androidquery.AQuery;

import java.io.IOException;
import java.util.ArrayList;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;


public class TaskAddUpdateFragment extends BaseFragment {
    AQuery aq;
    static int tasID;
    static BaseClass baseClass;
    static TaskAddUpdateFragment fragment;
    public static GridView gridViewTaskMenu;
    public static ACProgressFlower dialog;
    public static TasksList tasksObj;
    public static ArrayList<DocumentsList> apiDocsList = new ArrayList<>();
    public static ArrayList<AttachmentList> filesList;
    public static ArrayList<Integer> filesToDelete;
    public static ArrayList<Integer> assigneeList;
    public static int callPosition=0;

    public static TaskAddUpdateFragment newInstance() {
        callPosition = 0;
        fragment = new TaskAddUpdateFragment();
        return fragment;
    }
    public static TaskAddUpdateFragment newInstance(int taskId) {
        tasID = taskId;
        callPosition = 1;
        fragment = new TaskAddUpdateFragment();
        return fragment;
    }
    public static TaskAddUpdateFragment newInstance(TasksList tasksList) {
        tasksObj = tasksList;
        callPosition= 2;
        fragment = new TaskAddUpdateFragment();
        return fragment;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save_project, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     * <p>
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_project:

                if(callPosition ==0) {
                    new NewTask().execute();
                }
                if(callPosition == 1){
                    new NewTaskByParent().execute();
                }
                if(callPosition == 2){
                    if(tasksObj.getParentTaskID() == 0) {
                        new UpdateTask().execute();
                    }else{
                        new UpdateTaskByParent().execute();
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        //baseClass.setSelectedProject("0");
        tasksObj=null;
        tasID =0;
        assigneeList.clear();
        super.onDestroyView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.activity_task, container, false);
        gridViewTaskMenu = (GridView) view.findViewById(R.id.gridview_taskmenu);
        filesList = new ArrayList<>();
        filesToDelete = new ArrayList<>();
        assigneeList = new ArrayList<>();
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        dialog = new ACProgressFlower.Builder(getActivity())
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .fadeColor(Color.DKGRAY).build();
        if(tasksObj !=null) {
            if (tasksObj.getListAssignees().size() > 0) {
                try {
                    for (int loop = 0;
                         loop < tasksObj.getListAssignees().size(); loop++) {
                        TaskAddUpdateFragment.assigneeList.add(
                                tasksObj.getListAssignees().get(loop).getUserID());
                    }
                } catch (IndexOutOfBoundsException e) {
                }
            }
        }
        aq =new AQuery(view);
        SetValues();
        if( baseClass.db.getString("ProjectName") != null) {
           getToolbar().setTitle(baseClass.db.getString("ProjectName"));
        }
        aq.id(R.id.title_text).getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                baseClass.db.putString("TaskName", s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        aq.id(R.id.btn_title).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aq.id(R.id.title_text).enabled(true);
                aq.id(R.id.title_text).getEditText().requestFocus();
            }
        });
        aq.id(R.id.title_bar).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aq.id(R.id.title_text).enabled(false);
            }
        });
        gridViewTaskMenu.setAdapter(new AdapterTaskMenu(getActivity()));
        callFragmentWithReplace(R.id.edittask_container, TaskEditTitleFragment.newInstance(), "TaskTitle");
        return  view;
    }

    public void SetValues(){
        if(callPosition ==0) {
            setTaskValuesTinyDB(-1);
        }
        if(callPosition == 2){
            setTaskValuesTinyDB(1);
        }
    }
    public class NewTask extends AsyncTask<Void, Void, String> {
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
                        , assigneeList,filesList,baseClass);
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
    public class NewTaskByParent extends AsyncTask<Void, Void, String> {
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
                response = httpClient.postAddTaskByParent(Constants.CreateTask_API
                        , assigneeList, Integer.toString(tasID), filesList, baseClass);
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
    public class UpdateTask extends AsyncTask<Void, Void, String> {
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
                response = httpClient.postUpdateTask(Constants.UpdateTask_API
                        , assigneeList, filesList, filesToDelete, baseClass);
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
    public class UpdateTaskByParent extends AsyncTask<Void, Void, String> {
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
                response = httpClient.postUpdateTaskByParent(Constants.UpdateTask_API
                        , assigneeList, Integer.toString(tasksObj.getParentTaskID()), filesList, filesToDelete, baseClass);
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
        Log.e("Json", "/" + json);
        SetValues();
    }
    public void setTaskValuesTinyDB(int position){
        if(position > -1) {
            baseClass.db.putInt("TaskListNameID",tasksObj.getTaskListNameID());
            baseClass.db.putInt("TaskID", tasksObj.getTaskID());
            baseClass.db.putString("TaskName", tasksObj.getTaskName());
            baseClass.db.putString("ProjectName", tasksObj.getProjectName());
            getToolbar().setTitle(baseClass.db.getString("ProjectName"));
            baseClass.db.putInt("ProjectID", tasksObj.getProjectID());
            if(tasksObj.getDescription() != null) {
                baseClass.db.putString("TaskDesc", tasksObj.getDescription());
            }
            baseClass.db.putString("StartDate", tasksObj.getStartDate());
            baseClass.db.putString("EndDate", tasksObj.getEndDate());
            baseClass.db.putInt("Priority", tasksObj.getPriority());
        }else
        {
            baseClass.db.putString("TaskName", getString(R.string.Task_Title));
            baseClass.db.putInt("TaskListNameID", 0);
            baseClass.db.putString("StartDate", "");
            baseClass.db.putString("EndDate", "");
            baseClass.db.putInt("Priority", 0);
            baseClass.db.putString("TaskDesc", "");
        }
        aq.id(R.id.title_text).text(baseClass.db.getString("TaskName"));
    }
}