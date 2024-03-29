package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

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
 * Created by waqas on 2/2/16.
 */
public class AdapterForumsList extends BaseAdapter {

    private Context ctx;
    private BaseClass baseClass;
    private AQuery aq;
    int Clicked=0;
    ForumService service;
    private LayoutInflater l_Inflater;

    public AdapterForumsList(Context context) {
        l_Inflater = LayoutInflater.from(context);
        this.ctx = context;
        service = new ForumService((AppCompatActivity)ctx);
        baseClass = ((BaseClass) ctx.getApplicationContext());
    }

    @Override
    public int getCount() {
        return ForumsModel.getInstance().responseObject.size();
    }

    @Override
    public ForumsModel.ResponseObject getItem(int position) {
        return ForumsModel.getInstance().responseObject.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = l_Inflater.inflate(R.layout.layout_forum_row, null);
        aq = new AQuery(convertView);
        aq.id(R.id.comment_title).text(getItem(position).title);
        aq.id(R.id.notify_alert1).text(getItem(position).commentCount);
        aq.id(R.id.comment_description).text(ctx.getString(R.string.by)+ " "+ getItem(position).user.firstName
                +" "+getItem(position).user.lastName);
//                +",  "+ctx.getString(R.string.last_responce_on)+ " "+ baseClass.DateFormatter(getItem(position).updatedAt)
//                +" "+ baseClass.GetTime(baseClass.DateMilli(getItem(position).updatedAt)));
        aq.id(R.id.parent1).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFragmentWithBackStack(R.id.container, ForumsDetailFragment.newInstance(position), "ForumsDetailFragment");
            }
        });
        aq.id(R.id.btEdit).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (baseClass.getSelectedProject().equalsIgnoreCase("0")) {
                    Toast.makeText((AppCompatActivity)ctx,"Please Select Project",Toast.LENGTH_SHORT).show();
                    return;
                }
                callFragmentWithBackStack(R.id.container, EditForumFragment.newInstance(position), "EditForumFragment");
            }
        });
        aq.id(R.id.btDelete).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Clicked = position;
               NormalDialogCustomAttr(ctx.getString(R.string.deleted_forum),Clicked);
            }
        });
        return convertView;
    }
    public void DeleteForum(Object caller, Object model){
        GeneralModel.getInstance().setList((GeneralModel) model);
        if (GeneralModel.getInstance().responseObject) {
            UpdatedAfterDelete(Clicked);
            Toast.makeText(ctx, ctx.getString(R.string.forum_deleted), Toast.LENGTH_SHORT).show();
             }else {
            Toast.makeText(ctx, ctx.getString(R.string.response_error), Toast.LENGTH_SHORT).show();
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
                        service.deleteForum(Integer.toString(ForumsModel.getInstance().responseObject.get(position).ID),
                                true, new CallBack(AdapterForumsList.this, "DeleteForum"));
                    }
                });
    }
}
