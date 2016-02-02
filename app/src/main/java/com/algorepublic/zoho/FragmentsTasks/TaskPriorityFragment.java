package com.algorepublic.zoho.FragmentsTasks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterTaskPriority;
import com.algorepublic.zoho.fragments.BaseFragment;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskPriorityFragment extends BaseFragment {

    static TaskPriorityFragment fragment;
    AdapterTaskPriority adapter;
    AQuery aq;
    BaseClass baseClass;
    public static int position;

    public TaskPriorityFragment() {
    }
    @SuppressWarnings("unused")
    public static TaskPriorityFragment newInstance(int pos) {
        position = pos;
        if (fragment==null) {
            fragment = new TaskPriorityFragment();
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
        View view =  inflater.inflate(R.layout.fragment_task_priority, container, false);
        aq = new AQuery(view);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("None");
        arrayList.add("Low");
        arrayList.add("Medium");
        arrayList.add("High");
        aq.id(R.id.listview_priority).itemClicked(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelectedIndex(position);
                adapter.notifyDataSetChanged();
            }
        });
        adapter = new AdapterTaskPriority(getActivity(), arrayList);
        adapter.setSelectedIndex(baseClass.db.getInt("Priority"));
        aq.id(R.id.listview_priority).adapter(adapter);
        return view;
    }
}
