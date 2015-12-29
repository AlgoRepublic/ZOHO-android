package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.FragmentTasksList;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

import java.util.ArrayList;
import java.util.Calendar;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by android on 12/23/15.
 */
public class AdapterTasksList extends BaseAdapter implements StickyListHeadersAdapter {

    Context ctx;
    AQuery aq,aq_header;
    BaseClass baseClass;
    private LayoutInflater l_Inflater;
    ArrayList<ChildTasksList> tasksLists = new ArrayList<>();


    public AdapterTasksList(Context context) {
        l_Inflater = LayoutInflater.from(context);
        this.ctx = context;
        baseClass = ((BaseClass) ctx.getApplicationContext());
    }

    @Override
    public int getCount() {
        return FragmentTasksList.list.size();
    }

    @Override
    public Object getItem(int pos) {
        return FragmentTasksList.list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = l_Inflater.inflate(R.layout.layout_taskslist_row, null);
        aq = new AQuery(convertView);
        aq.id(R.id.start_date).text("Start Date: "+FragmentTasksList.list.get(position).getStartDate());
        aq.id(R.id.end_date).text("End Date: "+FragmentTasksList.list.get(position).getEndDate());
        aq.id(R.id.task_name).text(FragmentTasksList.list.get(position).getTaskName());
        aq.id(R.id.project_name).text(FragmentTasksList.list.get(position).getProjectName());
        return convertView;
    }
    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {

        convertView = l_Inflater.inflate(R.layout.layout_taskslist_header, parent , false);
        aq_header = new AQuery(convertView);
        if(FragmentTasksList.list.get(position).getStartDate().equalsIgnoreCase("3/0/1"))
            aq_header.id(R.id.header).text("No Due Date");
        else
        aq_header.id(R.id.header).text(FragmentTasksList.list.get(position).getStartDate());
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        //return the first character of the country as ID because this is what headers are based upon
        return FragmentTasksList.list.get(position).getStartDate().subSequence(0,1).charAt(0);
    }
    public String DateFormatter(String date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(date));

        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        return (mDay+"/"+mMonth+"/"+mYear);
    }

}
