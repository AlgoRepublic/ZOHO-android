package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.algorepublic.zoho.R;
import com.androidquery.AQuery;

/**
 * Created by android on 1/1/16.
 */
public class AdapterTaskComments extends BaseAdapter {

    Context ctx;
    private LayoutInflater l_Inflater;

    public AdapterTaskComments(Context context) {
        l_Inflater = LayoutInflater.from(context);
        this.ctx = context;
    }

    public int getCount() {
        return 0;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = l_Inflater.inflate(R.layout.layout_comments_maker, null);
            holder = new ViewHolder();
            holder.txt_Comment = (TextView) convertView.findViewById(R.id.comment_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        AQuery aq = new AQuery(convertView);
//        holder.txt_Comment.setText(itemDetailsrrayList.get(position).getComment());
//        aq.id(R.id.comment_image).image(itemDetailsrrayList.get(position).getAuthor());

        return convertView;
    }
    static class ViewHolder {
        TextView txt_Comment;
    }
}