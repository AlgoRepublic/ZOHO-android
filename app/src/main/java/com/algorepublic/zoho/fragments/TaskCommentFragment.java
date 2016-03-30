package com.algorepublic.zoho.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.algorepublic.zoho.Models.CreateCommentModel;
import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.Models.TaskCommentsModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterTaskComments;
import com.algorepublic.zoho.adapters.TaskComments;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.ForumService;
import com.algorepublic.zoho.services.TaskListService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

import java.util.ArrayList;

/**
 * Created by android on 1/1/16.
 */
public class TaskCommentFragment extends BaseFragment {

    AQuery aq;
    static TaskCommentFragment fragment;
    static int position;
    BaseClass baseClass;
    public static EditText comment_user;
    public static boolean flag= false;
    public static int ClickedPosition;
    AdapterTaskComments adapter;
    public static ListView listView;
    TaskListService service;
    ForumService forumService;
    public static ArrayList<TaskComments> arrayList = new ArrayList<>();
    TextView textView;


    public TaskCommentFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TaskCommentFragment newInstance(int pos) {
        position =pos;
        if(fragment==null){
            fragment = new TaskCommentFragment();
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
        // Inflate the layout for this fragment_forums
        View view = inflater.inflate(R.layout.fragment_task_comments, container, false);
        listView = (ListView) view.findViewById(R.id.listView_comments);
        comment_user = (EditText) view.findViewById(R.id.comment_user);
        aq = new AQuery(view);
        service = new TaskListService(getActivity());
        textView=(TextView)view.findViewById(R.id.comment_user);
        forumService = new ForumService(getActivity());
        adapter = new AdapterTaskComments(getActivity());
        listView.setAdapter(adapter);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        getToolbar().setTitle(getResources().getString(R.string.comments));
        service.getCommentsByTask(position,
                true,new CallBack(TaskCommentFragment.this,"TaskComments"));
        aq.id(R.id.comment_user).getTextView().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == getResources().getInteger(R.integer.add_comment)) {
                    if (flag == true) {
                        forumService.updateforumComments(ForumsDetailFragment
                                .arrayList.get(ClickedPosition).getCommentID(), ForumsDetailFragment
                                .comment_user.getText().toString(), true, new
                                CallBack(TaskCommentFragment.this, "UpdateComment"));
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
                baseClass.hideKeyPad(getActivity().getCurrentFocus());
                if (flag == true) {
                    forumService.updateforumComments(TaskCommentFragment
                            .arrayList.get(ClickedPosition).getCommentID(), TaskCommentFragment
                            .comment_user.getText().toString(), true, new
                            CallBack(TaskCommentFragment.this, "UpdateComment"));
                }else{
                    PerformAction();
                }
            }
        });
        return view;
    }
    public void TaskComments(Object caller, Object model) {
        TaskCommentsModel.getInstance().setList((TaskCommentsModel) model);
        if (TaskCommentsModel.getInstance().responseCode.equalsIgnoreCase("100")
                && TaskCommentsModel.getInstance().responseObject  != null) {
            GetGeneralList();
        }
        else
        {
            Snackbar.make(getView(), getString(R.string.invalid_credential), Snackbar.LENGTH_SHORT).show();
        }
        aq.id(R.id.alertMessage).text("No Comments");
        if(arrayList.size() ==0){
            aq.id(R.id.response_alert).visibility(View.VISIBLE);
        }else{
            aq.id(R.id.response_alert).visibility(View.GONE);
        }
    }
    public void GetGeneralList() {
        arrayList.clear();
        for (int loop = 0; loop < TaskCommentsModel.getInstance().responseObject.size(); loop++) {
            TaskComments taskComments = new TaskComments();
            taskComments.setCommentID(TaskCommentsModel.getInstance().responseObject.get(loop).Id);
            taskComments.setComment(TaskCommentsModel.getInstance().responseObject.get(loop).message);
            taskComments.setDateTime(GetDateTimeComment(TaskCommentsModel.getInstance().responseObject.get(loop).createdAt));
            taskComments.setUserName(TaskCommentsModel.getInstance().responseObject.get(loop).userObject.firstName);
            taskComments.setUserImagePath(TaskCommentsModel.getInstance().responseObject.get(loop).userObject.profileImagePath);
            taskComments.setUserImageID(TaskCommentsModel.getInstance().responseObject.get(loop).userObject.profilePictureID);
            arrayList.add(taskComments);
        }
        adapter.notifyDataSetChanged();
    }
    public void UpdateComment(Object caller, Object model){

        GeneralModel.getInstance().setList((GeneralModel) model);
        if (GeneralModel.getInstance().responseObject ==true) {
            flag = false;

            TaskCommentFragment
                    .arrayList.get(ClickedPosition).setComment(TaskCommentFragment
                    .comment_user.getText().toString());
            TaskCommentFragment
                    .arrayList.get(ClickedPosition).setDateTime(baseClass.GetDateTime());
            adapter.notifyDataSetChanged();
            TaskCommentFragment
                    .comment_user.setText("");
        }else {
            Snackbar.make(getView(),
                    getActivity().getString(R.string.response_error), Snackbar.LENGTH_SHORT).show();
        }
    }
    public void CreateComment(Object caller, Object model) {

        CreateCommentModel.getInstance().setList((CreateCommentModel) model);
        if (CreateCommentModel.getInstance().responseCode ==100){
            Snackbar.make(getView(), "Comment Added", Snackbar.LENGTH_SHORT).show();
            TaskComments taskComments = new TaskComments();
            taskComments.setCommentID(CreateCommentModel.getInstance().responseObject.Id);
            taskComments.setComment(CreateCommentModel.getInstance().responseObject.message);
            taskComments.setDateTime(GetDateTimeComment(DateMilli(CreateCommentModel.getInstance().responseObject.updatedAt)));
            taskComments.setUserName(CreateCommentModel.getInstance().responseObject.userObject.firstName);
            taskComments.setUserImagePath(CreateCommentModel.getInstance().responseObject.userObject.profileImagePath);
            taskComments.setUserImageID(CreateCommentModel.getInstance().responseObject.userObject.profilePictureID);
            arrayList.add(taskComments);
            adapter.notifyDataSetChanged();
        }
        else
        {
            Snackbar.make(getView(), getString(R.string.invalid_credential), Snackbar.LENGTH_SHORT).show();
        }
    }
    public void PerformAction()
    {
        String comment = aq.id(R.id.comment_user).getText().toString();
        if(aq.id(R.id.comment_user).getText().toString().isEmpty()) {
            Snackbar.make(getView(),getString(R.string.enter_comment),Snackbar.LENGTH_SHORT).show();
            return;
        }
        service.createComment(comment, position, Integer.parseInt(baseClass.getUserId()), true,
                new CallBack(TaskCommentFragment.this, "CreateComment"));
        aq.id(R.id.comment_user).text("");
        aq.id(R.id.response_alert).visibility(View.GONE);
    }
}
