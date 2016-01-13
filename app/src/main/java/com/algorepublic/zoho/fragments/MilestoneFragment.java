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
 * Use the {@link MilestoneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MilestoneFragment extends BaseFragment {

    AQuery aq;
    public MilestoneFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MilestoneFragment.
     */
    public static MilestoneFragment newInstance(String param1, String param2) {
        MilestoneFragment fragment = new MilestoneFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_milestone, container, false);
        aq = new AQuery(getActivity(), view);
        return view;
    }

}
