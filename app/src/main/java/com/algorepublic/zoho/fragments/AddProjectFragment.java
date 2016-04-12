package com.algorepublic.zoho.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.algorepublic.zoho.Models.CreateProjectModel;
import com.algorepublic.zoho.Models.TaskUserModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.DialogList;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddProjectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddProjectFragment extends BaseFragment {

    AQuery aq;
    BaseClass baseClass;
    TaskListService service;
    CharSequence[] userList,departmentList;
    View view;
    boolean isprivate= false;
    EditText owner_list,departments_list;
    int selectedOwner=-1,selectedDept=-1;
    ArrayList<DialogList> ownerList,deptList;
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
                ,ownerList.get(selectedOwner).getID()
                ,deptList.get(selectedDept).getID(),isprivate
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
        owner_list = (EditText) view.findViewById(R.id.owner_list);
        departments_list= (EditText) view.findViewById(R.id.departments_list);
        service = new TaskListService(getActivity());
        deptList = new ArrayList<>();
        for(int loop=0;loop<ProjectsFragment.allDeptList.size();loop++){
            DialogList dialogList = new DialogList();
            if(ProjectsFragment.allDeptList.get(loop).getDeptID()=="0"){
                dialogList.setName(getString(R.string.none));
            }else {
                dialogList.setName(ProjectsFragment.allDeptList.get(loop).getDeptName());
            }
            deptList.add(dialogList);
        }
        Collections.sort(deptList);
        departmentList = new CharSequence[deptList.size()];
        for(int loop=0;loop< deptList.size();loop++) {
            departmentList[loop] = deptList.get(loop).getName();
        }

        service.getTaskAssignee(Integer.parseInt(baseClass.getSelectedProject()), false,
                new CallBack(AddProjectFragment.this, "GetAllUsers"));
        owner_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            showOwnerList();
            }
        });
        departments_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            showDeptList();
            }
        });
        return view;
    }
    private void showOwnerList() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.select_user));
        builder.setSingleChoiceItems(userList, selectedOwner, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedOwner = which;
                owner_list.setText(userList[selectedOwner]);
                dialog.dismiss();
            }
        });
        builder.show();
    }
    private void showDeptList() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.select_user));
        builder.setSingleChoiceItems(departmentList, selectedDept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedDept = which;
                departments_list.setText(departmentList[selectedDept]);
                dialog.dismiss();
            }
        });
        builder.show();
    }
    public void GetAllUsers(Object caller, Object model) {
        TaskUserModel.getInstance().setList((TaskUserModel) model);
        if (TaskUserModel.getInstance().responseCode == 100) {
            ownerList = new ArrayList<>();
            for(int loop=0;loop< TaskUserModel.getInstance().responseObject.size();loop++) {
                DialogList dialogList = new DialogList();
                if(TaskUserModel.getInstance().responseObject.get(loop).ID==
                        Integer.parseInt(baseClass.getUserId())){
                    dialogList.setName(getString(R.string.me));
                }else {
                    dialogList.setName(TaskUserModel.getInstance().responseObject.get(loop).firstName);
                }
                dialogList.setID(TaskUserModel.getInstance().responseObject.get(loop).ID);
                ownerList.add(dialogList);
            }
            Collections.sort(ownerList);
            userList = new CharSequence[ownerList.size()];
            for(int loop=0;loop< ownerList.size();loop++) {
                userList[loop] = ownerList.get(loop).getName();
            }
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
