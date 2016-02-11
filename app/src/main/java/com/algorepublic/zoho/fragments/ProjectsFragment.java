package com.algorepublic.zoho.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.algorepublic.zoho.Models.ProjectsByClientModel;
import com.algorepublic.zoho.Models.ProjectsByDepartmentModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterProjectsClientList;
import com.algorepublic.zoho.adapters.AdapterProjectsDeptList;
import com.algorepublic.zoho.adapters.ProjectsList;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.ProjectsListService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProjectsFragment#newInstance} factory method to
 * create an instance of this fragment_forums.
 */
public class ProjectsFragment extends BaseFragment implements AdapterView.OnItemClickListener{

    private AQuery aq;
    private BaseClass baseClass;
    ProjectsListService service;
    StickyListHeadersListView listView;
    StickyListHeadersAdapter projectAdapter;
    ArrayList<ProjectsList> allProjectsList = new ArrayList<>();
    ArrayList<ProjectsList> ByClientList = new ArrayList<>();
    public static ArrayList<ProjectsList> ByDepartmentList = new ArrayList<>();

    public ProjectsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment_forums using the provided parameters.
     * @return A new instance of fragment_forums ProjectsFragment.
     */
    public static ProjectsFragment newInstance() {
        ProjectsFragment fragment = new ProjectsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment_forums
        View view  = inflater.inflate(R.layout.fragment_projects, container, false);
        listView = (StickyListHeadersListView) view.findViewById(R.id.projects_liststicky);
        listView.setOnItemClickListener(this);
        aq = new AQuery(getActivity(), view);
        String [] types = {"All","By Client","By Department"};
        aq.id(R.id.spinner_sort).getSpinner().setAdapter(new ArrayAdapter<String>(
                getActivity(),
                R.layout.layout_spinner,
                types
        ));
        getToolbar().setTitle(getString(R.string.projects));
        setHasOptionsMenu(true);
        aq.id(R.id.spinner_sort).getSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0) {
                    aq.id(R.id.projects_list).visibility(View.VISIBLE);
                    aq.id(R.id.projects_liststicky).visibility(View.GONE);
                    SetGeneralClientAdapter();
                }
                if(position==1) {
                    aq.id(R.id.projects_list).visibility(View.VISIBLE);
                    aq.id(R.id.projects_liststicky).visibility(View.GONE);
                    SetClientProjectsAdapter();
                }
                if(position==2) {
                    aq.id(R.id.projects_list).visibility(View.GONE);
                    aq.id(R.id.projects_liststicky).visibility(View.VISIBLE);
                    SetDepartmentProjectsAdapter();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    /**
     * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.  The fragment_forums's
     * view hierarchy is not however attached to its parent at this point.
     *
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment_forums is being re-constructed
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        service = new ProjectsListService(getActivity());
        service.getProjectsByClient_API(baseClass.getUserId(), true, new CallBack(this, "ProjectsByClient"));
        service.getProjectsByDepartment(baseClass.getUserId(), true, new CallBack(this, "ProjectsByDepartment"));
    }

    public void ProjectsByClient(Object caller, Object model){
        ProjectsByClientModel.getInstance().setList((ProjectsByClientModel) model);
    }
    public void ProjectsByDepartment(Object caller, Object model){
        ProjectsByDepartmentModel.getInstance().setList((ProjectsByDepartmentModel) model);
        if (ProjectsByClientModel.getInstance().responseCode == 100
                || ProjectsByClientModel.getInstance().responseData.size() != 0
                || ProjectsByDepartmentModel.getInstance().responseCode == 100
                || ProjectsByDepartmentModel.getInstance().responseData.size() != 0) {
            AddClientProjects();
        } else {
            Toast.makeText(getActivity(), getString(R.string.projects_list_empty), Toast.LENGTH_SHORT).show();
        }
    }
    public void AddClientProjects(){
        ByClientList.clear();
        for (int loop = 0; loop < ProjectsByClientModel.getInstance().responseData.size(); loop++) {
            for(int loop1=0;loop1<ProjectsByClientModel.getInstance().responseData.get(loop).projects.size();loop1++){
                ProjectsList projectsList = new ProjectsList();
                projectsList.setCompOrDeptName(ProjectsByClientModel.getInstance().responseData.get(loop).companyName);
                projectsList.setCompOrDeptID(ProjectsByClientModel.getInstance().responseData.get(loop).ID);
                projectsList.setProjectID(ProjectsByClientModel.getInstance().responseData.get(loop).projects.get(loop1).projectID);
                projectsList.setProjectName(ProjectsByClientModel.getInstance().responseData.get(loop).projects.get(loop1).projectName);
                projectsList.setProjectDesc(ProjectsByClientModel.getInstance().responseData.get(loop).projects.get(loop1).description);
                projectsList.setTotalTasks(ProjectsByClientModel.getInstance().responseData.get(loop).projects.get(loop1).totalTasks);
                projectsList.setTotalUsers(ProjectsByClientModel.getInstance().responseData.get(loop).projects.get(loop1).usersCount);
                projectsList.setTotalMilestones(ProjectsByClientModel.getInstance().responseData.get(loop).projects.get(loop1).toalMilestones);
                ByClientList.add(projectsList);
            }
        }
        AddDepartmentProjects();
    }

    public void AddDepartmentProjects(){
        ByDepartmentList.clear();
        for (int loop = 0; loop < ProjectsByDepartmentModel.getInstance().responseData.size(); loop++) {
            for(int loop1=0;loop1<ProjectsByDepartmentModel.getInstance().responseData.get(loop).projects.size();loop1++){
                ProjectsList projectsList = new ProjectsList();
                if(ProjectsByDepartmentModel.getInstance().responseData.get(loop).ID.equals("0")){
                    projectsList.setCompOrDeptName("Unassigned Projects");
                }else {
                    projectsList.setCompOrDeptName(ProjectsByDepartmentModel.getInstance().responseData.get(loop).departmentName);
                }
                projectsList.setCompOrDeptID(ProjectsByDepartmentModel.getInstance().responseData.get(loop).ID);
                projectsList.setProjectID(ProjectsByDepartmentModel.getInstance().responseData.get(loop).projects.get(loop1).projectID);
                projectsList.setProjectName(ProjectsByDepartmentModel.getInstance().responseData.get(loop).projects.get(loop1).projectName);
                projectsList.setProjectDesc(ProjectsByDepartmentModel.getInstance().responseData.get(loop).projects.get(loop1).description);
                projectsList.setTotalTasks(ProjectsByDepartmentModel.getInstance().responseData.get(loop).projects.get(loop1).totalTasks);
                projectsList.setTotalUsers(ProjectsByDepartmentModel.getInstance().responseData.get(loop).projects.get(loop1).usersCount);
                projectsList.setTotalMilestones(ProjectsByDepartmentModel.getInstance().responseData.get(loop).projects.get(loop1).toalMilestones);
                ByDepartmentList.add(projectsList);
            }
        }
        AddUpAllProjects();
    }


    public void AddUpAllProjects(){
        allProjectsList.clear();
        allProjectsList.addAll(ByClientList);
        allProjectsList.addAll(ByDepartmentList);
        SetGeneralClientAdapter();
    }
    public void SetGeneralClientAdapter(){
        aq.id(R.id.projects_list).adapter(new AdapterProjectsClientList(getActivity(), allProjectsList));
        aq.id(R.id.projects_list).itemClicked(this);
    }
    public void SetClientProjectsAdapter(){
        aq.id(R.id.projects_list).adapter(new AdapterProjectsClientList(getActivity(), ByClientList));
        aq.id(R.id.projects_list).itemClicked(this);
    }
    public void SetDepartmentProjectsAdapter(){

        projectAdapter = new AdapterProjectsDeptList(getActivity());
        listView.setAreHeadersSticky(false);
        listView.setAdapter(projectAdapter);
        listView.setOnItemClickListener(this);
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Log.e("selected project", ((TextView) view.findViewById(R.id.project_id)).getText().toString());
        baseClass.setSelectedProject(((TextView) view.findViewById(R.id.project_id)).getText().toString());
        baseClass.db.putInt("ProjectID", Integer.parseInt(baseClass.getSelectedProject()));
        for (int i = 0; i < aq.id(R.id.projects_list).getListView().getChildCount(); i++) {
            if(position == i ){
                try {
                    aq.id(R.id.projects_list).getListView().getChildAt(i).setBackgroundColor(Color.parseColor("#666666"));
                }catch (Exception e){
                    e.printStackTrace();
                }
                try {
                    listView.getChildAt(i).setBackgroundColor(Color.parseColor("#666666"));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else{
                try {
                    aq.id(R.id.projects_list).getListView().getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                }catch (Exception e){
                    e.printStackTrace();
                }
                try {
                    listView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.  For this method
     * to be called, you must have first called {@link #setHasOptionsMenu}.  See
     * {@link (Menu) Activity.onCreateOptionsMenu}
     * for more information.
     *
     * @param menu     The options menu in which you place your items.
     * @param inflater
     * @see #setHasOptionsMenu
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_tasklist, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     * <p>
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_project:
                callFragmentWithBackStack(R.id.container, AddProject.newInstance(), "AddProject");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getSelectedItemPosition(String projectId){

    }
}
