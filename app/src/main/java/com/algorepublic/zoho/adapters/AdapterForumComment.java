package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.ForumsDetailFragment;
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

/**
 * Created by waqas on 2/3/16.
 */
public class AdapterForumComment extends BaseAdapter {

    private Context ctx;
    private BaseClass baseClass;
    private AQuery aq;
    ForumService service;

    private LayoutInflater l_Inflater;

    public AdapterForumComment(Context context) {
        l_Inflater = LayoutInflater.from(context);
        this.ctx = context;
        service =  new ForumService((AppCompatActivity)ctx);
        baseClass = ((BaseClass) ctx.getApplicationContext());
    }

    @Override
    public int getCount() {
        return ForumsDetailFragment.arrayList.size();
    }

    @Override
    public TaskComments getItem(int position) {
        return ForumsDetailFragment.arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = l_Inflater.inflate(R.layout.layout_comments_maker, null);
        aq = new AQuery(convertView);

        if(ForumsDetailFragment.arrayList.get(position).getComment() !=null)
        aq.id(R.id.comment_description).text(Html.fromHtml(ForumsDetailFragment.arrayList.get(position).getComment()));

        aq.id(R.id.comment_title).text(ForumsDetailFragment.arrayList.get(position).getUserName()
                + " , " + ForumsDetailFragment.arrayList.get(position).getDateTime());
        if(ForumsDetailFragment.arrayList
                .get(position).getUserImagePath() != null) {
            Glide.with(ctx).load(Constants.Image_URL + ForumsDetailFragment
                    .arrayList.get(position).getUserImageID()
                    + "." + BaseClass.getExtensionType(ForumsDetailFragment.arrayList
                    .get(position).getUserImagePath())).into(aq.id(R.id.comment_image).getImageView());
        }
//        aq.id(R.id.btEdit).clicked(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ForumsDetailFragment.flag = true;ForumsDetailFragment.ClickedPosition = position;
//                ForumsDetailFragment.comment_user.setText(ForumsDetailFragment
//                        .arrayList.get(position).getComment());
//            }
//        });
        aq.id(R.id.btDelete).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = ctx.getString(R.string.delete_comment);
                ForumsDetailFragment.ClickedPosition = position;
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
                                service.deleteForumComment(ForumsDetailFragment
                                                .arrayList.get(position).getCommentID(), true,
                                        new CallBack(AdapterForumComment.this, "DeleteComment"));
                            }
                        });


            }
        });

        applyPermissions(position);

        return convertView;
    }

    /**
     * apply delete comments permissions based on user id;
     * @param pos
     */
    private void applyPermissions(int pos){
        if(!baseClass.hasPermission(ctx.getResources().getString(R.string.forums_edit_delete_own_comments))
                && !baseClass.hasPermission(ctx.getResources().getString(R.string.forums_edit_delete_others_comments))
                ){
            ((SwipeLayout)aq.id(R.id.swipe4).getView()).findViewById(R.id.editDeleteView).setVisibility(View.GONE);
            ((SwipeLayout)aq.id(R.id.swipe4).getView()).setSwipeEnabled(false);
            aq.id(R.id.rightarrow_layout).visibility(View.GONE);
        }else if(!baseClass.hasPermission(ctx.getResources().getString(R.string.forums_edit_delete_own_comments))
                && getItem(pos).getUserId()==Integer.parseInt(baseClass.getUserId())
                ){
            ((SwipeLayout)aq.id(R.id.swipe4).getView()).findViewById(R.id.editDeleteView).setVisibility(View.GONE);
            ((SwipeLayout)aq.id(R.id.swipe4).getView()).setSwipeEnabled(false);
            aq.id(R.id.rightarrow_layout).visibility(View.GONE);
        }else if(!baseClass.hasPermission(ctx.getResources().getString(R.string.forums_edit_delete_others_comments))
                && getItem(pos).getUserId()!=Integer.parseInt(baseClass.getUserId())
                ){
            ((SwipeLayout)aq.id(R.id.swipe4).getView()).findViewById(R.id.editDeleteView).setVisibility(View.GONE);
            ((SwipeLayout)aq.id(R.id.swipe4).getView()).setSwipeEnabled(false);
            aq.id(R.id.rightarrow_layout).visibility(View.GONE);
        }
    }
    public void DeleteComment(Object caller, Object model){
        GeneralModel.getInstance().setList((GeneralModel) model);
        if (GeneralModel.getInstance().responseObject ==true) {
            ForumsDetailFragment.arrayList.remove(ForumsDetailFragment.ClickedPosition);
            notifyDataSetChanged();
        }else {
            Toast.makeText(ctx, ctx.getString(R.string.response_error), Toast.LENGTH_SHORT).show();
        }
    }

}
