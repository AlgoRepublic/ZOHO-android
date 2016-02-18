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
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

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
        return ForumsCommentModel.getInstance().responseObject.forumComments.size();
    }

    @Override
    public ForumsCommentModel.ForumComments getItem(int position) {
        return ForumsCommentModel.getInstance().responseObject.forumComments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = l_Inflater.inflate(R.layout.layout_comments_maker, null);
        aq = new AQuery(convertView);

        aq.id(R.id.forum_title).text(getItem(position).user.firstName+" "+
        getItem(position).user.lastName+", on "+baseClass.DateFormatter
                (getItem(position).user.updatedAt)+" "+baseClass.GetTime(
                baseClass.DateMilli(getItem(position).user.updatedAt)));
        aq.id(R.id.forum_description).text(Html.fromHtml(getItem(position).message));
        aq.id(R.id.comment_image).text(getItem(position).user.profileImagePath);
        return convertView;
    }
}
