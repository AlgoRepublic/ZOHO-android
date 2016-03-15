package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.SeekBar;

import com.algorepublic.zoho.BaseActivity;
import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.StarRatingFragments.StarRatingLevelQuestionsFragment;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.StarRatingService;
import com.androidquery.AQuery;

/**
 * Created by android on 2/3/16.
 */
public class AdapterStarRatingQuestions extends BaseAdapter {
    Context mContext;
    StarRatingService service;
    int ClickedPosition, userProgress;
    private LayoutInflater l_Inflater;
    AQuery aq;
    int multiple=5;

    public AdapterStarRatingQuestions(Context mContext) {
        this.mContext = mContext;
        l_Inflater = LayoutInflater.from(mContext);
        service = new StarRatingService((AppCompatActivity)mContext);
    }
    @Override
    public int getCount() {
        return StarRatingLevelQuestionsFragment.Questions.size();
    }

    @Override
    public Object getItem(int position) {
        return StarRatingLevelQuestionsFragment.Questions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView = l_Inflater.inflate(R.layout.drawer_list_item, parent, false);
        aq = new AQuery(convertView);
        aq.id(R.id.quest_text).text(mContext.getString(R.string.Q)+": " + StarRatingLevelQuestionsFragment.Questions
                .get(position).getQuestion());
        aq.id(R.id.comment_edittext).text(StarRatingLevelQuestionsFragment.Questions
                .get(position).getComment());
        aq.id(R.id.seekBar).getSeekBar().setProgress(StarRatingLevelQuestionsFragment.Questions
                .get(position).getProgress());
        aq.id(R.id.percent_text).text(StarRatingLevelQuestionsFragment.Questions
                .get(position).getProgress() + "%");
        RatingBar ratingBar  =(RatingBar) convertView.findViewById(R.id.star_rating);
        ratingBar.setRating(GetStarValue(StarRatingLevelQuestionsFragment.Questions
                .get(position).getProgress()));
        aq.id(R.id.devstage_text).text(GetStageValue(GetStarValue(StarRatingLevelQuestionsFragment.Questions
                .get(position).getProgress())));

        aq.id(R.id.comment_edittext).getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("ID","/"+StarRatingLevelQuestionsFragment.Questions
                        .get(position).getID());
                service.StarEditComment(StarRatingLevelQuestionsFragment.Questions
                        .get(position).getID(), StarRatingLevelQuestionsFragment.Questions
                        .get(position).getComment(), false, new
                        CallBack(AdapterStarRatingQuestions.this, "UpdateComment"));
                StarRatingLevelQuestionsFragment.dialogAC.show();
            }
        });
        aq.id(R.id.seekBar).getSeekBar().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                userProgress = Math.round(progress / multiple) * multiple;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                ClickedPosition = position;
                service.StarUpdateProgress(StarRatingLevelQuestionsFragment.Questions
                        .get(position).getID(), StarRatingLevelQuestionsFragment.Questions
                        .get(position).getProgress(), false, new
                        CallBack(AdapterStarRatingQuestions.this, "UpdateProgress"));
                StarRatingLevelQuestionsFragment.dialogAC.show();
            }
        });
        return convertView;
    }
    public void UpdateComment(Object caller, Object model) {
        GeneralModel.getInstance().setList((GeneralModel) model);
        if (GeneralModel.getInstance().responseObject) {
            Snackbar.make(((AppCompatActivity) mContext).findViewById(android.R.id.content),
                    mContext.getString(R.string.update_comment), Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(((AppCompatActivity) mContext).findViewById(android.R.id.content),
                    mContext.getString(R.string.response_error), Snackbar.LENGTH_SHORT).show();
        }
        StarRatingLevelQuestionsFragment.dialogAC.dismiss();
    }
    public void UpdateProgress(Object caller, Object model) {
        GeneralModel.getInstance().setList((GeneralModel) model);
        if (GeneralModel.getInstance().responseObject) {
            View view = StarRatingLevelQuestionsFragment.listView.getChildAt(ClickedPosition);
            AQuery aq= new AQuery(view);
            aq.id(R.id.seekBar).progress(userProgress);
            aq.id(R.id.percent_text).getTextView().setText(userProgress + "%");
            RatingBar ratingBar  =(RatingBar) view.findViewById(R.id.star_rating);
            ratingBar.setRating(GetStarValue(userProgress));
            aq.id(R.id.devstage_text).text(GetStageValue(userProgress));
            Snackbar.make(((AppCompatActivity) mContext).findViewById(android.R.id.content),
                    mContext.getString(R.string.update_progress), Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(((AppCompatActivity) mContext).findViewById(android.R.id.content),
                    mContext.getString(R.string.response_error), Snackbar.LENGTH_SHORT).show();
        }
        StarRatingLevelQuestionsFragment.dialogAC.dismiss();
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
    public String GetStageValue(int progress)
    {
        String starValue = "";
        if (progress >= 0 && progress <= 25)
            starValue =  mContext.getString(R.string.ratingPrimaryStage);
        else if (progress > 25 && progress <= 50)
            starValue =  mContext.getString(R.string.ratingDevelopmentStage);
        else if (progress > 50 && progress <= 75)
            starValue =  mContext.getString(R.string.ratingMaturityStage);
        else
            starValue =  mContext.getString(R.string.ratingLeadingStage);

        return starValue;
    }
}
