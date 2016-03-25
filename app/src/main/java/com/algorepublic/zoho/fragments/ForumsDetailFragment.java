package com.algorepublic.zoho.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.algorepublic.zoho.Models.CreateForumCommentModel;
import com.algorepublic.zoho.Models.ForumsCommentModel;
import com.algorepublic.zoho.Models.ForumsModel;
import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterForumComment;
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

    AQuery aq;
    private BaseClass baseClass;
    static ForumsDetailFragment fragment;
    static int Position;
    public static int ClickedPosition;
    public static boolean flag= false;
    ForumService service;
    public static EditText comment_user;
    AdapterForumComment adapter;
    public static ArrayList<TaskComments> arrayList = new ArrayList<>();

    public static ForumsDetailFragment newInstance(int pos) {
        Position =pos;
        if(fragment==null){
            fragment = new ForumsDetailFragment();
        }
        return fragment;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_task_details, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     * <p>
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit_task:
                baseClass.hideKeyPad(getView());
                if (baseClass.getSelectedProject().equalsIgnoreCase("0")) {
                    Toast.makeText(getActivity(),"Please Select Project",Toast.LENGTH_SHORT).show();
                }else {
                    callFragmentWithBackStack(R.id.container, EditForumFragment.newInstance(Position), "EditForumFragment");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_forum_detail, container, false);
        comment_user = (EditText) view.findViewById(R.id.comment_user);
        aq = new AQuery(view);
        setHasOptionsMenu(true);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        adapter = new AdapterForumComment(getActivity());
        arrayList.clear();
        aq.id(R.id.forums_comment_list).adapter(adapter);
        service = new ForumService(getActivity());
        service.getForumsDetail(ForumsModel.getInstance().responseObject.get(Position).ID
                , true, new CallBack(ForumsDetailFragment.this, "ForumDetails"));
        aq.id(R.id.comment_description).text(getString(R.string.by) + " " + ForumsModel.getInstance().responseObject.get(Position).user.firstName
                + "," + getString(R.string.last_responce_on) +
                baseClass.DateFormatter(ForumsModel.getInstance().responseObject.get(Position).updatedAt) + " "
                + baseClass.GetTime(baseClass.DateMilli(ForumsModel.getInstance()
                .responseObject.get(Position).updatedAt)));

        aq.id(R.id.comment_user).getTextView().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == getResources().getInteger(R.integer.add_comment)) {
                    if (flag == true) {
                        service.updateforumComments(ForumsDetailFragment
                                .arrayList.get(ClickedPosition).getCommentID(), ForumsDetailFragment
                                .comment_user.getText().toString(), true, new
                                CallBack(ForumsDetailFragment.this, "UpdateComment"));
                    }else{
                        PerformAction();
                    }
                    return true;
                }
                return false;
            }
        });
        aq.id(R.id.send).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == true) {
                    service.updateforumComments(ForumsDetailFragment
                            .arrayList.get(ClickedPosition).getCommentID(), ForumsDetailFragment
                            .comment_user.getText().toString(), true, new
                            CallBack(ForumsDetailFragment.this, "UpdateComment"));
                } else {
                    PerformAction();
                }
            }
        });
        return view;
    }

    public void PerformAction()
    {
        String comment = aq.id(R.id.comment_user).getText().toString();
        if(aq.id(R.id.comment_user).getText().toString().equalsIgnoreCase("")) {
            Snackbar.make(getView(),getString(R.string.enter_comment),Snackbar.LENGTH_SHORT).show();
            return;
        }
        service.createforumComments(Integer.toString(ForumsModel.getInstance().responseObject.get(Position).ID)
                , comment, baseClass.getUserId(), false,
                new CallBack(ForumsDetailFragment.this, "CreateComment"));

        aq.id(R.id.response_alert).visibility(View.GONE);
    }
    public void UpdateComment(Object caller, Object model){
        GeneralModel.getInstance().setList((GeneralModel) model);
        if (GeneralModel.getInstance().responseObject ==true) {
            flag = false;
            ForumsDetailFragment
                    .arrayList.get(ClickedPosition).setComment(ForumsDetailFragment
                    .comment_user.getText().toString());
            ForumsDetailFragment
                    .arrayList.get(ClickedPosition).setDateTime(baseClass.GetDateTime());
            adapter.notifyDataSetChanged();
            ForumsDetailFragment
                    .comment_user.setText("");
        }else {
            Snackbar.make(getView(),
                    getActivity().getString(R.string.response_error), Snackbar.LENGTH_SHORT).show();
        }
    }
    public void CreateComment(Object caller, Object model) {
        CreateForumCommentModel.getInstance().setList((CreateForumCommentModel) model);
        if (CreateForumCommentModel.getInstance().responseCode ==100){
            Snackbar.make(getView(),"Comment Added",Snackbar.LENGTH_SHORT).show();
            aq.id(R.id.comment_user).text("");
            TaskComments taskComments = new TaskComments();
            taskComments.setCommentID(CreateForumCommentModel.getInstance().responseObject.Id);
            taskComments.setComment(CreateForumCommentModel.getInstance().responseObject.message);
            taskComments.setDateTime(GetDateTimeComment(DateMilli(CreateForumCommentModel.getInstance().responseObject.updatedAt)));
            taskComments.setUserName(baseClass.getFirstName());
            taskComments.setUserImagePath(baseClass.getProfileImage());
            taskComments.setUserImageID(baseClass.getProfileImageID());
            arrayList.add(taskComments);
            adapter.notifyDataSetChanged();
        }
        else
        {
            Snackbar.make(getView(), getString(R.string.response_error), Snackbar.LENGTH_SHORT).show();
        }
    }
    public void ForumDetails(Object caller, Object model){
        ForumsCommentModel.getInstance().setList((ForumsCommentModel) model);
        if (ForumsCommentModel.getInstance().responseObject.forumComments.size()!=0) {
            GetGeneralList();
        }else {
            Snackbar.make(getView(),getString(R.string.response_error),Snackbar.LENGTH_SHORT).show();
        }
        aq.id(R.id.alertMessage).text("No Comments");
        if(arrayList.size() ==0){
            aq.id(R.id.response_alert).visibility(View.VISIBLE);
        }else{
            aq.id(R.id.response_alert).visibility(View.GONE);
        }
        UpdateValues();
    }
    public void UpdateValues() {
        aq.id(R.id.comment_title).text(ForumsCommentModel.getInstance().responseObject.title);
        aq.id(R.id.comment_description).text(getString(R.string.by)+" " + ForumsModel.getInstance().responseObject.get(Position).user.firstName
                + "," + getString(R.string.last_responce_on) +
                baseClass.DateFormatter(ForumsModel.getInstance().responseObject.get(Position).updatedAt) + " "
                + baseClass.GetTime(baseClass.DateMilli(ForumsModel.getInstance()
                .responseObject.get(Position).updatedAt)));
        if((ForumsCommentModel.getInstance().responseObject.forumContent)!= null) {
            aq.id(R.id.content_description).text(Html.
                    fromHtml(ForumsCommentModel.getInstance().responseObject.forumContent));
        }
    }
    public void GetGeneralList() {
        arrayList.clear();
        for (int loop = 0; loop < ForumsCommentModel.getInstance().responseObject.forumComments.size(); loop++) {
            ForumsCommentModel.ForumComments forumComments =ForumsCommentModel.getInstance().responseObject.forumComments.get(loop);
            TaskComments taskComments = new TaskComments();
            taskComments.setCommentID(forumComments.commentID);
            taskComments.setComment(forumComments.message);
            taskComments.setDateTime(GetDateTimeComment(DateMilli(forumComments.createdAt)));
            taskComments.setUserName(forumComments.user.firstName);
            taskComments.setUserImagePath(forumComments.user.profileImagePath);
            arrayList.add(taskComments);
        }
        adapter.notifyDataSetChanged();
    }
}
