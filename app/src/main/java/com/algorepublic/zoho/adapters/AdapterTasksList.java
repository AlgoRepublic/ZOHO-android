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

import java.util.ArrayList;

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
    ArrayList<TasksList> tasksLists = new ArrayList<>();

    public AdapterTasksList(Context context, ArrayList<TasksList> arrayList) {
        tasksLists.addAll(arrayList);
        l_Inflater = LayoutInflater.from(context);
        this.ctx = context;
        baseClass = ((BaseClass) ctx.getApplicationContext());
    }

    @Override
    public int getCount() {
        return tasksLists.size();
    }

    @Override
    public Object getItem(int pos) {
        return tasksLists.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = l_Inflater.inflate(R.layout.layout_taskslist_row, null);
        aq = new AQuery(convertView);
        if (tasksLists.get(position).progress==100){
            aq.id(R.id.task_image).image(R.drawable.ic_check_circle_black_24dp);
        }
        Drawable shapeDrawable = aq.id(R.id.priority_bar).getView().getBackground();
        GradientDrawable colorDrawable = (GradientDrawable) shapeDrawable;
        colorDrawable.setColor(getPriorityWiseColor(tasksLists.get(position).getPriority()));
        aq.id(R.id.priority_bar).getView().setBackground(shapeDrawable);

        aq.id(R.id.task_comment).text(tasksLists.get(position).getCommentsCount() + " " + ctx.getString(R.string.task_comment));
        aq.id(R.id.task_users).text(tasksLists.get(position).getListAssignees().size() + " " + ctx.getString(R.string.task_user));
        aq.id(R.id.task_name).text(tasksLists.get(position).getTaskName());
        aq.id(R.id.project_name).text(tasksLists.get(position).getProjectName());
        if(tasksLists.get(position).getStartDate().equalsIgnoreCase("3/0/1"))
            aq.id(R.id.task_date).text("No Due Date");
        else
            aq.id(R.id.task_date).text(tasksLists.get(position).getEndDate());

        if(tasksLists.get(position).getProjectName().equalsIgnoreCase(""))
            aq.id(R.id.general).text("General");
        else
            aq.id(R.id.general).text(tasksLists.get(position).getProjectName());

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
            if (tasksLists.get(position).getHeader().equalsIgnoreCase("3/0/1"))
                aq_header.id(R.id.header).text("No Due Date");
            else
                aq_header.id(R.id.header).text(tasksLists.get(position).getHeader());
        }
        if(baseClass.getTaskSortType().equalsIgnoreCase("Priority"))
        {
            if(tasksLists.get(position).getPriority()==0)
            {
                aq_header.id(R.id.header).text("None");
            }
            if(tasksLists.get(position).getPriority()==1)
            {
                aq_header.id(R.id.header).text("Low");
            }
            if(tasksLists.get(position).getPriority()==2)
            {
                aq_header.id(R.id.header).text("Medium");
            }
            if(tasksLists.get(position).getPriority()==3)
            {
                aq_header.id(R.id.header).text("High");
            }
        }
        if(baseClass.getTaskSortType().equalsIgnoreCase("Alphabetically"))
        {
            aq_header.id(R.id.header).text(tasksLists.get(position).getTaskName().substring(0, 1));
        }
        if(baseClass.getTaskSortType().equalsIgnoreCase("TaskList"))
        {
            if(tasksLists.get(position).getTaskListNameID() == 0)
            aq_header.id(R.id.header).text("General");
            else
            aq_header.id(R.id.header).text(tasksLists.get(position).getTaskListName());
        }
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        long type = 0;
        //return the first character of the country as ID because this is what headers are based upon
        if(baseClass.getTaskSortType().equalsIgnoreCase("DueDate"))
            type = Long.parseLong(tasksLists.get(position).getEndMilli());
        if(baseClass.getTaskSortType().equalsIgnoreCase("Priority"))
            type = tasksLists.get(position).getPriority();
        if(baseClass.getTaskSortType().equalsIgnoreCase("Alphabetically"))
            type = tasksLists.get(position).getTaskName().charAt(0);
        if(baseClass.getTaskSortType().equalsIgnoreCase("TaskList"))
            type = tasksLists.get(position).getTaskListNameID();

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
