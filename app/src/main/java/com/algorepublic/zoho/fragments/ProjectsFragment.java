package com.algorepublic.zoho.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
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
public class ProjectsFragment extends Fragment implements AdapterView.OnItemClickListener{

    private AQuery aq;
    private BaseClass baseClass;
    private AdapterProjectsList projectAdapter;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_projects, container, false);
        aq = new AQuery(getActivity(), view);
        return view;
    }

    /**
     * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.  The fragment's
     * view hierarchy is not however attached to its parent at this point.
     *
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        ProjectsListService service = new ProjectsListService(getActivity());
        service.getProjectsList(baseClass.getUserId(), true, new CallBack(this, "ProjectsListCallback"));
    }

    public void ProjectsListCallback(Object caller, Object model){
        ProjectsModel.getInstance().setList((ProjectsModel) model);
        if (ProjectsModel.getInstance().responseCode.equalsIgnoreCase("100")
                || ProjectsModel.getInstance().responseData.size() != 0) {
            projectAdapter = new AdapterProjectsList(getActivity());
            aq.id(R.id.projects_list).adapter(projectAdapter).itemClicked(ProjectsFragment.this);
        } else {
            Toast.makeText(getActivity(), getString(R.string.projects_list_empty), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        projectAdapter.setSelectedIndex(position);
        projectAdapter.notifyDataSetChanged();
        Log.e("selected project", ((TextView) view.findViewById(R.id.project_id)).getText().toString());
        baseClass.setSelectedProject(((TextView) view.findViewById(R.id.project_id)).getText().toString());
    }
}
