package com.algorepublic.zoho.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.algorepublic.zoho.Models.CreateCommentModel;
import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterTaskComments;
import com.algorepublic.zoho.adapters.TaskComments;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.TaskListService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

import java.util.ArrayList;

import app.minimize.com.seek_bar_compat.SeekBarCompat;

/**
 * Created by android on 1/1/16.
 */
public class TaskCommentFragment extends BaseFragment {

    AQuery aq;
    static TaskCommentFragment fragment;
    static int position;
    BaseClass baseClass;
    AdapterTaskComments adapter;
    public static ListView listView;
    TaskListService service;
    public static ArrayList<TaskComments> arrayList = new ArrayList<>();

    SeekBarCompat seekBarCompat;

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task_comments, container, false);
        listView = (ListView) view.findViewById(R.id.listView_comments);
        aq = new AQuery(view);
        service = new TaskListService(getActivity());
        adapter = new AdapterTaskComments(getActivity());
        listView.setAdapter(adapter);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
       service.getCommentsByTask(4,
               true,new CallBack(TaskCommentFragment.this,"TaskComments"));
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
    public void TaskComments(Object caller, Object model) {
//        TaskCommentsModel.getInstance().setList((TaskCommentsModel) model);
//        if (TasksListModel.getInstance().responseCode == 100) {
//            GetGeneralList();
//        }
//        else
//        {
//            Toast.makeText(getActivity(), getString(R.string.invalid_credential), Toast.LENGTH_SHORT).show();
//        }
    }
    public void CreateComment(Object caller, Object model) {
        CreateCommentModel.getInstance().setList((CreateCommentModel) model);
        if (CreateCommentModel.getInstance().responseCode ==100){
            Snackbar.make(getView(),"Comment Added",Snackbar.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getActivity(), getString(R.string.invalid_credential), Toast.LENGTH_SHORT).show();
        }
    }
    public void PerformAction()
    {
        String comment = aq.id(R.id.comment_user).getText().toString();
        service.createComment(comment,4,Integer.parseInt(baseClass.getUserId()),false,
                new CallBack(TaskCommentFragment.this,"CreateComment"));
        if(aq.id(R.id.comment_user).getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getActivity(), "Enter Your Comment!", Toast.LENGTH_SHORT).show();
            return;
        }
        aq.id(R.id.comment_user).text("");
        TaskComments taskComments = new TaskComments();
        taskComments.setComment(comment);
        taskComments.setUserName(baseClass.getFirstName());
        taskComments.setUserImage("http://www.planwallpaper.com/static/images/magic-of-blue-universe-images.jpg");
        taskComments.setDateTime(GetDateTime());
        arrayList.add(taskComments);
        adapter.notifyDataSetChanged();
    }
}
