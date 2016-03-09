package com.algorepublic.zoho.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.algorepublic.zoho.Models.ForumsCommentModel;
import com.algorepublic.zoho.Models.ForumsModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterForumComment;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.ForumService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

/**
 * Created by waqas on 2/2/16.
 */
public class ForumsDetailFragment extends BaseFragment {
    private AQuery aq;
    private BaseClass baseClass;
    static ForumsDetailFragment fragment;
    static int Position;
    ListView forums_list;

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
        View view  = inflater.inflate(R.layout.layout_detailforum, container, false);
        aq = new AQuery(getActivity(), view);
        baseClass = ((BaseClass) getActivity().getApplicationContext());

        ForumService service = new ForumService(getActivity());
        service.getForumsDetail(ForumsModel.getInstance().responseObject.get(Position).ID
                ,true,new CallBack(ForumsDetailFragment.this,"ForumDetails"));

        aq.id(R.id.forum_title).text(ForumsModel.getInstance().responseObject.get(Position).title);
        aq.id(R.id.forum_description).text(getString(R.string.by) + ForumsModel.getInstance().responseObject.get(Position).user.firstName
                + ","+getString(R.string.last_responce_on) +
                baseClass.DateFormatter(ForumsModel.getInstance().responseObject.get(Position).updatedAt) + " "
                + baseClass.GetTime(baseClass.DateMilli(ForumsModel.getInstance()
                .responseObject.get(Position).updatedAt)));
        if((ForumsModel.getInstance().responseObject.get(Position).forumContent)!= null) {
              aq.id(R.id.content_description).text(Html.fromHtml(ForumsModel.getInstance()
                      .responseObject.get(Position).forumContent));
        }
        return view;
    }
    public void ForumDetails(Object caller, Object model){
        ForumsCommentModel.getInstance().setList((ForumsCommentModel) model);
        if (ForumsCommentModel.getInstance().responseObject.forumComments.size()!=0) {
            aq.id(R.id.forums_comment_list).adapter(new AdapterForumComment(getActivity()));
        }else {
            Snackbar.make(getView(),getString(R.string.response_error),Snackbar.LENGTH_SHORT).show();
        }

    }
}
