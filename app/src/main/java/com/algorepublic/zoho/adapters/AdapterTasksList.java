package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.TasksListFragment;
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
    private int lastPosition = -1;


    public AdapterTasksList(Context context) {
        l_Inflater = LayoutInflater.from(context);
        this.ctx = context;
        baseClass = ((BaseClass) ctx.getApplicationContext());
    }

    @Override
    public int getCount() {
        return TasksListFragment.generalList.size();
    }

    @Override
    public Object getItem(int pos) {
        return TasksListFragment.generalList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = l_Inflater.inflate(R.layout.layout_taskslist_row, null);
        aq = new AQuery(convertView);
        if (TasksListFragment.generalList.get(position).progress==100){
            aq.id(R.id.task_image).image(R.drawable.ic_check_circle_black_24dp);
        }
        Drawable shapeDrawable = aq.id(R.id.priority_bar).getView().getBackground();
        GradientDrawable colorDrawable = (GradientDrawable) shapeDrawable;
        colorDrawable.setColor(getPriorityWiseColor(TasksListFragment.generalList.get(position).getPriority()));
        aq.id(R.id.priority_bar).getView().setBackground(shapeDrawable);

        aq.id(R.id.task_name).text(TasksListFragment.generalList.get(position).getTaskName());
        aq.id(R.id.project_name).text(TasksListFragment.generalList.get(position).getProjectName());
        if(TasksListFragment.generalList.get(position).getStartDate().equalsIgnoreCase("3/0/1"))
            aq.id(R.id.task_date).text("No Due Date");
        else
            aq.id(R.id.task_date).text(TasksListFragment.generalList.get(position).getEndDate());

        if(TasksListFragment.generalList.get(position).getProjectName().equalsIgnoreCase(""))
            aq.id(R.id.general).text("General");
        else
            aq.id(R.id.general).text(TasksListFragment.generalList.get(position).getProjectName());

        Animation animation = AnimationUtils.loadAnimation(ctx, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        convertView.startAnimation(animation);
        lastPosition = position;

        return convertView;
    }
    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {

        convertView = l_Inflater.inflate(R.layout.layout_taskslist_header, parent , false);

        aq_header = new AQuery(convertView);

        if(baseClass.getTaskSortType().equalsIgnoreCase("DueDate")) {
            if (TasksListFragment.generalList.get(position).getHeader().equalsIgnoreCase("3/0/1"))
                aq_header.id(R.id.header).text("No Due Date");
            else
                aq_header.id(R.id.header).text(TasksListFragment.generalList.get(position).getHeader());
        }
        if(baseClass.getTaskSortType().equalsIgnoreCase("Priority"))
        {
            if(TasksListFragment.generalList.get(position).getPriority()==0)
            {
                aq_header.id(R.id.header).text("None");
            }
            if(TasksListFragment.generalList.get(position).getPriority()==1)
            {
                aq_header.id(R.id.header).text("Low");
            }
            if(TasksListFragment.generalList.get(position).getPriority()==2)
            {
                aq_header.id(R.id.header).text("Medium");
            }
            if(TasksListFragment.generalList.get(position).getPriority()==3)
            {
                aq_header.id(R.id.header).text("High");
            }
        }
        if(baseClass.getTaskSortType().equalsIgnoreCase("Alphabetically"))
        {
            aq_header.id(R.id.header).text(TasksListFragment.generalList.get(position).getTaskName().substring(0, 1));
        }
        if(baseClass.getTaskSortType().equalsIgnoreCase("TaskList"))
        {
            if(TasksListFragment.generalList.get(position).getCharToAscii() == 0)
            aq_header.id(R.id.header).text("General");
            else
            aq_header.id(R.id.header).text(TasksListFragment.generalList.get(position).getTaskListName());
        }
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        long type = 0;
        //return the first character of the country as ID because this is what headers are based upon
        if(baseClass.getTaskSortType().equalsIgnoreCase("DueDate"))
            type = Long.parseLong(TasksListFragment.generalList.get(position).getEndMilli());
        if(baseClass.getTaskSortType().equalsIgnoreCase("Priority"))
            type = TasksListFragment.generalList.get(position).getPriority();
        if(baseClass.getTaskSortType().equalsIgnoreCase("Alphabetically"))
            type = TasksListFragment.generalList.get(position).getTaskName().charAt(0);
        if(baseClass.getTaskSortType().equalsIgnoreCase("TaskList"))
            type = Long.parseLong(TasksListFragment.generalList.get(position).getCharToAscii().toString());

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
