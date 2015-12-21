package com.algorepublic.zoho.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

import java.util.ArrayList;

/**
 * Created by android on 7/2/15.
 */
public class AdapteAssignEmployees extends BaseAdapter {

    Context ctx;
    AQuery aq;
    BaseClass baseClass;
    private LayoutInflater l_Inflater;
    ArrayList<String> arraylist = new ArrayList<String>();

    public AdapteAssignEmployees(Context context, ArrayList<String> results) {
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
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
                convertView = l_Inflater.inflate(R.layout.layout_assignee_row, null);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        aq = new AQuery(convertView);

        aq.id(R.id.assignee_name).text(arraylist.get(position));
        return convertView;
    }

    static class ViewHolder {
        TextView assignee;
    }


}
