package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.TaskCommentFragment;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.ForumService;
import com.algorepublic.zoho.utils.BaseClass;
import com.algorepublic.zoho.utils.Constants;
import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.daimajia.swipe.SwipeLayout;
import com.flyco.animation.BounceEnter.BounceLeftEnter;
import com.flyco.animation.SlideExit.SlideRightExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
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
            holder.swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipe4);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        aq = new AQuery(convertView);
        if(TaskCommentFragment.arrayList.get(position).getComment() !=null) {
            holder.taskComment.setText(Html.fromHtml(
                    TaskCommentFragment.arrayList.get(position).getComment()).toString());
        }
            holder.userName.setText(TaskCommentFragment.arrayList.get(position).getUserName()
                + " , " + TaskCommentFragment.arrayList.get(position).getDateTime());
        if(TaskCommentFragment.arrayList
                .get(position).getUserImagePath() !=null) {
            Glide.with(ctx).load(Constants.Image_URL + TaskCommentFragment
                    .arrayList.get(position).getUserImageID()
                    + "." + BaseClass.getExtensionType(TaskCommentFragment.arrayList
                    .get(position).getUserImagePath())).into(holder.userImage);
        }
     applyPermissions(holder,position);
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
                String content = ctx.getString(R.string.delete_comment);
                TaskCommentFragment.ClickedPosition = position;
                final NormalDialog dialog = new NormalDialog(((AppCompatActivity) ctx));
                dialog.isTitleShow(false)//
                        .bgColor(ctx.getResources().getColor(R.color.colorBaseWrapper))//
                        .cornerRadius(5)//
                        .content(content)//
                        .contentGravity(Gravity.CENTER)//
                        .contentTextColor(ctx.getResources().getColor(R.color.colorBaseHeader))//
                        .dividerColor(ctx.getResources().getColor(R.color.colorContentWrapper))//
                        .btnTextSize(15.5f, 15.5f)//
                        .btnTextColor(ctx.getResources().getColor(R.color.colorBaseHeader)
                                , ctx.getResources().getColor(R.color.colorBaseHeader))//
                        .btnPressColor(ctx.getResources().getColor(R.color.colorBaseMenu))//
                        .widthScale(0.85f)//
                        .showAnim(new BounceLeftEnter())//
                        .dismissAnim(new SlideRightExit())//
                        .show();

                dialog.setOnBtnClickL(
                        new OnBtnClickL() {
                            @Override
                            public void onBtnClick() {
                                dialog.dismiss();
                            }
                        },
                        new OnBtnClickL() {
                            @Override
                            public void onBtnClick() {
                                dialog.dismiss();
                                service.deleteForumComment(TaskCommentFragment
                                                .arrayList.get(position).getCommentID(), true,
                                        new CallBack(AdapterTaskComments.this, "DeleteComment"));
                            }
                        });


            }
        });

        return convertView;
    }

    /**
     * apply permissions on task delete.
     */
    private void applyPermissions(ViewHolder holder,int position){
        if(!baseClass.hasPermission(ctx.getResources().getString(R.string.tasks_edit_delete_own_comment))
                && TaskCommentFragment.arrayList.get(position).getUserId()==Integer.parseInt(baseClass.getUserId())
                ) {
            holder.swipeLayout.findViewById(R.id.editDeleteView).setVisibility(View.GONE);
            holder.swipeLayout.setSwipeEnabled(false);
            holder.swipeLayout.findViewById(R.id.parent2).findViewById(R.id.rightarrow_layout).setVisibility(View.GONE);
        }
        if(!baseClass.hasPermission(ctx.getResources().getString(R.string.tasks_edit_delete_others_comment))
                && TaskCommentFragment.arrayList.get(position).getUserId()!=Integer.parseInt(baseClass.getUserId())
                ) {
            holder.swipeLayout.findViewById(R.id.editDeleteView).setVisibility(View.GONE);
            holder.swipeLayout.setSwipeEnabled(false);
            holder.swipeLayout.findViewById(R.id.parent2).findViewById(R.id.rightarrow_layout).setVisibility(View.GONE);
        }
    }
    public void DeleteComment(Object caller, Object model){
        GeneralModel.getInstance().setList((GeneralModel) model);
        if (GeneralModel.getInstance().responseObject ==true) {
            TaskCommentFragment.arrayList.remove(TaskCommentFragment.ClickedPosition);
            notifyDataSetChanged();
        }else {
            Toast.makeText(ctx,ctx.getString(R.string.response_error),Toast.LENGTH_SHORT).show();

        }
    }

    static class ViewHolder {
        TextView taskComment;
        TextView userName;
        CircularImageView userImage;
        SwipeLayout swipeLayout;
    }
}