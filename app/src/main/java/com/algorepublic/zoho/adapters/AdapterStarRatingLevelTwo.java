package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.utils.CustomExpListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 2/24/16.
 */
public class AdapterStarRatingLevelTwo extends BaseExpandableListAdapter {
    private final Context mContext;
    private final List<StarRatingHeadsLevelTwo> mListDataHeader;

    public AdapterStarRatingLevelTwo(Context mContext, List<StarRatingHeadsLevelTwo> mListDataHeader) {
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
        CustomExpListView secondLevelExpListView = new CustomExpListView(this.mContext);
        secondLevelExpListView.setAdapter(new AdapterStarRatingLevelThree
                (this.mContext, mListDataHeader.get(childPosition).getLevelThrees()));
        secondLevelExpListView.setGroupIndicator(null);

        return secondLevelExpListView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mListDataHeader.get(groupPosition);
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
            convertView = layoutInflater.inflate(R.layout.drawer_list_level_two, parent, false);
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