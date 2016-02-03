package com.algorepublic.zoho.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.algorepublic.zoho.Models.ForumsModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

/**
 * Created by waqas on 2/2/16.
 */
public class ForumsDetailFragment extends BaseFragment {
    private AQuery aq;
    private BaseClass baseClass;
    public static int Pos;
    static ForumsDetailFragment fragment;

    public static ForumsDetailFragment newInstance(int pos) {
        Pos =pos;
        if(fragment==null){
            fragment = new ForumsDetailFragment();
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.layout_detailforum, container, false);
        aq = new AQuery(getActivity(), view);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        aq.id(R.id.forum_title).text(ForumsModel.getInstance().responseObject.get(Pos).title);
        aq.id(R.id.forum_discription).text("by "+ ForumsModel.getInstance().responseObject.get(Pos).user.firstName
                +" , last responce on " +
                baseClass.DateFormatter(ForumsModel.getInstance().responseObject.get(Pos).updatedAt ) +" "
                + baseClass.GetTime(baseClass.DateMilli(ForumsModel.getInstance().responseObject.get(Pos).updatedAt)));
        aq.id(R.id.content_description).text(Html.fromHtml(ForumsModel.getInstance().responseObject.get(Pos).forumContent));




        return view;
    }
}
