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

import com.algorepublic.zoho.BaseActivity;
import com.algorepublic.zoho.MainActivity;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.StarRatingFragments.StarRatingBaseFragment;
import com.algorepublic.zoho.fragments.CalendarFragment;
import com.algorepublic.zoho.fragments.DepartmentFragment;
import com.algorepublic.zoho.fragments.DocumentsListFragment;
import com.algorepublic.zoho.fragments.ForumsFragment;
import com.algorepublic.zoho.fragments.HomeFragment;
import com.algorepublic.zoho.fragments.ProjectsFragment;
import com.algorepublic.zoho.fragments.TasksListFragment;
import com.algorepublic.zoho.fragments.UserFragment;
import com.androidquery.AQuery;

/**
 * Created by android on 8/24/15.
 */
public class AdapterMenuItems extends BaseAdapter{

    Context ctx; private LayoutInflater inflater;
    int lastPosition=0;
    int[] menu_names = {
            R.string.dashboard,
            R.string.projects,
            R.string.tasks,
            R.string.calendar,
            R.string.documents,
            R.string.users,
            R.string.forums,
            R.string.star_rating,
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
    public AdapterMenuItems(Context context) {
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

        ViewHolder holder;
            convertView = inflater.inflate(R.layout.layout_menu_items, parent, false);

            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.textview);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);

        final AQuery aq = new AQuery(convertView);
        if(lastPosition == position){
            aq.id(R.id.checkbox).checked(true);
            aq.id(R.id.checkbox).getCheckBox().setAlpha(0.7f);
        }
        holder.title.setText(menu_names[position]);
        holder.title.setTextColor(ctx.getResources().getColor(R.color.white));
        aq.id(R.id.imageview).image(menu_icon_white[position]);

        aq.id(R.id.checkbox).getCheckBox().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int loop = 0; loop < menu_names.length; loop++) {
                    if (loop == position) {
                        aq.id(R.id.checkbox).getCheckBox().setAlpha(0.7f);
                    } else {
                        View view = getViewByPosition(loop, MainActivity.gridView);
                        AQuery aQuery = new AQuery(view);
                        aQuery.id(R.id.checkbox).checked(false);
                        aQuery.id(R.id.checkbox).getCheckBox().setAlpha(1.0f);
                    }
                }
                CallFragment(position);
            }
        });
        return convertView;
    }
    public void CallFragment(int position){
        BaseActivity.drawer.closeDrawer(GravityCompat.START);
        for(int loop = 0;loop < ((AppCompatActivity)ctx).getSupportFragmentManager().getBackStackEntryCount();loop++) {
            ((AppCompatActivity)ctx).getSupportFragmentManager().popBackStack();
        }
        if(position==0){
            callFragmentWithReplace(R.id.container, HomeFragment.newInstance(), "HomeFragment");
        }if(position==1){
            callFragmentWithReplace(R.id.container, ProjectsFragment.newInstance(), "ProjectsFragment");
        }if(position==2){
            callFragmentWithReplace(R.id.container, TasksListFragment.newInstance(), "TasksListFragment");
        }if(position==3){
            callFragmentWithReplace(R.id.container, CalendarFragment.newInstance(), "FragmentCalendar");
        }if(position==4){
            callFragmentWithReplace(R.id.container, DocumentsListFragment.newInstance(), "DocumentsListFragment");
        }if(position==5){
           callFragmentWithReplace(R.id.container, UserFragment.newInstance(),"UserFragment");
        }if(position==6){
            callFragmentWithReplace(R.id.container, ForumsFragment.newInstance(), "ForumsFragment");
        }if(position==7){
            callFragmentWithReplace(R.id.container, StarRatingBaseFragment.newInstance(), "StarRatingLevelOneFragment");
        }if(position==8){
            callFragmentWithReplace(R.id.container, DepartmentFragment.newInstance(), "DepartmentFragment");
        }
    }
    public void callFragmentWithReplace(int containerId, Fragment fragment, String tag){

        ((AppCompatActivity) ctx).getSupportFragmentManager()
                .beginTransaction()
                .replace(containerId, fragment, tag)
                .setCustomAnimations(R.anim.slide_in_enter, R.anim.slide_in_exit,
                        R.anim.slide_pop_enter, R.anim.slide_pop_exit)
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
