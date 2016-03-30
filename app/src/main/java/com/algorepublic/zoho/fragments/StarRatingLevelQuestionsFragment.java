package com.algorepublic.zoho.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Toast;

import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.Models.StarRatingQuestionModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.StarRatingQuestion;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.StarRatingService;
import com.androidquery.AQuery;

import java.util.ArrayList;

/**
 * Created by android on 2/24/16.
 */
public class StarRatingLevelQuestionsFragment extends BaseFragment {
    public static StarRatingLevelQuestionsFragment fragment;
    AQuery aq;
    StarRatingService service;
    int ClickedPosition, userProgress;
    int multiple=5;
    LinearLayout QuestLayout;
    public  static ArrayList<StarRatingQuestion> Questions = new ArrayList<>();
    static int ID;

    public static StarRatingLevelQuestionsFragment newInstance(int Id) {
        ID =Id;
        if(fragment==null) {
            fragment = new StarRatingLevelQuestionsFragment();
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
        View view = inflater.inflate(R.layout.fragment_star_rating_question, container, false);
        aq = new AQuery(view);
        QuestLayout = (LinearLayout) aq
                        .id(R.id.questListView).visible().getView();

        service = new StarRatingService(getActivity());
        service.getStarRatingQuestion_API(ID, "enUS",
                        true, new CallBack(StarRatingLevelQuestionsFragment.this, "StarRatingQuestion"));
        return view;
    }
    public void StarRatingQuestion(Object caller, Object model) {
        StarRatingQuestionModel.getInstance().setList((StarRatingQuestionModel) model);
        if (StarRatingQuestionModel.getInstance().responseCode == 100) {
            GetListQuestions();
        }
        else
        {
            Toast.makeText(getActivity(), getString(R.string.invalid_credential), Toast.LENGTH_SHORT).show();
        }
    }
    public void AddViews(){
        View QuestionLayout;
        for(int loop=0;loop<Questions.size();loop++) {

            QuestionLayout = LayoutInflater.from(getActivity()).inflate(
                    R.layout.drawer_list_item, null);

            final AQuery aq_quest = new AQuery(QuestionLayout);
            aq_quest.id(R.id.quest_text).text(getString(R.string.Q) + ": " + Questions
                    .get(loop).getQuestion());
            aq_quest.id(R.id.comment_edittext).text(Questions
                    .get(loop).getComment());
            aq_quest.id(R.id.seekBar).getSeekBar().setProgress(Questions
                    .get(loop).getProgress());
            aq_quest.id(R.id.percent_text).text(Questions
                    .get(loop).getProgress() + "%");
            RatingBar ratingBar  =(RatingBar) QuestionLayout.findViewById(R.id.star_rating);
            ratingBar.setRating(GetStarValue(Questions
                    .get(loop).getProgress()));
            aq_quest.id(R.id.devstage_text).text(GetStageValue(GetStarValue(Questions
                    .get(loop).getProgress())));
            aq_quest.id(R.id.comment_edittext).getEditText().setTag(loop);
            aq_quest.id(R.id.seekBar).getSeekBar().setTag(loop);
            aq_quest.id(R.id.comment_edittext).getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {

                    service.StarEditComment(Questions
                            .get(ClickedPosition).getID(),
                            aq_quest.id(R.id.comment_edittext).getText().toString(), true, new
                            CallBack(StarRatingLevelQuestionsFragment.this, "UpdateComment"));
                }
            });
            aq_quest.id(R.id.comment_edittext).getEditText().setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    ClickedPosition = (Integer)v.getTag();
                    return false;
                }
            });
            aq_quest.id(R.id.seekBar).getSeekBar().setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    ClickedPosition = (Integer)v.getTag();
                    return false;
                }
            });
            aq_quest.id(R.id.seekBar).getSeekBar().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    userProgress = Math.round(progress / multiple) * multiple;
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    View view = QuestLayout.getChildAt(ClickedPosition);
                    AQuery aq= new AQuery(view);
                    aq.id(R.id.seekBar).progress(userProgress);
                    aq.id(R.id.percent_text).getTextView().setText(userProgress + "%");
                    RatingBar ratingBar  =(RatingBar) view.findViewById(R.id.star_rating);
                    ratingBar.setRating(GetStarValue(userProgress));
                    aq.id(R.id.devstage_text).text(GetStageValue(userProgress));

                    service.StarUpdateProgress(Questions
                            .get(ClickedPosition).getID(),
                            userProgress, true, new
                            CallBack(StarRatingLevelQuestionsFragment.this, "UpdateProgress"));
                }
            });
            QuestLayout.addView(QuestionLayout);
        }
    }

    public void GetListQuestions(){
        Questions.clear();
        for(int loop=0;loop<StarRatingQuestionModel.getInstance().responseData.size();loop++){
            StarRatingQuestion question = new StarRatingQuestion();
            question.setID(StarRatingQuestionModel.getInstance().responseData.get(loop).ID);
            question.setQuestion(StarRatingQuestionModel.getInstance().responseData.get(loop).question);
            question.setComment(StarRatingQuestionModel.getInstance().responseData.get(loop).comment);
            question.setProgress(StarRatingQuestionModel.getInstance().responseData.get(loop).progress);
            Questions.add(question);
        }
        AddViews();
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
            starValue = getString(R.string.ratingPrimaryStage);
        else if (progress > 25 && progress <= 50)
            starValue =  getString(R.string.ratingDevelopmentStage);
        else if (progress > 50 && progress <= 75)
            starValue =  getString(R.string.ratingMaturityStage);
        else
            starValue =  getString(R.string.ratingLeadingStage);

        return starValue;
    }
    public void UpdateComment(Object caller, Object model) {
        GeneralModel.getInstance().setList((GeneralModel) model);
        if (GeneralModel.getInstance().responseObject) {
            Snackbar.make(getView(),
                    getString(R.string.update_comment), Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(getView(),
                    getString(R.string.response_error), Snackbar.LENGTH_SHORT).show();
        }
    }
    public void UpdateProgress(Object caller, Object model) {
        GeneralModel.getInstance().setList((GeneralModel) model);
        if (GeneralModel.getInstance().responseObject) {
            Snackbar.make(getView(),
                    getString(R.string.update_progress), Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(getView(),
                    getString(R.string.response_error), Snackbar.LENGTH_SHORT).show();
        }
    }

}
