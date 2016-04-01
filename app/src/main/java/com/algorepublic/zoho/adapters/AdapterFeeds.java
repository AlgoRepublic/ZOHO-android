package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.algorepublic.zoho.Models.FeedsModel;
import com.algorepublic.zoho.Models.ForumsModel;
import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.EditForumFragment;
import com.algorepublic.zoho.fragments.ForumsDetailFragment;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.ForumService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;
import com.flyco.animation.BounceEnter.BounceLeftEnter;
import com.flyco.animation.SlideExit.SlideRightExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;

/**
 * Created by android on 3/29/16.
 */
public class AdapterFeeds extends BaseAdapter {

    private Context ctx;
    private BaseClass baseClass;
    private AQuery aq;
    int Clicked=0;
    ForumService service;
    private LayoutInflater l_Inflater;

    public AdapterFeeds(Context context) {
        l_Inflater = LayoutInflater.from(context);
        this.ctx = context;
        service = new ForumService((AppCompatActivity)ctx);
        baseClass = ((BaseClass) ctx.getApplicationContext());
    }

    @Override
    public int getCount() {
        return FeedsModel.getInstance().responseObject.size();
    }

    @Override
    public FeedsModel.ResponseObject getItem(int position) {
        return FeedsModel.getInstance().responseObject.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
       // return if(getItem(position).message==);
        return 0;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = l_Inflater.inflate(R.layout.layout_feeds_row, null);
        aq = new AQuery(convertView);
        aq.id(R.id.feed_title).text(getItem(position).userName+" "+getStringLine(getItem(position).updateType));
        aq.id(R.id.feed_description).text(getItem(position).message);
        aq.id(R.id.feed_comment_date).text(
                getItem(position).comments.size()+"-"+ctx.getString(R.string.comments));

//        aq.id(R.id.parent1).clicked(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                callFragmentWithBackStack(R.id.container, ForumsDetailFragment.newInstance(position), "ForumsDetailFragment");
//            }
//        });
//        aq.id(R.id.btEdit).clicked(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (baseClass.getSelectedProject().equalsIgnoreCase("0")) {
//                    Toast.makeText((AppCompatActivity)ctx,"Please Select Project",Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                callFragmentWithBackStack(R.id.container, EditForumFragment.newInstance(position), "EditForumFragment");
//            }
//        });.
//        aq.id(R.id.btDelete).clicked(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Clicked = position;
//               NormalDialogCustomAttr(ctx.getString(R.string.deleted_forum),Clicked);
//            }
//        });
        return convertView;
    }
    protected String getStringLine(int stringType){
        String lines=null;
        if(stringType==1)   // Status
        {
            lines = ctx.getString(R.string.added_status);
        }else if(stringType==2)   // Project
        {
            lines = ctx.getString(R.string.added_project);
        }else if(stringType==3)   // TaskList
        {
            lines = ctx.getString(R.string.added_tasklist);
        }else if(stringType==4)   // Task
        {
            lines = ctx.getString(R.string.added_task);
        }else if(stringType==41)   // TaskUpdate
        {
            lines = ctx.getString(R.string.updated_task);
        }else if(stringType==5)   // Page
        {
            lines = ctx.getString(R.string.added_page);
        }else if(stringType==6)   // Event
        {
            lines = ctx.getString(R.string.added_event);
        }else if(stringType==7)   // Forum
        {
            lines = ctx.getString(R.string.added_forum);
        }else if(stringType==8)   // Document
        {
            lines = ctx.getString(R.string.added_document);
        }else if(stringType==9)   // Milestone
        {
            lines = ctx.getString(R.string.added_milestone);
        }
        return lines;
    }
    public void DeleteForum(Object caller, Object model){
        GeneralModel.getInstance().setList((GeneralModel) model);
        if (GeneralModel.getInstance().responseObject) {
            UpdatedAfterDelete(Clicked);
            Snackbar.make(((AppCompatActivity) ctx).findViewById(android.R.id.content), ctx.getString(R.string.forum_deleted), Snackbar.LENGTH_SHORT).show();
        }else {
            Snackbar.make(((AppCompatActivity) ctx).findViewById(android.R.id.content), ctx.getString(R.string.response_error), Snackbar.LENGTH_SHORT).show();
        }
    }
    public void UpdatedAfterDelete(int pos){
        ForumsModel.getInstance().responseObject.remove(pos);
        notifyDataSetChanged();
    }
    public void callFragmentWithBackStack(int containerId, Fragment fragment, String tag){
        ((AppCompatActivity)ctx).getSupportFragmentManager()
                .beginTransaction().setCustomAnimations(R.anim.slide_in_enter, R.anim.slide_in_exit, R.anim.slide_pop_enter, R.anim.slide_pop_exit)
                .replace(containerId, fragment, tag)
                .addToBackStack(null)
                .commit();
    }
    private void NormalDialogCustomAttr(String content,final int position) {
        final NormalDialog dialog = new NormalDialog(ctx);
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
//                        service.deleteForum(Integer.toString(FeedsModel.getInstance().responseObject.get(position).ID),
//                                true, new CallBack(AdapterFeeds.this, "DeleteForum"));
                    }
                });
    }
}
