package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.DocsPreviewFragment;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.ForumService;
import com.algorepublic.zoho.utils.BaseClass;
import com.algorepublic.zoho.utils.Constants;
import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.daimajia.swipe.SwipeLayout;
import com.mikhaellopez.circularimageview.CircularImageView;


/**
 * Created by android on 1/13/16.
 */
public class AdapterDocsComments extends BaseAdapter {

    Context ctx;
    private LayoutInflater l_Inflater;
    BaseClass baseClass;
    ForumService service;
    AQuery aq;

    public AdapterDocsComments(Context context) {
        l_Inflater = LayoutInflater.from(context);
        this.ctx = context;
        service =  new ForumService((AppCompatActivity)ctx);
        baseClass = ((BaseClass) ctx.getApplicationContext());
    }

    public int getCount() {
        return DocsPreviewFragment.arrayList.size();
    }

    public TaskComments getItem(int position) {
        return DocsPreviewFragment.arrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    /**
     * apply delete comments permissions based on user id;
     * @param holder
     * @param pos
     */
    private void applyPermissions(ViewHolder holder,int pos){
        if(!baseClass.hasPermission(ctx.getResources().getString(R.string.documents_comments_edit_delete_own_comment))
                && !baseClass.hasPermission(ctx.getResources().getString(R.string.documents_comments_edit_delete_others_comment))
                ){
            holder.swipeLayout.findViewById(R.id.editDeleteView).setVisibility(View.GONE);
            holder.swipeLayout.setSwipeEnabled(false);
            aq.id(R.id.rightarrow_layout).visibility(View.GONE);
        }else if(!baseClass.hasPermission(ctx.getResources().getString(R.string.documents_comments_edit_delete_own_comment))
                && getItem(pos).getUserId()==Integer.parseInt(baseClass.getUserId())
                ){
            holder.swipeLayout.findViewById(R.id.editDeleteView).setVisibility(View.GONE);
            holder.swipeLayout.setSwipeEnabled(false);
            aq.id(R.id.rightarrow_layout).visibility(View.GONE);
        }else if(!baseClass.hasPermission(ctx.getResources().getString(R.string.documents_comments_edit_delete_others_comment))
                && getItem(pos).getUserId()!=Integer.parseInt(baseClass.getUserId())
                ){
            holder.swipeLayout.findViewById(R.id.editDeleteView).setVisibility(View.GONE);
            holder.swipeLayout.setSwipeEnabled(false);
            aq.id(R.id.rightarrow_layout).visibility(View.GONE);
        }
    }
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = l_Inflater.inflate(R.layout.layout_comments_maker, null);
            holder = new ViewHolder();
            holder.taskComment = (TextView) convertView.findViewById(R.id.comment_description);
            holder.userName = (TextView) convertView.findViewById(R.id.comment_title);
            holder.userImage = (CircularImageView) convertView.findViewById(R.id.comment_image);
            holder.swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipe4);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        aq = new AQuery(convertView);
        holder.taskComment.setText(Html.fromHtml(DocsPreviewFragment.arrayList.get(position).getComment()));
        holder.userName.setText(DocsPreviewFragment.arrayList.get(position).getUserName()
                + " , " + DocsPreviewFragment.arrayList.get(position).getDateTime());
        if(DocsPreviewFragment.arrayList
                .get(position).getUserImagePath() !=null) {
            Glide.with(ctx).load(Constants.Image_URL + DocsPreviewFragment
                    .arrayList.get(position).getUserImageID()
                    + "." + BaseClass.getExtensionType(DocsPreviewFragment.arrayList
                    .get(position).getUserImagePath())).into(holder.userImage);
        }
//        aq.id(R.id.btEdit).clicked(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DocsPreviewFragment.flag = true;
//                DocsPreviewFragment.ClickedPosition = position;
//                DocsPreviewFragment.comment_user.setText(DocsPreviewFragment
//                        .arrayList.get(position).getComment());
//            }
//        });
        aq.id(R.id.btDelete).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocsPreviewFragment.ClickedPosition = position;

                service.deleteForumComment(getItem(position).getCommentID(), true,
                        new CallBack(AdapterDocsComments.this, "DeleteComment"));
            }
        });
        applyPermissions(holder,position);
        return convertView;
    }
    public void DeleteComment(Object caller, Object model){
        GeneralModel.getInstance().setList((GeneralModel) model);
        if (GeneralModel.getInstance().responseObject ==true) {
            DocsPreviewFragment.arrayList.remove(DocsPreviewFragment.ClickedPosition);
            notifyDataSetChanged();
        }else {
            Toast.makeText(ctx, ctx.getString(R.string.response_error), Toast.LENGTH_SHORT).show();
        }
    }
    static class ViewHolder {
        TextView taskComment;
        TextView userName;
        CircularImageView userImage;
        SwipeLayout swipeLayout;
    }
}