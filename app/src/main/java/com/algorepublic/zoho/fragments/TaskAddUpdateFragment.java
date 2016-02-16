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

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;


public class TaskAddUpdateFragment extends BaseFragment {
    AQuery aq;
    static int position;
    static BaseClass baseClass;
    static TaskAddUpdateFragment fragment;
    public static GridView gridViewTaskMenu;
    public static ACProgressFlower dialog;
    public static TasksList tasksObj;
    public static ArrayList<DocumentsList> apiDocsList = new ArrayList<>();
    public static ArrayList<AttachmentList> filesList;
    public static ArrayList<Integer> filesToDelete;
    public static ArrayList<Integer> assigneeList;

    public static TaskAddUpdateFragment newInstance(TasksList tasksList,int pos) {
        position = pos;
        tasksObj = tasksList;
        if (fragment==null) {
            fragment = new TaskAddUpdateFragment();
        }
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

                if(position > -1) {
                    new UpdateTask().execute();
                }else{
                    new NewTask().execute();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setRetainInstance(true);
        getToolbar().setTitle(tasksObj.getProjectName());
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        baseClass.setSelectedProject("0");
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
        assigneeList.clear();
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        dialog = new ACProgressFlower.Builder(getActivity())
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .fadeColor(Color.DKGRAY).build();

        aq =new AQuery(view);
        setTaskValuesTinyDB();
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
                aq.id(R.id.title_text).setSelection(aq.id(R.id.title_text).getText().length());
            }
        });
        aq.id(R.id.title_bar).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aq.id(R.id.title_text).enabled(false);
            }
        });
        gridViewTaskMenu.setAdapter(new AdapterTaskMenu(getActivity(),position));
        if(savedInstanceState==null){
            callFragmentWithReplace(R.id.edittask_container, TaskEditTitleFragment.newInstance(position), "TaskTitle");
        }
        return  view;
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
    private void PopulateModel (String json) {
        Log.e("Json","/"+json);
        setTaskValuesTinyDB();
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
    public void setTaskValuesTinyDB(){
        if(position > -1) {
            baseClass.db.putInt("TaskListNameID",tasksObj.getTaskListNameID());
            baseClass.db.putInt("TaskID", tasksObj.getTaskID());
            baseClass.db.putString("TaskName", tasksObj.getTaskName());
            baseClass.db.putString("ProjectName", tasksObj.getProjectName());
            baseClass.db.putInt("ProjectID", tasksObj.getProjectID());
            if(tasksObj.getDescription() != null) {
                baseClass.db.putString("TaskDesc", tasksObj.getDescription());
            }
            baseClass.db.putString("StartDate", tasksObj.getStartDate());
            baseClass.db.putString("EndDate", tasksObj.getEndDate());
            baseClass.db.putInt("Priority", tasksObj.getPriority());
        }else
        {
            baseClass.db.putString("ProjectName", getString(R.string.project_title));
            baseClass.db.putString("TaskName", getString(R.string.Task_Title));
            baseClass.db.putInt("TaskListNameID", 0);
            baseClass.db.putString("StartDate", "");
            baseClass.db.putString("EndDate", "");
            baseClass.db.putInt("Priority", 0);
            baseClass.db.putString("TaskDesc","");
        }
        aq.id(R.id.title_text).text(baseClass.db.getString("TaskName"));
        aq.id(R.id.project_title).text(baseClass.db.getString("ProjectName"));
    }
}
