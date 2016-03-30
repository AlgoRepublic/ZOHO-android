package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.StarRatingLevelQuestionsFragment;
import com.algorepublic.zoho.utils.CustomExpListView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by android on 2/1/16.
 */
public class AdapterStarRatingLevelOne extends BaseExpandableListAdapter {
    private final Context mContext;
    private final List<StarRatingHeadsLevelOne> mListDataHeader;
    TextView lblListHeader;

    public AdapterStarRatingLevelOne(Context mContext, List<StarRatingHeadsLevelOne> mListDataHeader) {
        this.mContext = mContext;
        this.mListDataHeader = new ArrayList<>();
        this.mListDataHeader.addAll(mListDataHeader);
    }
    @Override
    public void onGroupCollapsed(int groupPosition) {
        StarRatingLevelQuestionsFragment.fragment = null;
        super.onGroupCollapsed(groupPosition);
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
        LayoutInflater layoutInflater = (LayoutInflater) this.mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.fragment_star_rating, parent, false);
        CustomExpListView mListView = (CustomExpListView) view.findViewById(R.id.starListView);
        mListView.setAdapter(new AdapterStarRatingLevelTwo
                (this.mContext, mListDataHeader.get(groupPosition).getLevelTwos()));

        return mListView;
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
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.drawer_list_level_one, parent, false);
            holder.imageView = (ImageView) convertView
                    .findViewById(R.id.imageViewlevelone);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
     lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setText(mListDataHeader.get(groupPosition).getTitle());
        if (isExpanded) {
            holder.imageView.setBackgroundResource(R.drawable.level_one_up);
        }else{
            holder.imageView.setBackgroundResource(R.drawable.level_one_down);
        }

        return convertView;
    }
    static class ViewHolder {
        private ImageView imageView;
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