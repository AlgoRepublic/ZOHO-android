package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.algorepublic.zoho.FragmentsTasks.TaskAssignFragment;
import com.algorepublic.zoho.Models.AllProjectsModel;
import com.algorepublic.zoho.Models.TaskAssigneeModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.AddUserFragment;
import com.algorepublic.zoho.fragments.TaskAddUpdateFragment;
import com.algorepublic.zoho.fragments.UserFragment;
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

        return AllProjectsModel.getInstance().responseData.size();
    }

    @Override
    public Object getItem(int pos) {
        return AllProjectsModel.getInstance().responseData.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView = l_Inflater.inflate(R.layout.layout_assignee_row, null);

        aq = new AQuery(convertView);

        aq.id(R.id.assignee_id).text(Integer.toString(AllProjectsModel.getInstance().responseData.get(position).projectID));
        aq.id(R.id.assignee_name).text(AllProjectsModel.getInstance().responseData.get(position).projectName);

        aq.id(R.id.assignee_checkbox).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserFragment.assigneeList.size()>0) {
                    for (int loop = 0; loop < UserFragment.assigneeList.size(); loop++) {
                        View view = getViewByPosition(position, AddUserFragment.listView);
                        AQuery aQuery = new AQuery(view);
                        if (aQuery.id(R.id.assignee_checkbox).isChecked()) {
                            UserFragment.assigneeList.add(AllProjectsModel.getInstance().responseData.get(position).projectID);
                            break;
                        } else if (UserFragment.assigneeList.get(loop) ==
                                AllProjectsModel.getInstance().responseData.get(position).projectID) {
                            UserFragment.assigneeList.remove(loop);
                        }
                    }
                }else {
                    UserFragment.assigneeList.add(AllProjectsModel.getInstance().responseData.get(position).projectID);
                }
            }
        });
        for(int loop=0;loop< UserFragment.assigneeList.size();loop++) {
            if (UserFragment.assigneeList.get(loop) ==
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
