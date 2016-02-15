package com.algorepublic.zoho.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.algorepublic.zoho.ActivityTask;
import com.algorepublic.zoho.Models.TaskAssigneeModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterTaskAssignee;
import com.algorepublic.zoho.services.CallBack;
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
    static  int position;
    static AddProjectFragment fragment;

    public AddProjectFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AddProjectFragment newInstance(int Pos) {
        position = Pos;
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

                break;

        }
        return super.onOptionsItemSelected(item);
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
        setHasOptionsMenu(true);
        service = new TaskListService(getActivity());
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        ArrayList<String> DeptList = new ArrayList<>();
        for(int loop=0;loop<ProjectsFragment.allDeptList.size();loop++){
            DeptList.add(ProjectsFragment.allDeptList.get(loop).getDeptName());
        }

        aq.id(R.id.departments_list).getSpinner().setAdapter(new ArrayAdapter<String>(
                getActivity(),
                R.layout.layout_spinner_project,
                DeptList
        ));
        service.getAllUsers(true, new CallBack(AddProjectFragment.this, "GetAllUsers"));
        return view;
    }
    public void GetAllUsers(Object caller, Object model) {
        TaskAssigneeModel.getInstance().setList((TaskAssigneeModel) model);
        if (TaskAssigneeModel.getInstance().responseCode == 100) {
            ArrayList DeptList= new ArrayList();
            for(int loop=0;loop<TaskAssigneeModel.getInstance().responseObject.size();loop++) {
                DeptList.add(TaskAssigneeModel.getInstance().responseObject.get(loop).firstName);
            }
            aq.id(R.id.owner_list).getSpinner().setAdapter(new ArrayAdapter<String>(
                    getActivity(),
                    R.layout.layout_spinner_project,
                    DeptList
            ));
        }
        else
        {
            Toast.makeText(getActivity(), getString(R.string.invalid_credential), Toast.LENGTH_SHORT).show();
        }
    }

}
