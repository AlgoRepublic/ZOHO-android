package com.algorepublic.zoho.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.algorepublic.zoho.MainActivity;
import com.algorepublic.zoho.Models.ProjectsByClientModel;
import com.algorepublic.zoho.Models.ProjectsByDepartmentModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterProjectsClientList;
import com.algorepublic.zoho.adapters.AdapterProjectsdeptList;
import com.algorepublic.zoho.adapters.ProjectsList;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.ProjectsListService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProjectsFragment#newInstance} factory method to
 * create an instance of this fragment_forums.
 */
public class ProjectsFragment extends BaseFragment implements AdapterView.OnItemClickListener{

    private AQuery aq;
    private BaseClass baseClass;
    ProjectsListService service;
    private AdapterProjectsClientList projectAdapter;
    public static ArrayList<ProjectsList> allProjectsList = new ArrayList<>();
    public static ArrayList<ProjectsList> ByClientList = new ArrayList<>();
    public static ArrayList<ProjectsList> BydepartmentList = new ArrayList<>();

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
        aq = new AQuery(getActivity(), view);
        String [] types = {"All","By Client","By Department"};
        aq.id(R.id.spinner_sort).getSpinner().setAdapter(new ArrayAdapter<String>(
                getActivity(),
                R.layout.layout_spinner,
                types
        ));
        aq.id(R.id.spinner_sort).getSpinner().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                    SetGeneralClientAdapter();
                if(position==1)
                    SetClientProjectsAdapter();
                if(position==2)
                    SetDepartmentProjectsAdapter();
            }
        });
        MainActivity.toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        setToolbar();
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
        service.getProjectsByClient_API("2", true, new CallBack(this, "ProjectsByClient"));
    }

    public void ProjectsByClient(Object caller, Object model){
        service.getProjectsByDepartment("2", true, new CallBack(this, "ProjectsByDepartment"));
        ProjectsByClientModel.getInstance().setList((ProjectsByClientModel) model);
    }
    public void ProjectsByDepartment(Object caller, Object model){
        ProjectsByDepartmentModel.getInstance().setList((ProjectsByDepartmentModel) model);
        if (ProjectsByClientModel.getInstance().responseCode.equalsIgnoreCase("100")
                || ProjectsByClientModel.getInstance().responseData.size() != 0
                || ProjectsByDepartmentModel.getInstance().responseCode.equalsIgnoreCase("100")
                || ProjectsByDepartmentModel.getInstance().responseData.size() != 0) {
            AddClientProjects();
        } else {
            Toast.makeText(getActivity(), getString(R.string.projects_list_empty), Toast.LENGTH_SHORT).show();
        }
    }
    public void AddDepartmentProjects(){
        BydepartmentList.clear();
        for (int loop = 0; loop < ProjectsByDepartmentModel.getInstance().responseData.size(); loop++) {
            ProjectsList projectsList = new ProjectsList();
            if(ProjectsByDepartmentModel.getInstance().responseData.get(loop).ID == 0){
                projectsList.setCompOrDeptName("Unassigned Projects");
                projectsList.setCharToAscii(CharToASCII(ProjectsByClientModel.getInstance().responseData.get(loop).companyName));
            }else {
                projectsList.setCharToAscii(CharToASCII("Unassigned Projects"));
                projectsList.setCompOrDeptName(ProjectsByDepartmentModel.getInstance().responseData.get(loop).departmentName);
            }
            projectsList.setCompOrDeptID(ProjectsByDepartmentModel.getInstance().responseData.get(loop).ID);

            for(int loop1=0;loop1<ProjectsByDepartmentModel.getInstance().responseData.get(loop).projects.size();loop1++){
                projectsList.setProjectID(ProjectsByDepartmentModel.getInstance().responseData.get(loop).projects.get(loop1).projectID);
                projectsList.setProjectName(ProjectsByDepartmentModel.getInstance().responseData.get(loop).projects.get(loop1).projectName);
                projectsList.setProjectDesc(ProjectsByDepartmentModel.getInstance().responseData.get(loop).projects.get(loop1).description);
            }
            BydepartmentList.add(projectsList);
        }
        Collections.sort(BydepartmentList);
        AddUpAllProjects();
    }

    public void AddClientProjects(){
        ByClientList.clear();
        for (int loop = 0; loop < ProjectsByClientModel.getInstance().responseData.size(); loop++) {
            ProjectsList projectsList = new ProjectsList();
            projectsList.setCompOrDeptName(ProjectsByClientModel.getInstance().responseData.get(loop).companyName);
            projectsList.setCompOrDeptID(ProjectsByClientModel.getInstance().responseData.get(loop).ID);

            for(int loop1=0;loop1<ProjectsByClientModel.getInstance().responseData.get(loop).projects.size();loop1++){
                projectsList.setProjectID(ProjectsByClientModel.getInstance().responseData.get(loop).projects.get(loop1).projectID);
                projectsList.setProjectName(ProjectsByClientModel.getInstance().responseData.get(loop).projects.get(loop1).projectName);
                projectsList.setProjectDesc(ProjectsByClientModel.getInstance().responseData.get(loop).projects.get(loop1).description);
            }
            ByClientList.add(projectsList);
        }
        AddDepartmentProjects();
    }

    public void AddUpAllProjects(){
        allProjectsList.clear();
        allProjectsList.addAll(ByClientList);
        allProjectsList.addAll(BydepartmentList);

    }
    public void SetGeneralClientAdapter(){
        aq.id(R.id.projects_list).adapter(new AdapterProjectsClientList(getActivity(), allProjectsList));
    }
    public void SetClientProjectsAdapter(){
        aq.id(R.id.projects_list).adapter(new AdapterProjectsClientList(getActivity(),ByClientList));
    }
    public void SetDepartmentProjectsAdapter(){
        aq.id(R.id.projects_list).adapter(new AdapterProjectsdeptList(getActivity()));
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Log.e("selected project", ((TextView) view.findViewById(R.id.project_id)).getText().toString());
        baseClass.setSelectedProject(((TextView) view.findViewById(R.id.project_id)).getText().toString());
    }

}
