package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.DocsPreviewFragment;
import com.algorepublic.zoho.fragments.TaskCommentFragment;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

/**
 * Created by android on 1/13/16.
 */
public class AdapterDocsComments extends BaseAdapter {

    Context ctx;
    private LayoutInflater l_Inflater;

    public AdapterDocsComments(Context context) {
        l_Inflater = LayoutInflater.from(context);
        this.ctx = context;
    }

    public int getCount() {
        return DocsPreviewFragment.arrayList.size();
    }

    public Object getItem(int position) {
        return DocsPreviewFragment.arrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = l_Inflater.inflate(R.layout.layout_comments_maker, null);
            holder = new ViewHolder();
            holder.taskComment = (TextView) convertView.findViewById(R.id.comment_text);
            holder.userName = (TextView) convertView.findViewById(R.id.user_name);
            holder.userImage = (RoundedImageView) convertView.findViewById(R.id.user_image);
            holder.dateTime = (TextView) convertView.findViewById(R.id.comment_datetime);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.taskComment.setText(DocsPreviewFragment.arrayList.get(position).getComment());
        holder.userName.setText(DocsPreviewFragment.arrayList.get(position).getUserName());
        Glide.with(ctx).load(DocsPreviewFragment.arrayList.get(position).getUserImage()).into(holder.userImage);
        holder.dateTime.setText(DocsPreviewFragment.arrayList.get(position).getDateTime());

        return convertView;
    }
    static class ViewHolder {
        TextView taskComment;
        TextView userName;
        RoundedImageView userImage;
        TextView dateTime;
    }
}