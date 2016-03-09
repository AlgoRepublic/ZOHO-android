package com.algorepublic.zoho.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.algorepublic.zoho.Models.CreateCommentModel;
import com.algorepublic.zoho.Models.ForumsCommentModel;
import com.algorepublic.zoho.Models.ForumsModel;
import com.algorepublic.zoho.Models.TaskCommentsModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterForumComment;
import com.algorepublic.zoho.adapters.AdapterTaskComments;
import com.algorepublic.zoho.adapters.TaskComments;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.ForumService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

import java.util.ArrayList;

/**
 * Created by waqas on 2/2/16.
 */
public class ForumsDetailFragment extends BaseFragment {

    private AQuery aq;
    private BaseClass baseClass;
    static ForumsDetailFragment fragment;
    static int Position;
    AdapterForumComment adapter;
    public static ArrayList<TaskComments> arrayList = new ArrayList<>();

    public static ForumsDetailFragment newInstance(int pos) {
        Position =pos;
        if(fragment==null){
            fragment = new ForumsDetailFragment();
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_forum_detail, container, false);
        aq = new AQuery(getActivity(), view);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        adapter = new AdapterForumComment(getActivity());
        aq.id(R.id.forums_comment_list).adapter(adapter);
        ForumService service = new ForumService(getActivity());
        service.getForumsDetail(ForumsModel.getInstance().responseObject.get(Position).ID
                ,true,new CallBack(ForumsDetailFragment.this,"ForumDetails"));

        aq.id(R.id.forum_title).text(ForumsModel.getInstance().responseObject.get(Position).title);
        aq.id(R.id.forum_description).text(getString(R.string.by)+" " + ForumsModel.getInstance().responseObject.get(Position).user.firstName
                + "," + getString(R.string.last_responce_on) +
                baseClass.DateFormatter(ForumsModel.getInstance().responseObject.get(Position).updatedAt) + " "
                + baseClass.GetTime(baseClass.DateMilli(ForumsModel.getInstance()
                .responseObject.get(Position).updatedAt)));
        if((ForumsModel.getInstance().responseObject.get(Position).forumContent)!= null) {
              aq.id(R.id.content_description).text(Html.fromHtml(ForumsModel.getInstance()
                      .responseObject.get(Position).forumContent));
        }
        aq.id(R.id.comment_user).getTextView().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == getResources().getInteger(R.integer.add_comment)) {
                    PerformAction();
                    return true;
                }
                return false;
            }
        });
        aq.id(R.id.send).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerformAction();
            }
        });
        return view;
    }

    public void PerformAction()
    {
        String comment = aq.id(R.id.comment_user).getText().toString();
//        service.createComment(comment,position,Integer.parseInt(baseClass.getUserId()),false,
//                new CallBack(TaskCommentFragment.this,"CreateComment"));
        if(aq.id(R.id.comment_user).getText().toString().equalsIgnoreCase("")) {
            Snackbar.make(getView(),getString(R.string.enter_comment),Snackbar.LENGTH_SHORT).show();
            return;
        }
        aq.id(R.id.comment_user).text("");
        TaskComments taskComments = new TaskComments();
        taskComments.setComment(comment);
        taskComments.setUserName(baseClass.getFirstName());
        taskComments.setUserImagePath(baseClass.getProfileImage());
        taskComments.setDateTime(GetDateTime());
        arrayList.add(taskComments);
        adapter.notifyDataSetChanged();
    }
    public void CreateComment(Object caller, Object model) {
        CreateCommentModel.getInstance().setList((CreateCommentModel) model);
        if (CreateCommentModel.getInstance().responseCode ==100){
            Snackbar.make(getView(),"Comment Added",Snackbar.LENGTH_SHORT).show();
        }
        else
        {
            Snackbar.make(getView(), getString(R.string.invalid_credential), Snackbar.LENGTH_SHORT).show();
        }
    }
    public void ForumDetails(Object caller, Object model){
        ForumsCommentModel.getInstance().setList((ForumsCommentModel) model);
        if (ForumsCommentModel.getInstance().responseObject.forumComments.size()!=0) {
           // GetGeneralList();
        }else {
            Snackbar.make(getView(),getString(R.string.response_error),Snackbar.LENGTH_SHORT).show();
        }
    }
    public void GetGeneralList() {
        arrayList.clear();
        for (int loop = 0; loop < TaskCommentsModel.getInstance().responseObject.size(); loop++) {
            TaskComments taskComments = new TaskComments();
            taskComments.setComment(TaskCommentsModel.getInstance().responseObject.get(loop).message);
            taskComments.setDateTime(GetDateTimeComment(TaskCommentsModel.getInstance().responseObject.get(loop).createdAt));
            taskComments.setUserName(TaskCommentsModel.getInstance().responseObject.get(loop).userObject.firstName);
            taskComments.setUserImagePath(TaskCommentsModel.getInstance().responseObject.get(loop).userObject.profileImagePath);
            arrayList.add(taskComments);
        }
        adapter.notifyDataSetChanged();
    }
}
