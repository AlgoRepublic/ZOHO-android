package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;

import com.algorepublic.zoho.fragments.TaskAddUpdateFragment;
import com.algorepublic.zoho.Models.TaskAssigneeModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

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

         convertView = l_Inflater.inflate(R.layout.layout_assignee_row, null);

        aq = new AQuery(convertView);

        aq.id(R.id.assignee_id).text(Integer.toString(TaskAssigneeModel.getInstance().responseObject.get(position).ID));
        aq.id(R.id.assignee_name).text(TaskAssigneeModel.getInstance().responseObject.get(position).firstName);
        aq.id(R.id.assignee_checkbox).getCheckBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    TaskAddUpdateFragment.assigneeList.add(position,TaskAssigneeModel.getInstance().responseObject.get(position).ID);
                else
                    TaskAddUpdateFragment.assigneeList.remove(position);
            }
        });
        for(int loop=0;loop< TaskAddUpdateFragment.assigneeList.size();loop++) {
            if (TaskAddUpdateFragment.assigneeList.get(loop) ==
                    Integer.parseInt(aq.id(R.id.assignee_id).getText().toString())) {
                aq.id(R.id.assignee_checkbox).checked(true);
                break;
            }else
            {
                aq.id(R.id.assignee_checkbox).checked(false);
            }
        }
        return convertView;
    }
}
