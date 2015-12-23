package com.algorepublic.zoho.FragmentsTasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.algorepublic.zoho.Models.TasksListModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.BaseFragment;
import com.androidquery.AQuery;

/**
 * Created by android on 12/17/15.
 */
public class TaskEditTitleFragment extends BaseFragment {

    static TaskEditTitleFragment fragment;
    public static int position;
    AQuery aq;

    public TaskEditTitleFragment() {
    }
    @SuppressWarnings("unused")
    public static TaskEditTitleFragment newInstance(int pos) {
        position = pos;
        if (fragment==null) {
            fragment = new TaskEditTitleFragment();
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
        View view = inflater.inflate(R.layout.fragment_edit_title_tasks, container, false);
        aq= new AQuery(view);
        aq.id(R.id.title_name).text(TasksListModel.getInstance().responseObject.get(position).Title);
        return view;
    }
}
