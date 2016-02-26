package com.algorepublic.zoho.StarRatingFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.BaseFragment;
import com.androidquery.AQuery;

/**
 * Created by waqas on 2/23/16.
 */
public class StarRatingBaseFragment extends BaseFragment {
    static StarRatingBaseFragment fragment;
    public static String arrow ="➪";
    public static String textLevel1,textLevel2,textLevel3;
    public static TextView textView1,textView2,textView3;
    AQuery aq;
    public static StarRatingBaseFragment newInstance() {
        if (fragment==null) {
            fragment = new StarRatingBaseFragment();
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
        View view = inflater.inflate(R.layout.fragment_star_rating_base, container, false);
        textView1 = (TextView) view.findViewById(R.id.textLevel1);
        textView2 = (TextView) view.findViewById(R.id.textLevel2);
        textView3 = (TextView) view.findViewById(R.id.textLevel3);
        aq = new AQuery(view);
        getToolbar().setTitle(getString(R.string.start_rating));
        if(savedInstanceState == null) {
            callFragment(R.id.starContainer, StarRatingLevelOneFragment.newInstance(), "StarRatingLevelOneFragment");
        }
        return view;
    }
}
