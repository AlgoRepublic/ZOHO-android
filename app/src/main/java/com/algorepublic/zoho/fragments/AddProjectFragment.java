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

import com.algorepublic.zoho.BaseActivity;
import com.algorepublic.zoho.Models.CreateProjectModel;
import com.algorepublic.zoho.Models.TaskUserModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.ProjectsList;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.ProjectsListService;
import com.algorepublic.zoho.services.TaskListService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import cc.cloudist.acplibrary.ACProgressFlower;

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
    ACProgressFlower dialogAC;
    NiceSpinner owner_list,departments_list;
    RadioGroup radioGroup;
    static AddProjectFragment fragment;

    public AddProjectFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AddProjectFragment newInstance() {
        return new AddProjectFragment();
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
                baseClass.hideKeyPad(getView());
                if(aq.id(R.id.project_name).getText().toString().isEmpty()){
                    Snackbar.make(getView(),getString(R.string.project_addname),Snackbar.LENGTH_SHORT).show();
                    return false;
                }
                if(aq.id(R.id.project_desc).getText().toString().isEmpty()){
                    Snackbar.make(getView(),getString(R.string.project_add_description),Snackbar.LENGTH_SHORT).show();
                    return false;
                }
                if(!aq.id(R.id.private_radio).isChecked() && !aq.id(R.id.public_radio).isChecked()){
                    Snackbar.make(getView(),getString(R.string.project_access_option),Snackbar.LENGTH_SHORT).show();
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
                , TaskUserModel.getInstance().responseObject.
                get(owner_list.getSelectedIndex()).ID
                ,ProjectsFragment.allDeptList.get(
                departments_list.getSelectedIndex()).getDeptID(),isprivate
                ,false,new CallBack(AddProjectFragment.this,"CreateProject"));
        dialogAC.show();
    }

    public void CreateProject(Object caller, Object model){
        CreateProjectModel.getInstance().setList((CreateProjectModel) model);
        if (CreateProjectModel.getInstance().responseObject !=null ) {
            Snackbar.make(getView(),getString(R.string.project_created),Snackbar.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().popBackStack();
        }else {
            Snackbar.make(getView() , getString(R.string.response_error), Snackbar.LENGTH_SHORT).show();
        }
        dialogAC.dismiss();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.fragment_add_project, container, false);
        InitializeDialog(getActivity());
        radioGroup = (RadioGroup) view.findViewById(R.id.private_public_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.public_radio:
                        isprivate = false;
                        break;
                    case R.id.private_radio:
                        isprivate = true;
                        break;
                }
            }
        });
        getToolbar().setTitle(getString(R.string.add_project));
        aq = new AQuery(view);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        aq.id(R.id.project_name).getEditText().setOnFocusChangeListener(baseClass.focusChangeListener);
        aq.id(R.id.project_desc).getEditText().setOnFocusChangeListener(baseClass.focusChangeListener);

        aq.id(R.id.public_radio).checked(true);
        aq.id(R.id.lblListHeader).text(getString(R.string.new_project));
        setHasOptionsMenu(true);
        owner_list = (NiceSpinner) view.findViewById(R.id.owner_list);
        departments_list= (NiceSpinner) view.findViewById(R.id.departments_list);
        service = new TaskListService(getActivity());
        deptList = new LinkedList<>();
        for(int loop=0;loop<ProjectsFragment.allDeptList.size();loop++){
            deptList.add(ProjectsFragment.allDeptList.get(loop).getDeptName());
        }
        departments_list.attachDataSource(deptList);
        service.getTaskAssignee(Integer.parseInt(baseClass.getSelectedProject()), false,
                new CallBack(AddProjectFragment.this, "GetAllUsers"));
        dialogAC.show();
        return view;
    }
    public void GetAllUsers(Object caller, Object model) {
        TaskUserModel.getInstance().setList((TaskUserModel) model);
        if (TaskUserModel.getInstance().responseCode == 100) {
            userList= new LinkedList<>();
            for(int loop=0;loop< TaskUserModel.getInstance().responseObject.size();loop++) {
                userList.add(TaskUserModel.getInstance().responseObject.get(loop).firstName);
            }
            Collections.sort(userList,ByAlphabet);
            owner_list.attachDataSource(userList);
        }else{
            Toast.makeText(getActivity(), getString(R.string.response_error), Toast.LENGTH_SHORT).show();
        }
        dialogAC.dismiss();
    }
    Comparator<String> ByAlphabet = new Comparator<String>() {
        @Override
        public int compare(String lhs, String rhs) {
            return (Double.valueOf(rhs).compareTo(Double.valueOf(lhs)));
        }
    };
}
