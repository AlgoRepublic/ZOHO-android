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
import android.widget.RadioGroup;
import android.widget.Toast;

import com.algorepublic.zoho.Models.CreateProjectModel;
import com.algorepublic.zoho.Models.TaskAssigneeModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.ProjectsListService;
import com.algorepublic.zoho.services.TaskListService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

import org.angmarch.views.NiceSpinner;

import java.util.LinkedList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddProjectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddProjectFragment extends BaseFragment {

    AQuery aq;
    BaseClass baseClass;
    TaskListService service;
    LinkedList<String> userList;
    LinkedList<String> deptList;
    View view;
    boolean isprivate= false;
    NiceSpinner owner_list,departments_list;
    RadioGroup radioGroup;
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
        ProjectsListService service = new ProjectsListService(getActivity());
        service.createProject(aq.id(R.id.project_name).getText().toString(),baseClass.getUserId()
                ,aq.id(R.id.project_desc).getText().toString()
                ,TaskAssigneeModel.getInstance().responseObject.
                get(owner_list.getSelectedIndex()).ID
                ,ProjectsFragment.allDeptList.get(
                departments_list.getSelectedIndex()).getDeptID(),isprivate
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
        radioGroup = (RadioGroup) view.findViewById(R.id.private_public_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.public_radio:
                        isprivate = false;
                        break;
                    case R.id.private_radio:
                        isprivate = true;
                        break;
                }
            }
        });
        aq = new AQuery(view);
        aq.id(R.id.public_radio).checked(true);
        setHasOptionsMenu(true);
        owner_list = (NiceSpinner) view.findViewById(R.id.owner_list);
        departments_list= (NiceSpinner) view.findViewById(R.id.departments_list);
        service = new TaskListService(getActivity());
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        deptList = new LinkedList<>();
        for(int loop=0;loop<ProjectsFragment.allDeptList.size();loop++){
            deptList.add(ProjectsFragment.allDeptList.get(loop).getDeptName());
        }
        departments_list.attachDataSource(deptList);
        service.getAllUsers(true, new CallBack(AddProjectFragment.this, "GetAllUsers"));
        return view;
    }
    public void GetAllUsers(Object caller, Object model) {
        TaskAssigneeModel.getInstance().setList((TaskAssigneeModel) model);
        if (TaskAssigneeModel.getInstance().responseCode == 100) {
            userList= new LinkedList<>();
            for(int loop=0;loop<TaskAssigneeModel.getInstance().responseObject.size();loop++) {
                userList.add(TaskAssigneeModel.getInstance().responseObject.get(loop).firstName);
            }
            owner_list.attachDataSource(userList);
        }
        else
        {
            Toast.makeText(getActivity(), getString(R.string.invalid_credential), Toast.LENGTH_SHORT).show();
        }
    }

}
