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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.algorepublic.zoho.BaseActivity;
import com.algorepublic.zoho.MainActivity;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.CalendarFragment;
import com.algorepublic.zoho.fragments.DepartmentFragment;
import com.algorepublic.zoho.fragments.DocumentsListFragment;
import com.algorepublic.zoho.fragments.ForumsFragment;
import com.algorepublic.zoho.fragments.HomeFragment;
import com.algorepublic.zoho.fragments.ProjectsFragment;
import com.algorepublic.zoho.fragments.StarRatingFragment;
import com.algorepublic.zoho.fragments.TasksListFragment;
import com.algorepublic.zoho.fragments.UserFragment;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by android on 8/24/15.
 */
public class AdapterMenuItems extends BaseAdapter{

    Context ctx; private LayoutInflater inflater;
    int lastPosition=0;
    BaseClass baseClass;
    List<String> menu_names ;

    List<Integer> menu_icon_white ;

    public AdapterMenuItems(Context context,List<String> menuName,List<Integer> menuIcon) {
        this.ctx = context;
        baseClass = ((BaseClass) ctx.getApplicationContext());
        inflater = LayoutInflater.from(context);
        menu_names = menuName;
        menu_icon_white = menuIcon;

    }


    public String getItem(int position) {
        return menu_names.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public int getCount() {
        return menu_names.size();
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
        holder.title.setText(menu_names.get(position));
        holder.title.setTextColor(ctx.getResources().getColor(R.color.white));
        aq.id(R.id.imageview).image(menu_icon_white.get(position));
        aq.id(R.id.imageview).getImageView()
                .setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        aq.id(R.id.checkbox).getCheckBox().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int loop = 0; loop < menu_names.size(); loop++) {
                    if (loop == position) {
                        aq.id(R.id.checkbox).getCheckBox().animate().alpha(0.7f);

                    } else {
                        View view = getViewByPosition(loop, MainActivity.gridView);
                        AQuery aQuery = new AQuery(view);
                        aQuery.id(R.id.checkbox).checked(false);
                        aQuery.id(R.id.checkbox).getCheckBox().animate().alpha(1.0f);
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
        if(getItem(position)==ctx.getResources().getString(R.string.dashboard)){
            callFragmentWithReplace(R.id.container, HomeFragment.newInstance(), "HomeFragment");
        }if(getItem(position)==ctx.getResources().getString(R.string.projects)){
            callFragmentWithReplace(R.id.container, ProjectsFragment.newInstance(), "ProjectsFragment");
        }if(getItem(position)==ctx.getResources().getString(R.string.tasks)){
            callFragmentWithReplace(R.id.container, TasksListFragment.newInstance(), "TasksListFragment");
        }if(getItem(position)==ctx.getResources().getString(R.string.calendar)){
            callFragmentWithReplace(R.id.container, CalendarFragment.newInstance(), "FragmentCalendar");
        }if(getItem(position)==ctx.getResources().getString(R.string.documents)){
            if (baseClass.getSelectedProject().equalsIgnoreCase("0")) {
                Toast.makeText(ctx, "Please Select Project", Toast.LENGTH_SHORT).show();
                return;
            }
            callFragmentWithReplace(R.id.container, DocumentsListFragment.newInstance(), "DocumentsListFragment");
        }if(getItem(position)==ctx.getResources().getString(R.string.users)){
            callFragmentWithReplace(R.id.container, UserFragment.newInstance(),"UserFragment");
        }if(getItem(position)==ctx.getResources().getString(R.string.forums)){
            if (baseClass.getSelectedProject().equalsIgnoreCase("0")) {
                Toast.makeText(ctx, "Please Select Project", Toast.LENGTH_SHORT).show();
                return;
            }
            callFragmentWithReplace(R.id.container, ForumsFragment.newInstance(), "ForumsFragment");
        }if(getItem(position)==ctx.getResources().getString(R.string.star_rating)){
            if (baseClass.getSelectedProject().equalsIgnoreCase("0")) {
                Toast.makeText(ctx, "Please Select Project", Toast.LENGTH_SHORT).show();
                return;
            }
            callFragmentWithReplace(R.id.container, StarRatingFragment.newInstance(), "StarRatingFragment");
        }if(getItem(position)==ctx.getResources().getString(R.string.departments)){
            if (baseClass.getSelectedProject().equalsIgnoreCase("0")) {
                Toast.makeText(ctx, "Please Select Project", Toast.LENGTH_SHORT).show();
                return;
            }
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
