package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.algorepublic.zoho.Models.ProjectsModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

/**
 * Created by android on 1/7/16.
 */
public class AdapterProjectsList extends BaseAdapter{

    Context ctx;
    BaseClass baseClass;
    AQuery aq;
    private LayoutInflater l_Inflater;



    public AdapterProjectsList(Context context) {
        l_Inflater = LayoutInflater.from(context);
        this.ctx = context;
        baseClass = ((BaseClass) ctx.getApplicationContext());
    }

    @Override
    public int getCount() {
        return ProjectsModel.getInstance().reponseData.size();
    }

    @Override
    public ProjectsModel.Response getItem(int pos) {
        return ProjectsModel.getInstance().reponseData.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = l_Inflater.inflate(R.layout.layout_project_row, null);
        aq = new AQuery(convertView);
        aq.id(R.id.project_title).text(getItem(position).name);
        aq.id(R.id.project_desc).text(getItem(position).desc);
        return convertView;
    }

}
