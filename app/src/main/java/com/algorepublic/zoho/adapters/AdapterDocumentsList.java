package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.DocumentsFragment;
import com.algorepublic.zoho.utils.BaseClass;
import com.algorepublic.zoho.utils.Constants;
import com.androidquery.AQuery;
import com.bumptech.glide.Glide;

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
        return DocumentsFragment.docsList.size();
    }

    @Override
    public Object getItem(int pos) {
        return DocumentsFragment.docsList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = l_Inflater.inflate(R.layout.layout_docs_row, null);
        aq = new AQuery(convertView);

        aq.id(R.id.file_name).text(DocumentsFragment.docsList.get(position).getFileName());
        if(DocumentsFragment.docsList.get(position).getFileTypeID()>=0 &&
                DocumentsFragment.docsList.get(position).getFileTypeID()<=4 ){
            Glide.with(ctx).load(Constants.Image_URL+DocumentsFragment.docsList.
                    get(position).getFileDescription()).into(aq.id(R.id.file_image).getImageView());
        }else {
            Glide.with(ctx).load(BaseClass.getIcon(DocumentsFragment.docsList.
                    get(position).getFileTypeID())).into(aq.id(R.id.file_image).getImageView());
        }
        return convertView;
    }
    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {

        convertView = l_Inflater.inflate(R.layout.layout_taskslist_header, parent , false);
        aq_header = new AQuery(convertView);

        if (DocumentsFragment.docsList.get(position).getCreatedAt().equalsIgnoreCase("3/0/1"))
            aq_header.id(R.id.header).text("No Date");
        else
            aq_header.id(R.id.header).text(DocumentsFragment.docsList.get(position).getCreatedAt());
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        //return the first character of the country as ID because this is what headers are based upon
        return Long.parseLong(DocumentsFragment.docsList.get(position).getCreatedMilli().substring(0,5));
    }

}
