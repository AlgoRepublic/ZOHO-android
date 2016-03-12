package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.DocsPreviewFragment;
import com.algorepublic.zoho.utils.BaseClass;
import com.algorepublic.zoho.utils.Constants;
import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;


/**
 * Created by android on 1/13/16.
 */
public class AdapterDocsComments extends BaseAdapter {

    Context ctx;
    private LayoutInflater l_Inflater;
    BaseClass baseClass;

    public AdapterDocsComments(Context context) {
        l_Inflater = LayoutInflater.from(context);
        this.ctx = context;
        baseClass = ((BaseClass) ctx.getApplicationContext());
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
            holder.taskComment = (TextView) convertView.findViewById(R.id.comment_description);
            holder.userName = (TextView) convertView.findViewById(R.id.comment_title);
            holder.userImage = (CircularImageView) convertView.findViewById(R.id.comment_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.taskComment.setText(DocsPreviewFragment.arrayList.get(position).getComment());
        holder.userName.setText(DocsPreviewFragment.arrayList.get(position).getUserName()
                + " , " + DocsPreviewFragment.arrayList.get(position).getDateTime());
        Glide.with(ctx).load(Constants.Image_URL + DocsPreviewFragment
                .arrayList.get(position).getUserImageID()
                +"."+BaseClass.getExtensionType(DocsPreviewFragment.arrayList
                .get(position).getUserImagePath())).into(holder.userImage);


        return convertView;
    }
    static class ViewHolder {
        TextView taskComment;
        TextView userName;
        CircularImageView userImage;
    }
}