package com.algorepublic.zoho.fragments;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterTaskComments;
import com.algorepublic.zoho.adapters.TaskComments;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

import java.util.ArrayList;

import app.minimize.com.seek_bar_compat.SeekBarCompat;

/**
 * Created by android on 1/1/16.
 */
public class FragmentTaskComment extends BaseFragment {

    AQuery aq;
    static FragmentTaskComment fragment;
    static int position;
    BaseClass baseClass;
    AdapterTaskComments adapter;
    public static ListView listView;
    public static ArrayList<TaskComments> arrayList = new ArrayList<>();

    SeekBarCompat seekBarCompat;

    public FragmentTaskComment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FragmentTaskComment newInstance(int pos) {
        position =pos;
        if(fragment==null){
            fragment = new FragmentTaskComment();
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
        adapter = new AdapterTaskComments(getActivity());
        listView.setAdapter(adapter);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        GetDateTime();
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
