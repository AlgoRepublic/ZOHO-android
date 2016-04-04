package com.algorepublic.zoho.fragments;


import android.os.Bundle;
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
import com.algorepublic.zoho.Models.TaskUserModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.ProjectsListService;
import com.algorepublic.zoho.services.TaskListService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

import org.angmarch.views.NiceSpinner;

import java.util.Collections;
import java.util.Comparator;
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
        return new AddProjectFragment();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_save_project, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_project:
                baseClass.hideKeyPad(getView());
                if(aq.id(R.id.project_name).getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), getActivity().getString(R.string.project_addname), Toast.LENGTH_SHORT).show();
                    return false;
                }
                if(aq.id(R.id.project_desc).getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), getActivity().getString(R.string.project_add_description), Toast.LENGTH_SHORT).show();
                    return false;
                }
                if(!aq.id(R.id.private_radio).isChecked() && !aq.id(R.id.public_radio).isChecked()){
                    Toast.makeText(getActivity(), getActivity().getString(R.string.project_access_option), Toast.LENGTH_SHORT).show();
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
                ,true,new CallBack(AddProjectFragment.this,"CreateProject"));
    }

    public void CreateProject(Object caller, Object model){
        CreateProjectModel.getInstance().setList((CreateProjectModel) model);
        if (CreateProjectModel.getInstance().responseObject !=null ) {
            Toast.makeText(getActivity(), getActivity().getString(R.string.project_created), Toast.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().popBackStack();
        }else {
            Toast.makeText(getActivity(), getActivity().getString(R.string.response_error), Toast.LENGTH_SHORT).show();
        }
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
            if(ProjectsFragment.allDeptList.get(loop).getDeptID()=="0"){
                deptList.add(getString(R.string.none));
            }else
                deptList.add(ProjectsFragment.allDeptList.get(loop).getDeptName());
        }
        Collections.sort(deptList,ByAlphabet);
        departments_list.attachDataSource(deptList);
        for(int loop=0;loop<deptList.size();loop++)
            if(deptList.get(loop).equalsIgnoreCase(getString(R.string.none)))
                departments_list.setSelectedIndex(loop);

        service.getTaskAssignee(Integer.parseInt(baseClass.getSelectedProject()), false,
                new CallBack(AddProjectFragment.this, "GetAllUsers"));
        return view;
    }
    public void GetAllUsers(Object caller, Object model) {
        TaskUserModel.getInstance().setList((TaskUserModel) model);
        if (TaskUserModel.getInstance().responseCode == 100) {
            userList= new LinkedList<>();
            for(int loop=0;loop< TaskUserModel.getInstance().responseObject.size();loop++) {
                if(TaskUserModel.getInstance().responseObject.get(loop).ID==
                        Integer.parseInt(baseClass.getUserId())){
                    userList.add(getString(R.string.me));
                }else
                    userList.add(TaskUserModel.getInstance().responseObject.get(loop).firstName);
            }
            Collections.sort(userList,ByAlphabet);
            owner_list.attachDataSource(userList);
            for(int loop=0;loop<userList.size();loop++)
                if(userList.get(loop).equalsIgnoreCase(getString(R.string.me)))
                    owner_list.setSelectedIndex(loop);
        }else{
            Toast.makeText(getActivity(), getString(R.string.response_error), Toast.LENGTH_SHORT).show();
        }
    }
    Comparator<String> ByAlphabet = new Comparator<String>() {
        @Override
        public int compare(String lhs, String rhs) {
            return (lhs.compareTo(rhs));
        }
    };
}
