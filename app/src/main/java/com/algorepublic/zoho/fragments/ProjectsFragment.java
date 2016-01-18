package com.algorepublic.zoho.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.algorepublic.zoho.Models.ProjectsModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterProjectsList;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.ProjectsListService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProjectsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectsFragment extends Fragment {

    private AQuery aq;
    private BaseClass baseClass;
    public ProjectsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ProjectsFragment.
     */
    public static ProjectsFragment newInstance() {
        ProjectsFragment fragment = new ProjectsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        if(ProjectsModel.getInstance().reponseData.size() == 0) {
            ProjectsListService service = new ProjectsListService(getActivity());
            service.getProjectsList(baseClass.getUserId(), true, new CallBack(this, "ProjectsListCallback"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_projects, container, false);
        aq = new AQuery(getActivity(), view);
        return view;
    }

    public void ProjectsListCallback(Object caller, Object model){
        ProjectsModel.getInstance().setList((ProjectsModel) model);
        if (ProjectsModel.getInstance().responseCode.equalsIgnoreCase("100")
                || ProjectsModel.getInstance().reponseData.size() != 0) {
            AdapterProjectsList projectAdapter = new AdapterProjectsList(getActivity());
            aq.id(R.id.projects_list).adapter(projectAdapter);
        } else {
            Toast.makeText(getActivity(), getString(R.string.projects_list_empty), Toast.LENGTH_SHORT).show();
        }
    }

}
