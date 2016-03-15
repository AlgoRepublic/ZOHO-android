package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.SearchView;

import com.algorepublic.zoho.BaseActivity;
import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.BaseFragment;
import com.algorepublic.zoho.fragments.TaskDetailFragment;
import com.algorepublic.zoho.fragments.TasksListFragment;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.TaskListService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;
import com.flyco.animation.BounceEnter.BounceLeftEnter;
import com.flyco.animation.SlideExit.SlideRightExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;

import java.util.ArrayList;
import java.util.Locale;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by android on 12/23/15.
 */
public class AdapterTasksList extends BaseAdapter implements StickyListHeadersAdapter {

    Context ctx;
    AQuery aq,aq_header;
    BaseClass baseClass;
    TaskListService service;
    private LayoutInflater l_Inflater;
    private int lastPosition = -1;
    int clickedPosition;
    ArrayList<TasksList> lists = new ArrayList<>();
    ArrayList<TasksList> tasksLists = new ArrayList<>();

    public AdapterTasksList(Context context, ArrayList<TasksList> arrayList) {
        tasksLists.addAll(arrayList);
        lists.addAll(arrayList);
        l_Inflater = LayoutInflater.from(context);
        this.ctx = context;
        service = new TaskListService((AppCompatActivity)ctx);
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
    public View getView(final int position, View convertView, ViewGroup parent) {

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

        if(tasksLists.get(position).getEndDate().equalsIgnoreCase("3/0/1")
                || tasksLists.get(position).getEndDate().equalsIgnoreCase("12/31/3938")) {
            aq.id(R.id.task_date).text(ctx.getString(R.string.no_date));
        }else {
            aq.id(R.id.task_date).text(tasksLists.get(position).getEndDate());
        }


        try {
            if (tasksLists.get(position).getProjectName().equalsIgnoreCase(""))
                aq.id(R.id.general).text(ctx.getString(R.string.pref_header_general));
            else
                aq.id(R.id.general).text(tasksLists.get(position).getProjectName());
        }catch (Exception e){
            e.printStackTrace();
        }
        aq.id(R.id.parent1).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFragmentWithBackStack(R.id.container, TaskDetailFragment.newInstance(tasksLists.get(position), position),"TaskDetail");
            }
        });
        aq.id(R.id.btDelete).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedPosition = position;
            NormalDialogCustomAttr(ctx.getString(R.string.delete_task),tasksLists.get(position));
            }
        });
        Animation animation = AnimationUtils.loadAnimation(ctx, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        convertView.startAnimation(animation);
        lastPosition = position;
        BaseFragment.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Filter(newText);
                return false;
            }
        });

        return convertView;
    }
    public void Filter(String text){
        text = text.toLowerCase(Locale.getDefault());
        ArrayList<TasksList> arrayList = new ArrayList<>();
        arrayList.addAll(lists);
        tasksLists.clear();
        for (int loop=0;loop<arrayList.size();loop++){
            if(arrayList.get(loop).getTaskName().toLowerCase().contains(text))
            {
                tasksLists.add(arrayList.get(loop));
            }
        }
        notifyDataSetChanged();
    }
    public void callFragmentWithBackStack(int containerId, Fragment fragment, String tag){
        ((AppCompatActivity)ctx).getSupportFragmentManager()
                .beginTransaction().setCustomAnimations( R.anim.slide_in_enter, R.anim.slide_in_exit, R.anim.slide_pop_enter, R.anim.slide_pop_exit)
                .replace(containerId, fragment, tag)
                .addToBackStack(null)
                .commit();
    }
    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {

        convertView = l_Inflater.inflate(R.layout.layout_taskslist_header, parent , false);

        aq_header = new AQuery(convertView);

        if(baseClass.getTaskFilterType().equalsIgnoreCase("DueDate")) {
            if (Long.parseLong(tasksLists.get(position).getHeader()) < System.currentTimeMillis()) {
                if(baseClass.DateFormatter(tasksLists.get(position).getHeader())
                        .equalsIgnoreCase(baseClass.DateFormatter(String.valueOf(System.currentTimeMillis()))))
                aq_header.id(R.id.header).text("Up Coming");
                else
                    aq_header.id(R.id.header).text("Over Due");
            }else if (tasksLists.get(position).getHeader().equalsIgnoreCase("62135535600000")
                    || tasksLists.get(position).getHeader().equalsIgnoreCase("-62135571600000")
                    || tasksLists.get(position).getHeader().equalsIgnoreCase("62135571600000"))
                aq_header.id(R.id.header).text("No Due Date");
            else {
                aq_header.id(R.id.header).text("Up Coming");
            }
        }
        if(baseClass.getTaskFilterType().equalsIgnoreCase("Priority"))
        {
            if(tasksLists.get(position).getPriority()==0)
            {
                aq_header.id(R.id.header).text(ctx.getString(R.string.none));
            }
            if(tasksLists.get(position).getPriority()==1)
            {
                aq_header.id(R.id.header).text(ctx.getString(R.string.low));
            }
            if(tasksLists.get(position).getPriority()==2)
            {
                aq_header.id(R.id.header).text(ctx.getString(R.string.medium));
            }
            if(tasksLists.get(position).getPriority()==3)
            {
                aq_header.id(R.id.header).text(ctx.getString(R.string.high));
            }
        }
        if(baseClass.getTaskFilterType().equalsIgnoreCase("Alphabetically"))
        {
            aq_header.id(R.id.header).text(tasksLists.get(position).getTaskName().substring(0, 1).toUpperCase());
        }
        if(baseClass.getTaskFilterType().equalsIgnoreCase("TaskList"))
        {
            if(tasksLists.get(position).getTaskListNameID() == 0)
            aq_header.id(R.id.header).text(ctx.getString(R.string.pref_header_general));
            else
            aq_header.id(R.id.header).text(tasksLists.get(position).getTaskListName());
        }
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        long type = 0;
        //return the first character of the country as ID because this is what headers are based upon
        if(baseClass.getTaskFilterType().equalsIgnoreCase("DueDate")) {
            if (Long.parseLong(tasksLists.get(position).getHeader()) < System.currentTimeMillis()) {
                if(baseClass.DateFormatter(tasksLists.get(position).getHeader())
                        .equalsIgnoreCase(baseClass.DateFormatter(String.valueOf(System.currentTimeMillis()))))
                    type = 3;
                else
                    type=1;
            }
            else if (tasksLists.get(position).getHeader().equalsIgnoreCase("62135535600000")
                || tasksLists.get(position).getHeader().equalsIgnoreCase("-62135571600000")
                || tasksLists.get(position).getHeader().equalsIgnoreCase("62135571600000"))
                type = 2;
            else
                type=3;
        }
        if(baseClass.getTaskFilterType().equalsIgnoreCase("Priority"))
            type = tasksLists.get(position).getPriority();
        if(baseClass.getTaskFilterType().equalsIgnoreCase("Alphabetically"))
            type = tasksLists.get(position).getTaskName().substring(0, 1).charAt(0);
        if(baseClass.getTaskFilterType().equalsIgnoreCase("TaskList"))
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
    private void NormalDialogCustomAttr(String content, final TasksList tasksList) {
        final NormalDialog dialog = new NormalDialog(((AppCompatActivity) ctx));
        dialog.isTitleShow(false)//
                .bgColor(ctx.getResources().getColor(R.color.colorBaseWrapper))//
                .cornerRadius(5)//
                .content(content)//
                .contentGravity(Gravity.CENTER)//
                .contentTextColor(ctx.getResources().getColor(R.color.colorBaseHeader))//
                .dividerColor(ctx.getResources().getColor(R.color.colorContentWrapper))//
                .btnTextSize(15.5f, 15.5f)//
                .btnTextColor(ctx.getResources().getColor(R.color.colorBaseHeader)
                        , ctx.getResources().getColor(R.color.colorBaseHeader))//
                .btnPressColor(ctx.getResources().getColor(R.color.colorBaseMenu))//
                .widthScale(0.85f)//
                .showAnim(new BounceLeftEnter())//
                .dismissAnim(new SlideRightExit())//
                .show();

        dialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                        service.deleteTask(tasksList.getTaskID()
                                , false, new CallBack(AdapterTasksList.this, "DeleteTask"));
                        TasksListFragment.dialogAC.show();
                    }
                });
    }
    public void DeleteTask(Object caller, Object model) {
        GeneralModel.getInstance().setList((GeneralModel) model);
        if (GeneralModel.getInstance().responseCode.equalsIgnoreCase("100")) {
            tasksLists.remove(clickedPosition);notifyDataSetChanged();
            Snackbar.make(((AppCompatActivity)ctx).findViewById(android.R.id.content), ctx.getString(R.string.task_deleted), Snackbar.LENGTH_SHORT).show();
        }
        else
        {
            Snackbar.make(((AppCompatActivity)ctx).findViewById(android.R.id.content), ctx.getString(R.string.response_error), Snackbar.LENGTH_SHORT).show();
        }
        TasksListFragment.dialogAC.dismiss();
    }
}
