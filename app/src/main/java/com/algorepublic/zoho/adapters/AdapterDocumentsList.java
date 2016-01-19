package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.DocumentsListFragment;
import com.algorepublic.zoho.utils.BaseClass;
import com.algorepublic.zoho.utils.Constants;
import com.androidquery.AQuery;
import com.bumptech.glide.Glide;

import java.util.Calendar;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by android on 1/7/16.
 */
public class AdapterDocumentsList extends BaseAdapter implements StickyListHeadersAdapter {

    Context ctx;
    AQuery aq,aq_header;
    BaseClass baseClass;
    private LayoutInflater l_Inflater;



    public AdapterDocumentsList(Context context) {
        l_Inflater = LayoutInflater.from(context);
        this.ctx = context;
        baseClass = ((BaseClass) ctx.getApplicationContext());
    }

    @Override
    public int getCount() {
        return DocumentsListFragment.docsList.size();
    }

    @Override
    public Object getItem(int pos) {
        return DocumentsListFragment.docsList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = l_Inflater.inflate(R.layout.layout_docs_row, null);
        aq = new AQuery(convertView);
        aq.id(R.id.file_name).text(DocumentsListFragment.docsList.get(position).getFileName());
        aq.id(R.id.file_time).text(GetTime(DocumentsListFragment.docsList.get(position).getCreatedMilli()));
        aq.id(R.id.file_image).image(BaseClass.getIcon(DocumentsListFragment.docsList.
                get(position).getFileTypeID()));

        return convertView;
    }
    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {

        convertView = l_Inflater.inflate(R.layout.layout_taskslist_header, parent , false);
        aq_header = new AQuery(convertView);

        if (DocumentsListFragment.docsList.get(position).getCreatedAt().equalsIgnoreCase("3/0/1"))
            aq_header.id(R.id.header).text("No Date");
        else
            aq_header.id(R.id.header).text(DocumentsListFragment.docsList.get(position).getCreatedAt());
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        //return the first character of the country as ID because this is what headers are based upon
        return Long.parseLong(DocumentsListFragment.docsList.get(position).getCreatedMilli().substring(0,5));
    }
    public String GetTime(String milli){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(milli));

        String delegate = "hh:mm aaa";
        String time  = (String) DateFormat.format(delegate, calendar.getTime());
        return (time);
    }
}
