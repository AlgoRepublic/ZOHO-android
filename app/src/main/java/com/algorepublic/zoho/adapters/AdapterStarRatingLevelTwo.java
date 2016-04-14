package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algorepublic.zoho.MainActivity;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.StarRatingFragment;
import com.algorepublic.zoho.fragments.StarRatingLevelQuestionsFragment;
import com.algorepublic.zoho.utils.BaseClass;
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
        mListView.setAdapter(new AdapterStarRatingLevelThree
                (this.mContext, mListDataHeader.get(childPosition).getLevelThrees()));
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
            convertView = layoutInflater.inflate(R.layout.drawer_list_level_two, parent, false);
            holder.imageView = (ImageView) convertView
                    .findViewById(R.id.imageViewleveltwo);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setText(mListDataHeader.get(groupPosition).getTitle());
        if(isExpanded){
            holder.imageView.setBackgroundResource(R.drawable.level_two_up);
            BaseClass.setViewAccordingToTheme(mContext, convertView, lblListHeader);
        }else{
            holder.imageView.setBackgroundResource(R.drawable.level_two_down);
            lblListHeader.setTextColor(mContext.getResources().getColor(R.color.colorBaseHeader));
            convertView.findViewById(R.id.listClick).setBackground(mContext.getResources().getDrawable(R.drawable.roundable_corner));
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