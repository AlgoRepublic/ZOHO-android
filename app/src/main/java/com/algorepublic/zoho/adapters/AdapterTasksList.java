package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.FragmentTasksList;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by android on 12/23/15.
 */
public class AdapterTasksList extends BaseAdapter implements StickyListHeadersAdapter {

    Context ctx;
    AQuery aq,aq_header;
    BaseClass baseClass;
    private LayoutInflater l_Inflater;


    public AdapterTasksList(Context context) {
        l_Inflater = LayoutInflater.from(context);
        this.ctx = context;
        baseClass = ((BaseClass) ctx.getApplicationContext());
    }

    @Override
    public int getCount() {
        return FragmentTasksList.list.size();
    }

    @Override
    public Object getItem(int pos) {
        return FragmentTasksList.list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = l_Inflater.inflate(R.layout.layout_taskslist_row, null);
        aq = new AQuery(convertView);
        if(FragmentTasksList.list.get(position).getStartDate().equalsIgnoreCase("3/0/1"))
            aq.id(R.id.start_date).text("No Due Date");
        aq.id(R.id.start_date).text(FragmentTasksList.list.get(position).getStartDate());
        aq.id(R.id.priority_bar).backgroundColor(getPriorityWiseColor(FragmentTasksList.list.get(position).priority));
        aq.id(R.id.task_name).text(FragmentTasksList.list.get(position).getTaskName());
        aq.id(R.id.project_name).text(FragmentTasksList.list.get(position).getProjectName());
        return convertView;
    }
    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {

        convertView = l_Inflater.inflate(R.layout.layout_taskslist_header, parent , false);
        aq_header = new AQuery(convertView);
        if(FragmentTasksList.list.get(position).getStartDate().equalsIgnoreCase("3/0/1"))
            aq_header.id(R.id.header).text("No Due Date");
        else
            aq_header.id(R.id.header).text(FragmentTasksList.list.get(position).getStartDate());
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        //return the first character of the country as ID because this is what headers are based upon
        return FragmentTasksList.list.get(position).getStartDate().subSequence(0,1).charAt(0);
    }

    private int getPriorityWiseColor(int priority){
        switch (priority){
            case 0:
                return ctx.getResources().getColor(android.R.color.darker_gray);
            case 1:
                return ctx.getResources().getColor(android.R.color.holo_orange_light);
            case 2:
                return ctx.getResources().getColor(android.R.color.holo_green_light);
            case 3:
                return ctx.getResources().getColor(android.R.color.holo_red_light);
            default:
                return ctx.getResources().getColor(android.R.color.darker_gray);
        }
    }

}
