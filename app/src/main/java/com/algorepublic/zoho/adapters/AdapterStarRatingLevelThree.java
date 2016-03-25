package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.StarRatingFragments.StarRatingLevelQuestionsFragment;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 2/24/16.
 */
public class AdapterStarRatingLevelThree extends BaseExpandableListAdapter {
    private final Context mContext;
    private final List<StarRatingHeadsLevelThree> mListDataHeader;

    public AdapterStarRatingLevelThree(Context mContext, List<StarRatingHeadsLevelThree> mListDataHeader) {
        this.mContext = mContext;
        this.mListDataHeader = new ArrayList<>();
        this.mListDataHeader.addAll(mListDataHeader);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        ExpandableListView secondLevelExpListView = new ExpandableListView(this.mContext);
//        secondLevelExpListView.setAdapter(new AdapterStarRatingLevelThree
//                (this.mContext, mListDataHeader.get(groupPosition)));
//        secondLevelExpListView.setGroupIndicator(null);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mListDataHeader.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupPosition;
    }

    @Override
    public int getGroupCount() {
        return this.mListDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.drawer_list_level_one, parent, false);
        }
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);

        lblListHeader.setText(mListDataHeader.get(groupPosition).getTitle());
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}