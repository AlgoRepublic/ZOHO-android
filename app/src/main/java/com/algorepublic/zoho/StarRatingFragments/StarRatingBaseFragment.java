package com.algorepublic.zoho.StarRatingFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.BaseFragment;
import com.androidquery.AQuery;

/**
 * Created by waqas on 2/23/16.
 */
public class StarRatingBaseFragment extends BaseFragment {
    static StarRatingBaseFragment fragment;
    public static StringBuilder textLevel = new StringBuilder();
    AQuery aq;
    public static StarRatingBaseFragment newInstance() {
            textLevel.append(">");
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
        aq = new AQuery(view);
        aq.id(R.id.textLevel).text(textLevel);
        getToolbar().setTitle(getString(R.string.start_rating));
        callFragment(R.id.starContainer, StarRatingLevelOneFragment.newInstance(), "StarRatingLevelOneFragment");
        return view;
    }
}
