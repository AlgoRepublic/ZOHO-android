package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.EditProjectFragment;
import com.algorepublic.zoho.fragments.ProjectsFragment;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by android on 2/2/16.
 */
public class AdapterProjectsDeptList extends BaseAdapter implements StickyListHeadersAdapter {

    Context ctx;
    AQuery aq,aq_header;
    BaseClass baseClass;
    private LayoutInflater l_Inflater;
    private int lastPosition = -1;


    public AdapterProjectsDeptList(Context context) {
        l_Inflater = LayoutInflater.from(context);
        this.ctx = context;
        baseClass = ((BaseClass) ctx.getApplicationContext());

    }

    @Override
    public int getCount() {
        return ProjectsFragment.ByDepartmentList.size();
    }

    @Override
    public Object getItem(int pos) {
        return ProjectsFragment.ByDepartmentList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView = l_Inflater.inflate(R.layout.layout_projectdeptlist_row, null);
        aq = new AQuery(convertView);
        aq.id(R.id.project_title).text(ProjectsFragment.ByDepartmentList.get(position).getProjectName());
        if(!ProjectsFragment.ByDepartmentList.get(position).getTotalTasks().isEmpty())
            aq.id(R.id.task_alert).text(ProjectsFragment.ByDepartmentList.get(position).getTotalTasks());
        if(!ProjectsFragment.ByDepartmentList.get(position).getTotalMilestones().isEmpty())
            aq.id(R.id.milestone_alert).text(ProjectsFragment.ByDepartmentList.get(position).getTotalMilestones());

        aq.id(R.id.project_id).text(ProjectsFragment.ByDepartmentList.get(position).getProjectID());
        if(ProjectsFragment.ByDepartmentList.get(position).getProjectDesc() != null)
            aq.id(R.id.project_desc).text(Html.fromHtml(ProjectsFragment.ByDepartmentList.get(position).getProjectDesc()));

        if(baseClass.getSelectedProject().equals(ProjectsFragment.ByDepartmentList.get(position).getProjectID())){
            convertView.setBackgroundColor(Color.parseColor("#ff99cc00"));
        }else{
            convertView.setBackgroundResource(android.R.color.transparent);
        }
        aq.id(R.id.project_edit).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFragmentWithBackStack(R.id.container, EditProjectFragment.newInstance(position), "EditProjectFragment");
            }
        });
//        Animation animation = AnimationUtils.loadAnimation(ctx, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
//        convertView.startAnimation(animation);
        lastPosition = position;
        return convertView;
    }
    public void callFragmentWithBackStack(int containerId, Fragment fragment, String tag){

        ((AppCompatActivity) ctx).getSupportFragmentManager()
                .beginTransaction()
                .replace(containerId, fragment, tag)
                .addToBackStack(null)
                .commit();

    }
    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {

        convertView = l_Inflater.inflate(R.layout.layout_generic_header, parent , false);
        aq_header = new AQuery(convertView);
        aq_header.id(R.id.header).text(ProjectsFragment.ByDepartmentList.get(position).getCompOrDeptName());
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return Long.valueOf(ProjectsFragment.ByDepartmentList.get(position).getCompOrDeptID());
    }


}
