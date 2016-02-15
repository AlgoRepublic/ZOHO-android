package com.algorepublic.zoho.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.algorepublic.zoho.R;
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

    public AddProjectFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AddProjectFragment newInstance() {
        AddProjectFragment fragment = new AddProjectFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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
        View view  = inflater.inflate(R.layout.add_user, container, false);
        aq = new AQuery(getActivity(), view);
        setHasOptionsMenu(true);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        ArrayList<String> DeptList = new ArrayList<>();
        for(int loop=0;loop<ProjectsFragment.allProjectsList.size();loop++){
            DeptList.add(ProjectsFragment.allProjectsList.get(loop).getCompOrDeptName());
        }
        aq.id(R.id.departments_list).getSpinner().setAdapter(new ArrayAdapter<String>(
                getActivity(),
                R.layout.layout_spinner_project,
                DeptList
        ));
        aq.id(R.id.owner_list).getSpinner().setAdapter(new ArrayAdapter<String>(
                getActivity(),
                R.layout.layout_spinner_project,
                DeptList
        ));
        return view;
    }

}
