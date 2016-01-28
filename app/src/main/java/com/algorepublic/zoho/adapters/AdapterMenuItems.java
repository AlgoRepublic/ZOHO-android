package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.algorepublic.zoho.BaseActivity;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.CalendarFragment;
import com.algorepublic.zoho.fragments.DocumentsListFragment;
import com.algorepublic.zoho.fragments.ProjectsFragment;
import com.algorepublic.zoho.fragments.TasksListFragment;
import com.androidquery.AQuery;

/**
 * Created by android on 8/24/15.
 */
public class AdapterMenuItems extends BaseAdapter{

    Context ctx; private LayoutInflater inflater;
    int[] menu_names = {
            R.string.dashboard,
            R.string.projects,
            R.string.tasks,
            R.string.calendar,
            R.string.documents,
            R.string.users,
            R.string.forums,
            R.string.start_rating,
            R.string.departments,
    };
    int[] menu_icon_white = {
            R.mipmap.dashboard_white,
            R.mipmap.projects_white,
            R.mipmap.tasks_white,
            R.mipmap.calender_white,
            R.mipmap.document_white,
            R.mipmap.users_white,
            R.mipmap.forums_white,
            R.mipmap.star_white,
            R.mipmap.departments_white
    };
    int[] menu_icon_blue = {
            R.mipmap.dashboard_blue,
            R.mipmap.projects_blue,
            R.mipmap.tasks_blue,
            R.mipmap.calender_blue,
            R.mipmap.document_blue,
            R.mipmap.users_blue,
            R.mipmap.forums_blue,
            R.mipmap.star_blue,
            R.mipmap.departments_blue
    };
    public AdapterMenuItems(Context context) {
        this.ctx = context;
       inflater = LayoutInflater.from(context);
    }


    @Override
    public Object getItem(int position) {
        return menu_names[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return menu_names.length;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.layout_menu_items, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.textview);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageview);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final AQuery aq = new AQuery(convertView);
        holder.title.setText(menu_names[position]);
        aq.id(R.id.imageview).image(menu_icon_white[position]);
        aq.id(R.id.checkbox).getCheckBox().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int loop = 0; loop < menu_names.length; loop++) {
                    if (aq.id(R.id.checkbox).isChecked()) {
                        if (position == loop) {
                            aq.id(R.id.imageview).image(menu_icon_blue[position]);
                            holder.title.setTextColor(ctx.getResources().getColor(R.color.blue_selected));
                        } else {
                            aq.id(R.id.checkbox).checked(false);
                        }
                    } else {
                        aq.id(R.id.imageview).image(menu_icon_white[position]);
                        holder.title.setTextColor(ctx.getResources().getColor(R.color.white));
                    }
                }
                CallFragment(position);
            }
        });
        return convertView;
    }
    public void CallFragment(int position){
        if(position==0){

        }if(position==1){
            callFragmentWithReplace(R.id.container, ProjectsFragment.newInstance(), "ProjectsFragment");
        }if(position==2){
            callFragmentWithReplace(R.id.container, TasksListFragment.newInstance(), "TasksListFragment");
        }if(position==3){
            callFragmentWithReplace(R.id.container, CalendarFragment.newInstance(), "FragmentCalendar");
        }if(position==4){
            callFragmentWithReplace(R.id.container, DocumentsListFragment.newInstance(), "DocumentsListFragment");
        }if(position==5){

        }if(position==6){

        }if(position==7){

        }if(position==8){

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
        ImageView imageView;
    }

}
