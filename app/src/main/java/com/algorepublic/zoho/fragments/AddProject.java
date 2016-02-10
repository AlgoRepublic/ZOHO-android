package com.algorepublic.zoho.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.algorepublic.zoho.R;
import com.androidquery.AQuery;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddProject#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddProject extends BaseFragment {

    AQuery aq;

    public AddProject() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddProject.
     */
    // TODO: Rename and change types and number of parameters
    public static AddProject newInstance() {
        AddProject fragment = new AddProject();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.add_project, container, false);
        aq = new AQuery(getActivity(), view);

        return view;
    }

}
