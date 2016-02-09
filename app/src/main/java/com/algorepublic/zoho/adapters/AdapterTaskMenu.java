package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.TextView;

import com.algorepublic.zoho.ActivityTask;
import com.algorepublic.zoho.BaseActivity;
import com.algorepublic.zoho.FragmentsTasks.TaskAssignFragment;
import com.algorepublic.zoho.FragmentsTasks.TaskAttachmentFragment;
import com.algorepublic.zoho.FragmentsTasks.TaskEditTitleFragment;
import com.algorepublic.zoho.FragmentsTasks.TaskListNameFragment;
import com.algorepublic.zoho.FragmentsTasks.TaskPriorityFragment;
import com.algorepublic.zoho.FragmentsTasks.TaskScheduleFragment;
import com.algorepublic.zoho.R;
import com.androidquery.AQuery;

/**
 * Created by android on 1/29/16.
 */
public class AdapterTaskMenu extends BaseAdapter {

    Context ctx; private LayoutInflater inflater;
    int[] menu_names = {
            R.string.task_desc,
            R.string.category,
            R.string.image,
            R.string.employees,
            R.string.schedule,
            R.string.priority
    };
    int[] menu_icon_white = {
            R.mipmap.task_desc_white,
            R.mipmap.category,
            R.mipmap.camera,
            R.mipmap.employee,
            R.mipmap.schedule,
            R.mipmap.priority
    };
    public AdapterTaskMenu(Context context) {
        this.ctx = context;
        inflater = LayoutInflater.from(context);
    }


    public Object getItem(int position) {
        return menu_names[position];
    }

    public long getItemId(int position) {
        return position;
    }

    public int getCount() {
        return menu_names.length;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_taskmenu, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.textview);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final AQuery aq = new AQuery(convertView);
        if (position==0) {
            aq.id(R.id.checkbox).getCheckBox().setBackgroundColor(ctx.getResources().getColor(R.color.colorBaseMenu));
        }else
            aq.id(R.id.checkbox).getCheckBox().setBackgroundColor(ctx.getResources().getColor(R.color.colorBaseHeader));

        holder.title.setText(menu_names[position]);
        holder.title.setTextColor(ctx.getResources().getColor(R.color.white));
        aq.id(R.id.imageview).image(menu_icon_white[position]);


        aq.id(R.id.checkbox).getCheckBox().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int loop = 0; loop < menu_names.length; loop++) {
                    if (loop==position) {
                        aq.id(R.id.checkbox).getCheckBox().setBackgroundColor(ctx.getResources().getColor(R.color.colorBaseMenu));
                    }else {
                        View  view = getViewByPosition(loop, ActivityTask.gridViewTaskMenu);
                        AQuery aQuery =  new AQuery(view);
                        aQuery.id(R.id.checkbox).checked(false);
                        aQuery.id(R.id.checkbox).getCheckBox().setBackgroundColor(ctx.getResources().getColor(R.color.colorBaseHeader));
                    }
                }
                CallFragment(position);
            }
        });
        return convertView;
    }
    public void CallFragment(int position){
        if(position==0){
            callFragmentWithReplace(R.id.edittask_container, TaskEditTitleFragment.newInstance(position), "TaskTitle");
        }if(position==1){
            callFragmentWithReplace(R.id.edittask_container, TaskListNameFragment.newInstance(position), "TaskListNameAttachment");
        }if(position==2){
            callFragmentWithReplace(R.id.edittask_container, TaskAttachmentFragment.newInstance(position), "TaskAttachment");
        }if(position==3){
            callFragmentWithReplace(R.id.edittask_container, TaskAssignFragment.newInstance(position), "TaskAssign");
        }if(position==4){
            callFragmentWithReplace(R.id.edittask_container, TaskScheduleFragment.newInstance(position), "TaskSchedule");
        }if(position==5){
            callFragmentWithReplace(R.id.edittask_container, TaskPriorityFragment.newInstance(position), "TasksPriority");
        }
        BaseActivity.drawer.closeDrawer(GravityCompat.START);
    }
    public void callFragmentWithReplace(int containerId, Fragment fragment, String tag){

        ((AppCompatActivity) ctx).getSupportFragmentManager()
                .beginTransaction()
                .replace(containerId, fragment, tag)
                .commit();

    }
    static class ViewHolder {
        TextView title;
        CheckBox checkBox;
    }
    public View getViewByPosition(int pos, GridView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}
