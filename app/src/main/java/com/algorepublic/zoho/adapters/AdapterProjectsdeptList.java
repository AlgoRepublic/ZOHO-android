package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.ProjectsFragment;
import com.algorepublic.zoho.fragments.TasksListFragment;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by android on 2/2/16.
 */
public class AdapterProjectsdeptList extends BaseAdapter implements StickyListHeadersAdapter {

    Context ctx;
    AQuery aq,aq_header;
    BaseClass baseClass;
    private LayoutInflater l_Inflater;
    private int lastPosition = -1;


    public AdapterProjectsdeptList(Context context) {
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
        return ProjectsFragment.BydepartmentList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = l_Inflater.inflate(R.layout.layout_projectdeptlist_row, null);
        aq = new AQuery(convertView);

        aq = new AQuery(convertView);
        aq.id(R.id.project_title).text(ProjectsFragment.BydepartmentList.get(position).getProjectName());
        aq.id(R.id.project_id).text(ProjectsFragment.BydepartmentList.get(position).getProjectID());

        if(ProjectsFragment.BydepartmentList.get(position).getProjectDesc() != null)
            aq.id(R.id.project_desc).text(Html.fromHtml(ProjectsFragment.BydepartmentList.get(position).getProjectDesc()));

        return convertView;
    }
    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {

        convertView = l_Inflater.inflate(R.layout.layout_generic_header, parent , false);
        aq_header = new AQuery(convertView);
        aq_header.id(R.id.header).text(ProjectsFragment.BydepartmentList.get(position).getCompOrDeptName());
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return ProjectsFragment.BydepartmentList.get(position).getCharToAscii();
    }


}
