package com.algorepublic.zoho.StarRatingFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterStarRatingLevelThree;
import com.algorepublic.zoho.adapters.AdapterStarRatingLevelTwo;
import com.algorepublic.zoho.adapters.StarRatingHeadsLevelThree;
import com.algorepublic.zoho.adapters.StarRatingHeadsLevelTwo;
import com.algorepublic.zoho.fragments.BaseFragment;

import java.util.ArrayList;

/**
 * Created by android on 2/24/16.
 */
public class StarRatingLevelThreeFragment extends BaseFragment {
    static StarRatingLevelThreeFragment fragment;
    ListView mListView;
    static ArrayList<StarRatingHeadsLevelThree> levelThrees= new ArrayList<>();

    public static StarRatingLevelThreeFragment newInstance(ArrayList<StarRatingHeadsLevelThree> threes) {
        levelThrees.addAll(threes);
        if (fragment == null) {
            fragment = new StarRatingLevelThreeFragment();
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
        mListView = (ListView) view.findViewById(R.id.starListView);
        mListView.setAdapter(new AdapterStarRatingLevelThree(getActivity(),levelThrees));
        return view;
    }
}