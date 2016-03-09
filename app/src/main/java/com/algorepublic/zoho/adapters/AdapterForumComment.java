package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.algorepublic.zoho.Models.ForumsCommentModel;
import com.algorepublic.zoho.Models.ForumsModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.ForumsDetailFragment;
import com.algorepublic.zoho.fragments.TaskCommentFragment;
import com.algorepublic.zoho.utils.BaseClass;
import com.algorepublic.zoho.utils.Constants;
import com.androidquery.AQuery;
import com.bumptech.glide.Glide;

/**
 * Created by waqas on 2/3/16.
 */
public class AdapterForumComment extends BaseAdapter {

    private Context ctx;
    private BaseClass baseClass;
    private AQuery aq;
    private LayoutInflater l_Inflater;

    public AdapterForumComment(Context context) {
        l_Inflater = LayoutInflater.from(context);
        this.ctx = context;
        baseClass = ((BaseClass) ctx.getApplicationContext());
    }

    @Override
    public int getCount() {
        return ForumsDetailFragment.arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return ForumsDetailFragment.arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = l_Inflater.inflate(R.layout.layout_comments_maker, null);
        aq = new AQuery(convertView);

        aq.id(R.id.forum_description).text(ForumsDetailFragment.arrayList.get(position).getComment());
        aq.id(R.id.forum_title).text(ForumsDetailFragment.arrayList.get(position).getUserName()
                + " , " + ForumsDetailFragment.arrayList.get(position).getDateTime());
        Glide.with(ctx).load(Constants.Image_URL + baseClass.getProfileImageID()
                +"."+BaseClass.getExtensionType(TaskCommentFragment.arrayList
                .get(position).getUserImagePath())).into(aq.id(R.id.comment_image).getImageView());
        return convertView;
    }
}
