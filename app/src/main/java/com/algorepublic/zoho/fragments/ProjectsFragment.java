package com.algorepublic.zoho.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.algorepublic.zoho.Models.AllProjectsByUserModel;
import com.algorepublic.zoho.Models.ProjectsByClientModel;
import com.algorepublic.zoho.Models.ProjectsByDepartmentModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterProjectsClientList;
import com.algorepublic.zoho.adapters.AdapterProjectsDeptList;
import com.algorepublic.zoho.adapters.DeptList;
import com.algorepublic.zoho.adapters.ProjectsList;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.ProjectsListService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProjectsFragment#newInstance} factory method to
 * create an instance of this fragment_forums.
 */
public class ProjectsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    private AQuery aq;
    private BaseClass baseClass;
    ProjectsListService service;
    public static StickyListHeadersListView listViewDept;
    public static ListView listViewClient;
    StickyListHeadersAdapter projectAdapter;
    AdapterProjectsClientList clientAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    static  ArrayList<ProjectsList> allProjectsList = new ArrayList<>();
    static  ArrayList<DeptList> allDeptList = new ArrayList<>();
    ArrayList<ProjectsList> ByClientList = new ArrayList<>();
    public static ArrayList<ProjectsList> ByDepartmentList = new ArrayList<>();
    static int lastposition=0;
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
        listViewDept = (StickyListHeadersListView) view.findViewById(R.id.projects_liststicky);
        listViewClient = (ListView) view.findViewById(R.id.projects_list);
        aq = new AQuery(getActivity(), view);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setProgressViewOffset(false, 0, 200);
        getToolbar().setTitle(getString(R.string.projects));
        setHasOptionsMenu(true);
        aq.id(R.id.sort).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallForFilter();
            }
        });
        aq.id(R.id.sort_button).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallForFilter();
            }
        });



        return view;
    }
    public void CallForFilter(){
        String[] menuItems = {getString(R.string.all_projects),getString(R.string.by_client),getString(R.string.by_dept)};
        final ActionSheetDialog dialog = new ActionSheetDialog(getActivity(),menuItems, getView());
        dialog.isTitleShow(false).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                if (isLoaded())
                    lastposition = position;
                Filter(lastposition);
            }
        });
    }
    public void Filter(int position) {

        if (position == 0) {
            aq.id(R.id.projects_list).visibility(View.VISIBLE);
            aq.id(R.id.projects_liststicky).visibility(View.GONE);
            SetGeneralClientAdapter();
        }
        if (position == 1) {
            aq.id(R.id.projects_list).visibility(View.VISIBLE);
            aq.id(R.id.projects_liststicky).visibility(View.GONE);
            SetClientProjectsAdapter();
        }
        if (position == 2) {
            aq.id(R.id.projects_list).visibility(View.GONE);
            aq.id(R.id.projects_liststicky).visibility(View.VISIBLE);
            SetDepartmentProjectsAdapter();
        }
    }
    public boolean isLoaded(){
        Boolean isloading=false;
        if(allProjectsList.size()==0)
            isloading = false;
        else
            isloading= true;

        return isloading;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        service = new ProjectsListService(getActivity());
        service.getAllProjectsByUser_API(baseClass.getUserId(), true, new CallBack(this, "AllProjects"));
        service.getProjectsByClient_API(baseClass.getUserId(), true, new CallBack(this, "ProjectsByClient"));
        service.getProjectsByDepartment(baseClass.getUserId(),
                true, new CallBack(this, "ProjectsByDepartment"));
        applyLightBackground(aq.id(R.id.sort).getView(), baseClass);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        service.getAllProjectsByUser_API(baseClass.getUserId(), true, new CallBack(this, "AllProjects"));

                                    }
                                }
        );
    }
    public void AllProjects(Object caller, Object model) {
        swipeRefreshLayout.setRefreshing(true);
        AllProjectsByUserModel.getInstance().setList((AllProjectsByUserModel) model);
        if (AllProjectsByUserModel.getInstance().responseCode == 100
                || AllProjectsByUserModel.getInstance().responseData.size() != 0) {
            AddAllProjects();

        } else {
            Snackbar.make(getView(), getString(R.string.response_error), Snackbar.LENGTH_SHORT).show();
        }
    }
    public void ProjectsByClient(Object caller, Object model){
        ProjectsByClientModel.getInstance().setList((ProjectsByClientModel) model);
        if (ProjectsByClientModel.getInstance().responseCode == 100
                || ProjectsByClientModel.getInstance().responseData.size() != 0){
            AddClientProjects();
        }else {
            Toast.makeText(getActivity(), getString(R.string.response_error), Toast.LENGTH_SHORT).show();
        }
    }
    public void ProjectsByDepartment(Object caller, Object model){
        ProjectsByDepartmentModel.getInstance().setList((ProjectsByDepartmentModel) model);
        if (ProjectsByDepartmentModel.getInstance().responseCode == 100
                || ProjectsByDepartmentModel.getInstance().responseData.size() != 0) {
            AddDepartmentProjects();
        } else {
            Toast.makeText(getActivity(), getString(R.string.response_error), Toast.LENGTH_SHORT).show();
        }
    }
    public void AddAllProjects(){
        allProjectsList.clear();
        for (int loop = 0; loop < AllProjectsByUserModel.getInstance().responseData.size(); loop++) {
                ProjectsList projectsList = new ProjectsList();
                projectsList.setCompOrDeptName("");
                projectsList.setCompOrDeptID("1");
                projectsList.setProjectID(Integer.toString(AllProjectsByUserModel.getInstance().responseData.get(loop).projectID));
                projectsList.setProjectName(AllProjectsByUserModel.getInstance().responseData.get(loop).projectName);
                projectsList.setOwnerID(AllProjectsByUserModel.getInstance().responseData.get(loop).ownerID);
                projectsList.setOwnerName(AllProjectsByUserModel.getInstance().responseData.get(loop).ownerName);
                projectsList.setProjectDesc(AllProjectsByUserModel.getInstance().responseData.get(loop).description);
                projectsList.setTotalTasks(AllProjectsByUserModel.getInstance().responseData.get(loop).totalTasks);
                projectsList.setTotalUsers(AllProjectsByUserModel.getInstance().responseData.get(loop).usersCount);
                projectsList.setTotalMilestones(AllProjectsByUserModel.getInstance().responseData.get(loop).toalMilestones);
                projectsList.setDeleted(AllProjectsByUserModel.getInstance().responseData.get(loop).IsDeleted);
                projectsList.setPrivate(AllProjectsByUserModel.getInstance().responseData.get(loop).Isprivate);
                allProjectsList.add(projectsList);
        }
        swipeRefreshLayout.setRefreshing(false);
        SetGeneralClientAdapter();
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
                projectsList.setOwnerID(ProjectsByClientModel.getInstance().responseData.get(loop).projects.get(loop1).ownerID);
                projectsList.setOwnerName(ProjectsByClientModel.getInstance().responseData.get(loop).projects.get(loop1).ownerName);
                projectsList.setProjectDesc(ProjectsByClientModel.getInstance().responseData.get(loop).projects.get(loop1).description);
                projectsList.setTotalTasks(ProjectsByClientModel.getInstance().responseData.get(loop).projects.get(loop1).totalTasks);
                projectsList.setTotalUsers(ProjectsByClientModel.getInstance().responseData.get(loop).projects.get(loop1).usersCount);
                projectsList.setTotalMilestones(ProjectsByClientModel.getInstance().responseData.get(loop).projects.get(loop1).toalMilestones);
                projectsList.setDeleted(ProjectsByClientModel.getInstance().responseData.get(loop).projects.get(loop1).IsDeleted);
                projectsList.setPrivate(ProjectsByClientModel.getInstance().responseData.get(loop).projects.get(loop1).Isprivate);
                ByClientList.add(projectsList);
            }
        }
    }

    public void AddDepartmentProjects(){
        ByDepartmentList.clear();
        for (int loop = 0; loop < ProjectsByDepartmentModel.getInstance().responseData.size(); loop++) {
            DeptList deptList = new DeptList();
            deptList.setDeptID(ProjectsByDepartmentModel.getInstance().responseData.get(loop).ID);
            deptList.setDeptName(ProjectsByDepartmentModel.getInstance().responseData.get(loop).departmentName);
            allDeptList.add(deptList);
            for(int loop1=0;loop1<ProjectsByDepartmentModel.getInstance().responseData.get(loop).projects.size();loop1++){
                ProjectsList projectsList = new ProjectsList();
                if(ProjectsByDepartmentModel.getInstance().responseData.get(loop).ID.equals("0")){
                    projectsList.setCompOrDeptName(getString(R.string.un_assigned));
                }else {
                    projectsList.setCompOrDeptName(ProjectsByDepartmentModel.getInstance().responseData.get(loop).departmentName);
                }
                projectsList.setCompOrDeptID(ProjectsByDepartmentModel.getInstance().responseData.get(loop).ID);
                projectsList.setProjectID(ProjectsByDepartmentModel.getInstance().responseData.get(loop).projects.get(loop1).projectID);
                projectsList.setProjectName(ProjectsByDepartmentModel.getInstance().responseData.get(loop).projects.get(loop1).projectName);
                projectsList.setOwnerID(ProjectsByDepartmentModel.getInstance().responseData.get(loop).projects.get(loop1).ownerID);
                projectsList.setOwnerName(ProjectsByDepartmentModel.getInstance().responseData.get(loop).projects.get(loop1).ownerName);
                projectsList.setProjectDesc(ProjectsByDepartmentModel.getInstance().responseData.get(loop).projects.get(loop1).description);
                projectsList.setTotalTasks(ProjectsByDepartmentModel.getInstance().responseData.get(loop).projects.get(loop1).totalTasks);
                projectsList.setTotalUsers(ProjectsByDepartmentModel.getInstance().responseData.get(loop).projects.get(loop1).usersCount);
                projectsList.setTotalMilestones(ProjectsByDepartmentModel.getInstance().responseData.get(loop).projects.get(loop1).toalMilestones);
                projectsList.setDeleted(ProjectsByDepartmentModel.getInstance().responseData.get(loop).projects.get(loop1).IsDeleted);
                projectsList.setPrivate(ProjectsByDepartmentModel.getInstance().responseData.get(loop).projects.get(loop1).Isprivate);
                ByDepartmentList.add(projectsList);
            }
        }
    }

    public void SetGeneralClientAdapter() {
        clientAdapter = new AdapterProjectsClientList(getActivity(), allProjectsList);
        listViewClient.setAdapter(clientAdapter);
    }
    public void SetClientProjectsAdapter(){
        clientAdapter = new AdapterProjectsClientList(getActivity(), ByClientList);
        listViewClient.setAdapter(clientAdapter);
    }
    public void SetDepartmentProjectsAdapter(){

        projectAdapter = new AdapterProjectsDeptList(getActivity());
        listViewDept.setAreHeadersSticky(false);
        listViewDept.setAdapter(projectAdapter);
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
                baseClass.hideKeyPad(getView());
                if(allDeptList.size()==0){
                    Snackbar.make(getView(),getString(R.string.project_loading),Snackbar.LENGTH_SHORT).show();
                }else {
                    callFragmentWithBackStack(R.id.container, AddProjectFragment.newInstance(), "AddProjectFragment");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        service = new ProjectsListService(getActivity());
        service.getAllProjectsByUser_API(baseClass.getUserId(), true, new CallBack(this, "AllProjects"));
        service.getProjectsByClient_API(baseClass.getUserId(), true, new CallBack(this, "ProjectsByClient"));
        service.getProjectsByDepartment(baseClass.getUserId(),
                true, new CallBack(this, "ProjectsByDepartment"));
        applyLightBackground(aq.id(R.id.sort).getView(), baseClass);
    }
}
