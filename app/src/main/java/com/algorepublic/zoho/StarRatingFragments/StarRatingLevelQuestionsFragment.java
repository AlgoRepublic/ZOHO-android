package com.algorepublic.zoho.StarRatingFragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.algorepublic.zoho.BaseActivity;
import com.algorepublic.zoho.Models.StarRatingQuestionModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterStarRatingQuestions;
import com.algorepublic.zoho.adapters.StarRatingQuestion;
import com.algorepublic.zoho.fragments.BaseFragment;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.StarRatingService;
import com.androidquery.AQuery;

import java.util.ArrayList;

import cc.cloudist.acplibrary.ACProgressFlower;

/**
 * Created by android on 2/24/16.
 */
public class StarRatingLevelQuestionsFragment extends BaseFragment {
    static StarRatingLevelQuestionsFragment fragment;
    AQuery aq;
    public static StarRatingService service;
    public static ListView listView;

    public  static ArrayList<StarRatingQuestion> Questions = new ArrayList<>();
    static int ID;

    public static StarRatingLevelQuestionsFragment newInstance(int Id) {
        ID =Id;
        if (fragment==null) {
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
        listView = (ListView) view.findViewById(R.id.questListView);
        aq = new AQuery(view);
        Questions.clear();
        service = new StarRatingService(getActivity());
        service.getStarRatingQuestion_API(ID, "en-US",
                true, new CallBack(StarRatingLevelQuestionsFragment.this, "StarRatingQuestion"));
        return view;
    }
    public void StarRatingQuestion(Object caller, Object model) {
        StarRatingQuestionModel.getInstance().setList((StarRatingQuestionModel) model);
        if (StarRatingQuestionModel.getInstance().responseCode == 100) {
            GetListQuestions();
            aq.id(R.id.alertMessage).text("No Questions");
            if(Questions.size() ==0){
                aq.id(R.id.response_alert).visibility(View.VISIBLE);
            }else{
                aq.id(R.id.response_alert).visibility(View.GONE);
            }
          listView.setAdapter(new AdapterStarRatingQuestions(getActivity()));
        }
        else
        {
            Snackbar.make(getView(), getString(R.string.response_error), Snackbar.LENGTH_SHORT).show();
        }
    }
    public void GetListQuestions(){
        for(int loop=0;loop<StarRatingQuestionModel.getInstance().responseData.size();loop++){
            StarRatingQuestion question = new StarRatingQuestion();
            question.setID(StarRatingQuestionModel.getInstance().responseData.get(loop).ID);
            question.setQuestion(StarRatingQuestionModel.getInstance().responseData.get(loop).question);
            question.setComment(StarRatingQuestionModel.getInstance().responseData.get(loop).comment);
            question.setProgress(StarRatingQuestionModel.getInstance().responseData.get(loop).progress);
            Questions.add(question);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
