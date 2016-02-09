package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

import java.util.ArrayList;

/**
 * Created by android on 2/9/16.
 */
public class AdapterTaskListName extends BaseAdapter {

    Context ctx;
    AQuery aq;
    BaseClass baseClass;
    int selectedIndex =-1;
    private LayoutInflater l_Inflater;
    ArrayList<TaskListName> arraylist = new ArrayList<TaskListName>();

    public AdapterTaskListName(Context context, ArrayList<TaskListName> results) {
        arraylist.addAll(results);
        l_Inflater = LayoutInflater.from(context);
        this.ctx = context;
        baseClass = ((BaseClass) ctx.getApplicationContext());
    }

    @Override
    public int getCount() {
        return arraylist.size();
    }

    @Override
    public Object getItem(int pos) {
        return arraylist.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = l_Inflater.inflate(R.layout.layout_priorty_row, null);
        }
        aq = new AQuery(convertView);
        if(selectedIndex == position)
        {
            aq.id(R.id.priority_checkbox).checked(true);
        }
        else
        {
            aq.id(R.id.priority_checkbox).checked(false);
        }
        aq.id(R.id.priority).text(arraylist.get(position).getTaskListName());
        return convertView;
    }
    public void setSelectedIndex(int index)
    {
        selectedIndex = index;
    }
}
