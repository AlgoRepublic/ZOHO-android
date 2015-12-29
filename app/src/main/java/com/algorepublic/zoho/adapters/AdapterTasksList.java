package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.algorepublic.zoho.Models.TasksListModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

import java.util.ArrayList;

/**
 * Created by android on 12/23/15.
 */
public class AdapterTasksList extends BaseAdapter {

    Context ctx;
    AQuery aq;
    BaseClass baseClass;
    private LayoutInflater l_Inflater;

    public AdapterTasksList(Context context) {
        l_Inflater = LayoutInflater.from(context);
        this.ctx = context;
        baseClass = ((BaseClass) ctx.getApplicationContext());
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getCount() {
        return TasksListModel.getInstance().responseObject.size();
    }

    @Override
    public Object getItem(int pos) {
        return TasksListModel.getInstance().responseObject.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = l_Inflater.inflate(R.layout.layout_taskslist_row, null);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        aq = new AQuery(convertView);
        aq.id(R.id.task_name).text(TasksListModel.getInstance().responseObject.get(position).Title);
        aq.id(R.id.project_name).text(TasksListModel.getInstance().responseObject.get(position).projectName);
        return convertView;
    }

    static class ViewHolder {
        TextView assignee;
    }


}
