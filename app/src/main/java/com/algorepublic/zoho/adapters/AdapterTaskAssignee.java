package com.algorepublic.zoho.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.algorepublic.zoho.ActivityTask;
import com.algorepublic.zoho.FragmentsTasks.TaskAssignFragment;
import com.algorepublic.zoho.Models.TaskAssigneeModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

import java.util.ArrayList;

/**
 * Created by android on 7/2/15.
 */
public class AdapterTaskAssignee extends BaseAdapter {

    Context ctx;
    AQuery aq;
    BaseClass baseClass;
    private LayoutInflater l_Inflater;

    public AdapterTaskAssignee(Context context) {
        l_Inflater = LayoutInflater.from(context);
        this.ctx = context;
        baseClass = ((BaseClass) ctx.getApplicationContext());
    }

    @Override
    public int getCount() {

        return TaskAssigneeModel.getInstance().responseObject.size();
    }

    @Override
    public Object getItem(int pos) {
        return TaskAssigneeModel.getInstance().responseObject.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
                convertView = l_Inflater.inflate(R.layout.layout_assignee_row, null);
        }
        aq = new AQuery(convertView);
        aq.id(R.id.assignee_name).text(TaskAssigneeModel.getInstance().responseObject.get(position).firstName);
        aq.id(R.id.assignee_checkbox).getCheckBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    ActivityTask.assigneeList.add(TaskAssigneeModel.getInstance().responseObject.get(position).ID);
                else
                    ActivityTask.assigneeList.remove(position);
            }
        });
        return convertView;
    }
}
