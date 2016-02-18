package com.algorepublic.zoho.fragments;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Toast;

import com.algorepublic.zoho.Models.CreateProjectModel;
import com.algorepublic.zoho.Models.ForumsModel;
import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.Models.TaskAssigneeModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterForumsList;
import com.algorepublic.zoho.adapters.AdapterTaskAssignee;
import com.algorepublic.zoho.adapters.ProjectsList;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.ProjectsListService;
import com.algorepublic.zoho.services.TaskListService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddProjectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddProjectFragment extends BaseFragment {

    AQuery aq;
    BaseClass baseClass;
    TaskListService service;
    ArrayList<String> userList;
    ArrayList<String> deptList;
    View view;
    static AddProjectFragment fragment;

    public AddProjectFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AddProjectFragment newInstance() {
        if (fragment == null) {
            fragment = new AddProjectFragment();
        }
        return fragment;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save_project, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_project:
                if(aq.id(R.id.project_name).getText().toString().isEmpty()){
                    Snackbar.make(getView(),"Please Add Project Name",Snackbar.LENGTH_SHORT).show();
                    return false;
                }
                if(aq.id(R.id.project_desc).getText().toString().isEmpty()){
                    Snackbar.make(getView(),"Please Add Project Description",Snackbar.LENGTH_SHORT).show();
                    return false;
                }
                if(!aq.id(R.id.private_radio).isChecked() && !aq.id(R.id.public_radio).isChecked()){
                    Snackbar.make(getView(),"Please Select The Access Option",Snackbar.LENGTH_SHORT).show();
                    return false;
                }
                CreateProject();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void CreateProject(){
        boolean isprivate= false;
        if(aq.id(R.id.private_radio).isChecked()== true){
            isprivate=true;
        }
        ProjectsListService service = new ProjectsListService(getActivity());
        service.createProject(aq.id(R.id.project_name).getText().toString(),baseClass.getUserId()
                ,aq.id(R.id.project_desc).getText().toString()
                ,TaskAssigneeModel.getInstance().responseObject.
                get(aq.id(R.id.owner_list).getSelectedItemPosition()).ID
                ,ProjectsFragment.allDeptList.get(
                aq.id(R.id.departments_list).getSelectedItemPosition()).getDeptID(),isprivate
                ,true,new CallBack(AddProjectFragment.this,"CreateProject"));
    }

    public void CreateProject(Object caller, Object model){
        CreateProjectModel.getInstance().setList((CreateProjectModel) model);
        if (CreateProjectModel.getInstance().responseObject !=null ) {
            Snackbar.make(getView(),"Project Created Successfully!",Snackbar.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getActivity(), getString(R.string.forums_list_empty), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.add_project, container, false);
        aq = new AQuery(view);
        setHasOptionsMenu(true);

        service = new TaskListService(getActivity());
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        deptList = new ArrayList<>();
        for(int loop=0;loop<ProjectsFragment.allDeptList.size();loop++){
            deptList.add(ProjectsFragment.allDeptList.get(loop).getDeptName());
        }

        aq.id(R.id.departments_list).getSpinner().setAdapter(new ArrayAdapter<String>(
                getActivity(),
                R.layout.layout_spinner_project,
                deptList
        ));
        service.getAllUsers(true, new CallBack(AddProjectFragment.this, "GetAllUsers"));
        return view;
    }
    public void GetAllUsers(Object caller, Object model) {
        TaskAssigneeModel.getInstance().setList((TaskAssigneeModel) model);
        if (TaskAssigneeModel.getInstance().responseCode == 100) {
            userList= new ArrayList();
            for(int loop=0;loop<TaskAssigneeModel.getInstance().responseObject.size();loop++) {
                userList.add(TaskAssigneeModel.getInstance().responseObject.get(loop).firstName);
            }
            aq.id(R.id.owner_list).getSpinner().setAdapter(new ArrayAdapter<String>(
                    getActivity(),
                    R.layout.layout_spinner_project,
                    userList
            ));
        }
        else
        {
            Toast.makeText(getActivity(), getString(R.string.invalid_credential), Toast.LENGTH_SHORT).show();
        }
    }

}
