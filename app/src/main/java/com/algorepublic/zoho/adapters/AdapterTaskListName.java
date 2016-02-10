package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;

import com.algorepublic.zoho.ActivityTask;
import com.algorepublic.zoho.Models.TaskAssigneeModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.TasksListFragment;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

import java.util.ArrayList;

/**
 * Created by android on 2/9/16.
 */
public class AdapterTaskListName extends BaseAdapter {

    Context ctx;
    AQuery aq;
    BaseClass baseClass;
    private LayoutInflater l_Inflater;
    int selectedIndex =-1;

    public AdapterTaskListName(Context context){
        l_Inflater = LayoutInflater.from(context);
        this.ctx = context;
        baseClass = ((BaseClass) ctx.getApplicationContext());
    }

    @Override
    public int getCount() {

        return TasksListFragment.taskListName.size();
    }

    @Override
    public Object getItem(int pos) {
        return TasksListFragment.taskListName.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = l_Inflater.inflate(R.layout.layout_priorty_row, null);

        aq = new AQuery(convertView);
        if(selectedIndex == position)
        {
            aq.id(R.id.priority_checkbox).checked(true);
        }
        else
        {
            aq.id(R.id.priority_checkbox).checked(false);
        }
        aq.id(R.id.priority).text(TasksListFragment.taskListName.get(position).getTaskListName());
        return convertView;
    }
    public void setSelectedIndex(int index)
    {
        selectedIndex = index;
    }
}
