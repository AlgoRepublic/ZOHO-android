package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.algorepublic.zoho.Models.ForumsModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

/**
 * Created by waqas on 2/2/16.
 */
public class AdapterForumsList extends BaseAdapter {

    private Context ctx;
    private BaseClass baseClass;
    private AQuery aq;
    private LayoutInflater l_Inflater;

    public AdapterForumsList(Context context) {
        l_Inflater = LayoutInflater.from(context);
        this.ctx = context;
        baseClass = ((BaseClass) ctx.getApplicationContext());
    }

    @Override
    public int getCount() {
        Log.e("ForumsAdapter Count",ForumsModel.getInstance().responseObject.size()+"");
        return ForumsModel.getInstance().responseObject.size();
    }

    @Override
    public ForumsModel.ResponseObject getItem(int position) {
        return ForumsModel.getInstance().responseObject.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = l_Inflater.inflate(R.layout.layouts_forum_row, null);
        aq = new AQuery(convertView);
        aq.id(R.id.forum_title).text(getItem(position).title);

        return convertView;
    }
}
