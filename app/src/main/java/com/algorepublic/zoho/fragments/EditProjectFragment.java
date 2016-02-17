package com.algorepublic.zoho.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.algorepublic.zoho.Models.CreateProjectModel;
import com.algorepublic.zoho.Models.TaskAssigneeModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.ProjectsList;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.ProjectsListService;
import com.algorepublic.zoho.services.TaskListService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

import java.util.ArrayList;

/**
 * Created by android on 2/15/16.
 */
public class EditProjectFragment extends BaseFragment {

    AQuery aq;
    BaseClass baseClass;
    static EditProjectFragment fragment;
    static int position;
    ArrayList<String> userList;
    ArrayAdapter<String> ownerListAdapter;
    ArrayAdapter<String> deptListAdapter;
    TaskListService service;
    static ArrayList<ProjectsList> projectsLists= new ArrayList<>();

    public EditProjectFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static EditProjectFragment newInstance(ArrayList<ProjectsList> lists,int Pos) {
        position = Pos;
        projectsLists = lists;
        if(fragment ==null) {
            fragment = new EditProjectFragment();
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
                if (!aq.id(R.id.active_radio).isChecked() && !aq.id(R.id.archive_radio).isChecked()) {
                    Snackbar.make(getView(), "Please Select The Project Status", Snackbar.LENGTH_SHORT).show();
                    return false;
                }
                UpdateProject();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
    public void UpdateProject(){
        boolean isprivate= false;
        boolean isstatus= false;
        if(aq.id(R.id.private_radio).isChecked()== true){
            isprivate=true;
        }
        if (aq.id(R.id.active_radio).isChecked() == true) {
            isstatus = true;
        }
        ProjectsListService service = new ProjectsListService(getActivity());
        service.updateProject(projectsLists.get(position).getProjectID()
                , aq.id(R.id.project_name).getText().toString(), baseClass.getUserId()
                , aq.id(R.id.project_desc).getText().toString()
                , TaskAssigneeModel.getInstance().responseObject.
                get(aq.id(R.id.owner_list).getSelectedItemPosition()).ID, isstatus
                , projectsLists.get(
                aq.id(R.id.departments_list).getSelectedItemPosition()).getCompOrDeptID(), isprivate
                , true, new CallBack(EditProjectFragment.this, "UpdateProject"));
    }
    public void UpdateProject(Object caller, Object model){
        CreateProjectModel.getInstance().setList((CreateProjectModel) model);
        if (CreateProjectModel.getInstance().responseObject !=null ) {
            Snackbar.make(getView(),"Project Updated Successfully!",Snackbar.LENGTH_SHORT).show();
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
        View view  = inflater.inflate(R.layout.add_project, container, false);
        aq = new AQuery(getActivity(), view);
        service = new TaskListService(getActivity());
        setHasOptionsMenu(true);
        aq.id(R.id.project_status).visible();
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        ArrayList<String> DeptList = new ArrayList<>();
        for(int loop=0;loop<ProjectsFragment.allDeptList.size();loop++){
            DeptList.add(ProjectsFragment.allDeptList.get(loop).getDeptName());
        }
        deptListAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.layout_spinner_project,
                DeptList);
                aq.id(R.id.departments_list).getSpinner().setAdapter(deptListAdapter);
        service.getAllUsers(true, new CallBack(EditProjectFragment.this, "GetAllUsers"));
        return view;
    }
    public void UpdateValues()
    {
        aq.id(R.id.project_name).text(projectsLists.get(position).getProjectName());
        aq.id(R.id.project_desc).text(Html.fromHtml(projectsLists.get(position).getProjectDesc()));
        String deptname = projectsLists.get(position).getCompOrDeptName();
        String ownername = projectsLists.get(position).getOwnerName();
        if (!deptname.equals(null)) {
            int spinnerPosition = deptListAdapter.getPosition(deptname);
            aq.id(R.id.departments_list).getSpinner().setSelection(spinnerPosition);
        }
        if (!ownername.equals(null)) {
            int spinnerPosition = ownerListAdapter.getPosition(ownername);
            aq.id(R.id.owner_list).getSpinner().setSelection(spinnerPosition);
        }
        if(projectsLists.get(position).getDeleted()== true){
            aq.id(R.id.archive_radio).checked(true);
            aq.id(R.id.active_radio).checked(false);
        }else {
            aq.id(R.id.active_radio).checked(true);
            aq.id(R.id.archive_radio).checked(false);
        }

        if(projectsLists.get(position).getPrivate()== true){
            aq.id(R.id.private_radio).checked(true);
            aq.id(R.id.public_radio).checked(false);
        }else {
            aq.id(R.id.public_radio).checked(true);
            aq.id(R.id.private_radio).checked(false);
        }
    }
    public void GetAllUsers(Object caller, Object model) {
        TaskAssigneeModel.getInstance().setList((TaskAssigneeModel) model);
        if (TaskAssigneeModel.getInstance().responseCode == 100) {
            userList= new ArrayList();
            for(int loop=0;loop<TaskAssigneeModel.getInstance().responseObject.size();loop++) {
                userList.add(TaskAssigneeModel.getInstance().responseObject.get(loop).firstName);
            }
            ownerListAdapter = new ArrayAdapter<String>(
                    getActivity(),
                    R.layout.layout_spinner_project,
                    userList
            );
            aq.id(R.id.owner_list).getSpinner().setAdapter(ownerListAdapter);
            UpdateValues();
        }
        else
        {
            Toast.makeText(getActivity(), getString(R.string.invalid_credential), Toast.LENGTH_SHORT).show();
        }
    }
}
