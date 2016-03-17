package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.TaskCommentFragment;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.ForumService;
import com.algorepublic.zoho.utils.BaseClass;
import com.algorepublic.zoho.utils.Constants;
import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;


/**
 * Created by android on 1/1/16.
 */
public class AdapterTaskComments extends BaseAdapter {

    Context ctx;
    private LayoutInflater l_Inflater;
    BaseClass baseClass;
    AQuery aq;
    ForumService service;

    public AdapterTaskComments(Context context) {
        l_Inflater = LayoutInflater.from(context);
        service =  new ForumService((AppCompatActivity)ctx);
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
        aq = new AQuery(convertView);
        holder.taskComment.setText(Html.fromHtml(TaskCommentFragment.arrayList.get(position).getComment()));
        holder.userName.setText(TaskCommentFragment.arrayList.get(position).getUserName()
                + " , " + TaskCommentFragment.arrayList.get(position).getDateTime());
        if(TaskCommentFragment.arrayList
                .get(position).getUserImagePath() !=null) {
            Glide.with(ctx).load(Constants.Image_URL + TaskCommentFragment
                    .arrayList.get(position).getUserImageID()
                    + "." + BaseClass.getExtensionType(TaskCommentFragment.arrayList
                    .get(position).getUserImagePath())).into(holder.userImage);
        }
//        aq.id(R.id.btEdit).clicked(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TaskCommentFragment.flag = true;TaskCommentFragment.ClickedPosition = position;
//                TaskCommentFragment.comment_user.setText(TaskCommentFragment
//                        .arrayList.get(position).getComment());
//            }
//        });
        aq.id(R.id.btDelete).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskCommentFragment.ClickedPosition = position;
                service.deleteForumComment(TaskCommentFragment
                                .arrayList.get(position).getCommentID(), true,
                        new CallBack(AdapterTaskComments.this, "DeleteComment"));
            }
        });

        return convertView;
    }
    public void DeleteComment(Object caller, Object model){
        GeneralModel.getInstance().setList((GeneralModel) model);
        if (GeneralModel.getInstance().responseObject ==true) {
            TaskCommentFragment.arrayList.remove(TaskCommentFragment.ClickedPosition);
            notifyDataSetChanged();
        }else {
            Snackbar.make(((AppCompatActivity) ctx).findViewById(android.R.id.content),
                    ctx.getString(R.string.response_error), Snackbar.LENGTH_SHORT).show();
        }
    }

    static class ViewHolder {
        TextView taskComment;
        TextView userName;
        CircularImageView userImage;
    }
}