package com.algorepublic.zoho.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.algorepublic.zoho.Models.StarRatingModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterStarRatingLevelOne;
import com.algorepublic.zoho.adapters.StarRatingHeadsLevelOne;
import com.algorepublic.zoho.adapters.StarRatingHeadsLevelThree;
import com.algorepublic.zoho.adapters.StarRatingHeadsLevelTwo;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.StarRatingService;

import java.util.ArrayList;

/**
 * Created by android on 2/1/16.
 */
public class StarRatingFragment extends BaseFragment {

    static StarRatingFragment fragment;
    public static ArrayList<StarRatingHeadsLevelOne> levelOneHead = new ArrayList<>();
    StarRatingService service;
    ExpandableListView mExpandableListView;
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
        mExpandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView);
        service = new StarRatingService(getActivity());
        service.getStarRatingHeads_API("en-Us",true,new CallBack(StarRatingFragment.this,"StarRatingHeads"));
        getToolbar().setTitle(getString(R.string.start_rating));
        return view;
    }

    public void StarRatingHeads(Object caller, Object model) {
        StarRatingModel.getInstance().setList((StarRatingModel) model);
        if (StarRatingModel.getInstance().responseCode == 100) {
            GetListHeads();
            if (mExpandableListView != null) {
                mExpandableListView.setAdapter(new AdapterStarRatingLevelOne(getActivity()));
            }
        }
        else
        {
            Toast.makeText(getActivity(), getString(R.string.invalid_credential), Toast.LENGTH_SHORT).show();
        }
    }


    public void GetListHeads() {
        levelOneHead.clear();
        for (int loop = 0; loop < StarRatingModel.getInstance().responseData.size(); loop++) {
            StarRatingHeadsLevelOne levelOne = new StarRatingHeadsLevelOne();
            ArrayList<StarRatingHeadsLevelTwo> levelTwoHead = new ArrayList<>();
            for (int loop1 = 0; loop1 < StarRatingModel.getInstance().responseData.get(loop)
                    .subCategories.size(); loop1++) {
                StarRatingHeadsLevelTwo levelTwo = new StarRatingHeadsLevelTwo();
                ArrayList<StarRatingHeadsLevelThree> levelThreeHead = new ArrayList<>();
                for (int loop2 = 0; loop2 < StarRatingModel.getInstance()
                        .responseData.get(loop).subCategories.get(loop1).questCategories.size(); loop2++) {
                    StarRatingHeadsLevelThree levelThree = new StarRatingHeadsLevelThree();
                    levelThree.setTitle(StarRatingModel.getInstance()
                            .responseData.get(loop).subCategories.get(loop1).questCategories.get(loop2).questTitle);
                    levelThree.setID(StarRatingModel.getInstance()
                            .responseData.get(loop).subCategories.get(loop1).questCategories.get(loop2).questID);
                    levelThreeHead.add(levelThree);
                }
                levelTwo.setTitle(StarRatingModel.getInstance()
                        .responseData.get(loop).subCategories.get(loop1).subTitle);
                levelTwo.setID(StarRatingModel.getInstance()
                        .responseData.get(loop).subCategories.get(loop1).subID);
                levelTwo.setLevelThrees(levelThreeHead);
                levelTwoHead.add(levelTwo);
            }
            levelOne.setTitle(StarRatingModel.getInstance()
                    .responseData.get(loop).title);
            levelOne.setID(StarRatingModel.getInstance()
                    .responseData.get(loop).ID);
            levelOne.setLevelTwos(levelTwoHead);
            levelOneHead.add(levelOne);
        }
    }
}
