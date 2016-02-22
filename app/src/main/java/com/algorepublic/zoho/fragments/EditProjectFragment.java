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

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by android on 2/15/16.
 */
public class EditProjectFragment extends BaseFragment {

    AQuery aq;
    BaseClass baseClass;
    static EditProjectFragment fragment;
    static int position;
    ArrayAdapter<String> ownerListAdapter;
    ArrayAdapter<String> deptListAdapter;
    TaskListService service;
    LinkedList<String> userList;
    LinkedList<String> deptList;
    NiceSpinner owner_list,departments_list;
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
                get(owner_list.getSelectedIndex()).ID, isstatus
                , projectsLists.get(
               departments_list.getSelectedIndex()).getCompOrDeptID(), isprivate
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
        owner_list = (NiceSpinner) view.findViewById(R.id.owner_list);
        departments_list= (NiceSpinner) view.findViewById(R.id.departments_list);
        service = new TaskListService(getActivity());
        setHasOptionsMenu(true);
        aq.id(R.id.project_status).visible();
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        deptList = new LinkedList<>();
        for(int loop=0;loop<ProjectsFragment.allDeptList.size();loop++){
            deptList.add(ProjectsFragment.allDeptList.get(loop).getDeptName());
        }
        departments_list.attachDataSource(deptList);
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
           departments_list.setSelectedIndex(spinnerPosition);
        }
        if (!ownername.equals(null)) {
            int spinnerPosition = ownerListAdapter.getPosition(ownername);
            owner_list.setSelectedIndex(spinnerPosition);
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
            userList= new LinkedList<>();
            for(int loop=0;loop<TaskAssigneeModel.getInstance().responseObject.size();loop++) {
                userList.add(TaskAssigneeModel.getInstance().responseObject.get(loop).firstName);
            }
            owner_list.attachDataSource(userList);
            UpdateValues();
        }
        else
        {
            Toast.makeText(getActivity(), getString(R.string.invalid_credential), Toast.LENGTH_SHORT).show();
        }
    }
}
