package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.ProjectsFragment;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by android on 1/7/16.
 */
public class AdapterProjectsClientList extends BaseAdapter implements StickyListHeadersAdapter {

    private Context ctx;
    private BaseClass baseClass;
    private AQuery aq;
    private LayoutInflater l_Inflater;
    private int lastPosition = -1;

    ArrayList<ProjectsList> arrayList= new ArrayList<>();

    public AdapterProjectsClientList(Context context,ArrayList<ProjectsList> list) {
        l_Inflater = LayoutInflater.from(context);
        this.ctx = context;
        arrayList.addAll(list);
        baseClass = ((BaseClass) ctx.getApplicationContext());
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int pos) {
        return arrayList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = l_Inflater.inflate(R.layout.layout_projectclientlist_row, null);
        aq = new AQuery(convertView);
        aq.id(R.id.project_title).text(arrayList.get(position).getProjectName());
        if(!arrayList.get(position).getTotalTasks().isEmpty())
            aq.id(R.id.task_alert).text(arrayList.get(position).getTotalTasks());
        if(!arrayList.get(position).getTotalUsers().isEmpty())
            aq.id(R.id.users_alert).text(arrayList.get(position).getTotalUsers());
        if(!arrayList.get(position).getTotalMilestones().isEmpty())
            aq.id(R.id.milestone_alert).text(arrayList.get(position).getTotalMilestones());

        aq.id(R.id.project_id).text(arrayList.get(position).getProjectID());
        if(arrayList.get(position).getProjectDesc() != null)
            aq.id(R.id.project_desc).text(Html.fromHtml(arrayList.get(position).getProjectDesc()));

        if(baseClass.getSelectedProject().equals(ProjectsFragment.ByDepartmentList.get(position).getProjectID())){
            convertView.setBackgroundColor(Color.parseColor("#666666"));
        }else{
            convertView.setBackgroundResource(android.R.color.transparent);
        }
        Log.e("OKay","Okay");
        Animation animation = AnimationUtils.loadAnimation(ctx, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        convertView.startAnimation(animation);
        lastPosition = position;
        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public long getHeaderId(int position) {
        return 0;
    }
}
