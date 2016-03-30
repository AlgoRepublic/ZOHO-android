package com.algorepublic.zoho.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.algorepublic.zoho.Models.StarRatingModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterStarRatingLevelOne;
import com.algorepublic.zoho.adapters.StarRatingHeadsLevelOne;
import com.algorepublic.zoho.adapters.StarRatingHeadsLevelThree;
import com.algorepublic.zoho.adapters.StarRatingHeadsLevelTwo;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.StarRatingService;
import com.algorepublic.zoho.utils.CustomExpListView;
import com.androidquery.AQuery;

import java.util.ArrayList;

/**
 * Created by android on 3/25/16.
 */
public class StarRatingFragment extends BaseFragment {
    static StarRatingFragment fragment;
    public static ArrayList<StarRatingHeadsLevelOne> levelOneHead = new ArrayList<>();
    AQuery aq; CustomExpListView mListView;
    StarRatingService service;
    public static StarRatingFragment newInstance() {
        fragment = new StarRatingFragment();
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
        mListView = (CustomExpListView) view.findViewById(R.id.starListView);
        aq= new AQuery(view);
        service = new StarRatingService(getActivity());
        if(levelOneHead.size()==0) {
            service.getStarRatingHeads_API("en-Us", true,
                    new CallBack(StarRatingFragment.this, "StarRatingHeads"));
        }else{
            setAdapter();
        }
        return view;
    }
    private void setAdapter(){
        if (mListView != null) {
            AdapterStarRatingLevelOne parentLevelAdapter = new AdapterStarRatingLevelOne(getActivity(), levelOneHead);
            mListView.setAdapter(parentLevelAdapter);
        }
    }
    public void StarRatingHeads(Object caller, Object model) {
        StarRatingModel.getInstance().setList((StarRatingModel) model);
        if (StarRatingModel.getInstance().responseCode == 100) {
            GetListHeads();
            aq.id(R.id.alertMessage).text("No Star Rating");
            if(levelOneHead.size() ==0){
                aq.id(R.id.response_alert).visibility(View.VISIBLE);
            }else{
                aq.id(R.id.response_alert).visibility(View.GONE);
            }
            setAdapter();
        }
        else
        {
            Snackbar.make(getView(), getString(R.string.response_error), Snackbar.LENGTH_SHORT).show();
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