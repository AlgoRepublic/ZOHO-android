package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.TaskCommentFragment;
import com.algorepublic.zoho.utils.BaseClass;
import com.algorepublic.zoho.utils.Constants;
import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;


/**
 * Created by android on 1/1/16.
 */
public class AdapterTaskComments extends BaseAdapter {

    Context ctx;
    private LayoutInflater l_Inflater;
    BaseClass baseClass;

    public AdapterTaskComments(Context context) {
        l_Inflater = LayoutInflater.from(context);
        baseClass = ((BaseClass) context.getApplicationContext());
        this.ctx = context;
    }

    public int getCount() {
        return TaskCommentFragment.arrayList.size();
    }

    public Object getItem(int position) {
        return TaskCommentFragment.arrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = l_Inflater.inflate(R.layout.layout_comments_maker, null);
            holder = new ViewHolder();
            holder.taskComment = (TextView) convertView.findViewById(R.id.comment_description);
            holder.userName = (TextView) convertView.findViewById(R.id.comment_title);
            holder.userImage = (CircularImageView) convertView.findViewById(R.id.comment_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.taskComment.setText(TaskCommentFragment.arrayList.get(position).getComment());
        holder.userName.setText(TaskCommentFragment.arrayList.get(position).getUserName()
        +" , "+TaskCommentFragment.arrayList.get(position).getDateTime());
        Glide.with(ctx).load(Constants.Image_URL + TaskCommentFragment
                .arrayList.get(position).getUserImageID()
                +"."+BaseClass.getExtensionType(TaskCommentFragment.arrayList
                .get(position).getUserImagePath())).into(holder.userImage);

        return convertView;
    }
    static class ViewHolder {
        TextView taskComment;
        TextView userName;
        CircularImageView userImage;
    }
}