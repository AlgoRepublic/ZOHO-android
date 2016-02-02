package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

import java.util.ArrayList;

/**
 * Created by android on 1/7/16.
 */
public class AdapterProjectsClientList extends BaseAdapter{

    private Context ctx;
    private BaseClass baseClass;
    private AQuery aq;
    private LayoutInflater l_Inflater;

    ArrayList<ProjectsParentList> arrayList= new ArrayList<>();

    public AdapterProjectsClientList(Context context,ArrayList<ProjectsParentList> list) {
        l_Inflater = LayoutInflater.from(context);
        this.ctx = context;
        arrayList.addAll(list);
        baseClass = ((BaseClass) ctx.getApplicationContext());
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int pos) {
        return arrayList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = l_Inflater.inflate(R.layout.layout_projectclientlist_row, null);
        aq = new AQuery(convertView);
        aq.id(R.id.project_title).text(arrayList.get(position).getProjectName());
        aq.id(R.id.project_id).text(arrayList.get(position).getProjectID());

        if(arrayList.get(position).getProjectDesc() != null)
            aq.id(R.id.project_desc).text(Html.fromHtml(arrayList.get(position).getProjectDesc()));

        return convertView;
    }

}
