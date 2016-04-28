package com.algorepublic.zoho.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.algorepublic.zoho.Models.CreateProjectModel;
import com.algorepublic.zoho.Models.TaskUserModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.DialogList;
import com.algorepublic.zoho.adapters.ProjectsList;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.ProjectsListService;
import com.algorepublic.zoho.services.TaskListService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by android on 2/15/16.
 */
public class EditProjectFragment extends BaseFragment {

    AQuery aq;
    BaseClass baseClass;
    static EditProjectFragment fragment;
    static int position;
    TaskListService service;
    RadioGroup radioGroup1,radioGroup2;
    CharSequence[] userList,departmentList;
    boolean isprivate= false;
    boolean isactive = false;
    EditText owner_list,departments_list;
    int selectedOwner=-1,selectedDept=-1;
    ArrayList<DialogList> ownerList,deptList;
    static ArrayList<ProjectsList> projectsLists= new ArrayList<>();

    public EditProjectFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static EditProjectFragment newInstance(ArrayList<ProjectsList> lists,int Pos) {
        position = Pos;
        projectsLists = lists;
        return new EditProjectFragment();
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
                if (!aq.id(R.id.active_radio).isChecked() && !aq.id(R.id.archive_radio).isChecked()) {
                    Snackbar.make(getView(), getString(R.string.project_status_option), Snackbar.LENGTH_SHORT).show();
                    return false;
                }

                UpdateProject();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
    public void UpdateProject(){

        ProjectsListService service = new ProjectsListService(getActivity());
        service.updateProject(projectsLists.get(position).getProjectID()
                , aq.id(R.id.project_name).getText().toString(), baseClass.getUserId()
                , aq.id(R.id.project_desc).getText().toString()
                , ownerList.get(selectedOwner).getID(), isactive
                , deptList.get(selectedDept).getID(), isprivate
                , true, new CallBack(EditProjectFragment.this, "UpdateProject"));
        Log.e("Owner", String.valueOf(selectedOwner));

    }
    public void UpdateProject(Object caller, Object model){
        CreateProjectModel.getInstance().setList((CreateProjectModel) model);
        if (CreateProjectModel.getInstance().responseObject != null ) {
            Snackbar.make(getView(),getString(R.string.project_updated),Snackbar.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().popBackStack();
        }else {
            Snackbar.make(getView(), getString(R.string.response_error), Snackbar.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_add_project, container, false);
        radioGroup1 = (RadioGroup) view.findViewById(R.id.private_public_group);
        radioGroup2 = (RadioGroup) view.findViewById(R.id.active_archive_status);
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.active_radio:
                        isactive = true;
                        break;
                    case R.id.archive_radio:
                        isactive = false;
                        break;
                }
            }
        });
        aq = new AQuery(getActivity(), view);
        aq.id(R.id.lblListHeader).text(getString(R.string.edit_project));
        owner_list = (EditText) view.findViewById(R.id.owner_list);
        departments_list= (EditText) view.findViewById(R.id.departments_list);
        owner_list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                baseClass.hideKeyPad(v);
                return false;
            }
        });
        departments_list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                baseClass.hideKeyPad(v);
                return false;
            }
        });

        service = new TaskListService(getActivity());
        setHasOptionsMenu(true);
        aq.id(R.id.active_archive_status).visible();
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        deptList = new ArrayList<>();
        for(int loop=0;loop<ProjectsFragment.allDeptList.size();loop++){
            DialogList dialogList = new DialogList();
            if(ProjectsFragment.allDeptList.get(loop).getDeptID()=="0"){
                dialogList.setName(getString(R.string.none));
                dialogList.setID(0);
            }else {
                dialogList.setName(ProjectsFragment.allDeptList.get(loop).getDeptName());
                dialogList.setID(Integer.parseInt(ProjectsFragment.allDeptList.get(loop).getDeptID()));
            }
            deptList.add(dialogList);
        }
        Collections.sort(deptList);
        departmentList = new CharSequence[deptList.size()];
        for(int loop=0;loop< deptList.size();loop++) {
            departmentList[loop] = deptList.get(loop).getName();
        }
        service.getOwnersByPermission(getString(R.string.projects_add), true,
                new CallBack(EditProjectFragment.this, "GetAllUsers"));
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
                selectedOwner = which;
                departments_list.setText(departmentList[selectedDept]);
                dialog.dismiss();
            }
        });
        builder.show();
    }
    public void UpdateValues() {
        aq.id(R.id.project_name).text(Html.fromHtml(projectsLists.get(position).getProjectName()));
        aq.id(R.id.project_desc).text(Html.fromHtml(projectsLists.get(position).getProjectDesc()));
        String deptID = projectsLists.get(position).getCompOrDeptID();
        String ownerID = projectsLists.get(position).getOwnerID();
        if (deptID != null) {
            for(int loop=0;loop<deptList.size();loop++)
                if(deptList.get(loop).getID() == Integer.parseInt(deptID)) {
                    selectedDept = loop;
                    departments_list.setText(deptList.get(loop).getName());
                }
        }
        if (ownerID != null) {
            for(int loop=0;loop<ownerList.size();loop++)
                if(ownerList.get(loop).getID() == Integer.parseInt(ownerID)) {
                    selectedOwner = loop;
                    owner_list.setText(ownerList.get(loop).getName());
                }
        }
        if(projectsLists.get(position).getDeleted()){
            aq.id(R.id.archive_radio).checked(true);
            aq.id(R.id.active_radio).checked(false);
        }else {
            aq.id(R.id.active_radio).checked(true);
            aq.id(R.id.archive_radio).checked(false);
        }

        if(projectsLists.get(position).getPrivate()){
            aq.id(R.id.private_radio).checked(true);
            aq.id(R.id.public_radio).checked(false);
        }else {
            aq.id(R.id.public_radio).checked(true);
            aq.id(R.id.private_radio).checked(false);
        }
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
            UpdateValues();
        }
        else {
            Toast.makeText(getActivity(), getString(R.string.response_error), Toast.LENGTH_SHORT).show();
        }
    }

}
