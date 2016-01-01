package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.util.Log;
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
        return FragmentTasksList.generalList.size();
    }

    @Override
    public Object getItem(int pos) {
        return FragmentTasksList.generalList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = l_Inflater.inflate(R.layout.layout_taskslist_row, null);
        aq = new AQuery(convertView);

        if(FragmentTasksList.generalList.get(position).getStartDate().equalsIgnoreCase("3/0/1"))
            aq.id(R.id.end_date).text("No Due Date");
        else
            aq.id(R.id.end_date).text(FragmentTasksList.generalList.get(position).getEndDate());

            aq.id(R.id.priority_bar).backgroundColor(getPriorityWiseColor(FragmentTasksList.generalList.get(position).priority));
            aq.id(R.id.task_name).text(FragmentTasksList.generalList.get(position).getTaskName());
            aq.id(R.id.project_name).text(FragmentTasksList.generalList.get(position).getProjectName());

        return convertView;
    }
    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {

        convertView = l_Inflater.inflate(R.layout.layout_taskslist_header, parent , false);
        aq_header = new AQuery(convertView);

        if(baseClass.getSortType().equalsIgnoreCase("DueDate")) {
            if (FragmentTasksList.generalList.get(position).getHeader().equalsIgnoreCase("3/0/1"))
                aq_header.id(R.id.header).text("No Due Date");
            else
                aq_header.id(R.id.header).text(FragmentTasksList.generalList.get(position).getHeader());
        }
        if(baseClass.getSortType().equalsIgnoreCase("Priority"))
        {
            if(FragmentTasksList.generalList.get(position).getPriority()==0)
            {
                aq_header.id(R.id.header).text("None");
            }
            if(FragmentTasksList.generalList.get(position).getPriority()==1)
            {
                aq_header.id(R.id.header).text("Low");
            }
            if(FragmentTasksList.generalList.get(position).getPriority()==2)
            {
                aq_header.id(R.id.header).text("Medium");
            }
            if(FragmentTasksList.generalList.get(position).getPriority()==3)
            {
                aq_header.id(R.id.header).text("High");
            }
        }
        if(baseClass.getSortType().equalsIgnoreCase("Alphabetically"))
        {
            aq_header.id(R.id.header).text(FragmentTasksList.generalList.get(position).getTaskName().substring(0, 1));
        }
        if(baseClass.getSortType().equalsIgnoreCase("TaskList"))
        {
            if(FragmentTasksList.generalList.get(position).getCharToAscii() == 0)
            aq_header.id(R.id.header).text("General");
            else
            aq_header.id(R.id.header).text(FragmentTasksList.generalList.get(position).getTaskListName());
        }
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        long type = 0;
        //return the first character of the country as ID because this is what headers are based upon
        if(baseClass.getSortType().equalsIgnoreCase("DueDate"))
            type = Long.parseLong(FragmentTasksList.generalList.get(position).getEndMilli());
        if(baseClass.getSortType().equalsIgnoreCase("Priority"))
            type = FragmentTasksList.generalList.get(position).getPriority();
        if(baseClass.getSortType().equalsIgnoreCase("Alphabetically"))
            type = FragmentTasksList.generalList.get(position).getTaskName().charAt(0);
        if(baseClass.getSortType().equalsIgnoreCase("TaskList"))
            type = FragmentTasksList.generalList.get(position).getCharToAscii();

        return type;
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
