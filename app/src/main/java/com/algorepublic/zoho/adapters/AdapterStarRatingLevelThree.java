package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.Models.StarRatingQuestionModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.StarRatingLevelQuestionsFragment;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.StarRatingService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 2/24/16.
 */
public class AdapterStarRatingLevelThree extends BaseExpandableListAdapter {
    AQuery aq;
    public static StarRatingService service;
    private final Context mContext;
    LayoutInflater layoutInflater;
    ImageView imageView;
    private final List<StarRatingHeadsLevelThree> mListDataHeader;

    public AdapterStarRatingLevelThree(Context mContext, List<StarRatingHeadsLevelThree> mListDataHeader) {
        this.mContext = mContext;
        layoutInflater = (LayoutInflater) this.mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        convertView = layoutInflater.inflate(R.layout.frame_layout, parent, false);

        callFragmentWithReplace(R.id.star_container,
                StarRatingLevelQuestionsFragment.
                        newInstance(mListDataHeader.
                                get(groupPosition).getID()), "StarRatingLevelQuestionsFragment");

        return convertView;
    }
    public void callFragmentWithReplace(int containerId, Fragment fragment, String tag){
        ((AppCompatActivity)mContext).getSupportFragmentManager()
                .beginTransaction()
                .replace(containerId, fragment, tag)
                .setCustomAnimations(R.anim.slide_in_enter, R.anim.slide_in_exit,
                        R.anim.slide_pop_enter, R.anim.slide_pop_exit)
                .commit();
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
            convertView = layoutInflater.inflate(R.layout.drawer_list_level_three, parent, false);
            holder.imageView = (ImageView) convertView
                    .findViewById(R.id.imageViewlevelthree);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setText(mListDataHeader.get(groupPosition).getTitle());
        if(isExpanded){
            holder.imageView.setBackgroundResource(R.drawable.level_three_up);
            BaseClass.setViewAccordingToTheme(mContext, convertView, lblListHeader);
        }else{
            holder.imageView.setBackgroundResource(R.drawable.level_three_down);
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