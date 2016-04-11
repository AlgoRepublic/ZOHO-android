package com.algorepublic.zoho.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

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
    LinkedList<String> userList,userListID,deptList,deptListID;
    boolean isprivate= false;
    boolean isactive = false;
    NiceSpinner owner_list,departments_list;
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
        Integer ownerId=0;String deptId=null;
        String deptName = deptList.get(departments_list.getSelectedIndex());
        String ownerName = userList.get(owner_list.getSelectedIndex());
        if (deptName != null) {
            for(int loop=0;loop<ProjectsFragment.allDeptList.size();loop++)
                if(ProjectsFragment.allDeptList.get(loop).getDeptName().equalsIgnoreCase(deptName))
                   deptId = ProjectsFragment.allDeptList.get(loop).getDeptID();
        }
        if (ownerName != null) {
            for(int loop=0;loop<TaskUserModel.getInstance().responseObject.size();loop++)
                if(TaskUserModel.getInstance().responseObject.get(loop).firstName.equalsIgnoreCase(ownerName))
                    ownerId = TaskUserModel.getInstance().responseObject.get(loop).ID;
        }
        ProjectsListService service = new ProjectsListService(getActivity());
        service.updateProject(projectsLists.get(position).getProjectID()
                , aq.id(R.id.project_name).getText().toString(), baseClass.getUserId()
                , aq.id(R.id.project_desc).getText().toString()
                , ownerId, isactive
                , deptId, isprivate
                , true, new CallBack(EditProjectFragment.this, "UpdateProject"));
        Log.e("Owner", String.valueOf(owner_list.getSelectedIndex()));

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
        owner_list = (NiceSpinner) view.findViewById(R.id.owner_list);
        departments_list= (NiceSpinner) view.findViewById(R.id.departments_list);
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
        deptList = new LinkedList<>();
        deptListID = new LinkedList<>();
        for(int loop=0;loop<ProjectsFragment.allDeptList.size();loop++){
            if(ProjectsFragment.allDeptList.get(loop).getDeptID()=="0"){
                deptList.add(getString(R.string.none));
            }else
            deptList.add(ProjectsFragment.allDeptList.get(loop).getDeptName());
            deptListID.add(ProjectsFragment.allDeptList.get(loop).getDeptID());
        }
        Collections.sort(deptList,ByAlphabet);
        departments_list.attachDataSource(deptList);

        service.getTaskAssignee(Integer.parseInt(baseClass.getSelectedProject()), true,
                new CallBack(EditProjectFragment.this, "GetAllUsers"));
        return view;
    }
    public void UpdateValues() {
        aq.id(R.id.project_name).text(Html.fromHtml(projectsLists.get(position).getProjectName()));
        aq.id(R.id.project_desc).text(Html.fromHtml(projectsLists.get(position).getProjectDesc()));
        String deptID = projectsLists.get(position).getCompOrDeptID();
        String ownerID = projectsLists.get(position).getOwnerID();
        if (deptID != null) {
            for(int loop=0;loop<deptList.size();loop++)
                if(deptListID.get(loop).equalsIgnoreCase(deptID))
                    departments_list.setSelectedIndex(loop);
        }
        if (ownerID != null) {
            for(int loop=0;loop<userList.size();loop++)
                if(userListID.get(loop).equalsIgnoreCase(ownerID))
                    owner_list.setSelectedIndex(loop);
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
            userList= new LinkedList<>();
            userListID= new LinkedList<>();
            for(int loop=0;loop< TaskUserModel.getInstance().responseObject.size();loop++) {
                if(TaskUserModel.getInstance().responseObject.get(loop).ID==
                        Integer.parseInt(baseClass.getUserId())){
                    userList.add(getString(R.string.me));
                }else
                userList.add(TaskUserModel.getInstance().responseObject.get(loop).firstName);
                userListID.add(Integer.toString(TaskUserModel.getInstance().responseObject.get(loop).ID));
            }
            Collections.sort(userList,ByAlphabet);
            owner_list.attachDataSource(userList);
            UpdateValues();
        }
        else {
            Toast.makeText(getActivity(), getString(R.string.response_error), Toast.LENGTH_SHORT).show();
        }
    }
    Comparator<String>  ByAlphabet = new Comparator<String>() {
        @Override
        public int compare(String lhs, String rhs) {
            return (lhs.compareTo(rhs));
        }
    };
}
