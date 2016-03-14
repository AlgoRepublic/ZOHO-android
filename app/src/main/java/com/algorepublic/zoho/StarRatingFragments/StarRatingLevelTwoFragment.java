package com.algorepublic.zoho.StarRatingFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterStarRatingLevelOne;
import com.algorepublic.zoho.adapters.AdapterStarRatingLevelTwo;
import com.algorepublic.zoho.adapters.StarRatingHeadsLevelOne;
import com.algorepublic.zoho.adapters.StarRatingHeadsLevelTwo;
import com.algorepublic.zoho.fragments.BaseFragment;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.StarRatingService;

import java.util.ArrayList;

/**
 * Created by android on 2/24/16.
 */
public class StarRatingLevelTwoFragment extends BaseFragment {
    static StarRatingLevelTwoFragment fragment;
    ListView mListView;
    static ArrayList<StarRatingHeadsLevelTwo> levelTwos= new ArrayList<>();

    public static StarRatingLevelTwoFragment newInstance(ArrayList<StarRatingHeadsLevelTwo> twos) {
        levelTwos.addAll(twos);
        if (fragment == null) {
            fragment = new StarRatingLevelTwoFragment();
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
        StarRatingBaseFragment.textView1.setText(GetTitle());
        mListView = (ListView) view.findViewById(R.id.starListView);
        mListView.setAdapter(new AdapterStarRatingLevelTwo(getActivity(), levelTwos));
        return view;
    }
    public String GetTitle(){
        String value = "";
        if(StarRatingBaseFragment.textLevel1 != ""){
            value = StarRatingBaseFragment.arrow+" "+
                    StarRatingBaseFragment.textLevel1;
        }
        return value;
    }

    @Override
    public void onDestroy() {
        StarRatingBaseFragment.textView1.setText("➪ ");
        StarRatingBaseFragment.textLevel1="";
        super.onDestroy();
    }
}