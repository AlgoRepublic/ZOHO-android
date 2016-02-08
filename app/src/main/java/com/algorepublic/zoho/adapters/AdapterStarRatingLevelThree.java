package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.algorepublic.zoho.Models.StarRatingQuestionModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.StarRatingService;
import com.androidquery.AQuery;

import java.util.ArrayList;

/**
 * Created by android on 2/3/16.
 */
public class AdapterStarRatingLevelThree extends BaseExpandableListAdapter {
    private final Context mContext;
    ArrayList<StarRatingHeadsLevelThree> levelThrees =  new ArrayList<>();
    StarRatingService service;LayoutInflater layoutInflater;
    AQuery aq,aq_quest; LinearLayout QuestLayout;

    public AdapterStarRatingLevelThree(Context mContext,ArrayList<StarRatingHeadsLevelThree> threes) {
        this.mContext = mContext;
        service = new StarRatingService((AppCompatActivity)mContext);
        levelThrees.addAll(threes);
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
            if(convertView == null) {
                layoutInflater = (LayoutInflater) this.mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.drawer_list_item, parent, false);
                aq = new AQuery(convertView);
                QuestLayout = (LinearLayout) aq
                        .id(R.id.layout_questions).visible().getView();
            }
        try {
            StarRatingQuestionModel.getInstance().responseData.clear();
        }catch (NullPointerException e){}
        QuestLayout.removeAllViews();
        QuestLayout.invalidate();
        service.getStarRatingQuestion_API(levelThrees.get(groupPosition).getID(), "en-US",
                true, new CallBack(AdapterStarRatingLevelThree.this, "StarRatingQuestion"));
        return convertView;
    }
    public void StarRatingQuestion(Object caller, Object model) {
        StarRatingQuestionModel.getInstance().setList((StarRatingQuestionModel) model);
        if (StarRatingQuestionModel.getInstance().responseCode == 100) {
            View QuestionLayout;
            for(int loop=0;loop<StarRatingQuestionModel.getInstance().responseData.size();loop++) {
                if(loop==0) {
                    QuestionLayout = layoutInflater.inflate(
                            R.layout.drawer_list_item1, null);
                }else
                {
                    QuestionLayout = layoutInflater.inflate(
                            R.layout.drawer_list_item2, null);
                }
                aq_quest = new AQuery(QuestionLayout);
                aq_quest.id(R.id.quest_text).text("Q: " + StarRatingQuestionModel.getInstance()
                        .responseData.get(loop).question);
                aq_quest.id(R.id.comment_edittext).text(StarRatingQuestionModel.getInstance()
                        .responseData.get(loop).question);
                aq_quest.id(R.id.seekBar).getSeekBar().setProgress(StarRatingQuestionModel.getInstance()
                        .responseData.get(loop).progress);
                aq_quest.id(R.id.percent_text).text(StarRatingQuestionModel.getInstance()
                        .responseData.get(loop).progress + "%");
                final RatingBar ratingBar  =(RatingBar) QuestionLayout.findViewById(R.id.star_rating);
                ratingBar.setProgress(GetStarValue(StarRatingQuestionModel.getInstance()
                        .responseData.get(loop).progress));
                aq_quest.id(R.id.comment_edit).clicked(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        aq_quest.id(R.id.comment_edittext).enabled(true).clickable(true);
                        aq_quest.id(R.id.comment_edittext).getEditText().requestFocus();
                        aq_quest.id(R.id.comment_edittext).getEditText().setFocusable(true);
                    }
                });
                aq_quest.id(R.id.seekBar).getSeekBar().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        aq_quest.id(R.id.percent_text).getTextView().setText(progress + "%");
                        aq_quest.id(R.id.star_rating).progress(GetStarValue(progress));
                        ratingBar.setProgress(progress);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                QuestLayout.addView(QuestionLayout);
            }
        }
        else
        {
            Toast.makeText(((AppCompatActivity)mContext), mContext.getString(R.string.invalid_credential), Toast.LENGTH_SHORT).show();
        }
    }
    public int GetStarValue(int progress)
    {
        int starValue = 0;
        if (progress >= 0 && progress <= 20)
        {
            starValue= 1;
        }
        else if (progress > 20 && progress <= 35)
        {
            starValue = 2;
        }
        else if (progress > 35 && progress <= 60) {
            starValue =  3;
        }
        else if (progress > 60 && progress <= 80) {
            starValue =  4;
        }
        else if (progress > 80 && progress <= 100) {
            starValue = 5;
        }
        return starValue;
    }
    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return levelThrees.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return levelThrees.size();
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
            convertView = layoutInflater.inflate(R.layout.drawer_list_level_three, parent, false);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setText(levelThrees.get(groupPosition).getTitle());
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
