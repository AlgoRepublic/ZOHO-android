package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.algorepublic.zoho.FragmentsTasks.TaskListNameFragment;
import com.algorepublic.zoho.FragmentsTasks.TaskPriorityFragment;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.TaskAddUpdateFragment;
import com.algorepublic.zoho.fragments.TasksListFragment;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

/**
 * Created by android on 2/9/16.
 */
public class AdapterTaskListName extends BaseAdapter {

    Context ctx;
    AQuery aq;
    BaseClass baseClass;
    private LayoutInflater l_Inflater;
    int selectedIndex =-1;

    public AdapterTaskListName(Context context){
        l_Inflater = LayoutInflater.from(context);
        this.ctx = context;
        baseClass = ((BaseClass) ctx.getApplicationContext());
    }

    @Override
    public int getCount() {

        return TasksListFragment.taskListName.size();
    }

    @Override
    public Object getItem(int pos) {
        return TasksListFragment.taskListName.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView = l_Inflater.inflate(R.layout.layout_priorty_row, null);

        aq = new AQuery(convertView);
        if(selectedIndex == position)
        {
            aq.id(R.id.priority_checkbox).checked(true);
        }
        else
        {
            aq.id(R.id.priority_checkbox).checked(false);
        }
        aq.id(R.id.priority).text(TasksListFragment.taskListName.get(position).getTaskListName());
        aq.id(R.id.layout_booklist).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedIndex = position;
                baseClass.db.putInt("TaskListNameID", TasksListFragment.taskListName.get(selectedIndex).getTaskListID());
                baseClass.db.putString("TaskListName", TasksListFragment.taskListName.get(selectedIndex).getTaskListName());
                View view = getViewByPosition(selectedIndex,TaskListNameFragment.listView);
                RadioButton radioButton = (RadioButton) view.findViewById(R.id.priority_checkbox);
                for(int loop=0;loop<TasksListFragment.taskListName.size();loop++)
                {
                    View  view1 = getViewByPosition(loop, TaskListNameFragment.listView);
                    RadioButton radioButton1 = (RadioButton) view1.findViewById(R.id.priority_checkbox);
                    radioButton1.setChecked(false);
                }
                radioButton.setChecked(true);
            }
        });
        aq.id(R.id.priority_checkbox).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedIndex = position;
                baseClass.db.putInt("TaskListNameID", TasksListFragment.taskListName.get(selectedIndex).getTaskListID());
                baseClass.db.putString("TaskListName", TasksListFragment.taskListName.get(selectedIndex).getTaskListName());
                RadioButton radioButton = (RadioButton) v.findViewById(R.id.priority_checkbox);
                for (int loop = 0; loop < TasksListFragment.taskListName.size(); loop++) {
                    View view1 = getViewByPosition(loop, TaskListNameFragment.listView);
                    RadioButton radioButton1 = (RadioButton) view1.findViewById(R.id.priority_checkbox);
                    radioButton1.setChecked(false);
                }
                radioButton.setChecked(true);
            }
        });
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
    public void setSelectedIndex(int index)
    {
        selectedIndex = index;
    }
}
