package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.algorepublic.zoho.FragmentsTasks.TaskAssignFragment;
import com.algorepublic.zoho.Models.TaskAssigneeModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.TaskAddUpdateFragment;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

/**
 * Created by android on 3/3/16.
 */
public class AdapterAddUserList extends BaseAdapter {

    Context ctx;
    AQuery aq;
    BaseClass baseClass;
    private LayoutInflater l_Inflater;

    public AdapterAddUserList(Context context) {
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
        if(TaskAssigneeModel.getInstance().responseObject.get(position).ID==Integer.parseInt(baseClass.getUserId())) {
            aq.id(R.id.assignee_name).text("Me");
        }else{
            aq.id(R.id.assignee_name).text(TaskAssigneeModel.getInstance().responseObject.get(position).firstName);
        }
        aq.id(R.id.assignee_checkbox).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TaskAddUpdateFragment.assigneeList.size()>0) {
                    for (int loop = 0; loop < TaskAddUpdateFragment.assigneeList.size(); loop++) {
                        View view = getViewByPosition(position, TaskAssignFragment.listView);
                        AQuery aQuery = new AQuery(view);
                        if (aQuery.id(R.id.assignee_checkbox).isChecked()) {
                            TaskAddUpdateFragment.assigneeList.add(TaskAssigneeModel.getInstance().responseObject.get(position).ID);
                            break;
                        } else if (TaskAddUpdateFragment.assigneeList.get(loop) ==
                                TaskAssigneeModel.getInstance().responseObject.get(position).ID) {
                            TaskAddUpdateFragment.assigneeList.remove(loop);
                        }
                    }
                }else {
                    TaskAddUpdateFragment.assigneeList.add(TaskAssigneeModel.getInstance().responseObject.get(position).ID);
                }
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
    public View getViewByPosition(int pos, ListView listView) {
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
