package com.algorepublic.zoho.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.StarRatingLevelOneAdapter;
import com.algorepublic.zoho.adapters.TaskComments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by android on 2/1/16.
 */
public class StarRatingFragment extends BaseFragment {

    static StarRatingFragment fragment;
    static int position;
    public static ListView listView;
    public static ArrayList<TaskComments> arrayList = new ArrayList<>();

    public StarRatingFragment() {
    }
    @SuppressWarnings("unused")
    public static StarRatingFragment newInstance() {
        if (fragment==null) {
            fragment = new StarRatingFragment();
        }
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_star_rating, container, false);
        List<String> listDataHeader = new ArrayList<>();
        String[] mItemHeaders = getResources().getStringArray(R.array.items_array_expandable_level_one);
        Collections.addAll(listDataHeader, mItemHeaders);
        ExpandableListView mExpandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView);
        if (mExpandableListView != null) {
            StarRatingLevelOneAdapter parentLevelAdapter = new StarRatingLevelOneAdapter(getActivity(), listDataHeader);
            mExpandableListView.setAdapter(parentLevelAdapter);
        }
        return view;
    }
}
